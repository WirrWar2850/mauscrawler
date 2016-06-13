package system.processor;

import system.plugins.MausPageContentInterface;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fabi on 19.02.2015.
 */
public class MausPage {
    private String seed = null;
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, MausPageContentInterface> content = new HashMap<>();

    public MausPage(String name) {
        this.setAttribute("name", name);
    }

    public MausPage(String name, String seed) {
        this.seed = seed;
        this.setAttribute("name",name);
    }

    public String getSeed() {
        return this.seed;
    }

    public void setAttribute(String name, String value) {
        this.attributes.put(name, value);
    }

    public String getAttribute(String name) {
        return this.attributes.get(name);
    }

    public void setContent(String name, MausPageContentInterface content) {
        this.content.put(name, content);
        content.setRootFolder(getRootFolder());
    }

    public MausPageContentInterface getContent(String name) {
        return this.content.get(name);
    }

    public void save() {
        File folder = new File(getRootFolder());

        folder.mkdirs();

        try {
            FileWriter fout = new FileWriter(getRootFolder() + "index.txt");

            for(String att : attributes.keySet()) {
                fout.write(att + " = " + attributes.get(att) + "\n");
            }

            fout.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        content.values().forEach(MausPageContentInterface::save);
    }

    public String getRootFolder() {
        String goodName = this.getAttribute("name").replace("/", "");
        goodName = goodName.replace("\\", "").replace("http:", "").replace("https:", "").replace(".", "_").replace("?", "").replace("&", "").replace("=", "");
        return "maus/result/" + (seed == null ? "" : seed+"/") + goodName + "/";
    }

    public String toString() {
        return this.seed + " - " + this.getAttribute("name");
    }
}