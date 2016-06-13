package system.plugins;

import system.processor.MausPage;

/**
 * interface for handling groups of websites within
 * one content handler (for statistics for example)
 *
 * Created by Fabi on 23.02.2015.
 */
public interface MausGroupContentInterface {
    void handle(MausPage page);

    void save();

    void setRootFolder();
    String getRootFolder();
    String getFilePath();
}