package system.processor;

import crawler.GraphCrawler;
import system.plugins.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class to execute all steps from crawling
 * to saving the generated data
 *
 * Created by Fabi on 20.02.2015.
 */
public class MausProcessor <T extends GraphCrawler> implements MausCrawlerListenerInterface, MausProcessorListenerInterface {
    private List<SeedStorage> seedStorages = new ArrayList<>();
    private SeedStorage currentStorage = null;
    private Iterator<SeedStorage> storageIterator;

    private MausPageCollector collector = new MausPageCollector();
    private MausCrawlerConfig<T> crawlerConfig;

    private volatile int activeCrawlers = 0;

    private ExecutorService crawlerThreadPool;
    private ExecutorService processorThreadPool;

    private long lastFinishTime = 0;

    public MausProcessor(MausCrawlerConfig<T> crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
        this.crawlerConfig.addMausCrawlerListener(this);
        this.crawlerConfig.addMausProcessorListener(this);
        this.crawlerThreadPool = Executors.newFixedThreadPool(crawlerConfig.getWorkerThreads());
        int processorThreads = crawlerConfig.getWorkerThreads()/4;
        if(processorThreads == 0) {
            processorThreads = 1;
        }
        this.processorThreadPool = Executors.newFixedThreadPool(processorThreads);
    }

    private void loadSeedStorages() {
        String seedRoot = "maus/" + crawlerConfig.getSeed() + "/";

        File f = new File(seedRoot);
        int seedCount = 0;

        for(String file : f.list()) {
            seedStorages.add(new SeedStorage(seedRoot + file));
        }
        for(SeedStorage storage : seedStorages) {
            seedCount += storage.size();
        }

        storageIterator = seedStorages.iterator();

        System.out.println("Loaded " + seedCount + " seeds!");
    }

    public void doSomeCrawlingThings() throws Exception{
        loadSeedStorages();
        System.out.println("Started crawling at " + crawlerConfig.getTimer().start());
        while(hasPageToCrawl()) {
            System.out.println("Enqueuing next page for crawling...");

            this.activeCrawlers++;

            crawlNextPage();
        }
        lastFinishTime = System.currentTimeMillis();
        while(this.activeCrawlers > 0 && System.currentTimeMillis() - lastFinishTime < crawlerConfig.getCrawlerTimeoutMilliseconds()) {
            Thread.sleep(1000);
        }

        crawlerThreadPool.shutdown();
        processorThreadPool.shutdown();

        finishCrawling();

        if(this.activeCrawlers > 0) {
            System.out.println("Stopped crawling due to timeout!");
        }

        System.out.println("Finished crawling in " + crawlerConfig.getTimer().stop());
        System.out.println(crawlerConfig.getWithdrawnPages() + " Probes have been withdrawn due to filtering.");
    }

    private void crawlNextPage() throws Exception {
        while(currentStorage == null || !currentStorage.hasNext()) {
            if(!storageIterator.hasNext()) {
                System.out.println("no more seeds to crawl!");
            } else {
                currentStorage = storageIterator.next();
                System.out.println("loaded next storage with " + currentStorage.getSeeds().size() + " seeds");
            }
        }

        crawlPage(currentStorage.nextSeed());
    }

    private boolean hasPageToCrawl() {
        if((currentStorage == null || !currentStorage.hasNext()) && !storageIterator.hasNext()) {
            return false;
        }
        return true;
    }

    private void crawlPage(String url) throws Exception {
        this.crawlerThreadPool.execute(new MausCrawlerRunnable<T>(this.collector, this.crawlerConfig, url, currentStorage.getName()));
    }

    /**
     * called once all work is finished
     */
    private void finishCrawling() {
        List<List<MausGroupContentInterface>> groups = crawlerConfig.getAllGroups();
        for(List<MausGroupContentInterface> g : groups) {
            g.stream().forEach(MausGroupContentInterface::save);
        }
        this.collector.getPages().forEach(MausPage::save);
    }

    @Override
    public synchronized void onFinishedCrawling(MausCrawlerRunnable worker) {
        System.out.println("Crawled '" + worker + "', start processing...");
        this.processorThreadPool.execute(new MausProcessorRunnable<>(this.crawlerConfig, worker.getPage(), worker.getGraph()));
        this.updateTimeout();
    }

    @Override
    public void onFinishedProcessing(MausProcessorRunnable worker) {
        this.activeCrawlers--;
        System.out.println("Finished '" + worker + "', " + this.activeCrawlers + " active crawlers left...");
        this.updateTimeout();
    }

    private void updateTimeout() {
        this.lastFinishTime = System.currentTimeMillis();
    }
}