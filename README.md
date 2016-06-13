# mauscrawler
A crawler4j based crawler for crawling and analyzing websites created during a data-mining course at university.

## Description and motivation
The crawler is supposed to work as a multithreaded application in order to maximize the utilization of available cpu power and particularly to minimize the bottleneck posed by the politeness-delay used for web-crawling. By crawling multiple websites at once, you get your results way faster than by doing this step by step.
While crawling the sites, a directed graph is created for every site containing pages as nodes and internal links as edges. This graph structure is used later for structural analysis of the crawled sites.
After the initial crawling, the site is being processed in a pipeline of mechanisms from refining the generated graph to analyzing the content of the page for word-cloud generation for example.
There are several interfaces against which you can implement your own post-processing classes, on per site basis or for all sites (e.g. for calculation of averages).
After the processing of the pages, all generated contents are saved to disk in the **maus/result/** folder, so you can browse them and do whatever you want ;).

## Setting up and starting the crawler for the first time
Project is able to run as-is. You have to insert your crawling seed into **maus/seed/{files}** and there is a group result created for every one of those files and a group result for all files together.
Use the **maus/config/exclude.txt** file to insert words you don't want to be used for word-cloud seed generation.
After setting up those files, you can configure the crawler in **src/main/java/root/Mainclass.java** where you can decide what classes to use for post-processing your sites, when to stop crawling a single site (crawling depth, page number and politeness-delay) and how many threads to use for the whole process (crawling threads for the initial crawling and worker threads for the processing of the crawled sites).
