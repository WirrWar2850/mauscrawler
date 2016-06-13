package example.indices;

import crawler.graph.DirectedGraph;
import system.consumer.GraphConsumerInterface;

/**
 * Created by Fabi on 03.03.2015.
 */
public class VertexCountConsumer implements GraphConsumerInterface {
    @Override
    public String getName() {
        return "vertex-count";
    }

    @Override
    public String consume(DirectedGraph graph) {
        return ""+graph.getVertices().size();
    }
}
