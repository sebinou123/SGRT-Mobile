package sfadmldb.sgrt;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
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

    //String about the required number of choice
    static final String NB_CHOICE_NEED = "5";

    //The selected item in the list ball or counter
    int currentListItemSelected;

    //List for each view
    ListView lstBille;
    ListView lstCompteur;

    //Array to put in listview
    private String[] arrayBille;
    private String[] arrayCompteur;

    //url to the server
    //public static final String url = "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username=demo";
    //public static final String url = "http://192.168.1.8/myhost-exemple/reponseChoix.php";
    //public static final String url = "http://172.20.33.43:52567/SGRT/public/home";
     public static final String url = "http://www.info.climoilou.qc.ca/E2016/420-669-LI/420-669-E16-02/production/";

    static final int PICK_CONTACT_REQUEST = 1;  // The request code


    /**
     * Method called when the application start. This method set the view, instantiate every element and add them to the view. Also,
     * he set the current language to the application. Add event handler when a button is pressed.
     *
     * @param savedInstanceState - a save of previous state, who can be reloaded by passing it to the method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
                    //currentPath = "bille/########";
                    //sendRequestPostBilleOnly();
                }else if(itemValue.matches(getResources().getString(R.string.lstBille2)))
                {
                    currentListItemSelected = 2;
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
                    //currentPath = "compteur/########";
                    //sendRequestPostCompteurOnly();
                }else if(itemValue.matches(getResources().getString(R.string.lstCompteur2)))
                {
                    currentListItemSelected = 2;
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
                currentTab = "1";

            }
            else if(tabId.matches(tab2name))
            {
               currentTab = "2";
               currentPath = "choix/choixStatus";
               sendRequestPostChoix(currentPath);
            }
            else
            {
                currentTab = "3";

            }
        }
    }


    /**
     * Method who send a request to get a response about if the user have made his choice.
     * Return an error if the request fail or he didn't made his choice
     * Call an other request if valid
     *
     * @param path - the path on the server
     */
    private void sendRequestPostChoix(String path){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url /**+ path*/ ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(consultPanel.this, response, Toast.LENGTH_LONG).show();
                        /**
                        ParseJSONChoiceFait pjcf = new ParseJSONChoiceFait(response);
                        pjcf.parseJSON();
                        if(ParseJSONChoiceFait.nbChoix[0].matches(consultPanel.NB_CHOICE_NEED))
                        {
                            currentPath = "choix/getChoix";
                            sendRequestPostChoixValide(currentPath);
                        }
                        else
                        {
                            TextView txtTemp = new TextView(tblChoice.getContext());
                            txtTemp.setText(getResources().getString(R.string.errorChoice));
                            tblChoice.addView(txtTemp);
                        }*/
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(consultPanel.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ensId","1");

                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + user.getUser().getToken());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(consultPanel.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ensId","1");
                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + user.getUser().getToken());
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

    }

    /**
     * Method who send a request to get nomber of ball for each course for each teacher
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostBilleOnly(String path){

    }

    /**
     * Method who send a request to get for each teacher the number of ball and counter for each course.
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostBilleAndCompteur(String path){

    }

    /**
     * Method who send a request to get for each teacher the number of ball and counter for each course.
     * Return an error if the request fail.
     * Call showJSON to show the data on response.
     *
     * @param path - the path on the server
     */
    private void sendRequestPostCompteurAndBille(String path){

    }

    /**
     * Method to show the data according to the selected tab and list
     *
     * @param json - the json response
     */
    private void showJSON(String json){

        if(currentTab.matches("1")) {
            if(currentListItemSelected == 1)
            {
                ParseJSONBillesOnly pjb = new ParseJSONBillesOnly(json);
                pjb.parseJSON();
            }
            else if(currentListItemSelected == 2)
            {
                ParseJSONBillesAndCompteur pjbac = new ParseJSONBillesAndCompteur(json);
                pjbac.parseJSON();
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
            if(currentListItemSelected == 1)
            {
                ParseJSONCompteurOnly pjc = new ParseJSONCompteurOnly(json);
                pjc.parseJSON();
            }
            else if(currentListItemSelected == 2)
            {
                ParseJSONCompteurAndBilles pjcab = new ParseJSONCompteurAndBilles(json);
                pjcab.parseJSON();
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
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

}
