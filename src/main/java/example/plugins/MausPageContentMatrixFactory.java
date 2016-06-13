package example.plugins;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.processor.MausPage;
import system.plugins.MausPageContentFactoryInterface;
import system.plugins.MausPageContentInterface;
import system.plugins.MausPageContentText;

/**
 * Created by Fabi on 23.02.2015.
 */
public class MausPageContentMatrixFactory implements MausPageContentFactoryInterface<Integer> {
    @Override
    public String getContentName() {
        return "Adjazenz-Matrix";
    }

    @Override
    public MausPageContentInterface createContent(MausPage page, DirectedGraph<DefaultNode<Integer>> graph) {
        MausPageContentInterface content = new MausPageContentText("matrix.txt", graph.toString());
        content.setRootFolder(page.getRootFolder());

        return content;
    }
}
