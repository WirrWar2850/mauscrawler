package system.plugins;

import java.io.File;

/**
 * Created by Fabi on 23.02.2015.
 */
public abstract class MausPageContentAbstract implements MausPageContentInterface {
    private String rootFolder;
    private String filePath;

    @Override
    public void setRootFolder(String folder) {
        this.rootFolder = folder;
    }

    @Override
    public String getRootFolder() {
        return this.rootFolder;
    }

    @Override
    public void save() {
        File f = new File(this.getRootFolder() + this.getFilePath());
        if(!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }
}
