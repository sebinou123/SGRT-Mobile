package sfadmldb.sgrt.model;

/**
 *	This class stock all the information about the course.
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class Cours {

    private String idCours;
    private String nomCours;
    private int billes;
    private int compteur;
    private int bid;

    /**
     * Constructor of course class
     *
     * @param id - String id of course
     * @param nom - String name of course
     * @param billes - Integer marbles of course
     * @param compteur - Integer Time counter of course
     * @param bid - Integer bid of course
     */
    public Cours(String id, String nom, int billes, int compteur, int bid) {
        setBilles(billes);
        setCompteur(compteur);
        setIdCours(id);
        setNomCours(nom);
        setBid(bid);
    }

    /**
     * Getter of the parameter id
     *
     * @return id - Integer
     */
    public String getIdCours() {
        return idCours;
    }

    /**
     * Setter of the parameter id
     *
     * @param idCours - String
     */
    public void setIdCours(String idCours) {
        this.idCours = idCours;
    }

    /**
     * Getter of the parameter name
     *
     * @return name - String
     */
    public String getNomCours() {
        return nomCours;
    }

    /**
     * Getter of the parameter counter
     *
     * @return counter - Integer
     */
    public int getCompteur() {
        return compteur;
    }

    /**
     * Setter of the parameter counter
     *
     * @param compteur - Integer
     */
    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }

    /**
     * Getter of the parameter marbles
     *
     * @return marbles - Integer
     */
    public int getBilles() {
        return billes;
    }

    /**
     * Setter of the parameter marbles
     *
     * @param billes - Integer
     */
    public void setBilles(int billes) {
        this.billes = billes;
    }

    /**
     * Getter of the parameter bid
     *
     * @return bid - Integer
     */
    public int getBid() {
        return bid;
    }

    /**
     * Setter of the parameter bid
     *
     * @param bid - Integer
     */
    public void setBid(int bid) {
        this.bid = bid;
    }

    /**
     * Setter of the parameter name
     *
     * @param nomCours - String
     */
    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    /**
     * Show a String about the information of the course
     *
     * @return String representation of course
     */
    public String toString() {
        return "id : " + idCours + " nom : " + nomCours + " compteur : " + compteur + " billes : " + billes + " bid : " + bid;
    }
}
