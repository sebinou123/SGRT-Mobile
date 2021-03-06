package sfadmldb.sgrt.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *	This class parse the JSON file about course
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ParseJSONCours {
    //Parameter of the JSON file
    public static String[] no;
    public static String[] titre;

    //Column name of the JSON file
    public static final String KEY_NO_COURS = "cou_no";
    public static final String KEY_TITRE_COURS = "cou_titre";

    //String json push by the web service
    private String json;

    //json length
    public static int arrayLength;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONCours(String json){

        this.json = json;
    }

    /**
     *	This method extract from the JSON object the information and put them in is associate array
     */
    public void parseJSON(){

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);

            arrayLength = jsonArray.length();

            no = new String[jsonArray.length()];
            titre = new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++){

                JSONObject e = jsonArray.getJSONObject(i);

                no[i] = e.getString(KEY_NO_COURS);
                titre[i] = e.getString(KEY_TITRE_COURS);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
