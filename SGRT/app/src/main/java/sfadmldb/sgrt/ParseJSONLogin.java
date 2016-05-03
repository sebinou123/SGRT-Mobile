package sfadmldb.sgrt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *	This class parse the JSON file from the web service for the login
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ParseJSONLogin {

    //Parameter of the JSON file
    public static String[] token;




    //Column name of the JSON file
    public static final String KEY_TOKEN = "token";



    //
    private String json;

    public static int arrayLength;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONLogin(String json){

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

            token = new String[jsonArray.length()];


            for(int i=0;i<jsonArray.length();i++){

                JSONObject e = jsonArray.getJSONObject(i);

                token[i] = e.getString(KEY_TOKEN);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
