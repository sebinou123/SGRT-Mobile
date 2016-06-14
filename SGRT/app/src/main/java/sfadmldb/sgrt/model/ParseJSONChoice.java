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
    public static ArrayList<String[]> coursDonnesEte;
    public static ArrayList<String[]> coursDonnesAutomne;
    public static ArrayList<String[]> coursDonnesHiver;
    public static String[] no;
    public static String[] titre;
    public static String[] priority;



    //Column name of the JSON file
    public static final String KEY_NO_COURS = "cou_no";
    public static final String KEY_TITRE_COURS = "cou_titre";
    public static final String KEY_PRIORITY_COURS = "chx_priorite";
    public static final String KEY_AUTOMNE = "1";
    public static final String KEY_HIVER = "2";
    public static final String KEY_ETE = "3";

    //String json push by the web service
    private String json;

    //json length
    public static int arrayLength = 5;

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
            coursDonnesAutomne = new ArrayList<>();

            JSONArray a1 = jsonObject.getJSONArray(KEY_AUTOMNE);

            no = new String[a1.length()];
            titre = new String[a1.length()];
            priority = new String[a1.length()];

            for(int i = 0; i < a1.length(); i++ ){

                JSONObject objtemp = a1.getJSONObject(i);

                priority[i] = objtemp.getString(KEY_PRIORITY_COURS);
                no[i] = objtemp.getString(KEY_NO_COURS);
                titre[i] = objtemp.getString(KEY_TITRE_COURS);
            }

            coursDonnesAutomne.add(no);
            coursDonnesAutomne.add(titre);
            coursDonnesAutomne.add(priority);

            cours.add(coursDonnesAutomne);

            JSONArray a2 = jsonObject.getJSONArray(KEY_HIVER);

            coursDonnesHiver = new ArrayList<>();

            no = new String[a2.length()];
            titre = new String[a2.length()];
            priority = new String[a2.length()];

            for(int i = 0; i < a2.length(); i++ ){

                JSONObject objtemp = a2.getJSONObject(i);

                priority[i] = objtemp.getString(KEY_PRIORITY_COURS);
                no[i] = objtemp.getString(KEY_NO_COURS);
                titre[i] = objtemp.getString(KEY_TITRE_COURS);
            }

            coursDonnesHiver.add(no);
            coursDonnesHiver.add(titre);
            coursDonnesHiver.add(priority);

            cours.add(coursDonnesHiver);

            JSONArray a3 = jsonObject.getJSONArray(KEY_ETE);

            coursDonnesEte = new ArrayList<>();

            no = new String[a3.length()];
            titre = new String[a3.length()];
            priority = new String[a3.length()];

            for(int i = 0; i < a3.length(); i++ ){

                JSONObject objtemp = a3.getJSONObject(i);

                priority[i] = objtemp.getString(KEY_PRIORITY_COURS);
                no[i] = objtemp.getString(KEY_NO_COURS);
                titre[i] = objtemp.getString(KEY_TITRE_COURS);
            }

            coursDonnesEte.add(no);
            coursDonnesEte.add(titre);
            coursDonnesEte.add(priority);

            cours.add(coursDonnesEte);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
