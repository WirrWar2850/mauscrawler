package system.plugins;

/**
 * grants more freedom for creating individual contents based on
 * crawled websites
 *
 * gets saved when the site content gets saved, too
 *
 * Created by Fabi on 19.02.2015.
 */
public interface MausPageContentInterface {
    public void setRootFolder(String folder);
    public String getRootFolder();
    public void save();

    /**
     * returns the file path used inside of root folder
     * @return String
     */
    public String getFilePath();
}