package sfadmldb.sgrt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1232161 on 2016-04-26.
 */
public class ParseJSONCompteurOnly {

    //Parameter of the JSON file
    public static String[] no;
    public static String[] titre;



    //Column name of the JSON file
    public static final String KEY_NO_COURS = "cou_no";
    public static final String KEY_TITRE_COURS = "cou_titre";


    //
    private String json;

    public static int arrayLength;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONCompteurOnly(String json){

        this.json = json;
    }

    /**
     *	This method extract from the JSON object the information and put them in is associate array
     */
    protected void parseJSON(){

        JSONArray jsonArray=null;
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
