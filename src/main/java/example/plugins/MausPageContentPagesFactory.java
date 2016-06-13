package example.plugins;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.processor.MausPage;
import system.plugins.MausPageContentFactoryInterface;
import system.plugins.MausPageContentInterface;
import system.plugins.MausPageContentMap;

/**
 * Created by Fabi on 23.02.2015.
 */
public class MausPageContentPagesFactory implements MausPageContentFactoryInterface<Integer> {
    @Override
    public String getContentName() {
        return "pages-content";
    }

    @Override
    public MausPageContentInterface createContent(MausPage page, DirectedGraph<DefaultNode<Integer>> graph) {
        MausPageContentMap result = new MausPageContentMap("pages");

        for(DefaultNode<Integer> n : graph.getVertices()) {
            result.setContent(n.toString() + ".html", n.getAttribute("html-content"));
            result.setContent(n.toString() + ".txt", n.getAttribute("text-content"));
        }

        return result;
    }
}
