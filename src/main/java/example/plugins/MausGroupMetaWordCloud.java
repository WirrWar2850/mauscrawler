package example.plugins;

import system.plugins.MausGroupContentInterface;
import system.processor.MausPage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by Fabi on 3/4/2015.
 */
public class MausGroupMetaWordCloud implements MausGroupContentInterface {
    private String filename = "maus/result/";
    private Map<String,Integer> words = new HashMap<>();
    private int maxWords = 100;
    private int maxUpperCount = 20;

    public MausGroupMetaWordCloud(String seed) {
        if(seed != null && !seed.isEmpty()) {
            this.filename += seed + "/";
        }
        this.filename += "meta-wordcloud.csv";
    }

    @Override
    public synchronized void handle(MausPage page) {
        Map<String,Integer> value = ((MausPageContentWordCloud)(page.getContent("wordcloud-seed"))).getWords();

        for(Map.Entry<String,Integer> entry : value.entrySet()) {
            if(this.words.containsKey(entry.getKey())) {
                this.words.put(entry.getKey(), this.words.get(entry.getKey()) + entry.getValue());
            } else {
                this.words.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void save() {
        File f = new File(filename);

        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        List<String> candidates = sortedValues(this.words);

        int minValue = this.words.get(candidates.get(candidates.size()-1));

        StringBuilder builder = new StringBuilder();
        for(String s : candidates) {
            int toValue = this.words.get(s) / minValue;
            toValue = toValue > maxUpperCount ? maxUpperCount : toValue;

            for(int i=0;i<toValue;i++) {
                builder.append(s).append(" ");
            }
            builder.append("\n");
        }

        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(f));

            fout.write(builder.toString());

            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private List<String> sortedValues(Map<String,Integer> input) {
        List<Map.Entry<String,Integer>> value = new ArrayList<>(input.entrySet());

        Collections.sort(value, (a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> result = new ArrayList<>();

        int count = 0;
        for(Map.Entry<String,Integer> e : value) {
            result.add(e.getKey());
            count++;
            if(count >= maxWords) {
                break;
            }
        }

        return result;
    }
}
