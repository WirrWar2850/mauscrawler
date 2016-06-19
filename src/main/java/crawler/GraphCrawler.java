package crawler;

import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Fabi on 06.02.2015.
 */
public class GraphCrawler extends WebCrawler {
    private final static Pattern FILTER = Pattern.compile(".*(\\.(css|js|gif|jpe?g|png|mp3|mp3|zip|gz))$");

    public void addPage(Page page) {
        Map<String,Object> customData = ((HashMap<String,Object>)this.getMyController().getCustomData());
        DirectedGraph<DefaultNode<Integer>> graph = (DirectedGraph<DefaultNode<Integer>>)customData.get("graph");

        HtmlParseData htmlParseData = (HtmlParseData)page.getParseData();

        DefaultNode<Integer> n = new DefaultNode<>(page.getWebURL().getDocid());

        n.setAttribute("parent-url", page.getWebURL().getParentUrl());
        n.setAttribute("url", page.getWebURL().getURL().toLowerCase());
        n.setAttribute("parent-docid", page.getWebURL().getParentDocid());
        n.setAttribute("docid", page.getWebURL().getDocid());
        n.setAttribute("html-content", htmlParseData.getHtml());
        n.setAttribute("text-content", htmlParseData.getText());
        n.setAttribute("title", htmlParseData.getTitle());
        n.setAttribute("links", page.getParseData().getOutgoingUrls());
        n.setAttribute("crawl-depth", Integer.valueOf(page.getWebURL().getDepth()));
        n.setAttribute("meta-tags", ((HtmlParseData) page.getParseData()).getMetaTags());
        n.setAttribute("language", page.getLanguage());
        n.setAttribute("size", page.getContentData().length);

        graph.addVertex(n);
        if(page.getWebURL().getParentUrl() != null) {
            graph.addEdge(graph.getVertex(page.getWebURL().getParentDocid()), n);
        }
    }

    @Override
    public Object getMyLocalData() {
        Map<String,Object> customData = ((HashMap<String,Object>)this.getMyController().getCustomData());
        DirectedGraph<DefaultNode<Integer>> graph = (DirectedGraph<DefaultNode<Integer>>)customData.get("graph");
        return graph;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        Map<String,Object> customData = ((HashMap<String,Object>)this.getMyController().getCustomData());
        String baseURL = (String)customData.get("base-url");
        String href = url.getURL().toLowerCase();
        return href.startsWith(baseURL.trim()) && !FILTER.matcher(href).matches();
    }

    @Override
    public void visit(Page page) {
        page.getWebURL().getParentDocid();
        addPage(page);
    }
}