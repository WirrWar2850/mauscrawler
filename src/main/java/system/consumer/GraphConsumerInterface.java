package system.consumer;

import crawler.graph.DirectedGraph;

/**
 * ideal destination for calculating indices on the crawled graph structure
 *
 * Created by Fabi on 17.02.2015.
 */
public interface GraphConsumerInterface<N extends Comparable<N>> {
    /**
     * returns name of the generated content (e.g. index-name)
     * result of this consumer will be stored under the given name
     * @return String
     */
    String getName();

    /**
     * returns String representation of the generated content
     * @param graph Graph of the crawled website
     * @return String
     */
    String consume(DirectedGraph<N> graph);
}
