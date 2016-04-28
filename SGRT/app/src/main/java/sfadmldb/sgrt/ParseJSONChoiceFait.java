package sfadmldb.sgrt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1232161 on 2016-04-28.
 */
public class ParseJSONChoiceFait {
    //Parameter of the JSON file
    public static String[] nbChoix;
    public static String[] annee;


    //Column name of the JSON file
    public static final String KEY_NB_CHOIX = "choixFait";
    public static final String KEY_ANNEE = "tac_annee";

    //
    private String json;

    public static int arrayLength;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONChoiceFait(String json){

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

            nbChoix = new String[jsonArray.length()];
            annee = new String[jsonArray.length()];


            for(int i=0;i<jsonArray.length();i++){

                JSONObject e = jsonArray.getJSONObject(i);

                nbChoix[i] = e.getString(KEY_NB_CHOIX);
                annee[i] = e.getString(KEY_ANNEE);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
