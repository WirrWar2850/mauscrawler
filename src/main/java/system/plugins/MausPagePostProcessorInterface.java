package system.plugins;

import crawler.graph.DirectedGraph;

/**
 * Created by Fabi on 24.02.2015.
 */
public interface MausPagePostProcessorInterface<N extends Comparable<N>> {
    void process(DirectedGraph<N> graph);
}
