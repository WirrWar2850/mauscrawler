package example.indices;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.consumer.GraphConsumerInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 06.03.2015.
 */
public class LanguageConsumer implements GraphConsumerInterface<DefaultNode<Integer>> {
    @Override
    public String getName() {
        return "languages";
    }

    @Override
    public String consume(DirectedGraph<DefaultNode<Integer>> graph) {
        List<String> languages = new ArrayList<>();
        for(DefaultNode<Integer> n : graph.getVertices()) {
            if(!languages.contains(n.<String>getAttribute("language"))) {
                languages.add(n.<String>getAttribute("language"));
            }
        }
        StringBuilder builder = new StringBuilder();

        boolean empty = true;
        for(String s : languages) {
            if(!empty) {
                builder.append(",");
            }
            builder.append(s);
            empty = false;
        }

        return builder.toString();
    }
}
