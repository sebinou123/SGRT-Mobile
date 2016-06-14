package sfadmldb.sgrt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *	This class parse the JSON file from the web service for the choice tab
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ParseJSONChoice {

    //Parameter of the JSON file
    public static String[] no;
    public static String[] titre;
    public static String[] priority;



    //Column name of the JSON file
    public static final String KEY_NO_COURS = "cou_no";
    public static final String KEY_TITRE_COURS = "cou_titre";
    public static final String KEY_PRIORITY_COURS = "chx_priorite";

    //
    private String json;

    public static int arrayLength;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONChoice(String json){

        this.json = json;
    }

    /**
     *	This method extract from the JSON object the information and put them in is associate array
     */
    protected void parseJSON(){

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);

            arrayLength = jsonArray.length();

            no = new String[jsonArray.length()];
            titre = new String[jsonArray.length()];
            priority = new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++){

                JSONObject e = jsonArray.getJSONObject(i);

                priority[i] = e.getString(KEY_PRIORITY_COURS);
                no[i] = e.getString(KEY_NO_COURS);
                titre[i] = e.getString(KEY_TITRE_COURS);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
