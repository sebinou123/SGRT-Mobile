package sfadmldb.sgrt;

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
    public static String[] userConnected;
    public static String[] userId;

    //Column name of the JSON file
    public static final String KEY_TOKEN = "jwt";
    public static final String KEY_USER = "connected_user";
    public static final String KEY_USER_ID = "user_id";



    //
    private JSONObject json;



    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONLogin(JSONObject json){

        this.json = json;
    }

    /**
     *	This method extract from the JSON object the information and put them in is associate array
     */
    protected void parseJSON(){




            token = new String[1];
            userConnected = new String[1];
            userId = new String[1];


            try {


                token[0] = json.getString(KEY_TOKEN);
                userConnected[0] = json.getString(KEY_USER);
                userId[0] = json.getString(KEY_USER_ID);



            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}
