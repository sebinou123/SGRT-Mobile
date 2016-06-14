package sfadmldb.sgrt.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *	This class parse the JSON file from the web service
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class ParseJSONCompteurAndBilles {

    //Parameter of the JSON file
    public static HashMap<String,String> lstCours;
    public static ArrayList<String> profInitial;
    public static ArrayList<Prof> lstProf;

    //Column name of the JSON file
    public static final String KEY_COURS = "cours";
    public static final String KEY_BILLES = "billes";
    public static final String KEY_COMPTEUR = "compteur";
    public static final String KEY_BID = "bid";
    public static final String KEY_VIDE = "";

    //String json push by the web service
    private String json;

    //Boolean to know if we want to show the bid for each courses
    public static boolean afficherBid = false;

    /**
     * Constructor of the JSON class
     *
     * @param json - the string JSON
     */
    public ParseJSONCompteurAndBilles(String json){

        this.json = json;
    }

    /**
     *	This method extract from the JSON object the information and put them in is associate array
     */
    public void parseJSON(){

        JSONObject objtemp1;
        JSONObject objtemp2;
        JSONObject objtemp3;

        int billes;
        int compteurs;
        int bid;

        String initProf;
        String noCours;
        String nomCours;

        try {
            ArrayList<Cours> lstCoursTemp;
            lstCours = new HashMap<>();
            lstProf = new ArrayList<>();
            profInitial = new ArrayList<>();
            JSONObject issueObj = new JSONObject(json);
            Iterator iterator = issueObj.keys();
            afficherBid = false;

            while(iterator.hasNext()){
                lstCoursTemp = new ArrayList<>();
                initProf = (String)iterator.next();
                if(!initProf.matches(KEY_VIDE))
                {
                    profInitial.add(initProf);
                    objtemp1 = issueObj.getJSONObject(initProf);
                    objtemp2 = objtemp1.getJSONObject(KEY_COURS);
                    Iterator iterator2 = objtemp2.keys();
                    while(iterator2.hasNext()){
                        noCours = (String)iterator2.next();
                        if(!noCours.matches(KEY_VIDE))
                        {
                            objtemp3 = objtemp2.getJSONObject(noCours);
                            nomCours =  objtemp3.getString(KEY_COURS);
                            billes =  objtemp3.getInt(KEY_BILLES);
                            compteurs =  objtemp3.getInt(KEY_COMPTEUR);
                            bid = 0;

                            if(objtemp3.length() == 4)
                            {
                                 afficherBid = true;
                                 bid = objtemp3.getInt(KEY_BID);
                            }

                            if(!lstCours.isEmpty()){
                                if(!lstCours.containsKey(noCours))
                                {
                                    lstCours.put(noCours, nomCours);
                                }
                            }else{
                                lstCours.put(noCours, nomCours);
                            }

                            lstCoursTemp.add(new Cours(noCours,nomCours,billes,compteurs,bid));
                        }
                    }
                        lstProf.add(new Prof(initProf,lstCoursTemp));
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method who verified if a teacher have information on courses (marbles, counter, bid) by comparing
     * this teacher with the array of teacher who have information on courses.
     *
     * @param alias - teacher alias
     *
     * @return Boolean - true if a teacher have courses or not
     */
    public static Boolean getProfHaveCours(String alias)
    {
        Boolean existe = false;

        for (int i = 0; i<profInitial.size();i++) {

            if(profInitial.get(i).matches(alias))
            {
                existe = true;
            }

        }

        return existe;
    }

    /**
     * Find if the alias who represent the user exit in the array of all users.
     *
     * @param alias - User alias
     *
     * @return Prof - if it existe or null
     */
    public static Prof getProf(String alias)
    {
        Prof profTemp = null;

        for (Prof prof: lstProf) {

            if(prof.getInitial().matches(alias))
            {
                 profTemp = prof;
            }

        }

        return profTemp;
    }
}
