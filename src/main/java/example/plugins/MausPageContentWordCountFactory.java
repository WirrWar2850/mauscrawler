package example.plugins;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.plugins.MausPageContentFactoryInterface;
import system.plugins.MausPageContentInterface;
import system.plugins.MausPageContentText;
import system.processor.MausPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 05.03.2015.
 */
public class MausPageContentWordCountFactory implements MausPageContentFactoryInterface<Integer> {
    @Override
    public String getContentName() {
        return "word-count";
    }

    @Override
    public MausPageContentInterface createContent(MausPage page, DirectedGraph<DefaultNode<Integer>> graph) {
        MausPageContentWordCloud cloud = (MausPageContentWordCloud)page.getContent("wordcloud-seed");
        StringBuilder builder = new StringBuilder();

        List<String> keys = new ArrayList<>(cloud.getWords().keySet());

        builder.append("##;##\n");
        builder.append("@File LiveGraph compatible CSV File\n");
        builder.append("word;count\n");

        for(String s : keys) {
            builder.append(s + ";" + cloud.getWords().get(s) + "\n");
        }

        return new MausPageContentText("word-count.csv", builder.toString());
    }
}
