package sfadmldb.sgrt;

import java.util.ArrayList;

/**
 * Created by Sebastien on 2016-05-26.
 */
public class Prof {

    private String initial;
    private ArrayList<Cours> arrayCours;


    public Prof(String init, ArrayList<Cours> cours)
    {
        setInitial(init);
        arrayCours = new ArrayList<>();
        setArrayCours(cours);
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public ArrayList<Cours> getArrayCours() {
        return arrayCours;
    }

    public void setArrayCours(ArrayList arrayCours) {
        this.arrayCours = arrayCours;
    }

    public void ajouterCours(String id, String nom, int billes, int compteur, int bid){
        arrayCours.add(new Cours(id,nom,billes,compteur,bid));
    }

    public String toString(){

        String affichage = "initiale : " + initial;

        for (Cours cours: arrayCours) {
            affichage += " cours : " + cours.toString();
        }

        return affichage;
    }

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
