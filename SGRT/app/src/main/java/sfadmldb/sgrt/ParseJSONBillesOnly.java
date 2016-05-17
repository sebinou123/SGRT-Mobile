package sfadmldb.sgrt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *  This class parse the JSON file from the web service for the ball tab
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ParseJSONBillesOnly {

    //Parameter of the JSON file
    public static String[] no;
    public static String[] titre;
    public static String[] profName;
    public static String[] nbBille;




    //Column name of the JSON file
    public static final String KEY_NO_COURS = "cou_no";
    public static final String KEY_TITRE_COURS = "cou_titre";
    public static final String KEY_PROF_NAME = "ens_alias";
    public static final String KEY_NUMBER_BILLE = "nb_bille";

    //
    private String json;

    public static int arrayLength;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONBillesOnly(String json){

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
            profName = new String[jsonArray.length()];
            nbBille = new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++){

                JSONObject e = jsonArray.getJSONObject(i);

                no[i] = e.getString(KEY_NO_COURS);
                titre[i] = e.getString(KEY_TITRE_COURS);
                profName[i] = e.getString(KEY_PROF_NAME);
                nbBille[i] = e.getString(KEY_NUMBER_BILLE);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
