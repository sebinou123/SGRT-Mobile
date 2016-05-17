package sfadmldb.sgrt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *	This class parse the JSON file from the web service for the choice tab to know the number of choice he did and the year
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ParseJSONChoiceFait {
    //Parameter of the JSON file
    public static Boolean[] nbChoix;
    public static String[] annee;


    //Column name of the JSON file
    public static final String KEY_NB_CHOIX = "choixFait";
    public static final String KEY_ANNEE = "tac_annee";

    //
    private JSONObject json;

    public static int arrayLength;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONChoiceFait(JSONObject json){

        this.json = json;
    }

    /**
     *	This method extract from the JSON object the information and put them in is associate array
     */
    protected void parseJSON(){

        nbChoix = new Boolean[1];
        annee = new String[1];



        try {


            nbChoix[0] = json.getBoolean(KEY_NB_CHOIX);
            annee[0] = json.getString(KEY_ANNEE);




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
