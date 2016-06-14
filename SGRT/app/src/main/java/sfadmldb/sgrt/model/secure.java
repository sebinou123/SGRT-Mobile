package sfadmldb.sgrt.model;

/**
 * Class who give method to make validation to secure the application.
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class secure {

    /**
     * Check if the system ( mobile ) is using rooting who can damage the application.
     *
     * @return boolean - true if rooted
     */
    public static boolean checkRootMethod(){
        String buildTags = android.os.Build.TAGS;

        return buildTags != null && buildTags.contains("test-keys");
    }
}
