package example.plugins;

import com.drew.lang.annotations.NotNull;
import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.plugins.MausPageContentFactoryInterface;
import system.plugins.MausPageContentInterface;
import system.plugins.MausPageContentText;
import system.processor.MausPage;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Fabi on 3/4/2015.
 */
public class MausPageContentWordCloudFactory implements MausPageContentFactoryInterface<Integer> {
    private List<String> exclude = new ArrayList<>();
    private static final String signs = "\\p{Punct}";
    private static final String[] specialExcludes = new String[]{"^[0-9]+$", "^[\\s\\t]*$"};
    private Map<String,Integer> words = new HashMap<>();

    public MausPageContentWordCloudFactory(@NotNull List<String> exclude) {
        this.exclude.addAll(exclude);
    }

    public MausPageContentWordCloudFactory(String filename) {
        File f = new File(filename);

        if(!f.exists()) {
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            exclude.addAll(reader.lines().collect(Collectors.toList()));

            reader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getContentName() {
        return "wordcloud-seed";
    }

    @Override
    public MausPageContentInterface createContent(MausPage page, DirectedGraph<DefaultNode<Integer>> graph) {
        StringBuilder buffer = new StringBuilder();

        for(DefaultNode<Integer> n : graph.getVertices()) {
            buffer.append(n.<String>getAttribute("text-content"));
        }

        String[] words = explodeContent(buffer.toString());
        this.words = createCountMap(words);

        return new MausPageContentWordCloud("wordcloud.txt", this.words);
    }

    private Map<String,Integer> createCountMap(String[] words) {
        Map<String,Integer> value = new HashMap<>();
        for(String s : words) {
            if(!value.containsKey(s)) {
                value.put(s, 0);
            }
            value.put(s, value.get(s)+1);
        }
        return value;
    }

    private String[] explodeContent(String content) {
        String c = content.replaceAll(signs, " ");
        c = c.replaceAll("[\\r?\\n]+", " ");
        String[] arr = c.split("([\\s\\t])|(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        List<String> erg = new ArrayList<>();
        for(String s : arr) {
            s = s.trim();
            if(!s.isEmpty() && s.length() > 2) {
                erg.add(s);
            }
        }

        erg = erg.stream().filter(s -> !exclude.contains(s)).collect(Collectors.toList());

        for(String f : specialExcludes) {
            erg = erg.stream().filter(s -> !exclude.contains(f)).collect(Collectors.toList());
        }

        return erg.toArray(new String[erg.size()]);
    }
}