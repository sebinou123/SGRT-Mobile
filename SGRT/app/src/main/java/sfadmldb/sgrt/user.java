package sfadmldb.sgrt;

/**
 *	This class stock all the information about the user (except his password).
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class user {

    private int id;
    private String name;

    /**
     *	Constructor of user class without parameter
     */
    public user(){}

    /**
     * Constructor of user class with parameter
     *
     * @param id - id of the user (Integer)
     * @param name - name of the user (String)
     *
     */
    public user(int id, String name)
    {
        this.setId(id);
        this.setName(name);
    }

    /**
     *	Setter of the parameter id
     *
     * @param id - Integer
     */
    private void setId(int id)
    {
        this.id = id;
    }

    /**
     * Getter of the parameter id
     *
     * @return id - Integer
     */
    private int getId()
    {
        return this.id;
    }

    /**
     * Setter of the parameter name
     *
     * @param name - String
     */
    private void setName(String name)
    {
        this.name = name;
    }

    /**
     * Getter of the parameter name
     *
     * @return name - String
     */
    private String getString()
    {
        return this.name;
    }
}
