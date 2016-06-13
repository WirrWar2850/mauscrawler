package example.plugins;

import system.plugins.MausGroupContentFactoryInterface;
import system.plugins.MausGroupContentInterface;

/**
 * Created by Fabi on 28.02.2015.
 */
public class MausGroupContentIndexImageExporterFactory implements MausGroupContentFactoryInterface {
    @Override
    public MausGroupContentInterface create(String seed) {
        return new MausGroupContentIndexImageExporter(seed);
    }
}
