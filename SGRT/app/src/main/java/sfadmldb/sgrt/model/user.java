package sfadmldb.sgrt.model;

/**
 *	This class stock all the information about the user (except his password).
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class user {

    private String id;
    private String name;
    private String email;
    private String token;

    private static user INSTANCE;

    /**
     *	Constructor of user class without parameter
     */
    private user(){}


    /**
     *	Constructor of user class who implement the singleton pattern
     */
    public static user getUser()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new user();
        }
        return INSTANCE;
    }

    /**
     *	Setter of the parameter id
     *
     * @param id - Integer
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Getter of the parameter id
     *
     * @return id - Integer
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Setter of the parameter name
     *
     * @param name - String
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Getter of the parameter name
     *
     * @return name - String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter of the parameter email
     *
     * @param email - String
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Getter of the parameter email
     *
     * @return email - String
     */
    public String getEmail()
    {
        return this.email;
    }

    /**
     * Setter of the parameter email
     *
     * @param token - String
     */
    public void setToken(String token)
    {
        this.token = token;
    }

    /**
     * Getter of the parameter email
     *
     * @return email - String
     */
    public String getToken()
    {
        return this.token;
    }



}
