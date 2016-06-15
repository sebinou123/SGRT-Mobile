package sfadmldb.sgrt.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sfadmldb.sgrt.model.Cours;
import sfadmldb.sgrt.model.Memory;
import sfadmldb.sgrt.model.ParseJSONChoice;
import sfadmldb.sgrt.model.ParseJSONChoiceFait;
import sfadmldb.sgrt.model.ParseJSONCompteurAndBilles;
import sfadmldb.sgrt.model.ParseJSONCours;
import sfadmldb.sgrt.model.ParseJSONProf;
import sfadmldb.sgrt.model.Prof;
import sfadmldb.sgrt.R;
import sfadmldb.sgrt.model.secure;
import sfadmldb.sgrt.model.user;


/**
 *  This class build an activity who show information about the choice made or not by the user, marbles and times counter on each course.
 *  Call webservice to get informations and create table to show these data. Give the option to logout of the application.
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ConsultPanel extends AppCompatActivity {

    //The tab of each view ball, choice and counter
    TabHost tabHost;

    //The current tab selected
    String currentTab;

    //The current path on the server
    String currentPath;

    //The table to chow data
    TableLayout tblChoice;
    TableLayout tblCompteur;
    TableLayout tblBilles;

    //The toolbar of the app
    Toolbar toolbar;

    //Name of each tab
    String tab1name;
    String tab2name;
    String tab3name;

    //The selected item in the list ball or counter
    int currentListItemSelected;
    int currentSpinnerItemSelected;

    private Spinner listSession;

    //List for each view
    ListView lstBille;
    ListView lstCompteur;

    //Progress bar waiting on webservice
    private ProgressBar progressbarChoix;
    private ProgressBar progressbarCompteur;
    private ProgressBar progressbarBilles;

    //Error generate by the webservice show on textview
    private TextView errorLoadingTextChoice;
    private TextView errorLoadingTextBilles;
    private TextView errorLoadingTextCompteur;


    //url to the server
    public static final String url = "http://10.209.55.124/";

    //Default when there are no data about marbles, counter and bid
    private static final String defaultBillesOrCompteur = "0";
    private static final String defaultBillesAndCompteur = "0 / 0";
    private static final String defaultBillesAndCompteurAndBid = "0 / 0 / 0";

    // The request code
    static final int PICK_CONTACT_REQUEST = 1;

    //Object to call request to the server
    private RequestQueue requestQueue;




    /*
     * Method called when the application start. This method set the view, instantiate every element and add them to the view. Also,
     * he set the current language to the application. Add event handler when a button is pressed.
     *
     * @param savedInstanceState - a save of previous state, who can be reloaded by passing it to the method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!secure.checkRootMethod())
        {

            if(getResources().getBoolean(R.bool.portrait_only)){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_panel);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");

        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle("SGRT");
        }

        tblChoice = (TableLayout) findViewById(R.id.tblChoice);
        tblBilles = (TableLayout) findViewById(R.id.tblBilles);
        tblCompteur = (TableLayout) findViewById(R.id.tblCompteur);

        errorLoadingTextChoice = (TextView) findViewById(R.id.errorLoadDonnees);
        errorLoadingTextBilles = (TextView) findViewById(R.id.errorLoadDonneesBilles);
        errorLoadingTextCompteur = (TextView) findViewById(R.id.errorLoadDonneesCompteur);

        TextView spinnerTitle = (TextView) findViewById(R.id.spinnerTitle);
        if (spinnerTitle != null) {
            spinnerTitle.setText(getResources().getString(R.string.lblSpinnerTitle));
        }

        setSupportActionBar(toolbar);
        BarListener listener = new BarListener();

        tabHost = (TabHost)findViewById(R.id.tabHost);

        if (tabHost != null) {

            tabHost.setup();
            tabHost.setOnTabChangedListener(listener);

            tab1name = getResources().getString(R.string.tab1);
            tab2name = getResources().getString(R.string.tab2);
            tab3name = getResources().getString(R.string.tab3);

            //Tab 1
            TabHost.TabSpec spec = tabHost.newTabSpec(tab1name);
            spec.setContent(R.id.tab1);
            spec.setIndicator(tab1name);
            tabHost.addTab(spec);

            //Tab 2
            spec = tabHost.newTabSpec(tab2name);
            spec.setContent(R.id.tab2);
            spec.setIndicator(tab2name);
            tabHost.addTab(spec);

            //Tab 3
            spec = tabHost.newTabSpec(tab3name);
            spec.setContent(R.id.tab3);
            spec.setIndicator(tab3name);
            tabHost.addTab(spec);
        }

        ListListener lstListener = new ListListener();

        String[] arrayBille = new String[2];
        arrayBille[0] = getResources().getString(R.string.lstBille1);
        arrayBille[1] = getResources().getString(R.string.lstBille2);

        ArrayAdapter<String> myAdapterBille= new
                ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                arrayBille);
        lstBille=(ListView) findViewById(R.id.listBille);
            if (lstBille != null) {
                lstBille.setAdapter(myAdapterBille);
                lstBille.setOnItemClickListener(lstListener);
            }


        String[] arrayCompteur = new String[2];
        arrayCompteur[0] = getResources().getString(R.string.lstCompteur1);
        arrayCompteur[1] = getResources().getString(R.string.lstCompteur2);

        ArrayAdapter<String> myAdapterCompteur=new
                ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                arrayCompteur);
        lstCompteur=(ListView) findViewById(R.id.listCompteur);

        if (lstCompteur != null) {
            lstCompteur.setAdapter(myAdapterCompteur);
            lstCompteur.setOnItemClickListener(lstListener);
        }

        progressbarChoix = (ProgressBar) findViewById(R.id.progressBarChoix);

        if (progressbarChoix != null) {
            progressbarChoix.getIndeterminateDrawable().setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);
            progressbarChoix.setVisibility(View.INVISIBLE);
        }

        progressbarBilles = (ProgressBar) findViewById(R.id.progressBarBilles);

        if (progressbarBilles != null) {
            progressbarBilles.getIndeterminateDrawable().setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);
            progressbarBilles.setVisibility(View.INVISIBLE);
        }

        progressbarCompteur = (ProgressBar) findViewById(R.id.progressBarCompteur);

        if (progressbarCompteur != null) {
            progressbarCompteur.getIndeterminateDrawable().setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);
            progressbarCompteur.setVisibility(View.INVISIBLE);
        }

        TabWidget widget = tabHost.getTabWidget();

        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            v.setBackgroundResource(R.drawable.apptheme_tab_indicator_holo);
        }

        SpinnerListener spinnerListener = new SpinnerListener();

        listSession = (Spinner) findViewById(R.id.spinnerSession);

        List<String> spinnerList = new ArrayList<>();
        spinnerList.add(getResources().getString(R.string.listSpinner1));
        spinnerList.add(getResources().getString(R.string.listSpinner2));
        spinnerList.add(getResources().getString(R.string.listSpinner3));
        spinnerList.add(getResources().getString(R.string.listSpinner4));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
               this,
               android.R.layout.simple_spinner_item,
               spinnerList
         );

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        listSession.setAdapter(adapter);
        listSession.setOnItemSelectedListener(spinnerListener);
        }
    }

    /**
     * Inner class who implements the OnItemClickListener for the listView
     */
    public class ListListener implements AdapterView.OnItemClickListener
    {

        /**
         *  Interface definition for a callback to be invoked when an item in this AdapterView has been clicked.
         *
         * @param parent - The AdapterView where the click happened.
         * @param view -  The view within the AdapterView that was clicked (this will be a view provided by the adapter)
         * @param position - The position of the view in the adapter.
         * @param id -  The row id of the item that was clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String  itemValue;

            if(parent.getId() == lstBille.getId())
            {

                itemValue = (String) lstBille.getItemAtPosition(position);

                if(itemValue.matches(getResources().getString(R.string.lstBille1)))
                {
                    currentListItemSelected = 1;
                    sendRequestGetProf(currentPath);
                }else
                {
                    currentListItemSelected = 2;
                    sendRequestGetProf(currentPath);

                    if(ParseJSONCompteurAndBilles.afficherBid)
                    {
                        TextView viewTemp = (TextView) view;
                        viewTemp.setText(getResources().getString(R.string.lstBille2alt));
                    }
                }

            }
            else
            {

                itemValue = (String) lstCompteur.getItemAtPosition(position);

                if(itemValue.matches(getResources().getString(R.string.lstCompteur1)))
                {
                    currentListItemSelected = 1;
                    sendRequestGetProf(currentPath);
                }else
                {
                    currentListItemSelected = 2;
                    sendRequestGetProf(currentPath);

                    if(ParseJSONCompteurAndBilles.afficherBid)
                    {
                        TextView viewTemp = (TextView) view;
                        viewTemp.setText(getResources().getString(R.string.lstCompteur2alt));
                    }
                }
            }


        }
    }

    /**
     * Inner class who implements the OnItemSelectedListener for the spinner
     */
    public class SpinnerListener implements AdapterView.OnItemSelectedListener
    {

        /**
         *  Interface definition for a callback to be invoked when an item in this AdapterView has been clicked.
         *
         * @param parentView - The AdapterView where the click happened.
         * @param selectedItemView -  The view within the AdapterView that was clicked (this will be a view provided by the adapter)
         * @param position - The position of the view in the adapter.
         * @param id -  The row id of the item that was clicked.
         */
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            String  itemValue;

            if(parentView.getId() == listSession.getId())
            {
                itemValue = (String) listSession.getItemAtPosition(position);

                if(itemValue.matches(getResources().getString(R.string.listSpinner2)))
                {
                    currentPath = "choix/choixStatus";
                    currentSpinnerItemSelected = 0;
                    sendRequestPostChoix(currentPath);
                }
                else if(itemValue.matches(getResources().getString(R.string.listSpinner3)))
                {
                    currentPath = "choix/choixStatus";
                    currentSpinnerItemSelected = 1;
                    sendRequestPostChoix(currentPath);
                }
                else if(itemValue.matches(getResources().getString(R.string.listSpinner4)))
                {
                    currentPath = "choix/choixStatus";
                    currentSpinnerItemSelected = 2;
                    sendRequestPostChoix(currentPath);
                }
                else{
                    currentSpinnerItemSelected = 3;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /**
     *  Inner class who implements the OnTabChangeListener for the tabhost
     */
    private class BarListener implements TabHost.OnTabChangeListener {

        /**
         * Interface definition for a callback to be invoked when an tab in Tabhost has been clicked.
         *
         * @param tabId - Id of the selected tab
         */
        @Override
        public void onTabChanged(String tabId) {

            if(tabId.matches(tab1name))
            {
                tblBilles.removeAllViews();
                currentTab = "1";
                currentPath = "gestion/getEnseignant";
                currentSpinnerItemSelected = 0;

            }
            else if(tabId.matches(tab2name))
            {
                tblChoice.removeAllViews();
                currentTab = "2";
                currentPath = "choix/choixStatus";
                currentSpinnerItemSelected = 0;
                errorLoadingTextChoice.setText("");
            }
            else
            {
                tblCompteur.removeAllViews();
                currentTab = "3";
                currentPath = "gestion/getEnseignant";
                currentSpinnerItemSelected = 0;

            }
        }
    }




    /**
     * Method who send a request to know if he did his choice for the current task.
     * Return an error if the request fail.
     * Call the method to get his choice only if he did maid his choice.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostChoix(String path){

        progressbarChoix.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("user_id", user.getUser().getId());
        map.put("web", "true");

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, ConsultPanel.url + path, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                errorLoadingTextChoice.setText("");
                progressbarChoix.setVisibility(View.INVISIBLE);
                Memory.deleteCache(ConsultPanel.this);
                    ParseJSONChoiceFait pjcf = new ParseJSONChoiceFait(result);
                    pjcf.parseJSON();
                    if(ParseJSONChoiceFait.nbChoix[0] || ParseJSONChoiceFait.nbChoix[1] || ParseJSONChoiceFait.nbChoix[2])
                    {
                        currentPath = "choix/getChoix";
                        sendRequestPostChoixValide(currentPath);
                    }
                    else
                    {
                        errorLoadingTextChoice.setText(getResources().getString(R.string.errorChoice));
                    }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressbarChoix.setVisibility(View.INVISIBLE);
                errorLoadingTextChoice.setText(getResources().getString(R.string.lblerrorWebService));

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", "Bearer " +  user.getUser().getToken());
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        objectRequest(sr);

    }


    /**
     * Method who send a request to get for a teacher all of his choice.
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostChoixValide(String path){

        progressbarChoix.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        errorLoadingTextChoice.setText("");
                        progressbarChoix.setVisibility(View.INVISIBLE);
                        Memory.deleteCache(ConsultPanel.this);
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tblChoice.removeAllViews();
                        progressbarChoix.setVisibility(View.INVISIBLE);
                        errorLoadingTextChoice.setText(getResources().getString(R.string.lblerrorWebService));
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("user_id", user.getUser().getId());
                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };


        stringRequest(stringRequest);
    }



    /**
     * Method who send a request to get counter, marbles and bid of each teacher on each course
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostCompteurAndBilles(String path){

        if(currentTab.matches("1")){
            progressbarBilles.setVisibility(View.VISIBLE);
        }else{
            progressbarCompteur.setVisibility(View.VISIBLE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(currentTab.matches("1")){
                            errorLoadingTextBilles.setText("");
                            progressbarBilles.setVisibility(View.INVISIBLE);
                        }else{
                            errorLoadingTextCompteur.setText("");
                            progressbarCompteur.setVisibility(View.INVISIBLE);
                        }

                        Memory.deleteCache(ConsultPanel.this);
                       showJSON(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(currentTab.matches("1")){
                            errorLoadingTextBilles.setText(getResources().getString(R.string.lblerrorWebService));
                            progressbarBilles.setVisibility(View.INVISIBLE);
                        }else{
                            errorLoadingTextCompteur.setText(getResources().getString(R.string.lblerrorWebService));
                            progressbarCompteur.setVisibility(View.INVISIBLE);
                        }
                    }
                }){


            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };

        stringRequest(stringRequest);

    }

    /**
     * Method who send a request to get all teacher
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestGetProf(String path){

        if(currentTab.matches("1")){
            progressbarBilles.setVisibility(View.VISIBLE);
        }else{
            progressbarCompteur.setVisibility(View.VISIBLE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(currentTab.matches("1")){
                            errorLoadingTextBilles.setText("");
                            progressbarBilles.setVisibility(View.INVISIBLE);
                        }else{
                            errorLoadingTextCompteur.setText("");
                            progressbarCompteur.setVisibility(View.INVISIBLE);
                        }

                        Memory.deleteCache(ConsultPanel.this);
                        ParseJSONProf pjsp = new ParseJSONProf(response);
                        pjsp.parseJSON();
                        sendRequestGetCours("gestion/getCours");
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(currentTab.matches("1")){
                            progressbarBilles.setVisibility(View.INVISIBLE);
                            errorLoadingTextBilles.setText(getResources().getString(R.string.lblerrorWebService));
                        }else{
                            progressbarCompteur.setVisibility(View.INVISIBLE);
                            errorLoadingTextCompteur.setText(getResources().getString(R.string.lblerrorWebService));
                        }
                    }
                }){


            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };

        stringRequest(stringRequest);
    }

    /**
     * Method who send a request to get all courses
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestGetCours(String path){

        if(currentTab.matches("1")){
            progressbarBilles.setVisibility(View.VISIBLE);
        }else{
            progressbarCompteur.setVisibility(View.VISIBLE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(currentTab.matches("1")){
                            errorLoadingTextBilles.setText("");
                            progressbarBilles.setVisibility(View.INVISIBLE);
                        }else{
                            errorLoadingTextCompteur.setText("");
                            progressbarCompteur.setVisibility(View.INVISIBLE);
                        }

                        Memory.deleteCache(ConsultPanel.this);
                        ParseJSONCours pjsc = new ParseJSONCours(response);
                        pjsc.parseJSON();
                        sendRequestPostCompteurAndBilles("billes/getBilles");
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(currentTab.matches("1")){
                            progressbarBilles.setVisibility(View.INVISIBLE);
                            errorLoadingTextBilles.setText(getResources().getString(R.string.lblerrorWebService));
                        }else{
                            progressbarCompteur.setVisibility(View.INVISIBLE);
                            errorLoadingTextCompteur.setText(getResources().getString(R.string.lblerrorWebService));
                        }
                    }
                }){


            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };

        stringRequest(stringRequest);
    }

    /**
     * Method call to set the gravity of a textview for api < 17
     *
     * @param t - the textView
     */
    public void setTextGravity(TextView t)
    {
        t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

    }

    /**
     * Method call to set the gravity of a textview for api >= 17
     *
     * @param t - the textView
     */
    @TargetApi(17)
    public void setTextGravity17(TextView t)
    {
        t.setGravity(Gravity.CENTER);
    }

    /**
     * Method call to set the backgroundDrawable of a textview for api < 16
     *
     * @param t - the textView
     * @param i - resource id
     */
    @SuppressWarnings("deprecation")
    public void setTextview(TextView t, Integer i)
    {
        t.setBackgroundDrawable(getResources().getDrawable(i));
    }

    /**
     * Method call to set the backgroundDrawable of a textview for api == 16
     *
     * @param t - the textView
     * @param i - resource id
     */
    @SuppressWarnings("deprecation")
    @TargetApi(16)
    public void setTextview16(TextView t, Integer i)
    {
        t.setBackground(getResources().getDrawable(i));
    }

    /**
     * Method call to set the backgroundDrawable of a textview for api >= 21
     *
     * @param t - the textView
     * @param i - resource id
     */
    @TargetApi(21)
    public void setTextview21(TextView t, Integer i)
    {
        t.setBackground(getResources().getDrawable(i,null));
    }

    /**
     * Method call to set the color of a textview for api < 23
     *
     * @param t - the textview
     * @param i - resource id
     */
    @SuppressWarnings("deprecation")
    public void setTextviewColor(TextView t, Integer i)
    {
        t.setTextColor(getResources().getColor(i));
    }

    /**
     * Method call to set the color of a textview for api == 23
     *
     * @param t - the textview
     * @param i - resource id
     */
    @TargetApi(23)
    public void setTextviewColor23(TextView t, Integer i)
    {
        t.setTextColor(getResources().getColor(i,null));
    }

    /**
     * Method call to set the textApparence of a textview for api < 23
     *
     * @param t - the textview
     * @param i - resource id
     */
    @SuppressWarnings("deprecation")
    public void setTextApparence(TextView t, Integer i)
    {
        t.setTextAppearance(this, i);
    }

    /**
     * Method call to set the textApparence of a textview for api == 23
     *
     * @param t - the textview
     * @param i - resource id
     */
    @TargetApi(23)
    public void setTextApparence23(TextView t, Integer i)
    {
        t.setTextAppearance(i);
    }

    /**
     * Method to show the data according to the selected tab and list
     *
     * @param json - the json response
     */
    private void showJSON(String json){

        TableRow row;
        TextView txtno;
        TextView txtvide;
        TextView txttitre;
        TextView nomProf;
        TextView donnee;
        TextView lblBandC;
        TextView lblCandB;
        TextView txtpriority;
        TextView nolbl;
        TextView titrelbl;
        TextView prioritylbl;
        TextView anneeChoix;


        //marbles tab
        if(currentTab.matches("1")) {
            tblBilles.removeAllViews();
            if(currentListItemSelected == 1)
            {
                tblBilles.removeAllViews();
                ParseJSONCompteurAndBilles pjco = new ParseJSONCompteurAndBilles(json);
                pjco.parseJSON();

                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");

                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }

                txtvide.setPadding(50,50,50,50);

                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }


                row.addView(txtvide);

                for (int i = 0; i < ParseJSONCours.no.length; i++) {

                    txtno = new TextView(tblBilles.getContext());

                    txtno.setText(ParseJSONCours.no[i]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txtno, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(txtno, R.drawable.cell_title);
                    } else {
                        setTextview(txtno, R.drawable.cell_title);
                    }
                    txtno.setPadding(50, 50, 50, 50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(txtno);
                    }else{
                        setTextGravity(txtno);
                    }

                    row.addView(txtno);
                }

                tblBilles.addView(row);

                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }
                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for (int j = 0; j < ParseJSONCours.titre.length; j++) {

                    txttitre = new TextView(tblBilles.getContext());

                    txttitre.setText(ParseJSONCours.titre[j]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txttitre, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(txttitre, R.drawable.cell_title);
                    } else {
                        setTextview(txttitre, R.drawable.cell_title);
                    }
                    txttitre.setPadding(50, 50, 50, 50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(txttitre);
                    }else{
                        setTextGravity(txttitre);
                    }

                    row.addView(txttitre);
                }

                tblBilles.addView(row);

                for (String alias : ParseJSONProf.allAlias) {
                    row = new TableRow(tblBilles.getContext());
                    nomProf = new TextView(tblBilles.getContext());
                    nomProf.setText(alias);
                    if(Build.VERSION.SDK_INT >= 21) {
                        setTextview21(nomProf, R.drawable.cell_title);
                    }else if(Build.VERSION.SDK_INT >= 16){
                        setTextview16(nomProf, R.drawable.cell_title);
                    }else{
                        setTextview(nomProf, R.drawable.cell_title);
                    }
                    nomProf.setPadding(50,50,50,50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(nomProf);
                    }else{
                        setTextGravity(nomProf);
                    }

                    if(Build.VERSION.SDK_INT >= 23){
                        setTextviewColor23(nomProf, R.color.colorBlack);
                    }else{
                        setTextviewColor(nomProf, R.color.colorBlack);
                    }

                    row.addView(nomProf);

                    //If we have data for each teacher, else we show default value
                    if(ParseJSONCompteurAndBilles.getProfHaveCours(alias))
                    {
                        Prof profTemp = ParseJSONCompteurAndBilles.getProf(alias);
                        for (int k = 0; k<ParseJSONCours.no.length; k++) {
                            //If we have data for a specific course
                            if(profTemp.getCoursExiste(ParseJSONCours.no[k]) != null)
                            {
                                Cours coursTemp = profTemp.getCoursExiste(ParseJSONCours.no[k]);
                                donnee = new TextView(tblBilles.getContext());
                                String affichageDonneesCandB = "" + coursTemp.getBilles();
                                donnee.setText(affichageDonneesCandB);

                                if(Build.VERSION.SDK_INT >= 21) {
                                    setTextview21(donnee, R.drawable.cell);
                                }else if(Build.VERSION.SDK_INT >= 16){
                                    setTextview16(donnee, R.drawable.cell);
                                }else{
                                    setTextview(donnee, R.drawable.cell);
                                }

                                donnee.setPadding(50,50,50,50);
                                if(Build.VERSION.SDK_INT >= 17){
                                    setTextGravity17(donnee);
                                }else{
                                    setTextGravity(donnee);
                                }

                                if(Build.VERSION.SDK_INT >= 23){
                                    setTextviewColor23(donnee, R.color.colorBlack);
                                }else{
                                    setTextviewColor(donnee, R.color.colorBlack);
                                }

                                row.addView(donnee);
                            //Default marbles
                            }else{
                                donnee = new TextView(tblBilles.getContext());
                                donnee.setText(defaultBillesOrCompteur);

                                if(Build.VERSION.SDK_INT >= 21) {
                                    setTextview21(donnee, R.drawable.cell);
                                }else if(Build.VERSION.SDK_INT >= 16){
                                    setTextview16(donnee, R.drawable.cell);
                                }else{
                                    setTextview(donnee, R.drawable.cell);
                                }

                                donnee.setPadding(50,50,50,50);
                                if(Build.VERSION.SDK_INT >= 17){
                                    setTextGravity17(donnee);
                                }else{
                                    setTextGravity(donnee);
                                }

                                if(Build.VERSION.SDK_INT >= 23){
                                    setTextviewColor23(donnee, R.color.colorBlack);
                                }else{
                                    setTextviewColor(donnee, R.color.colorBlack);
                                }
                                row.addView(donnee);
                            }
                        }
                    }else{
                        for (String ignored : ParseJSONCours.no) {
                            donnee = new TextView(tblBilles.getContext());
                            donnee.setText(defaultBillesOrCompteur);

                            if (Build.VERSION.SDK_INT >= 21) {
                                setTextview21(donnee, R.drawable.cell);
                            } else if (Build.VERSION.SDK_INT >= 16) {
                                setTextview16(donnee, R.drawable.cell);
                            } else {
                                setTextview(donnee, R.drawable.cell);
                            }

                            donnee.setPadding(50, 50, 50, 50);
                            if (Build.VERSION.SDK_INT >= 17) {
                                setTextGravity17(donnee);
                            } else {
                                setTextGravity(donnee);
                            }

                            if (Build.VERSION.SDK_INT >= 23) {
                                setTextviewColor23(donnee, R.color.colorBlack);
                            } else {
                                setTextviewColor(donnee, R.color.colorBlack);
                            }
                            row.addView(donnee);
                        }
                    }

                    tblBilles.addView(row);
                }
            }
            //mables and counter
            else
            {
                tblBilles.removeAllViews();
                ParseJSONCompteurAndBilles pjco = new ParseJSONCompteurAndBilles(json);
                pjco.parseJSON();

                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");

                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }

                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for (int i = 0; i < ParseJSONCours.no.length; i++) {

                    txtno = new TextView(tblBilles.getContext());

                    txtno.setText(ParseJSONCours.no[i]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txtno, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(txtno, R.drawable.cell_title);
                    } else {
                        setTextview(txtno, R.drawable.cell_title);
                    }
                    txtno.setPadding(50, 50, 50, 50);
                    if (Build.VERSION.SDK_INT >= 17) {
                        setTextGravity17(txtno);
                    } else {
                        setTextGravity(txtno);
                    }

                    row.addView(txtno);
                }

                tblBilles.addView(row);

                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }
                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for (int j = 0; j < ParseJSONCours.titre.length; j++) {

                    txttitre = new TextView(tblBilles.getContext());

                    txttitre.setText(ParseJSONCours.titre[j]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txttitre, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(txttitre, R.drawable.cell_title);
                    } else {
                        setTextview(txttitre, R.drawable.cell_title);
                    }

                    txttitre.setPadding(50, 50, 50, 50);
                    if (Build.VERSION.SDK_INT >= 17) {
                        setTextGravity17(txttitre);
                    } else {
                        setTextGravity(txttitre);
                    }

                    row.addView(txttitre);
                }

                tblBilles.addView(row);

                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");

                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }

                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for(String ignored : ParseJSONCours.no) {

                    lblBandC = new TextView(tblBilles.getContext());

                    lblBandC.setText(getResources().getString(R.string.lblBandC));

                    if (ParseJSONCompteurAndBilles.afficherBid) {
                        lblBandC.setText(getResources().getString(R.string.lblBandCandBid));
                    }

                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(lblBandC, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(lblBandC, R.drawable.cell_title);
                    } else {
                        setTextview(lblBandC, R.drawable.cell_title);
                    }
                    lblBandC.setPadding(50, 50, 50, 50);
                    if (Build.VERSION.SDK_INT >= 17) {
                        setTextGravity17(lblBandC);
                    } else {
                        setTextGravity(lblBandC);
                    }

                    row.addView(lblBandC);
                }

                tblBilles.addView(row);
                for (String alias : ParseJSONProf.allAlias) {
                    row = new TableRow(tblBilles.getContext());
                    nomProf = new TextView(tblBilles.getContext());
                    nomProf.setText(alias);
                    if(Build.VERSION.SDK_INT >= 21) {
                        setTextview21(nomProf, R.drawable.cell_title);
                    }else if(Build.VERSION.SDK_INT >= 16){
                        setTextview16(nomProf, R.drawable.cell_title);
                    }else{
                        setTextview(nomProf, R.drawable.cell_title);
                    }
                    nomProf.setPadding(50,50,50,50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(nomProf);
                    }else{
                        setTextGravity(nomProf);
                    }

                    if(Build.VERSION.SDK_INT >= 23){
                        setTextviewColor23(nomProf, R.color.colorBlack);
                    }else{
                        setTextviewColor(nomProf, R.color.colorBlack);
                    }

                    row.addView(nomProf);

                    //If we have data for each teacher, else we show default value
                    if(ParseJSONCompteurAndBilles.getProfHaveCours(alias))
                    {
                        Prof profTemp = ParseJSONCompteurAndBilles.getProf(alias);
                        for (int k = 0; k<ParseJSONCours.no.length; k++) {
                            //If we have data for a specific course
                            if(profTemp.getCoursExiste(ParseJSONCours.no[k]) != null)
                            {
                                Cours coursTemp = profTemp.getCoursExiste(ParseJSONCours.no[k]);
                                donnee = new TextView(tblBilles.getContext());

                                String affichageBille = coursTemp.getBilles() + " / " + coursTemp.getCompteur();
                                donnee.setText(affichageBille);

                                if(ParseJSONCompteurAndBilles.afficherBid)
                                {
                                    affichageBille += " / " + coursTemp.getBid();
                                    donnee.setText(affichageBille);
                                }

                                if(Build.VERSION.SDK_INT >= 21) {
                                    setTextview21(donnee, R.drawable.cell);
                                }else if(Build.VERSION.SDK_INT >= 16){
                                    setTextview16(donnee, R.drawable.cell);
                                }else{
                                    setTextview(donnee, R.drawable.cell);
                                }

                                donnee.setPadding(50,50,50,50);
                                if(Build.VERSION.SDK_INT >= 17){
                                    setTextGravity17(donnee);
                                }else{
                                    setTextGravity(donnee);
                                }

                                if(Build.VERSION.SDK_INT >= 23){
                                    setTextviewColor23(donnee, R.color.colorBlack);
                                }else{
                                    setTextviewColor(donnee, R.color.colorBlack);
                                }

                                row.addView(donnee);
                            //Default marbles and counter
                            }else{
                                donnee = new TextView(tblCompteur.getContext());
                                donnee.setText(defaultBillesAndCompteur);

                                //Default marbles, counter and bid
                                if(ParseJSONCompteurAndBilles.afficherBid)
                                {
                                    donnee.setText(defaultBillesAndCompteurAndBid);
                                }

                                if(Build.VERSION.SDK_INT >= 21) {
                                    setTextview21(donnee, R.drawable.cell);
                                }else if(Build.VERSION.SDK_INT >= 16){
                                    setTextview16(donnee, R.drawable.cell);
                                }else{
                                    setTextview(donnee, R.drawable.cell);
                                }

                                donnee.setPadding(50,50,50,50);
                                if(Build.VERSION.SDK_INT >= 17){
                                    setTextGravity17(donnee);
                                }else{
                                    setTextGravity(donnee);
                                }

                                if(Build.VERSION.SDK_INT >= 23){
                                    setTextviewColor23(donnee, R.color.colorBlack);
                                }else{
                                    setTextviewColor(donnee, R.color.colorBlack);
                                }
                                row.addView(donnee);
                            }
                        }
                    //default counter and marbles
                    }else{
                        for (String ignored : ParseJSONCours.no) {
                            donnee = new TextView(tblBilles.getContext());
                            donnee.setText(defaultBillesAndCompteur);

                            //Default marbles, counter and bid
                            if (ParseJSONCompteurAndBilles.afficherBid) {
                                donnee.setText(defaultBillesAndCompteurAndBid);
                            }

                            if (Build.VERSION.SDK_INT >= 21) {
                                setTextview21(donnee, R.drawable.cell);
                            } else if (Build.VERSION.SDK_INT >= 16) {
                                setTextview16(donnee, R.drawable.cell);
                            } else {
                                setTextview(donnee, R.drawable.cell);
                            }

                            donnee.setPadding(50, 50, 50, 50);
                            if (Build.VERSION.SDK_INT >= 17) {
                                setTextGravity17(donnee);
                            } else {
                                setTextGravity(donnee);
                            }

                            if (Build.VERSION.SDK_INT >= 23) {
                                setTextviewColor23(donnee, R.color.colorBlack);
                            } else {
                                setTextviewColor(donnee, R.color.colorBlack);
                            }
                            row.addView(donnee);
                        }
                    }

                    tblBilles.addView(row);
                }
            }
        }
        //choice tab
        else if(currentTab.matches("2"))
        {

            tblChoice.removeAllViews();

            ParseJSONChoice pjc = new ParseJSONChoice(json);
            pjc.parseJSON();

            if(ParseJSONChoiceFait.nbChoix[currentSpinnerItemSelected])
            {


                anneeChoix = new TextView(tblChoice.getContext());
                if (Build.VERSION.SDK_INT < 23) {
                    setTextApparence(anneeChoix,R.style.CustomTitleText);
                } else {
                    setTextApparence23(anneeChoix,R.style.CustomTitleText);
                }
                String titleAnnees = getResources().getString(R.string.choiceTitle)+ " " + ParseJSONChoiceFait.annee[0];
                anneeChoix.setText(titleAnnees);
                anneeChoix.setPadding(0,50,0,50);
                anneeChoix.setGravity(Gravity.CENTER_HORIZONTAL);
                tblChoice.addView(anneeChoix);

                row = new TableRow(tblChoice.getContext());
                nolbl = new TextView(tblChoice.getContext());
                titrelbl = new TextView(tblChoice.getContext());
                prioritylbl = new TextView(tblChoice.getContext());

                nolbl.setText(getResources().getString(R.string.nolbl));
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(nolbl, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(nolbl, R.drawable.cell_title);
                }else{
                    setTextview(nolbl, R.drawable.cell_title);
                }
                nolbl.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(nolbl);
                }else{
                    setTextGravity(nolbl);
                }
                titrelbl.setText(getResources().getString(R.string.titrelbl));
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(titrelbl, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(titrelbl, R.drawable.cell_title);
                }else{
                    setTextview(titrelbl, R.drawable.cell_title);
                }
                titrelbl.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(titrelbl);
                }else{
                    setTextGravity(titrelbl);
                }
                prioritylbl.setText(getResources().getString(R.string.prioritylbl));
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(prioritylbl, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(prioritylbl, R.drawable.cell_title);
                }else{
                    setTextview(prioritylbl, R.drawable.cell_title);
                }
                prioritylbl.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(prioritylbl);
                }else{
                    setTextGravity(prioritylbl);
                }


                row.addView(nolbl);
                row.addView(titrelbl);
                row.addView(prioritylbl);

                tblChoice.addView(row);



                for (int i = 0; i < ParseJSONChoice.arrayLength; i++) {
                    row = new TableRow(tblChoice.getContext());
                    txtno = new TextView(tblChoice.getContext());
                    txtno.setText(ParseJSONChoice.cours.get(currentSpinnerItemSelected).get(0)[i]);
                    if(Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txtno, R.drawable.cell);
                    }else if(Build.VERSION.SDK_INT >= 16){
                        setTextview16(txtno, R.drawable.cell);
                    }else{
                        setTextview(txtno, R.drawable.cell);
                    }
                    txtno.setPadding(50,50,50,50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(txtno);
                    }else{
                        setTextGravity(txtno);
                    }
                    if(Build.VERSION.SDK_INT >= 23){
                        setTextviewColor23(txtno, R.color.colorBlack);
                    }else{
                        setTextviewColor(txtno, R.color.colorBlack);
                    }
                    row.addView(txtno);
                    txttitre = new TextView(tblChoice.getContext());
                    txttitre.setText(ParseJSONChoice.cours.get(currentSpinnerItemSelected).get(1)[i]);
                    if(Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txttitre, R.drawable.cell);
                    }else if(Build.VERSION.SDK_INT >= 16){
                        setTextview16(txttitre, R.drawable.cell);
                    }else{
                        setTextview(txttitre, R.drawable.cell);
                    }
                    txttitre.setPadding(50,50,50,50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(txttitre);
                    }else{
                        setTextGravity(txttitre);
                    }
                    if(Build.VERSION.SDK_INT >= 23){
                        setTextviewColor23(txttitre, R.color.colorBlack);
                    }else{
                        setTextviewColor(txttitre, R.color.colorBlack);
                    }
                    row.addView(txttitre);
                    txtpriority = new TextView(tblChoice.getContext());
                    txtpriority.setText(ParseJSONChoice.cours.get(currentSpinnerItemSelected).get(2)[i]);
                    if(Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txtpriority, R.drawable.cell);
                    }else if(Build.VERSION.SDK_INT >= 16){
                        setTextview16(txtpriority, R.drawable.cell);
                    }else{
                        setTextview(txtpriority, R.drawable.cell);
                    }
                    txtpriority.setPadding(50,50,50,50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(txtpriority);
                    }else{
                        setTextGravity(txtpriority);
                    }
                    if(Build.VERSION.SDK_INT >= 23){
                        setTextviewColor23(txtpriority, R.color.colorBlack);
                    }else{
                        setTextviewColor(txtpriority, R.color.colorBlack);
                    }
                    row.addView(txtpriority);
                    tblChoice.addView(row);
                }
            }else{
                errorLoadingTextChoice.setText(getResources().getString(R.string.errorChoice));
            }


        }else
        {
            tblCompteur.removeAllViews();

            if(currentListItemSelected == 1)
            {
                tblCompteur.removeAllViews();
                ParseJSONCompteurAndBilles pjco = new ParseJSONCompteurAndBilles(json);
                pjco.parseJSON();

                row = new TableRow(tblChoice.getContext());

                txtvide = new TextView(tblChoice.getContext());
                txtvide.setText("");
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }
                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for (int i = 0; i < ParseJSONCours.no.length; i++) {

                    txtno = new TextView(tblChoice.getContext());

                    txtno.setText(ParseJSONCours.no[i]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txtno, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(txtno, R.drawable.cell_title);
                    } else {
                        setTextview(txtno, R.drawable.cell_title);
                    }
                    txtno.setPadding(50, 50, 50, 50);
                    if (Build.VERSION.SDK_INT >= 17) {
                        setTextGravity17(txtno);
                    } else {
                        setTextGravity(txtno);
                    }

                    row.addView(txtno);
                }

                tblCompteur.addView(row);

                row = new TableRow(tblChoice.getContext());

                txtvide = new TextView(tblChoice.getContext());
                txtvide.setText("");
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }
                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for (int j = 0; j < ParseJSONCours.titre.length; j++) {

                    txttitre = new TextView(tblChoice.getContext());

                    txttitre.setText(ParseJSONCours.titre[j]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txttitre, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(txttitre, R.drawable.cell_title);
                    } else {
                        setTextview(txttitre, R.drawable.cell_title);
                    }
                    txttitre.setPadding(50, 50, 50, 50);
                    if (Build.VERSION.SDK_INT >= 17) {
                        setTextGravity17(txttitre);
                    } else {
                        setTextGravity(txttitre);
                    }

                    row.addView(txttitre);
                }

                tblCompteur.addView(row);

                for (String alias : ParseJSONProf.allAlias) {
                    row = new TableRow(tblCompteur.getContext());
                    nomProf = new TextView(tblCompteur.getContext());
                    nomProf.setText(alias);
                    if(Build.VERSION.SDK_INT >= 21) {
                        setTextview21(nomProf, R.drawable.cell_title);
                    }else if(Build.VERSION.SDK_INT >= 16){
                        setTextview16(nomProf, R.drawable.cell_title);
                    }else{
                        setTextview(nomProf, R.drawable.cell_title);
                    }
                    nomProf.setPadding(50,50,50,50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(nomProf);
                    }else{
                        setTextGravity(nomProf);
                    }

                    if(Build.VERSION.SDK_INT >= 23){
                        setTextviewColor23(nomProf, R.color.colorBlack);
                    }else{
                        setTextviewColor(nomProf, R.color.colorBlack);
                    }

                    row.addView(nomProf);

                    //If we have data for each teacher, else we show default value
                    if(ParseJSONCompteurAndBilles.getProfHaveCours(alias))
                    {
                        Prof profTemp = ParseJSONCompteurAndBilles.getProf(alias);
                        for (int k = 0; k<ParseJSONCours.no.length; k++) {
                            //If we have data for a specific course
                            if(profTemp.getCoursExiste(ParseJSONCours.no[k]) != null)
                            {
                                Cours coursTemp = profTemp.getCoursExiste(ParseJSONCours.no[k]);
                                donnee = new TextView(tblCompteur.getContext());
                                String affichageCompteur = "" + coursTemp.getCompteur();
                                donnee.setText(affichageCompteur);

                                if(Build.VERSION.SDK_INT >= 21) {
                                    setTextview21(donnee, R.drawable.cell);
                                }else if(Build.VERSION.SDK_INT >= 16){
                                    setTextview16(donnee, R.drawable.cell);
                                }else{
                                    setTextview(donnee, R.drawable.cell);
                                }

                                donnee.setPadding(50,50,50,50);
                                if(Build.VERSION.SDK_INT >= 17){
                                    setTextGravity17(donnee);
                                }else{
                                    setTextGravity(donnee);
                                }

                                if(Build.VERSION.SDK_INT >= 23){
                                    setTextviewColor23(donnee, R.color.colorBlack);
                                }else{
                                    setTextviewColor(donnee, R.color.colorBlack);
                                }

                                row.addView(donnee);
                            //Default counter
                            }else{
                                donnee = new TextView(tblCompteur.getContext());
                                donnee.setText(defaultBillesOrCompteur);

                                if(Build.VERSION.SDK_INT >= 21) {
                                    setTextview21(donnee, R.drawable.cell);
                                }else if(Build.VERSION.SDK_INT >= 16){
                                    setTextview16(donnee, R.drawable.cell);
                                }else{
                                    setTextview(donnee, R.drawable.cell);
                                }

                                donnee.setPadding(50,50,50,50);
                                if(Build.VERSION.SDK_INT >= 17){
                                    setTextGravity17(donnee);
                                }else{
                                    setTextGravity(donnee);
                                }

                                if(Build.VERSION.SDK_INT >= 23){
                                    setTextviewColor23(donnee, R.color.colorBlack);
                                }else{
                                    setTextviewColor(donnee, R.color.colorBlack);
                                }
                                row.addView(donnee);
                            }
                        }
                    //Default counter
                    }else{
                        for (String ignored : ParseJSONCours.no) {
                            donnee = new TextView(tblCompteur.getContext());
                            donnee.setText(defaultBillesOrCompteur);

                            if (Build.VERSION.SDK_INT >= 21) {
                                setTextview21(donnee, R.drawable.cell);
                            } else if (Build.VERSION.SDK_INT >= 16) {
                                setTextview16(donnee, R.drawable.cell);
                            } else {
                                setTextview(donnee, R.drawable.cell);
                            }

                            donnee.setPadding(50, 50, 50, 50);
                            if (Build.VERSION.SDK_INT >= 17) {
                                setTextGravity17(donnee);
                            } else {
                                setTextGravity(donnee);
                            }

                            if (Build.VERSION.SDK_INT >= 23) {
                                setTextviewColor23(donnee, R.color.colorBlack);
                            } else {
                                setTextviewColor(donnee, R.color.colorBlack);
                            }
                            row.addView(donnee);
                        }
                    }

                    tblCompteur.addView(row);
                }
            }
            else
            {
                tblCompteur.removeAllViews();
                ParseJSONCompteurAndBilles pjco = new ParseJSONCompteurAndBilles(json);
                pjco.parseJSON();

                row = new TableRow(tblChoice.getContext());

                txtvide = new TextView(tblChoice.getContext());
                txtvide.setText("");
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }
                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for (int i = 0; i < ParseJSONCours.no.length; i++) {

                    txtno = new TextView(tblCompteur.getContext());

                    txtno.setText(ParseJSONCours.no[i]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txtno, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(txtno, R.drawable.cell_title);
                    } else {
                        setTextview(txtno, R.drawable.cell_title);
                    }
                    txtno.setPadding(50, 50, 50, 50);
                    if (Build.VERSION.SDK_INT >= 17) {
                        setTextGravity17(txtno);
                    } else {
                        setTextGravity(txtno);
                    }

                    row.addView(txtno);
                }

                tblCompteur.addView(row);

                row = new TableRow(tblCompteur.getContext());

                txtvide = new TextView(tblCompteur.getContext());
                txtvide.setText("");
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }
                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for (int j = 0; j < ParseJSONCours.titre.length; j++) {

                    txttitre = new TextView(tblCompteur.getContext());

                    txttitre.setText(ParseJSONCours.titre[j]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(txttitre, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(txttitre, R.drawable.cell_title);
                    } else {
                        setTextview(txttitre, R.drawable.cell_title);
                    }
                    txttitre.setPadding(50, 50, 50, 50);
                    if (Build.VERSION.SDK_INT >= 17) {
                        setTextGravity17(txttitre);
                    } else {
                        setTextGravity(txttitre);
                    }

                    row.addView(txttitre);
                }

                tblCompteur.addView(row);

                row = new TableRow(tblCompteur.getContext());

                txtvide = new TextView(tblCompteur.getContext());
                txtvide.setText("");
                if(Build.VERSION.SDK_INT >= 21) {
                    setTextview21(txtvide, R.drawable.cell_title);
                }else if(Build.VERSION.SDK_INT >= 16){
                    setTextview16(txtvide, R.drawable.cell_title);
                }else{
                    setTextview(txtvide, R.drawable.cell_title);
                }
                txtvide.setPadding(50,50,50,50);
                if(Build.VERSION.SDK_INT >= 17){
                    setTextGravity17(txtvide);
                }else{
                    setTextGravity(txtvide);
                }

                row.addView(txtvide);

                for (String ignored : ParseJSONCours.no) {

                    lblCandB = new TextView(tblCompteur.getContext());

                    lblCandB.setText(getResources().getString(R.string.lblCandB));

                    if (ParseJSONCompteurAndBilles.afficherBid) {
                        lblCandB.setText(getResources().getString(R.string.lblCandBandBid));
                    }

                    if (Build.VERSION.SDK_INT >= 21) {
                        setTextview21(lblCandB, R.drawable.cell_title);
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        setTextview16(lblCandB, R.drawable.cell_title);
                    } else {
                        setTextview(lblCandB, R.drawable.cell_title);
                    }
                    lblCandB.setPadding(50, 50, 50, 50);
                    if (Build.VERSION.SDK_INT >= 17) {
                        setTextGravity17(lblCandB);
                    } else {
                        setTextGravity(lblCandB);
                    }

                    row.addView(lblCandB);
                }

                tblCompteur.addView(row);

                for (String alias : ParseJSONProf.allAlias) {
                    row = new TableRow(tblCompteur.getContext());
                    nomProf = new TextView(tblCompteur.getContext());
                    nomProf.setText(alias);
                    if(Build.VERSION.SDK_INT >= 21) {
                        setTextview21(nomProf, R.drawable.cell_title);
                    }else if(Build.VERSION.SDK_INT >= 16){
                        setTextview16(nomProf, R.drawable.cell_title);
                    }else{
                        setTextview(nomProf, R.drawable.cell_title);
                    }
                    nomProf.setPadding(50,50,50,50);
                    if(Build.VERSION.SDK_INT >= 17){
                        setTextGravity17(nomProf);
                    }else{
                        setTextGravity(nomProf);
                    }

                    if(Build.VERSION.SDK_INT >= 23){
                        setTextviewColor23(nomProf, R.color.colorBlack);
                    }else{
                        setTextviewColor(nomProf, R.color.colorBlack);
                    }

                    row.addView(nomProf);

                    //If we have data for each teacher, else we show default value
                    if(ParseJSONCompteurAndBilles.getProfHaveCours(alias))
                    {
                        Prof profTemp = ParseJSONCompteurAndBilles.getProf(alias);
                        for (int k = 0; k<ParseJSONCours.no.length; k++) {
                            //If we have data for a specific course
                            if(profTemp.getCoursExiste(ParseJSONCours.no[k]) != null)
                            {
                                Cours coursTemp = profTemp.getCoursExiste(ParseJSONCours.no[k]);
                                donnee = new TextView(tblCompteur.getContext());
                                String affichageDonnesCandB = coursTemp.getCompteur() + " / " + coursTemp.getBilles();
                                donnee.setText(affichageDonnesCandB);

                                if(ParseJSONCompteurAndBilles.afficherBid)
                                {
                                    affichageDonnesCandB += " / " + coursTemp.getBid();
                                    donnee.setText(affichageDonnesCandB);
                                }

                                if(Build.VERSION.SDK_INT >= 21) {
                                    setTextview21(donnee, R.drawable.cell);
                                }else if(Build.VERSION.SDK_INT >= 16){
                                    setTextview16(donnee, R.drawable.cell);
                                }else{
                                    setTextview(donnee, R.drawable.cell);
                                }

                                donnee.setPadding(50,50,50,50);
                                if(Build.VERSION.SDK_INT >= 17){
                                    setTextGravity17(donnee);
                                }else{
                                    setTextGravity(donnee);
                                }

                                if(Build.VERSION.SDK_INT >= 23){
                                    setTextviewColor23(donnee, R.color.colorBlack);
                                }else{
                                    setTextviewColor(donnee, R.color.colorBlack);
                                }

                                row.addView(donnee);
                            //Default Counter, marbles
                            }else{
                                donnee = new TextView(tblCompteur.getContext());
                                donnee.setText(defaultBillesAndCompteur);

                                //Default Counter, marbles and bid
                                if(ParseJSONCompteurAndBilles.afficherBid)
                                {
                                    donnee.setText(defaultBillesAndCompteurAndBid);
                                }

                                if(Build.VERSION.SDK_INT >= 21) {
                                    setTextview21(donnee, R.drawable.cell);
                                }else if(Build.VERSION.SDK_INT >= 16){
                                    setTextview16(donnee, R.drawable.cell);
                                }else{
                                    setTextview(donnee, R.drawable.cell);
                                }

                                donnee.setPadding(50,50,50,50);
                                if(Build.VERSION.SDK_INT >= 17){
                                    setTextGravity17(donnee);
                                }else{
                                    setTextGravity(donnee);
                                }

                                if(Build.VERSION.SDK_INT >= 23){
                                    setTextviewColor23(donnee, R.color.colorBlack);
                                }else{
                                    setTextviewColor(donnee, R.color.colorBlack);
                                }
                                row.addView(donnee);
                            }
                        }
                    //Default Counter, marbles
                    }else{
                        for (String ignored : ParseJSONCours.no) {
                            donnee = new TextView(tblCompteur.getContext());
                            donnee.setText(defaultBillesAndCompteur);

                            //Default Counter, marbles and bid
                            if (ParseJSONCompteurAndBilles.afficherBid) {
                                donnee.setText(defaultBillesAndCompteurAndBid);
                            }

                            if (Build.VERSION.SDK_INT >= 21) {
                                setTextview21(donnee, R.drawable.cell);
                            } else if (Build.VERSION.SDK_INT >= 16) {
                                setTextview16(donnee, R.drawable.cell);
                            } else {
                                setTextview(donnee, R.drawable.cell);
                            }

                            donnee.setPadding(50, 50, 50, 50);
                            if (Build.VERSION.SDK_INT >= 17) {
                                setTextGravity17(donnee);
                            } else {
                                setTextGravity(donnee);
                            }

                            if (Build.VERSION.SDK_INT >= 23) {
                                setTextviewColor23(donnee, R.color.colorBlack);
                            } else {
                                setTextviewColor(donnee, R.color.colorBlack);
                            }
                            row.addView(donnee);
                        }
                    }

                    tblCompteur.addView(row);
                }
            }

        }

    }

    /**
     *  Create the menu by binding the xml file with the good item
     *
     * @param menu - the menu to create
     *
     * @return boolean - creation of each menu item succeed or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *  Specified what to do when the user selected an item in the menu.
     *
     * @param item - item in the menu
     *
     * @return boolean - if the item is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Setting.class);
            startActivityForResult(intent, PICK_CONTACT_REQUEST);
        } else if (id == R.id.action_logout) {
            new AlertDialog.Builder(ConsultPanel.this)
                    .setTitle(getResources().getString(R.string.lblLogout))
                    .setMessage(getResources().getString(R.string.lblLogoutDetail))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            closeActivity();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method who close the current activity
     */
    private void closeActivity(){

         this.finish();
    }

    /**
     * Set The default language at the end of the activity about the selection of prefered language
     *
     * @param requestCode - the code when requested
     * @param resultCode -  the code when the request as ended
     * @param data - data return at the end of the request
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setLangRecreate(Setting.langue);
    }

    /**
     * This method set a new language for the application and reload the view to show the change
     *
     * @param langval - the prefixe about the language ex : fr, en
     */
    public void setLangRecreate(String langval) {

        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    /**
     * Called when a key down event has occurred. He don't come back to the previous activity because of the login implementation ( the user
     * need to logout to come back )
     *
     * @param keyCode -  The value in event.getKeyCode().
     * @param event -   Description of the key event.
     *
     * @return boolean - true on Keydown pressed
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        boolean keyPressed = false;

        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            keyPressed = true;
        }

        return keyPressed;
    }

    /**
     * Method who add request to the queue
     *
     * @param request - StringRequest
     */
    private void stringRequest(StringRequest request) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ConsultPanel.this);
        }
            requestQueue.add(request);
    }

    /**
     * Method who add request to the queue
     *
     * @param request - ObjectRequest
     */
    private void objectRequest(JsonObjectRequest request) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ConsultPanel.this);
        }
            requestQueue.add(request);
    }


}
