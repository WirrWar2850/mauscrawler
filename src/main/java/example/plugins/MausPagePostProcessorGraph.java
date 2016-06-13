package example.plugins;

import crawler.graph.DefaultEdge;
import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import edu.uci.ics.crawler4j.url.WebURL;
import system.plugins.MausPagePostProcessorInterface;

import java.util.List;
import java.util.Set;

/**
 * Created by Fabi on 24.02.2015.
 */
public class MausPagePostProcessorGraph implements MausPagePostProcessorInterface<DefaultNode<Integer>> {
    @Override
    public void process(DirectedGraph<DefaultNode<Integer>> graph) {
        int count = 0;

        List<DefaultEdge<DefaultNode<Integer>>> edges = graph.getEdges();
        for(DefaultNode<Integer> n : graph.getVertices()) {
            for(WebURL link : n.<Set<WebURL>>getAttribute("links")) {
                if(link.getDocid() != -1) {
                    Integer targetid = link.getDocid();
                    Integer sourceid = n.getAttribute("docid");

                    if(graph.hasVertex(new DefaultNode<>(sourceid)) &&
                            graph.hasVertex(new DefaultNode<>(targetid))) {

                        DefaultEdge<DefaultNode<Integer>> edge = new DefaultEdge<>();
                        edge.setStartVertex(new DefaultNode<>(sourceid));
                        edge.setEndVertex(new DefaultNode<>(targetid));

                        if (!edges.contains(edge)) {
                            count++;
                            graph.addEdge(new DefaultNode<>(sourceid), new DefaultNode<>(targetid));
                        }
                    }
                }
            }
        }
        System.out.println("Added " + count + " edges via postprocessing");
    }
}