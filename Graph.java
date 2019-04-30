package uk.ac.bris.cs.scotlandyard.ui.ai.GraphComponent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    List<Edge> edges=new LinkedList<>();
    List<Leaf> leaves=new LinkedList<>();
    public void addLeaves(Leaf a){
        leaves.add(a);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public List<Leaf> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<Leaf> leaves) {
        this.leaves = leaves;
    }
}
