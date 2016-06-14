package sfadmldb.sgrt.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 *	This class parse the JSON file from the web service for the choice tab
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ParseJSONChoice {

    //Parameter of the JSON file
    public static ArrayList<ArrayList<String[]>> cours;
    public static ArrayList<String[]> coursDonnes;
    public static String[] no;
    public static String[] titre;
    public static String[] priority;



    //Column name of the JSON file
    public static final String KEY_NO_COURS = "cou_no";
    public static final String KEY_TITRE_COURS = "cou_titre";
    public static final String KEY_PRIORITY_COURS = "chx_priorite";

    //String json push by the web service
    private String json;

    //json length
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
    public void parseJSON(){

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);

            cours = new ArrayList<>();
            coursDonnes = new ArrayList<>();

            JSONArray a1 = jsonObject.getJSONArray("1");

            arrayLength = a1.length();

            no = new String[a1.length()];
            titre = new String[a1.length()];
            priority = new String[a1.length()];

            for(int i = 0; i < a1.length(); i++ ){

                JSONObject objtemp = a1.getJSONObject(i);

                priority[i] = objtemp.getString(KEY_PRIORITY_COURS);
                no[i] = objtemp.getString(KEY_NO_COURS);
                titre[i] = objtemp.getString(KEY_TITRE_COURS);
            }

            coursDonnes.add(priority);
            coursDonnes.add(no);
            coursDonnes.add(titre);

            cours.add(coursDonnes);

            JSONArray a2 = jsonObject.getJSONArray("2");

            no = new String[a2.length()];
            titre = new String[a2.length()];
            priority = new String[a2.length()];

            for(int i = 0; i < a2.length(); i++ ){

                JSONObject objtemp = a2.getJSONObject(i);

                priority[i] = objtemp.getString(KEY_PRIORITY_COURS);
                no[i] = objtemp.getString(KEY_NO_COURS);
                titre[i] = objtemp.getString(KEY_TITRE_COURS);
            }

            coursDonnes.add(priority);
            coursDonnes.add(no);
            coursDonnes.add(titre);

            cours.add(coursDonnes);

            JSONArray a3 = jsonObject.getJSONArray("3");

            no = new String[a3.length()];
            titre = new String[a3.length()];
            priority = new String[a3.length()];

            for(int i = 0; i < a3.length(); i++ ){

                JSONObject objtemp = a3.getJSONObject(i);

                priority[i] = objtemp.getString(KEY_PRIORITY_COURS);
                no[i] = objtemp.getString(KEY_NO_COURS);
                titre[i] = objtemp.getString(KEY_TITRE_COURS);
            }

            coursDonnes.add(priority);
            coursDonnes.add(no);
            coursDonnes.add(titre);

            cours.add(coursDonnes);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
