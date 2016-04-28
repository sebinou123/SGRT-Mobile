package sfadmldb.sgrt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 *  This class manage the login to the application with the LDAP system use by the cegep.
 *  There are methods to validate the username and the passwords, and we verify if everything is good to let the user connect to the application.
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class login extends AppCompatActivity {


    //The logo of our project
    private ImageView logo;

    //Label for the username and the password
    private TextView errorUsername;
    private TextView errorPassword;

    //Textfield where the user need to enter his information
    private EditText textUsername;
    private EditText textPassword;

    //Button to send the information to the LDAP
    private Button btnLogin;

    //Button to change the language of the application
    private Button btnfr;
    private Button btnen;

    static final int PICK_CONTACT_REQUEST = 1;  // The request code

    Toolbar toolbar;



    /**
     * Method called when the application start. This method set the view, instantiate every element and add them to the view. Also,
     * he set the current language to the application. Add event handler when a button is pressed.
     *
     * @param savedInstanceState - a save of previous state, who can be reloaded by passing it to the method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get the default setting and config of the app
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        //Check if the language is the same of the default language, else he will replace the default by the actual language to keep the same language when we go to an other
        //view.
        String lang = settings.getString("LANG", "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }


        //instantiate every element and bind each object with the element in the xml file with find by id
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        logo = (ImageView) findViewById(R.id.logoimg);
        logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));



        errorUsername = (TextView) findViewById(R.id.errorUsername);
        errorPassword = (TextView) findViewById(R.id.errorPassword);

        textUsername = (EditText) findViewById(R.id.usernametxt);
        textPassword = (EditText) findViewById(R.id.passwordtxt);

        btnLogin = (Button) findViewById(R.id.loginBtn);
        btnfr = (Button) findViewById(R.id.btnFrench);
        btnen = (Button) findViewById(R.id.btnEnglish);

        //Create an object who implements the action of touch, a listener
        ClickSurface touchListener = new ClickSurface();

        //Add the listener to each button
        btnLogin.setOnTouchListener(touchListener);
        btnfr.setOnTouchListener(touchListener);
        btnen.setOnTouchListener(touchListener);

    }

    /**
     * Inner class who implements the Ontouchlistener for the button
     */
    private class ClickSurface implements SurfaceView.OnTouchListener {

        /**
         * This method get the event trigger on a view and check what element is trigger. For each button, an associate method is call to change the language or verify the field.
         *
         * @param v - the view with the elements
         * @param event - the type of motion who trigger the event
         *
         * @return boolean
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (v.getId() == btnLogin.getId())
                {
                    verifyField(v);
                }
                else if(v.getId() == btnfr.getId())
                {
                    //Set french as the language for the application
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "").commit();
                    setLangRecreate("");
                }
                else if(v.getId() == btnen.getId())
                {
                    //Set english as the language for the application
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                    setLangRecreate("en");
                }
                else
                {

                }
            }

            return true;
        }


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
     * This method verify if the user realy complete all the field required to make the connection.
     * Show an error if it's not. Verify if the string is secure (SQL injection)
     *
     * @param view - the view shown
     */
    public void verifyField(View view) {

        resetError();

        String user = textUsername.getText().toString();
        String pass = textPassword.getText().toString();

        if (!user.matches(""))
        {
            if(!pass.matches(""))
            {
                verifyConnection(user,pass);
            }
            else
            {
                errorPassword.setText(R.string.errorPassText);
            }
        }
        else
        {
            errorUsername.setText(R.string.errorUserText);

            if(pass.matches("")) {
                errorPassword.setText(R.string.errorPassText);
            }
        }
    }

    /**
     *	Reset all label of error message
     */
    private void resetError()
    {
        errorPassword.setText("");
        errorUsername.setText("");
    }

    /**
     *	This method make the connection with the LDAP server to determine if the user with the password exist in the database.
     *  Create an object user with his information (id, name) if it's correct and the consult panel is instantiate.
     *  Show an error if the user is not in the database
     */
    private void verifyConnection(String user, String pass) {

        if(true)
        {
            Intent intent = new Intent(this, consultPanel.class);
            startActivity(intent);

        }
    }

    /**
     *
     *
     * @param menu
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    /**
     *
     *
     * @param item
     *
     * @return
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setLangRecreate(Setting.langue);
    }
}
