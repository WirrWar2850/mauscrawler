package crawler.graph;

/**
 * Created by Fabi on 17.02.2015.
 */
public class DefaultEdge<N> {
    private N startVertex;
    private N endVertex;

    public void setStartVertex(N startVertex) {
        this.startVertex = startVertex;
    }

    public void setEndVertex(N endVertex) {
        this.endVertex = endVertex;
    }

    public N getStartVertex() {
        return startVertex;
    }

    public N getEndVertex() {
        return endVertex;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof DefaultEdge) {
            return startVertex.equals(((DefaultEdge) o).getStartVertex()) && endVertex.equals(((DefaultEdge) o).getEndVertex());
        }
        return false;
    }
}