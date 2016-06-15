package sfadmldb.sgrt.model;

import android.content.Context;

import java.io.File;

/**
 *  This class give method to clear the cache for better performance
 *
 *  @author Sébastien Fillion
 *  @version 1.0
 */
public class Memory {

    /**
     * Method to clear the cash after the request on webservice
     *
     * @param context - current context
     */
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    /**
     *  Method to access the cash folder and delete it to free memory
     *
     * @param dir - file of cash
     *
     * @return - if an error occure on delete
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!= null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }
}
