package sfadmldb.sgrt.view;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Locale;

import sfadmldb.sgrt.R;
import sfadmldb.sgrt.model.secure;

/**
 *  This class manage the prefered language, the user select a language and return to the previous activity
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class Setting extends AppCompatActivity {

    //Current language
    public static String langue = "";

    //List to change the language of the application
    private ListView lstLang;

    private Button btnreturn;

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

        btnreturn = (Button) findViewById(R.id.btnReturn);

        //Create an object who implements the action of touch, a listener
        ClickSurface touchListener = new ClickSurface();

        //Add the listener to each button
            if (btnreturn != null) {
                btnreturn.setOnTouchListener(touchListener);
            }

            ListListener lstListener = new ListListener();

            String[] arrayLang = new String[2];
            arrayLang[0] = getResources().getString(R.string.french);
            arrayLang[1] = getResources().getString(R.string.english);

            ArrayAdapter<String> myAdapterBille= new
                    ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    arrayLang);
            lstLang=(ListView) findViewById(R.id.listViewLang);
            if (lstLang != null) {
                lstLang.setAdapter(myAdapterBille);
                lstLang.setOnItemClickListener(lstListener);
            }
        }
    }

    /**
     * Inner class who implements the OnItemClickListener for the listView
     */
    private class ListListener implements AdapterView.OnItemClickListener
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

            if(parent.getId() == lstLang.getId())
            {

                itemValue = (String) lstLang.getItemAtPosition(position);

                if(itemValue.matches(getResources().getString(R.string.french)))
                {
                    //Set french as the language for the application
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "").apply();
                    setLangRecreate("");
                    Setting.langue = "";
                    getViewByPosition(0,lstLang).setSelected(true);
                    getViewByPosition(1,lstLang).setSelected(false);
                }else
                {
                    //Set english as the language for the application
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").apply();
                    setLangRecreate("en");
                    Setting.langue = "en";
                    view.setSelected(true);
                    getViewByPosition(0,lstLang).setSelected(false);
                    getViewByPosition(1,lstLang).setSelected(true);
                }

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
         * @return boolean - a button as been pressed
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            Boolean pressed = false;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

               if(v.getId() == btnreturn.getId())
                {
                    finish();
                    pressed = true;
                }
            }

            return pressed;
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
     *
     *
     * @param pos - position in the listview
     * @param listView - the listview
     *
     * @return the view associate with the given position
     */
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
