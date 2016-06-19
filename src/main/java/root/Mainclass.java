package root;

import crawler.GraphCrawler;
import example.indices.*;
import example.plugins.*;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import system.processor.MausCrawlerConfig;
import system.processor.MausProcessor;


/**
 * Mäusetool für Website Crawling Blabla
 *
 * Created by Fabi on 06.02.2015.
 */
public class Mainclass {
    public static void main(String args[]) throws Exception {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        // configure the appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.ERROR);
        console.activateOptions();
        // add appender to any Logger (here is root)
        Logger.getRootLogger().addAppender(console);

        String seed = "seed";

        if(args.length > 0) {
            seed = args[0];
        }

        MausCrawlerConfig<GraphCrawler> config = new MausCrawlerConfig<>(GraphCrawler.class);

        config.setSeed(seed);
        config.addPostProcessor(new MausPagePostProcessorGraph()); // comment to compute only Spanningtree

        config.addPageFilter(new MausPageFilterInvalid());

        config.addConsumer(new ZagrebGroupConsumer());
        config.addConsumer(new WienerConsumer());
        config.addConsumer(new RandicIndexConsumer());
        config.addConsumer(new BalabanIndexConsumer());
        config.addConsumer(new VertexCountConsumer());
        config.addConsumer(new LanguageConsumer());
        config.addConsumer(new AverageDepthConsumer());

        config.addContentFactory(new MausPageContentMatrixFactory());
//        config.addContentFactory(new MausPageContentPagesFactory());
        config.addContentFactory(new MausPageContentGraphMLFactory());
        config.addContentFactory(new MausPageContentWordCloudFactory("maus/config/exclude.txt"));
        config.addContentFactory(new MausPageContentWordCountFactory());

        config.addGroupContentFactory(new MausGroupContentIndexImageExporterFactory());
        config.addGroupContentFactory(new MausGroupRichCSVExporterFactory());
        config.addGroupContentFactory(new MausGroupMetaWordCloudFactory());
        config.addGroupContentFactory(new MausGroupPercentilesCSVExporterFactory(5));
        config.addGroupContentFactory(new MausGroupPercentilesCSVExporterFactory(10));
        config.addGroupContentFactory(new MausGroupMedianCSVExporterFactory());

        config.setNumberOfCrawlers(8);
        config.setNumberOfWorkerThreads(8);
        config.setCrawlDepth(4);
        config.setMaxPages(300);
        config.setPolitenessDelay(150);

        MausProcessor proc = new MausProcessor<>(config);

        proc.doSomeCrawlingThings();
    }
}