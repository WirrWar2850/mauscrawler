package example.indices;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.consumer.GraphConsumerInterface;

import java.util.Locale;

/**
 * Created by Fabi on 06.03.2015.
 */
public class AverageDepthConsumer implements GraphConsumerInterface<DefaultNode<Integer>> {
    @Override
    public String getName() {
        return "average-depth";
    }

    @Override
    public String consume(DirectedGraph<DefaultNode<Integer>> graph) {
        Integer depth = 0;

        for(DefaultNode<Integer> n : graph.getVertices()) {
            depth += n.<Integer>getAttribute("crawl-depth");
        }

        return String.format(Locale.getDefault(), "%.5f", Float.valueOf(depth)/Float.valueOf(graph.getVertices().size()));
    }
}
