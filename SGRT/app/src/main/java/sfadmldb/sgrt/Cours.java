package sfadmldb.sgrt;

/**
 * Created by Sebastien on 2016-05-26.
 */
public class Cours {

    private String idCours;
    private String nomCours;
    private int billes;
    private int compteur;
    private int bid;

    public Cours(String id, String nom, int billes, int compteur, int bid) {
        setBilles(billes);
        setCompteur(compteur);
        setIdCours(id);
        setNomCours(nom);
        setBid(bid);
    }

    public String getIdCours() {
        return idCours;
    }

    public void setIdCours(String idCours) {
        this.idCours = idCours;
    }

    public String getNomCours() {
        return nomCours;
    }

    public int getCompteur() {
        return compteur;
    }

    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }

    public int getBilles() {
        return billes;
    }

    public void setBilles(int billes) {
        this.billes = billes;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public String toString() {
        return "id : " + idCours + " nom : " + nomCours + " compteur : " + compteur + " billes : " + billes + " bid : " + bid;
    }




    }
