package sfadmldb.sgrt.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *	This class parse the JSON file about Professor
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ParseJSONProf {
    //Parameter of the JSON file
    public static ArrayList<String> allAlias;

    //Column name of the JSON file
    public static final String KEY_ENS_ALIAS = "ens_alias";

    //
    private String json;

    public static int arrayLength;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONProf(String json){

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

            allAlias = new ArrayList<>(json.length());

            for(int i=0;i<jsonArray.length();i++){

                JSONObject e = jsonArray.getJSONObject(i);

                allAlias.add(e.getString(KEY_ENS_ALIAS));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
