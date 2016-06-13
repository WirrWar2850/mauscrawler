package example.plugins;

import system.plugins.MausGroupContentInterface;
import system.processor.MausPage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabi on 03.03.2015.
 */
public class MausGroupGenericCSVExporter implements MausGroupContentInterface {
    private String filename = "maus/result/";
    private List<Map<String, String>> values = new ArrayList<>();
    private String[] indices;

    public MausGroupGenericCSVExporter(String seed, String filename, String[] indices) {
        this.indices = indices;
        if(seed != null && !seed.isEmpty()) {
            this.filename += seed + "/";
        }
        this.filename += filename + ".csv";
    }

    @Override
    public void handle(MausPage page) {
        Map<String, String> entry = new HashMap<>();
        for(String index : indices) {
            entry.put(index, page.getAttribute(index));
        }
        values.add(entry);
    }

    @Override
    public void save() {
        File f = new File(filename);

        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(f));

            fout.write("##;##\n");
            fout.write("@File " + filename + " CSV File LiveGraph Compatible\n");


            boolean empty = true;
            for(String index : indices) {
                if(!empty) {
                    fout.write(";");
                }
                empty = false;
                fout.write(index);
            }
            fout.write("\n");

            for(Map<String,String> probe : this.values) {
                fout.write(entryToString(probe) + "\n");
            }

            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String entryToString(Map<String,String> entry) {
        StringBuilder value = new StringBuilder();

        boolean empty = true;
        for(String index : indices) {
            if(!empty) {
                value.append(";");
            }
            empty = false;
            value.append(entry.get(index));
        }
        return value.toString();
    }

    @Override
    public void setRootFolder() {

    }

    @Override
    public String getRootFolder() {
        return null;
    }

    @Override
    public String getFilePath() {
        return null;
    }
}
