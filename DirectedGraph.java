import java.util.*;
import java.io.*;

public class DirectedGraph  {
    ArrayList<DirectedNodeList> dGraph;
    int numVertex;
   boolean [] marked;
   int[] finishingNumber;
   int finCount;
   int maxCC;
   int numSCC;
   HashMap<Integer, ArrayList<Integer>> stronglyCCs;
   ArrayList<Integer> scc;
   
    int[] components;
    
    
    public DirectedGraph() {
        dGraph = new ArrayList<>();
        numVertex=0;
        finCount=0;
        maxCC=0;
        numSCC=0;
        stronglyCCs = new HashMap<>();
        scc = new ArrayList<>();        
    }
    
    public int getNumVertex(){
        return numVertex;
    }
    
    public DirectedGraph(int n) {
      numVertex =n;
      dGraph = new ArrayList<>(n);
      marked= new boolean[n];
      finishingNumber= new int[numVertex];
      finCount=0;
      scc = new ArrayList<>();
      stronglyCCs=new HashMap<>();
      // maxCC=0;
      // numSCC=0;
      components= new int[numVertex];
      
      for (int i=0;i<numVertex;i++)
       dGraph.add(new DirectedNodeList());
       
    }
    
    
    public boolean isEdge (int u, int v){
        if (u>=0 && u<numVertex && v>=0 && v<numVertex) { 
           
           if (u!=v) 
           return getNeighborList(u).getOutList().contains(v);
           else return true;
        
        }
        else throw new IndexOutOfBoundsException();
    }
    
    
    public void addEdge(int u, int v) {
       // assume all vertices are created
       // directed edge u to v will cause outdegree of u to go up and indegree of v to go up.
       //System.out.println(numVertex);
       //System.out.println("u ="+u+"v = "+v);
       if (u>=0 && u<numVertex && v>=0 && v<numVertex) { 
           
           if (u!=v && !isEdge(u,v)) {
           getNeighborList(u).addToOutList(v);
           getNeighborList(v).addToInList(u);
        }
        }
        else throw new IndexOutOfBoundsException();
    }
    
    
    public DirectedNodeList getNeighborList(int u) {
        return dGraph.get(u);
    }
    
    
    public void printAdjacency(int u) {
       DirectedNodeList dnl = getNeighborList(u);
       System.out.println ("vertices going into "+u+"  "+dnl.getInList());
       System.out.println ("vertices going out of "+u+"  "+dnl.getOutList());
       System.out.println();
    }
    
    
    public void postOrderDepthFirstTraversal(boolean reverse) {
       for (int i=0;i<numVertex;i++) 
       if (!marked[i])
           postOrderDFT (i,reverse);   
    }
    
    
    public void postOrderDFT(int v,boolean reverse){
        
        marked[v]=true;
        if (reverse) {
        for (Integer u:dGraph.get(v).getInList())
        if (!marked[u]) postOrderDFT(u,reverse);
    }
        else {
             for (Integer u:dGraph.get(v).getOutList())
              if (!marked[u]) postOrderDFT(u,reverse);
            }
        finishingNumber[finCount++]=v;
    }
    
    
    public void depthFirstTraversal() {
        int max=0;
       for (int j=numVertex-1;j>=0;j--) {
           int u=finishingNumber[j];
       
       if (!marked[u]) {
           numSCC++;
           //System.out.println ("leader "+u);
           
           maxCC=0;
           scc=new ArrayList<>();
           dFT(u);
           for (Integer k: scc)
           components[k]=u;
           stronglyCCs.put(u,scc);
       }
       
       if (maxCC>max) max=maxCC;
       }
        System.out.println("max SCC size = "+max);
    }
    
    
    public void dFT(int v){
       // System.out.println(v);
        scc.add(v);
         maxCC++;
        marked[v]=true;
        
        for (Integer u:dGraph.get(v).getOutList())
        if (!marked[u]) dFT(u);
       
    }   
}