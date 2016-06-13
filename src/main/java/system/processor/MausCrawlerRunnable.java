package system.processor;

import crawler.GraphCrawler;
import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import system.utilities.PerformanceTimer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main worker class for multithreaded crawling of multiple web-domains
 *
 * Created by Fabi on 06.03.2015.
 */
public class MausCrawlerRunnable<T extends GraphCrawler> implements Runnable {
    private MausPageCollector collector;
    private MausCrawlerConfig<T> crawlerConfig;
    private String url;
    private String seed;
    private PerformanceTimer timer = new PerformanceTimer();
    private MausPage page;
    private DirectedGraph<DefaultNode<Integer>> graph;

    public MausCrawlerRunnable(MausPageCollector collector, MausCrawlerConfig<T> config, String url, String seed) {
        this.collector = collector;
        this.crawlerConfig = config;
        this.url = url;
        this.seed = seed;
    }

    @Override
    public void run() {
        try {
            this.doTheWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doTheWork() throws Exception{
        System.out.println("Crawling " + url + " [" + timer.start() + "]");

        String goodName = this.url.replace("/", "");
        goodName = goodName.replace("\\", "").replace("http:", "").replace("https:", "").replace(".", "_").replace("?", "").replace("&", "").replace("=", "");

        String crawlStorageFolder = "data/" + seed + "/" + goodName;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(crawlerConfig.getCrawlDepth());
        config.setMaxPagesToFetch(crawlerConfig.getMaxPages());
        config.setPolitenessDelay(crawlerConfig.getPolitenessDelay());
        config.setIncludeBinaryContentInCrawling(false);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        controller.addSeed(url);

        Map<String,Object> customData = new HashMap<>();
        customData.put("base-url", url);
        customData.put("graph", new DirectedGraph<DefaultNode<Integer>>());

        controller.setCustomData(customData);

        controller.start(crawlerConfig.getCrawlerClass(), crawlerConfig.getNumberOfCrawlers());

        page = new MausPage(url, seed);
        List<Object> localData = controller.getCrawlersLocalData();
        graph = (DirectedGraph<DefaultNode<Integer>>)localData.get(0);

        System.out.println("Finished crawling '" + page + "' [" + timer.stop() + "]");

        crawlerConfig.onFinishedCrawling(this);
    }

    public String toString() {
        return this.seed + " - " + this.url;
    }

    public MausPage getPage() {
        return this.page;
    }

    public DirectedGraph<DefaultNode<Integer>> getGraph() {
        return this.graph;
    }
}
