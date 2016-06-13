package example.plugins;

import system.plugins.MausPageContentText;

import java.util.*;

/**
 * Created by Fabi on 3/4/2015.
 */
public class MausPageContentWordCloud extends MausPageContentText {
    Map<String,Integer> words = new HashMap<>();
    private int maxWords = 50;
    private int maxUpperCount = 100;

    public MausPageContentWordCloud(String filePath, Map<String,Integer> words) {
        super(filePath, "");
        this.words = words;
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

        this.setText(builder.toString());
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

    public Map<String,Integer> getWords() {
        return this.words;
    }
}
