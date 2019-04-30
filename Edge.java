package uk.ac.bris.cs.scotlandyard.ui.ai.GraphComponent;

public class Edge {
    public Leaf leaf;
    public Leaf source;

    private final int weight;
    Edge(Leaf leaf, int weight, Leaf source){
        this.weight=weight;
        this.leaf=leaf;
        this.source=source;
    }
    public Leaf GetDestination(){
        return leaf;
    }
    public Leaf GetSource(){
        return source;
    }
    public String rtUse(){
        return  leaf + " " + source;
    }
    public int GetWeight(){
        return weight;
    }

}
