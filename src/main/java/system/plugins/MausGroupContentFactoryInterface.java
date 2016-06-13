package system.plugins;

/**
 * creates group content instances, therefore
 * gets plugged into MausProcessor
 *
 * Created by Fabi on 23.02.2015.
 */
public interface MausGroupContentFactoryInterface {
    MausGroupContentInterface create(String seed);
}
