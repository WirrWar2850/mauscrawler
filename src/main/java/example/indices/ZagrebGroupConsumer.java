package example.indices;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.consumer.GraphConsumerInterface;

import java.util.Map;

/**
 * Created by Rene on 24.02.2015.
 */
public class ZagrebGroupConsumer implements GraphConsumerInterface {

    @Override
    public String getName() {
        return "zagreb-index";
    }

    @Override
    public String consume(DirectedGraph graph) {
        Map<DefaultNode<Integer>, Map<DefaultNode<Integer>, Integer>> matrix = graph.getAdjacencyMatrix();
        int sum=0;
        for(DefaultNode<Integer> i : matrix.keySet()){
            for(DefaultNode<Integer> j : matrix.keySet()){
                if(matrix.get(i).get(j) != null && matrix.get(i).get(j)==1){
                   sum+=getDegree(matrix.get(i))*getDegree(matrix.get(j));
                }
            }
        }
        return String.valueOf(sum);
    }

    private int getDegree(Map<DefaultNode<Integer>, Integer> edges){
        int degree=0;
        for (DefaultNode<Integer> j: edges.keySet()){
                degree+=edges.get(j);
        }
        return degree;
    }
}
