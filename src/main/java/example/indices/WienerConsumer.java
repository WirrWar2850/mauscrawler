package example.indices;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.consumer.GraphConsumerInterface;

import java.util.Locale;
import java.util.Map;

/**
 * Created by Rene on 24.02.2015.
 */
public class WienerConsumer implements GraphConsumerInterface {
    @Override
    public String getName() {
        return "wiener-index";
    }

    @Override
    public String consume(DirectedGraph graph) {
        Map<DefaultNode<Integer>, Map<DefaultNode<Integer>, Integer>> matrixTrans = graph.getAdjacencyMatrixTransitive();
        int sum=0;
        for(DefaultNode<Integer> i : matrixTrans.keySet()) {
            int d;
            for(DefaultNode<Integer> j : matrixTrans.keySet()) {
                if (matrixTrans.get(i).containsKey(j)) {
                    d = matrixTrans.get(i).get(j);
                    sum += d;
                }
            }
        }
        float index= sum/2;
        return String.format(Locale.getDefault(), "%.2f", index);
    }
}
