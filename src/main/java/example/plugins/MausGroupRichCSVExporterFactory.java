package example.plugins;

import system.plugins.MausGroupContentFactoryInterface;
import system.plugins.MausGroupContentInterface;

/**
 * Created by Fabi on 03.03.2015.
 */
public class MausGroupRichCSVExporterFactory implements MausGroupContentFactoryInterface {
    @Override
    public MausGroupContentInterface create(String seed) {
        return new MausGroupGenericCSVExporter(seed, "rich-csv-sample",
                new String[]{"name","vertex-count",
                        "balaban-index","randic-index",
                        "wiener-index","zagreb-index",
                        "average-depth","languages"});
    }
}
