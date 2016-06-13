package crawler.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fabi on 17.02.2015.
 */
public class DefaultNode<N extends Comparable<N>> implements Comparable<DefaultNode<N>> {
    private N id;
    private Map<String, Object> attributes = null;

    public DefaultNode(N id) {
        this.id = id;
    }

    public N getId() {
        return id;
    }

    @Override
    public int compareTo(DefaultNode<N> other) {
        return id.compareTo(other.getId());
    }

    public <T> void setAttribute(String name, T value) {
        if(attributes == null) {
            attributes = new HashMap<>();
        }
        this.attributes.put(name, value);
    }

    public <T> T getAttribute(String name) {
        if(attributes == null) {
            return null;
        }
        return (T)this.attributes.get(name);
    }

    public boolean hasAttribute(String name) {
        if(attributes == null) {
            return false;
        }
        return this.attributes.get(name) != null;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof DefaultNode) {
            return ((DefaultNode) o).compareTo(this) == 0;
        }
        return false;
    }
}