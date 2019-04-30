package uk.ac.bris.cs.scotlandyard.ui.ai;
import uk.ac.bris.cs.scotlandyard.ui.ai.GraphComponent.Edge;
import uk.ac.bris.cs.scotlandyard.ui.ai.GraphComponent.Leaf;
import uk.ac.bris.cs.scotlandyard.ui.ai.GraphComponent.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Dalghoritm {
    private List<Edge> listOFedges;
    private List<Leaf> listOFleaves;
    Dalghoritm(List<Edge> listOFedges,List<Leaf> listOFleaves){
        listOFedges=this.listOFedges=new ArrayList<>();
        listOFleaves=this.listOFleaves=new ArrayList<>();
    }
    private int Getdistance(Leaf source, Leaf destination){
        for(Edge edge : listOFedges){
            if(edge.GetSource()==source && edge.GetDestination()==destination){
                edge.GetWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }
    private int GetMINIMALdistance(Leaf source, Graph graph)
    {
        //need a method to check if source and destination are connected one with each other which i will call vecini( neighboors)
        return 0;
    }

}
