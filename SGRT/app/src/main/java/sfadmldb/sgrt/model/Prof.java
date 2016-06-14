package sfadmldb.sgrt.model;

import java.util.ArrayList;

import sfadmldb.sgrt.model.Cours;

/**
 *	This class stock all the information about the teacher.
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class Prof {

    private String initial;
    private ArrayList<Cours> arrayCours;


    /**
     * Constructor of teacher class
     *
     * @param init - String initial teacher
     * @param cours - ArrayList of teacher courses
     */
    public Prof(String init, ArrayList<Cours> cours)
    {
        setInitial(init);
        arrayCours = new ArrayList<>();
        setArrayCours(cours);
    }

    /**
     * Getter of the parameter initial
     *
     * @return initial - String
     */
    public String getInitial() {
        return initial;
    }

    /**
     * Setter of the parameter initial
     *
     * @param initial - String
     */
    public void setInitial(String initial) {
        this.initial = initial;
    }

    /**
     * Getter of the parameter arrayCours
     *
     * @return arrayCours - ArrayList
     */
    public ArrayList<Cours> getArrayCours() {
        return arrayCours;
    }

    /**
     * Setter of the parameter arrayCours
     *
     * @param arrayCours - ArrayList
     */
    public void setArrayCours(ArrayList arrayCours) {
        this.arrayCours = arrayCours;
    }

    public void ajouterCours(String id, String nom, int billes, int compteur, int bid){
        arrayCours.add(new Cours(id,nom,billes,compteur,bid));
    }

    /**
     * Show a String about the information of the teacher
     *
     * @return String representation of teacher courses
     */
    public String toString(){

        String affichage = "initiale : " + initial;

        for (Cours cours: arrayCours) {
            affichage += " cours : " + cours.toString();
        }

        return affichage;
    }

    /**
     * Method to know if a specified course exist in his list of courses
     *
     * @param idCours - course to compare
     *
     * @return Cours or null if the compared course doesn't exist
     */
    public Cours getCoursExiste(String idCours)
    {
        Cours coursTemp = null;

        for (Cours cours: arrayCours) {
            if(cours.getIdCours().matches(idCours))
            {
                coursTemp = cours;
            }
        }

        return coursTemp;
    }
}
