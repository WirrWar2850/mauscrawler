package example.plugins;

import system.plugins.MausGroupContentFactoryInterface;
import system.plugins.MausGroupContentInterface;

/**
 * Created by Fabi on 3/4/2015.
 */
public class MausGroupMetaWordCloudFactory implements MausGroupContentFactoryInterface {
    @Override
    public MausGroupContentInterface create(String seed) {
        return new MausGroupMetaWordCloud(seed);
    }
}
