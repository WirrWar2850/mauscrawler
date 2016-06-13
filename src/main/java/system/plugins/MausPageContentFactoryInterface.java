package system.plugins;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.processor.MausPage;

/**
 * creates page contents and therefore
 * gets plugged into the mausprocessor
 *
 * Created by Fabi on 23.02.2015.
 */
public interface MausPageContentFactoryInterface <N extends Comparable<N>> {
    public String getContentName();
    public MausPageContentInterface createContent(MausPage page, DirectedGraph<DefaultNode<N>> graph);
}
