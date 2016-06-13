package example.plugins;

import system.plugins.MausGroupContentFactoryInterface;
import system.plugins.MausGroupContentInterface;

/**
 * Created by Fabi on 3/17/2015.
 */
public class MausGroupPercentilesCSVExporterFactory implements MausGroupContentFactoryInterface {
    private int percentiles;

    public MausGroupPercentilesCSVExporterFactory(int numPercentiles) {
        this.percentiles = numPercentiles;
    }

    @Override
    public MausGroupContentInterface create(String seed) {
        return new MausGroupIndexPercentilesCSVExporter(seed, percentiles);
    }
}
