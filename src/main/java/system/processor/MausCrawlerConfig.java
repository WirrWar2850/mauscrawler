package system.processor;

import com.drew.lang.annotations.NotNull;
import crawler.GraphCrawler;
import system.consumer.GraphConsumerInterface;
import system.plugins.*;
import system.utilities.PerformanceTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabi on 06.03.2015.
 */
public class MausCrawlerConfig <T extends GraphCrawler> {
    private volatile int crawlDepth = 6;
    private volatile int maxPages = 200;
    private volatile int politenessDelay = 250;
    private volatile int workerThreads = 2;
    private volatile int numberOfCrawlers = 2;
    private volatile long crawlerTimeoutMilliseconds = 300000;
    private volatile String seed = "seed";

    private List<MausPageFilterInterface> pageFilters = new ArrayList<>();
    private List<MausPagePostProcessorInterface> postProcessors = new ArrayList<>();
    private List<GraphConsumerInterface> graphConsumers = new ArrayList<>();
    private List<MausPageContentFactoryInterface> contentFactories = new ArrayList<>();

    private List<MausGroupContentFactoryInterface> groupFactories = new ArrayList<>();
    private Map<String,List<MausGroupContentInterface>> seedGroups = new HashMap<>();
    private List<MausGroupContentInterface> groups = new ArrayList<>();

    private List<MausCrawlerListenerInterface> crawlerListener = new ArrayList<>();
    private List<MausProcessorListenerInterface> processorListener = new ArrayList<>();

    private PerformanceTimer timer = new PerformanceTimer();

    private volatile int withdrawnPages = 0;

    private Class<T> crawlerClass;

    public MausCrawlerConfig(@NotNull Class<T> crawlerClass) {
        this.crawlerClass = crawlerClass;
    }

    public int getCrawlDepth() {
        return this.crawlDepth;
    }

    public int getMaxPages() {
        return this.maxPages;
    }

    public int getPolitenessDelay() {
        return this.politenessDelay;
    }

    public String getSeed() {
        return seed;
    }

    public long getCrawlerTimeoutMilliseconds() {
        return this.crawlerTimeoutMilliseconds;
    }

    public Class<T> getCrawlerClass() {
        return this.crawlerClass;
    }

    public void incrementWithdrawnPage() {
        this.withdrawnPages++;
    }

    public int getWithdrawnPages() {
        return this.withdrawnPages;
    }

    public PerformanceTimer getTimer() {
        return this.timer;
    }

    public void setCrawlDepth(int crawlDepth) {
        this.crawlDepth = crawlDepth;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    public void setPolitenessDelay(int politenessDelay) {
        this.politenessDelay = politenessDelay;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public void setCrawlerTimeoutMilliseconds(long timeout) {
        this.crawlerTimeoutMilliseconds = timeout;
    }

    /**
     * sets the number of crawlers used to crawl one
     * seed url
     *
     * @param number int
     */
    public void setNumberOfCrawlers(int number) {
        this.numberOfCrawlers = number;
    }

    public int getNumberOfCrawlers() {
        return this.numberOfCrawlers;
    }

    /**
     * sets the number of seed urls crawled simultaneously
     *
     * @param number int
     */
    public void setNumberOfWorkerThreads(int number) {
        this.workerThreads = number;
    }

    public int getWorkerThreads() {
        return this.workerThreads;
    }

    public synchronized void addConsumer(GraphConsumerInterface consumer) {
        this.graphConsumers.add(consumer);
    }

    public synchronized void removeConsumer(GraphConsumerInterface consumer) {
        this.graphConsumers.remove(consumer);
    }

    public List<GraphConsumerInterface> getGraphConsumers() {
        return this.graphConsumers;
    }

    public synchronized void addContentFactory(MausPageContentFactoryInterface factory) {
        this.contentFactories.add(factory);
    }

    public synchronized void removeContentFactory(MausPageContentFactoryInterface factory) {
        this.contentFactories.remove(factory);
    }

    public List<MausPageContentFactoryInterface> getContentFactories() {
        return this.contentFactories;
    }

    public synchronized void addPostProcessor(MausPagePostProcessorInterface processor) {
        this.postProcessors.add(processor);
    }

    public synchronized void removePostProcessor(MausPagePostProcessorInterface processor) {
        this.postProcessors.remove(processor);
    }

    public List<MausPagePostProcessorInterface> getPostProcessors() {
        return this.postProcessors;
    }

    public synchronized void addPageFilter(MausPageFilterInterface filter) {
        this.pageFilters.add(filter);
    }

    public synchronized void removePageFilter(MausPageFilterInterface filter) {
        this.pageFilters.remove(filter);
    }

    public List<MausPageFilterInterface> getPageFilters() {
        return this.pageFilters;
    }

    public synchronized void addGroupContentFactory(MausGroupContentFactoryInterface factory) {
        this.groupFactories.add(factory);
    }

    public synchronized void removeGroupContentFactory(MausGroupContentFactoryInterface factory) {
        this.groupFactories.remove(factory);
    }

    public synchronized List<MausGroupContentInterface> getGlobalContentGroups() {
        if(this.groups.isEmpty()) {
            this.groups = new ArrayList<>();
            for(MausGroupContentFactoryInterface f : this.groupFactories) {
                this.groups.add(f.create(null));
            }
        }
        return this.groups;
    }

    public synchronized List<MausGroupContentInterface> getSeedContentGroups(String seed) {
        if(!this.seedGroups.containsKey(seed)) {
            this.seedGroups.put(seed, new ArrayList<>());
            for(MausGroupContentFactoryInterface f : this.groupFactories) {
                this.seedGroups.get(seed).add(f.create(seed));
            }
        }
        return this.seedGroups.get(seed);
    }

    public synchronized List<List<MausGroupContentInterface>> getAllGroups() {
        List<List<MausGroupContentInterface>> result = new ArrayList<>();

        result.add(groups);
        result.addAll(seedGroups.values());

        return result;
    }

    public void addMausCrawlerListener(MausCrawlerListenerInterface listener) {
        this.crawlerListener.add(listener);
    }

    public void removeMausCrawlerListener(MausCrawlerListenerInterface listener) {
        this.crawlerListener.remove(listener);
    }

    public synchronized void onFinishedCrawling(MausCrawlerRunnable who) {
        for(MausCrawlerListenerInterface l : this.crawlerListener) {
            l.onFinishedCrawling(who);
        }
    }

    public void addMausProcessorListener(MausProcessorListenerInterface listener) {
        this.processorListener.add(listener);
    }

    public void removeMausProcessorListener(MausProcessorListenerInterface listener) {
        this.processorListener.remove(listener);
    }

    public synchronized void onFinishedProcessing(MausProcessorRunnable who) {
        for(MausProcessorListenerInterface l : this.processorListener) {
            l.onFinishedProcessing(who);
        }
    }
}