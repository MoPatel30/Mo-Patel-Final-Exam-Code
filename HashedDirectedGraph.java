/**
 * Write a description of class HashedDirectedGraph here.
 *
 * @author Mohammed Patel
 * @version Take-home Final 12/18
*/
import java.util.*;

public class HashedDirectedGraph {
    HashMap<Integer,DirectedNodeList> hdg;
    int numVertex;
    int numEdges;
    
    
    public HashedDirectedGraph(){   
        numVertex = 0;
        numEdges=0;
        hdg = new HashMap<>();       
    }
    
    
    public HashedDirectedGraph(int n){   
        numVertex = n;
        numEdges=0;
        hdg = new HashMap<>(n);        
    }
    
    
    public void addVertex(int k){
        if (hdg.get(k)==null)
        hdg.put(k,new DirectedNodeList());
    }
    
    
    public boolean isEdge(int u, int v){   
        return (hdg.get(u).getOutList().contains(v));
    }
    
    
    //assume vertices u and v exist
    public void addEdge(int u, int v){
        if  ((u!=v) && !isEdge(u,v)){
            DirectedNodeList d1= hdg.get(u);
            DirectedNodeList d2 = hdg.get(v);
            
            d1.addToOutList(v);
            d2.addToInList(u);
            numEdges++;
        }
    }
    
    
    public int getNumVertex(){
        return numVertex;
    }
    
    
    public int getNumEdges(){
        return numEdges;
    }     
}