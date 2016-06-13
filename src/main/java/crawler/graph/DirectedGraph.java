package crawler.graph;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.*;

/**
 * Created by Fabi on 17.02.2015.
 */
public class DirectedGraph<N extends Comparable> {
    private List<N> nodes = new ArrayList<>();
    private Map<N, List<DefaultEdge<N>>> edges = new TreeMap<>();

    public synchronized void addVertex(N vertex) {
        if(nodes.contains(vertex)) {
            throw new RuntimeException("There is already a node with value " + vertex);
        }
        nodes.add(vertex);
        edges.put(vertex, new ArrayList<>());
    }

    public synchronized void addEdge(N start, N end) {
        DefaultEdge<N> d = new DefaultEdge();

        if(!this.hasVertex(start) || !this.hasVertex(end)) {
            throw new RuntimeException("Either start or end node is not in graph");
        }

        d.setStartVertex(start);
        d.setEndVertex(end);
        if(!this.getEdges().contains(d)) {
            edges.get(start).add(d);
        }
    }

    public synchronized boolean hasVertex(N vertex) {
        return nodes.contains(vertex);
    }

    public synchronized N getVertex(N vertex) {
        if(this.nodes.contains(vertex)) {
            return this.nodes.get(this.nodes.indexOf(vertex));
        }
        return null;
    }

    public synchronized List<N> getVertices() {
        return this.nodes;
    }

    public synchronized List<DefaultEdge<N>> getEdges() {
        List<DefaultEdge<N>> val = new ArrayList<>();

        for(N node : edges.keySet()) {
            val.addAll(edges.get(node));
        }

        return val;
    }

    public synchronized List<DefaultEdge<N>> getOutgoingEdges(N vertex) {
        return edges.get(vertex);
    }

    public synchronized Integer distance(N from, N to) {
        if(!nodes.contains(from) || !nodes.contains(to)) {
            return null;
        }

        return this.getAdjacencyMatrixTransitive().get(from).get(to);
    }

    public synchronized Map<N, Map<N, Integer>> getAdjacencyMatrix() {
        Map<N, Map<N, Integer>> matrix = new TreeMap<>();

        for(N node : nodes) {
            matrix.put(node, new TreeMap<>());
            matrix.get(node).put(node, 0); // always reach itself
        }

        for(N node : nodes) {
            for(DefaultEdge<N> edge : edges.get(node)) {
                matrix.get(edge.getStartVertex()).put(edge.getEndVertex(), 1); // reachable in 1 step
            }
        }

        return matrix;
    }

    public synchronized Map<N, Map<N, Integer>> getAdjacencyMatrixTransitive() {
        Map<N, Map<N, Integer>> matrix = getAdjacencyMatrix();

        for(N k : matrix.keySet()) {
            for(N i : matrix.keySet()) {
                for(N j : nodes) {
                    if(matrix.get(i).containsKey(k) && matrix.get(k).containsKey(j)) {
                        Integer nDist = matrix.get(i).get(k) + matrix.get(k).get(j);
                        Integer oDist = matrix.get(i).containsKey(j) ? matrix.get(i).get(j) : null;
                        if(oDist == null || nDist < matrix.get(i).get(j)) {
                            matrix.get(i).put(j, nDist);
                        }
                    }
                }
            }
        }

        return matrix;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();

        Map<N, Map<N, Integer>> matrix = getAdjacencyMatrixTransitive();

        b.append(String.format("%4s", " "));
        for(N node : nodes) {
            b.append(String.format("%4s", node));
        }
        b.append("\n");

        for(N node : nodes) {
            b.append(String.format("%4s", node));
            for(N nodeHelp : nodes) {
                if(matrix.get(node).containsKey(nodeHelp)) {
                    b.append(String.format("%4s", matrix.get(node).get(nodeHelp)));
                } else {
                    b.append(String.format("%4s", " "));
                }
            }
            b.append("\n");
        }

        return b.toString();
    }

    /**
     * creates a GraphML String to be saved as a .graphml file
     *
     * @return String
     */
    public String toGraphML(String graphId) {
        StringBuffer output = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");

        output.append("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"  \n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns\n" +
                "     http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n");

        output.append("<key id=\"d0\" for=\"node\" attr.name=\"title\" attr.type=\"string\">\n\t<default>UNDEFINED</default>\n</key>\n");
        output.append("<key id=\"d1\" for=\"node\" attr.name=\"url\" attr.type=\"string\">\n\t<default>UNDEFINED</default>\n</key>\n");
        output.append("<key id=\"d2\" for=\"node\" attr.name=\"parent\" attr.type=\"string\">\n\t<default>none</default>\n</key>\n");
        output.append("<key id=\"d3\" for=\"node\" attr.name=\"crawldepth\" attr.type=\"int\">\n\t<default>0</default>\n</key>\n");
        output.append("<key id=\"d4\" for=\"node\" attr.name=\"filesize\" attr.type=\"int\">\n\t<default>0</default>\n</key>\n");
        output.append("<key id=\"d5\" for=\"edge\" attr.name=\"spanning\" attr.type=\"boolean\">\n\t<default>false</default>\n</key>\n");

        output.append("<graph id=\"" + graphId + "\" edgedefault=\"directed\">\n");

        for(N node : this.nodes) {
            String optionalData = "";
            if(node instanceof DefaultNode) {
                if(((DefaultNode) node).hasAttribute("title")) {
                    optionalData = "\t<data key=\"d0\">" + StringEscapeUtils.escapeXml10(((DefaultNode) node).getAttribute("title").toString()) + "</data>\n";
                }
                if(((DefaultNode) node).hasAttribute("url")) {
                    optionalData += "\t<data key=\"d1\">" + StringEscapeUtils.escapeXml10(((DefaultNode) node).getAttribute("url").toString()) + "</data>\n";
                }
                if(((DefaultNode) node).hasAttribute("parent-docid")) {
                    optionalData += "\t<data key=\"d2\">n" + ((DefaultNode) node).getAttribute("parent-docid") + "</data>\n";
                }
                if(((DefaultNode) node).hasAttribute("crawl-depth")) {
                    optionalData += "\t<data key=\"d3\">" + ((DefaultNode) node).getAttribute("crawl-depth") + "</data>\n";
                }
                if(((DefaultNode) node).hasAttribute("size")) {
                    optionalData += "\t<data key=\"d4\">" + ((DefaultNode) node).getAttribute("size") + "</data>\n";
                }
            }
            output.append("<node id=\"n" + node + "\"" + (optionalData.isEmpty() ? "/>\n" : ">\n" + optionalData + "</node>\n"));
        }
        for(DefaultEdge<N> edge : this.getEdges()) {
            String optionalData = "";
            if(edge.getStartVertex() instanceof DefaultNode) {
                DefaultNode source = (DefaultNode) edge.getStartVertex();
                DefaultNode sink = (DefaultNode) edge.getEndVertex();

                if(sink.hasAttribute("parent-docid") && sink.getAttribute("parent-docid").equals(source.getId())) {
                    optionalData += "<data key=\"d5\">true</data>";
                }
            }
            output.append("<edge source=\"n" + edge.getStartVertex() + "\" target=\"n" + edge.getEndVertex() + "\"" + (optionalData.isEmpty() ? "/>\n" : (">\n\t"  + optionalData + "\n</edge>\n")));
        }

        output.append("</graph>\n</graphml>\n");

        return output.toString();
    }
}