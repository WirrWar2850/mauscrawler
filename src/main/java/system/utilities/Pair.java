package system.utilities;

/**
 * Created by Fabi on 17.02.2015.
 */
public class Pair<U,V> {
    public U _1;
    public V _2;

    public Pair(U _1, V _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public String toString() {
        return "(" + _1 + "," + _2 + ")";
    }
}