package system.plugins;

import crawler.graph.DirectedGraph;
import system.processor.MausPage;

/**
 * filter interface used to determine if a crawling result
 * should be used as a probe or not
 *
 * Created by Fabi on 02.03.2015.
 */
public interface MausPageFilterInterface {
    String getName();
    boolean useProbe(MausPage page, DirectedGraph graph);
}
