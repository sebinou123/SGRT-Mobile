package sfadmldb.sgrt;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class consultPanel extends AppCompatActivity {

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

    //List for each view
    ListView lstBille;
    ListView lstCompteur;

    //Array to put in listview
    private String[] arrayBille;
    private String[] arrayCompteur;
    private String[] arrayNoCours = {"604","603","202", "123","607"};
    private String[] arrayTitre = { "Programmation orienté objet I","Programmation orienté objet II","Programmation orienté objet III", "Programmation d\'interfacce","Sécurité" } ;
    private String[] arrayPriority = { "1","2","3", "4","5" };
    private String[] arrayNomProf = { "SF","ML","DB", "JF","AD", "DC" };
    private String[] array1 = { "","3","0", "","1" };
    private String[] array2 = { "1","","", "","5" };
    private String[] array3 = { "1","0","", "3","3" };
    private String[] array4 = { "","1","", "",""};
    private String[] array5 = { "","","1", "","" };
    private String[] array6 = { "0","","0", "3","5" };
    private List<String[]> arrayDonnecompteur = new ArrayList();
    private String[] array7 = { "6","0","0", "","0" };
    private String[] array8 = { "0","10","1", "","0" };
    private String[] array9 = { "0","0","23", "0","0" };
    private String[] array10 = { "2","0","", "",""};
    private String[] array11 = { "","","0", "","1" };
    private String[] array12 = { "0","","0", "0","0" };
    private List<String[]> arrayDonnebille = new ArrayList();

    private ProgressBar progressbarChoix;


    //url to the server
    public static final String url = "http://www.info.climoilou.qc.ca/E2016/420-669-LI/420-669-E16-02/production/SGRT/public/";

    static final int PICK_CONTACT_REQUEST = 1;  // The request code




    /*
     * Method called when the application start. This method set the view, instantiate every element and add them to the view. Also,
     * he set the current language to the application. Add event handler when a button is pressed.
     *
     * @param savedInstanceState - a save of previous state, who can be reloaded by passing it to the method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(secure.checkRootMethod() == false)
        {
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

        toolbar.setTitle("SGRT");
        tblChoice = (TableLayout) findViewById(R.id.tblChoice);
        tblBilles = (TableLayout) findViewById(R.id.tblBilles);
        tblCompteur = (TableLayout) findViewById(R.id.tblCompteur);

        setSupportActionBar(toolbar);
        BarListener listener = new BarListener();

        tabHost = (TabHost)findViewById(R.id.tabHost);
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

        ListListener lstListener = new ListListener();

        arrayBille = new String[2];
        arrayBille[0] = getResources().getString(R.string.lstBille1);
        arrayBille[1] = getResources().getString(R.string.lstBille2);

        ArrayAdapter<String> myAdapterBille=new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayBille);
        lstBille=(ListView) findViewById(R.id.listBille);
        lstBille.setAdapter(myAdapterBille);
        lstBille.setOnItemClickListener(lstListener);

        arrayCompteur = new String[2];
        arrayCompteur[0] = getResources().getString(R.string.lstCompteur1);
        arrayCompteur[1] = getResources().getString(R.string.lstCompteur2);

        ArrayAdapter<String> myAdapterCompteur=new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayCompteur);
        lstCompteur=(ListView) findViewById(R.id.listCompteur);
        lstCompteur.setAdapter(myAdapterCompteur);
        lstCompteur.setOnItemClickListener(lstListener);

        progressbarChoix = (ProgressBar) findViewById(R.id.progressBarChoix);
        progressbarChoix.setVisibility(View.INVISIBLE);

            TabWidget widget = tabHost.getTabWidget();
            for(int i = 0; i < widget.getChildCount(); i++) {
                View v = widget.getChildAt(i);
                v.setBackgroundResource(R.drawable.apptheme_tab_indicator_holo);
            }
        }
    }

    /**
     * Inner class who implements the OnItemClickListener for the button
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

            String  itemValue = "";

            if(parent.getId() == lstBille.getId())
            {

                itemValue = (String) lstBille.getItemAtPosition(position);

                if(itemValue.matches(getResources().getString(R.string.lstBille1)))
                {
                    currentListItemSelected = 1;
                    showJSON("");
                    //currentPath = "bille/########";
                    //sendRequestPostBilleOnly();
                }else if(itemValue.matches(getResources().getString(R.string.lstBille2)))
                {
                    currentListItemSelected = 2;
                    showJSON("");
                    //currentPath = "bille#########";
                    //sendRequestPostBilleAndCompteur()
                }else
                {
                }

            }
            else if(parent.getId() == lstCompteur.getId())
            {

                itemValue = (String) lstCompteur.getItemAtPosition(position);

                if(itemValue.matches(getResources().getString(R.string.lstCompteur1)))
                {
                    currentListItemSelected = 1;
                    showJSON("");
                    //currentPath = "compteur/########";
                    //sendRequestPostCompteurOnly();
                }else if(itemValue.matches(getResources().getString(R.string.lstCompteur2)))
                {
                    currentListItemSelected = 2;
                    showJSON("");
                    //currentPath = "compteur/########";
                    //sendRequestPostCompteurAndBille()
                }else
                {
                }
            }
            else{

            }

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

            }
            else if(tabId.matches(tab2name))
            {
                tblChoice.removeAllViews();
               currentTab = "2";
               currentPath = "choix/choixStatus";
               sendRequestPostChoix(currentPath);

            }
            else
            {
                tblCompteur.removeAllViews();
                currentTab = "3";

            }
        }
    }




    /**
     * Method to send request to the web server to get response about if the user is valid and exist in the LDAP directory
     * Create an object user with his information (id, name, email) if it's correct and the consult panel is instantiate.
     * Show an error if the user is not in the database
     *
     * @param path - the path on the server
     */
    private void sendRequestPostChoix(String path){

        progressbarChoix.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user.getUser().getId());
        map.put("web", "true");

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, consultPanel.url + path, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {

                progressbarChoix.setVisibility(View.INVISIBLE);

                    ParseJSONChoiceFait pjcf = new ParseJSONChoiceFait(result);
                    pjcf.parseJSON();
                    if(ParseJSONChoiceFait.nbChoix[0])
                    {
                        currentPath = "choix/getChoix";
                        sendRequestPostChoixValide(currentPath);
                    }
                    else
                    {
                        TextView txtTemp = new TextView(tblChoice.getContext());
                        txtTemp.setText(getResources().getString(R.string.errorChoice));
                        tblChoice.addView(txtTemp);
                    }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(consultPanel.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", "Bearer " +  user.getUser().getToken());
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);

    }


    /**
     * Method who send a request to get for a teacher all of his choice.
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostChoixValide(String path){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressbarChoix.setVisibility(View.INVISIBLE);
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbarChoix.setVisibility(View.INVISIBLE);
                        TextView txtTemp = new TextView(tblChoice.getContext());
                        txtTemp.setText(getResources().getString(R.string.lblerrorWebService));
                        tblChoice.addView(txtTemp);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_id",user.getUser().getId());
                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Method who send a request to get counter of each teacher on each course
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostCompteurOnly(String path){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                         showJSON(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbarChoix.setVisibility(View.INVISIBLE);
                        TextView txtTemp = new TextView(tblChoice.getContext());
                        txtTemp.setText(getResources().getString(R.string.lblerrorWebService));
                        tblCompteur.addView(txtTemp);
                    }
                }){


            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Method who send a request to get nomber of ball for each course for each teacher
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostBilleOnly(String path){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + path ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbarChoix.setVisibility(View.INVISIBLE);
                        TextView txtTemp = new TextView(tblChoice.getContext());
                        txtTemp.setText(getResources().getString(R.string.lblerrorWebService));
                        tblBilles.addView(txtTemp);
                    }
                }){


            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Method who send a request to get for each teacher the number of ball and counter for each course.
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostBilleAndCompteur(String path){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url /**+ path*/ ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbarChoix.setVisibility(View.INVISIBLE);
                        TextView txtTemp = new TextView(tblChoice.getContext());
                        txtTemp.setText(getResources().getString(R.string.lblerrorWebService));
                        tblBilles.addView(txtTemp);
                    }
                }){


            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Method who send a request to get for each teacher the number of ball and counter for each course.
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostCompteurAndBille(String path){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url /**+ path*/ ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbarChoix.setVisibility(View.INVISIBLE);
                        TextView txtTemp = new TextView(tblChoice.getContext());
                        txtTemp.setText(getResources().getString(R.string.lblerrorWebService));
                        tblCompteur.addView(txtTemp);
                    }
                }){


            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " +  user.getUser().getToken());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Method to show the data according to the selected tab and list
     *
     * @param json - the json response
     */
    private void showJSON(String json){

        if(currentTab.matches("1")) {
            tblBilles.removeAllViews();
            if(currentListItemSelected == 1)
            {
                tblBilles.removeAllViews();
               /** ParseJSONBillesOnly pjb = new ParseJSONBillesOnly(json);
                pjb.parseJSON();*/

                TableRow row;
                TextView txtno;
                TextView txtvide;
                TextView txttitre;
                TextView nomProf;
                TextView donnee;
                arrayDonnecompteur.add(array1);
                arrayDonnecompteur.add(array2);
                arrayDonnecompteur.add(array3);
                arrayDonnecompteur.add(array4);
                arrayDonnecompteur.add(array5);
                arrayDonnecompteur.add(array6);


                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayNoCours.length; i++) {

                    txtno = new TextView(tblBilles.getContext());

                    txtno.setText(arrayNoCours[i]);
                    txtno.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    txtno.setPadding(50,50,50,50);
                    txtno.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(txtno);
                }

                tblBilles.addView(row);

                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayTitre.length; i++) {

                    txttitre = new TextView(tblBilles.getContext());

                    txttitre.setText(arrayTitre[i]);
                    txttitre.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    txttitre.setPadding(50,50,50,50);
                    txttitre.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(txttitre);
                }

                tblBilles.addView(row);

                for (int i = 0; i < 6; i++) {
                    row = new TableRow(tblBilles.getContext());
                    nomProf = new TextView(tblBilles.getContext());
                    nomProf.setText(arrayNomProf[i]);
                    nomProf.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    nomProf.setPadding(50,50,50,50);
                    nomProf.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    nomProf.setTextColor(getResources().getColor(R.color.colorBlack));
                    row.addView(nomProf);
                    for (int j = 0; j < 5; j++) {

                        donnee = new TextView(tblBilles.getContext());
                        donnee.setText(arrayDonnecompteur.get(i)[j]);
                        donnee.setBackground(getResources().getDrawable(R.drawable.cell,null));
                        donnee.setPadding(50,50,50,50);
                        donnee.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        donnee.setTextColor(getResources().getColor(R.color.colorBlack));
                        row.addView(donnee);
                    }
                    tblBilles.addView(row);;
                }
            }
            else if(currentListItemSelected == 2)
            {
                tblBilles.removeAllViews();
               /** ParseJSONBillesAndCompteur pjbac = new ParseJSONBillesAndCompteur(json);
                pjbac.parseJSON();*/
                TableRow row;
                TextView txtno;
                TextView txtvide;
                TextView txttitre;
                TextView nomProf;
                TextView donnee;
                TextView lblBandC;
                arrayDonnecompteur.add(array1);
                arrayDonnecompteur.add(array2);
                arrayDonnecompteur.add(array3);
                arrayDonnecompteur.add(array4);
                arrayDonnecompteur.add(array5);
                arrayDonnecompteur.add(array6);
                arrayDonnebille.add(array7);
                arrayDonnebille.add(array8);
                arrayDonnebille.add(array9);
                arrayDonnebille.add(array10);
                arrayDonnebille.add(array11);
                arrayDonnebille.add(array12);


                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayNoCours.length; i++) {

                    txtno = new TextView(tblBilles.getContext());

                    txtno.setText(arrayNoCours[i]);
                    txtno.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    txtno.setPadding(50,50,50,50);
                    txtno.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(txtno);
                }

                tblBilles.addView(row);

                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayTitre.length; i++) {

                    txttitre = new TextView(tblBilles.getContext());

                    txttitre.setText(arrayTitre[i]);
                    txttitre.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    txttitre.setPadding(50,50,50,50);
                    txttitre.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(txttitre);
                }

                tblBilles.addView(row);

                row = new TableRow(tblBilles.getContext());

                txtvide = new TextView(tblBilles.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayNoCours.length; i++) {

                    lblBandC = new TextView(tblBilles.getContext());

                    lblBandC.setText(getResources().getString(R.string.lblBandC));
                    lblBandC.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    lblBandC.setPadding(50,50,50,50);
                    lblBandC.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(lblBandC);
                }

                tblBilles.addView(row);

                for (int i = 0; i < 6; i++) {
                    row = new TableRow(tblBilles.getContext());
                    nomProf = new TextView(tblBilles.getContext());
                    nomProf.setText(arrayNomProf[i]);
                    nomProf.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    nomProf.setPadding(50,50,50,50);
                    nomProf.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    nomProf.setTextColor(getResources().getColor(R.color.colorBlack));
                    row.addView(nomProf);
                    for (int j = 0; j < 5; j++) {

                        donnee = new TextView(tblBilles.getContext());
                        donnee.setText(arrayDonnebille.get(i)[j] + " / " + arrayDonnecompteur.get(i)[j]);
                        donnee.setBackground(getResources().getDrawable(R.drawable.cell,null));
                        donnee.setPadding(50,50,50,50);
                        donnee.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        donnee.setTextColor(getResources().getColor(R.color.colorBlack));
                        row.addView(donnee);
                    }
                    tblBilles.addView(row);;
                }
            }else{}
        }
        else if(currentTab.matches("2"))
        {
            tblChoice.removeAllViews();

            ParseJSONChoice pjc = new ParseJSONChoice(json);
            pjc.parseJSON();

            TableRow row;
            TextView txtno;
            TextView txttitre;
            TextView txtpriority;
            TextView nolbl;
            TextView titrelbl;
            TextView prioritylbl;
            TextView anneeChoix;

            anneeChoix = new TextView(tblChoice.getContext());
            anneeChoix.setTextAppearance(this,R.style.CustomTitleText);
            anneeChoix.setText(getResources().getString(R.string.choiceTitle)+ " " + ParseJSONChoiceFait.annee[0]);
            anneeChoix.setPadding(0,50,0,50);
            anneeChoix.setGravity(Gravity.CENTER_HORIZONTAL);
            tblChoice.addView(anneeChoix);

            row = new TableRow(tblChoice.getContext());
            nolbl = new TextView(tblChoice.getContext());
            titrelbl = new TextView(tblChoice.getContext());
            prioritylbl = new TextView(tblChoice.getContext());

            nolbl.setText(getResources().getString(R.string.nolbl));
            nolbl.setBackground(getResources().getDrawable(R.drawable.cell_title));
            nolbl.setPadding(50,50,50,50);
            nolbl.setGravity(View.TEXT_ALIGNMENT_CENTER);
            titrelbl.setText(getResources().getString(R.string.titrelbl));
            titrelbl.setBackground(getResources().getDrawable(R.drawable.cell_title));
            titrelbl.setPadding(50,50,50,50);
            titrelbl.setGravity(View.TEXT_ALIGNMENT_CENTER);
            prioritylbl.setText(getResources().getString(R.string.prioritylbl));
            prioritylbl.setBackground(getResources().getDrawable(R.drawable.cell_title));
            prioritylbl.setPadding(50,50,50,50);
            prioritylbl.setGravity(View.TEXT_ALIGNMENT_CENTER);


            row.addView(nolbl);
            row.addView(titrelbl);
            row.addView(prioritylbl);

            tblChoice.addView(row);

            for (int i = 0; i < ParseJSONChoice.arrayLength; i++) {
                row = new TableRow(tblChoice.getContext());
                txtno = new TextView(tblChoice.getContext());
                txtno.setText(ParseJSONChoice.no[i]);
                txtno.setBackground(getResources().getDrawable(R.drawable.cell));
                txtno.setPadding(50,50,50,50);
                txtno.setGravity(View.TEXT_ALIGNMENT_CENTER);
                txtno.setTextColor(getResources().getColor(R.color.colorBlack));
                row.addView(txtno);
                txttitre = new TextView(tblChoice.getContext());
                txttitre.setText(ParseJSONChoice.titre[i]);
                txttitre.setBackground(getResources().getDrawable(R.drawable.cell));
                txttitre.setPadding(50,50,50,50);
                txttitre.setGravity(View.TEXT_ALIGNMENT_CENTER);
                txttitre.setTextColor(getResources().getColor(R.color.colorBlack));
                row.addView(txttitre);
                txtpriority = new TextView(tblChoice.getContext());
                txtpriority.setText(ParseJSONChoice.priority[i]);
                txtpriority.setBackground(getResources().getDrawable(R.drawable.cell));
                txtpriority.setPadding(50,50,50,50);
                txtpriority.setGravity(View.TEXT_ALIGNMENT_CENTER);
                txtpriority.setTextColor(getResources().getColor(R.color.colorBlack));
                row.addView(txtpriority);
                tblChoice.addView(row);
            }





        }else if(currentTab.matches("3"))
        {
            tblCompteur.removeAllViews();

            if(currentListItemSelected == 1)
            {
                tblCompteur.removeAllViews();
               /** ParseJSONCompteurOnly pjc = new ParseJSONCompteurOnly(json);
                pjc.parseJSON();*/

                TableRow row;
                TextView txtno;
                TextView txtvide;
                TextView txttitre;
                TextView nomProf;
                TextView donnee;
                arrayDonnecompteur.add(array1);
                arrayDonnecompteur.add(array2);
                arrayDonnecompteur.add(array3);
                arrayDonnecompteur.add(array4);
                arrayDonnecompteur.add(array5);
                arrayDonnecompteur.add(array6);


                row = new TableRow(tblChoice.getContext());

                txtvide = new TextView(tblChoice.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayNoCours.length; i++) {

                    txtno = new TextView(tblChoice.getContext());

                    txtno.setText(arrayNoCours[i]);
                    txtno.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    txtno.setPadding(50,50,50,50);
                    txtno.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(txtno);
                }

                tblCompteur.addView(row);

                row = new TableRow(tblChoice.getContext());

                txtvide = new TextView(tblChoice.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayTitre.length; i++) {

                    txttitre = new TextView(tblChoice.getContext());

                    txttitre.setText(arrayTitre[i]);
                    txttitre.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    txttitre.setPadding(50,50,50,50);
                    txttitre.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(txttitre);
                }

                tblCompteur.addView(row);

                for (int i = 0; i < 6; i++) {
                    row = new TableRow(tblCompteur.getContext());
                    nomProf = new TextView(tblCompteur.getContext());
                    nomProf.setText(arrayNomProf[i]);
                    nomProf.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    nomProf.setPadding(50,50,50,50);
                    nomProf.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    nomProf.setTextColor(getResources().getColor(R.color.colorBlack));
                    row.addView(nomProf);
                    for (int j = 0; j < 5; j++) {

                        donnee = new TextView(tblCompteur.getContext());
                        donnee.setText(arrayDonnecompteur.get(i)[j]);
                        donnee.setBackground(getResources().getDrawable(R.drawable.cell,null));
                        donnee.setPadding(50,50,50,50);
                        donnee.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        donnee.setTextColor(getResources().getColor(R.color.colorBlack));
                        row.addView(donnee);
                    }
                    tblCompteur.addView(row);;
                }
            }
            else if(currentListItemSelected == 2)
            {
                tblCompteur.removeAllViews();
               /** ParseJSONCompteurAndBilles pjcab = new ParseJSONCompteurAndBilles(json);
                pjcab.parseJSON();*/

                TableRow row;
                TextView txtno;
                TextView txtvide;
                TextView txttitre;
                TextView nomProf;
                TextView donnee;
                TextView lblCandB;
                arrayDonnecompteur.add(array1);
                arrayDonnecompteur.add(array2);
                arrayDonnecompteur.add(array3);
                arrayDonnecompteur.add(array4);
                arrayDonnecompteur.add(array5);
                arrayDonnecompteur.add(array6);
                arrayDonnebille.add(array7);
                arrayDonnebille.add(array8);
                arrayDonnebille.add(array9);
                arrayDonnebille.add(array10);
                arrayDonnebille.add(array11);
                arrayDonnebille.add(array12);


                row = new TableRow(tblChoice.getContext());

                txtvide = new TextView(tblChoice.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayNoCours.length; i++) {

                    txtno = new TextView(tblCompteur.getContext());

                    txtno.setText(arrayNoCours[i]);
                    txtno.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    txtno.setPadding(50,50,50,50);
                    txtno.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(txtno);
                }

                tblCompteur.addView(row);

                row = new TableRow(tblCompteur.getContext());

                txtvide = new TextView(tblCompteur.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayTitre.length; i++) {

                    txttitre = new TextView(tblCompteur.getContext());

                    txttitre.setText(arrayTitre[i]);
                    txttitre.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    txttitre.setPadding(50,50,50,50);
                    txttitre.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(txttitre);
                }

                tblCompteur.addView(row);

                row = new TableRow(tblCompteur.getContext());

                txtvide = new TextView(tblCompteur.getContext());
                txtvide.setText("");
                txtvide.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                txtvide.setPadding(50,50,50,50);
                txtvide.setGravity(View.TEXT_ALIGNMENT_CENTER);

                row.addView(txtvide);

                for (int i = 0; i < arrayNoCours.length; i++) {

                    lblCandB = new TextView(tblCompteur.getContext());

                    lblCandB.setText(getResources().getString(R.string.lblCandB));
                    lblCandB.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    lblCandB.setPadding(50,50,50,50);
                    lblCandB.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    row.addView(lblCandB);
                }

                tblCompteur.addView(row);

                for (int i = 0; i < 6; i++) {
                    row = new TableRow(tblCompteur.getContext());
                    nomProf = new TextView(tblCompteur.getContext());
                    nomProf.setText(arrayNomProf[i]);
                    nomProf.setBackground(getResources().getDrawable(R.drawable.cell_title,null));
                    nomProf.setPadding(50,50,50,50);
                    nomProf.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    nomProf.setTextColor(getResources().getColor(R.color.colorBlack));
                    row.addView(nomProf);
                    for (int j = 0; j < 5; j++) {

                        donnee = new TextView(tblCompteur.getContext());
                        donnee.setText(arrayDonnecompteur.get(i)[j] + " / " + arrayDonnebille.get(i)[j] );
                        donnee.setBackground(getResources().getDrawable(R.drawable.cell,null));
                        donnee.setPadding(50,50,50,50);
                        donnee.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        donnee.setTextColor(getResources().getColor(R.color.colorBlack));
                        row.addView(donnee);
                    }
                    tblCompteur.addView(row);;
                }
            }else{}

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Setting.class);
            startActivityForResult(intent, PICK_CONTACT_REQUEST);
        } else if (id == R.id.action_logout) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
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

        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
        }

        return true;
    }
}
