package example.plugins;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.plugins.MausPageContentFactoryInterface;
import system.plugins.MausPageContentInterface;
import system.plugins.MausPageContentText;
import system.processor.MausPage;

/**
 * Created by Fabi on 24.02.2015.
 */
public class MausPageContentGraphMLFactory implements MausPageContentFactoryInterface<Integer> {
    @Override
    public String getContentName() {
        return "graphml-file";
    }

    @Override
    public MausPageContentInterface createContent(MausPage page, DirectedGraph<DefaultNode<Integer>> graph) {
        MausPageContentInterface content = new MausPageContentText("graph.graphml", graph.toGraphML(page.getAttribute("name")));
        content.setRootFolder(page.getRootFolder());
        return content;
    }
}