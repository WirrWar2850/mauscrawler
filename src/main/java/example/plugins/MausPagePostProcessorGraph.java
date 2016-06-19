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

                    if(graph.hasVertex(sourceid) &&
                            graph.hasVertex(targetid)) {

                        DefaultEdge<DefaultNode<Integer>> edge = new DefaultEdge<>();
                        edge.setStartVertex(graph.getVertex(sourceid));
                        edge.setEndVertex(graph.getVertex(targetid));

                        if (!edges.contains(edge)) {
                            count++;
                            try {
                                graph.addEdge(edge);
                            } catch(Exception e) {
                                count--;
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Added " + count + " edges via postprocessing");
    }
}