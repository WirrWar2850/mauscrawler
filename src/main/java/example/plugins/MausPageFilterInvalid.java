package example.plugins;

import crawler.graph.DirectedGraph;
import system.plugins.MausPageFilterInterface;
import system.processor.MausPage;

/**
 * Filter pages without usable content
 * Created by Fabi on 02.03.2015.
 */
public class MausPageFilterInvalid implements MausPageFilterInterface {
    @Override
    public String getName() {
        return "invalid-page-filter";
    }

    @Override
    public boolean useProbe(MausPage page, DirectedGraph graph) {
        return graph.getVertices().size() > 1; // only root node, no content
    }
}
