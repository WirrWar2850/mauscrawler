package system.processor;

import crawler.GraphCrawler;
import crawler.graph.DefaultNode;
import crawler.graph.DirectedGraph;
import system.consumer.GraphConsumerInterface;
import system.plugins.MausGroupContentInterface;
import system.plugins.MausPageContentFactoryInterface;
import system.plugins.MausPageFilterInterface;
import system.plugins.MausPagePostProcessorInterface;
import system.utilities.PerformanceTimer;

/**
 * does the processing work after the initial crawling
 * of a website
 *
 * Created by Fabi on 06.03.2015.
 */
public class MausProcessorRunnable<T extends GraphCrawler> implements Runnable {
    MausPage page;
    DirectedGraph<DefaultNode<Integer>> graph;
    MausCrawlerConfig<T> crawlerConfig;
    PerformanceTimer timer = new PerformanceTimer();

    public MausProcessorRunnable(MausCrawlerConfig<T> config, MausPage page, DirectedGraph<DefaultNode<Integer>> graph) {
        this.page = page;
        this.graph = graph;
        this.crawlerConfig = config;
    }

    @Override
    public void run() {
        System.out.println("Starting processing of '" + page + "' [" + timer.start() + "]");

        for(MausPageFilterInterface f : crawlerConfig.getPageFilters()) {
            if(!f.useProbe(page, graph)) {
                crawlerConfig.incrementWithdrawnPage();
                System.out.println("Probe '" + page + "' withdrawn by filter '" + f.getName() + "' (" + crawlerConfig.getWithdrawnPages() + ") [" + timer.stop() + "]");
                crawlerConfig.onFinishedProcessing(this);
                return;
            }
        }

        for(MausPagePostProcessorInterface processor : crawlerConfig.getPostProcessors()) {
            processor.process(graph);
        }

        try {
            for (GraphConsumerInterface c : crawlerConfig.getGraphConsumers()) {
                page.setAttribute(c.getName(), c.consume(graph));
            }

            for (MausPageContentFactoryInterface f : crawlerConfig.getContentFactories()) {
                page.setContent(f.getContentName(), f.createContent(page, graph));
            }

            for (MausGroupContentInterface g : crawlerConfig.getSeedContentGroups(page.getSeed())) {
                g.handle(page);
            }

            for (MausGroupContentInterface g : crawlerConfig.getGlobalContentGroups()) {
                g.handle(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
            crawlerConfig.incrementWithdrawnPage();
            crawlerConfig.onFinishedProcessing(this);
            return;
        }

        page.save();

        System.out.println("Finished processing '" + this + "' [" + timer.stop() + "]");

        crawlerConfig.onFinishedProcessing(this);
    }

    @Override
    public String toString() {
        return this.page.toString();
    }
}
