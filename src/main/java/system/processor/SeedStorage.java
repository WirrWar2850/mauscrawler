package system.processor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 19.02.2015.
 */
public class SeedStorage {
    private List<String> urls;
    private int currentUrl = 0;
    private String filename;

    /**
     * creates a new SeedStorage and loads urls from
     * filename
     * @param filename
     */
    public SeedStorage(String filename) {
        this.urls = new ArrayList<>();
        loadSeeds(filename);
        this.filename = filename;
    }

    public String getName() {
        String[] parts = this.filename.split("/");
        return parts[parts.length-1].replace('.', '_');
    }

    private void loadSeeds(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line = br.readLine();

            while(line != null) {
                if(!line.startsWith("--") && !line.isEmpty()) {
                    urls.add(line.trim());
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String nextSeed() {
        return urls.get(currentUrl++);
    }

    public boolean hasNext() {
        return urls.size() > currentUrl;
    }

    public int size() {
        return urls.size();
    }

    public List<String> getSeeds() {
        return urls;
    }
}
