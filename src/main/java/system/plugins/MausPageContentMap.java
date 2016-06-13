package system.plugins;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fabi on 23.02.2015.
 */
public class MausPageContentMap extends MausPageContentAbstract {
    Map<String, Serializable> content = new HashMap<>();

    public MausPageContentMap(String foldername) {
        this.setFilePath(foldername);
    }

    public void setContent(String name, Serializable content) {
        this.content.put(name, content);
    }

    public Serializable getContent(String name) {
        return this.content.get(name);
    }

    @Override
    public void save() {
        File folder = new File(getRootFolder() + getFilePath());
        if(!folder.exists()) {
            folder.mkdirs();
        }

        for(String name : this.content.keySet()) {
            try {
                BufferedWriter fout = new BufferedWriter(new FileWriter(getRootFolder() + getFilePath() + (getFilePath().endsWith("/") ? "" : "/") + name));

                fout.write(content.get(name).toString());

                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
