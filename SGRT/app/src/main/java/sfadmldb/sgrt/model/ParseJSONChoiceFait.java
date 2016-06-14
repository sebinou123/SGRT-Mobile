package sfadmldb.sgrt.model;

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
    public static final String KEY_SESSION_AUTOMNE = "A";
    public static final String KEY_SESSION_HIVER = "H";
    public static final String KEY_SESSION_ETE = "E";

    //String json push by the web service
    private JSONObject json;

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
    public void parseJSON(){

        annee = new String[1];

        try {
            JSONObject objectTemp = json.getJSONObject(KEY_NB_CHOIX);
            nbChoix = new Boolean[objectTemp.length()];
            nbChoix[0] = objectTemp.getBoolean(KEY_SESSION_AUTOMNE);
            nbChoix[1] = objectTemp.getBoolean(KEY_SESSION_HIVER);
            nbChoix[2] = objectTemp.getBoolean(KEY_SESSION_ETE);
            annee[0] = json.getString(KEY_ANNEE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
