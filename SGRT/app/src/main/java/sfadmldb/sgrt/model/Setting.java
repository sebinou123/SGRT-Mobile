package sfadmldb.sgrt.model;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import sfadmldb.sgrt.R;

/**
 *  This class manage the prefered language, the user select a language and return to the previous activity
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class Setting extends AppCompatActivity {

    public static String langue = "";


    //Button to change the language of the application
    private Button btnfr;
    private Button btnen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!secure.checkRootMethod())
        {
            if(getResources().getBoolean(R.bool.portrait_only)){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

        btnfr = (Button) findViewById(R.id.btnFr);
        btnen = (Button) findViewById(R.id.btnEn);
            Button btnreturn = (Button) findViewById(R.id.btnReturn);

        //Create an object who implements the action of touch, a listener
        ClickSurface touchListener = new ClickSurface();

        //Add the listener to each button
        btnfr.setOnTouchListener(touchListener);
        btnen.setOnTouchListener(touchListener);
            if (btnreturn != null) {
                btnreturn.setOnTouchListener(touchListener);
            }
        }
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

               if(v.getId() == btnfr.getId())
                {
                    //Set french as the language for the application
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "").apply();
                    setLangRecreate("");
                    Setting.langue = "";
                }
                else if(v.getId() == btnen.getId())
                {
                    //Set english as the language for the application
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").apply();
                    setLangRecreate("en");
                    Setting.langue = "en";
                }
                else
                {
                    finish();
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
}
