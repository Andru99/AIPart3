package uk.ac.bris.cs.scotlandyard.ui.ai.GraphComponent;
import java.util.List;
public class Leaf {
    final private String data;
    final private String id;
    private List< Leaf > children;
    public Leaf(String data, String id){
        this.id=id;
        this.data=data;
    }
    private String getId(){
        return id;
    }
    private String getData(){
        return data;
    }
    private List <Leaf> returnChild(){
        return children;
    }
    @Override
    public String toString() {
        return data;
    }
}

