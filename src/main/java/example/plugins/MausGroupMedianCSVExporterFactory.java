package example.plugins;

import system.plugins.MausGroupContentFactoryInterface;
import system.plugins.MausGroupContentInterface;

/**
 * Created by Fabi on 09.04.2015.
 */
public class MausGroupMedianCSVExporterFactory implements MausGroupContentFactoryInterface {
    @Override
    public MausGroupContentInterface create(String seed) {
        return new MausGroupMedianCSVExporter(seed, new String[]{"wiener-index", "balaban-index", "randic-index", "zagreb-index", "vertex-count", "average-depth"});
    }
}
