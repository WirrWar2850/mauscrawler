package example.indices;

import crawler.graph.DefaultEdge;
import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.consumer.GraphConsumerInterface;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rene on 24.02.2015.
 */
public class RandicIndexConsumer implements GraphConsumerInterface {

    @Override
    public String getName() {
        return "randic-index";
    }

    @Override
    public String consume(DirectedGraph graph) {
        List<DefaultEdge> edges = graph.getEdges();
        double sum=0;
        for (DefaultEdge e:edges){
            if(e.getStartVertex().equals(e.getEndVertex())) {
                continue;
            }
            double mult=getDegree((DefaultNode) e.getStartVertex(),edges)*getDegree((DefaultNode) e.getEndVertex(),edges);
            if(mult > 0) {
                double erg = 1 / Math.sqrt(mult);
                sum += erg;
            }
        }
        return String.format(Locale.getDefault(), "%.5f", sum);
    }

    private int getDegree(DefaultNode node,List<DefaultEdge>edges){
        int outDegree=0;
        int inDegree=0;
        for (DefaultEdge e:edges){
            if (e.getStartVertex().equals(node)) {
                outDegree++;
            }
            if (e.getEndVertex().equals(node)) {
                inDegree++;
            }
        }
        return inDegree+outDegree;
    }
}
