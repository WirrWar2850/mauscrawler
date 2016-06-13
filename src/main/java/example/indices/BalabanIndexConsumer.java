package example.indices;

import crawler.graph.DefaultEdge;
import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.consumer.GraphConsumerInterface;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Rene on 24.02.2015.
 */
public class BalabanIndexConsumer implements GraphConsumerInterface {

    @Override
    public String getName() {
        return "balaban-index";
    }

    @Override
    public String consume(DirectedGraph graph) {
        Map<DefaultNode<Integer>, Map<DefaultNode<Integer>, Integer>> matrixTrans = graph.getAdjacencyMatrixTransitive();
        List<DefaultEdge<DefaultNode<Integer>>> edges = graph.getEdges();
        double sum=0;
        for (DefaultEdge<DefaultNode<Integer>> e:edges){
            int mult=getDistanceSum(e.getStartVertex(), matrixTrans)*getDistanceSum(e.getEndVertex(), matrixTrans);
            if(mult>0) {
                double erg = 1 / Math.sqrt(mult);
                sum += erg;
            }
        }
        double index= (graph.getEdges().size()/(getMy(graph)+1))*sum;
        return String.format(Locale.getDefault(), "%.5f", index);
    }

    private int getDistanceSum(DefaultNode<Integer> node,Map<DefaultNode<Integer>, Map<DefaultNode<Integer>, Integer>> matrix){
        Map<DefaultNode<Integer>, Integer> row=matrix.get(node);
        int sum=0;

        for (Integer n:row.values()) {
            sum+=n;
        }
        return sum;
    }

    private int getMy(DirectedGraph graph){
        return graph.getEdges().size()-graph.getVertices().size()+1;
    }
}
