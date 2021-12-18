/**
 * Write a description of class AirportRoutes here.
 *
 * @author Mohammed Patel
 * @version Take-Home Final 12/18
*/
import java.security.*;
import java.util.*;
import java.io.*;

public class AirportRoutes{
    ArrayList<String> airports = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> combined;
    ArrayList<Edge> routes = new ArrayList<>();
    ArrayList<Integer> scc = new ArrayList<>();
    ArrayList<DirectedNodeList> dGraph;

    HashMap<Integer, ArrayList<Integer>> strongCC = new HashMap<>();
    HashedDirectedGraph rd =  new HashedDirectedGraph();
    HashMap<String,Integer> labeledAirports;
    int[] finishingNumber;
    int[] components;
    boolean[] marked;

    int maxNumOfCC = 0;
    int finalCount = 0;
    int numOfAirports;    
    int numOfSCC = 0;
    
    
    public AirportRoutes(){     
        dGraph = new ArrayList<>();
        numOfAirports = 0;
    }
    
    
    public AirportRoutes(int n){     
        labeledAirports = new HashMap<>(n);
        finishingNumber = new int[n];
        dGraph = new ArrayList<>(n);
        marked = new boolean[n];
        components = new int[n];    
        numOfAirports = n;
        
        for(int i=0; i<numOfAirports; i++){
            dGraph.add(new DirectedNodeList());
        }
    }
    
    
    public void readAndStoreData(String airportsFile, String routesFile){
        BufferedReader r1;
        BufferedReader r2;
            
        try{
            r1 = new BufferedReader(new FileReader(airportsFile)); 
            r2 = new BufferedReader(new FileReader(routesFile)); 
            String line = r1.readLine(); 
            String route = r2.readLine();
            int count = 0;
            
            while (line != null){
                labeledAirports.put(line,count); 
                airports.add(line);
                line = r1.readLine();
                count++;
            }
            
            r1.close(); 
            
            while(route != null){
               String[] path = route.split(" ");   
               Edge e = new Edge(labeledAirports.get(path[0]),labeledAirports.get(path[1]));
               routes.add(e);
               addEdge(labeledAirports.get(path[0]),labeledAirports.get(path[1]));
               route = r2.readLine();
            }
            
            r2.close();           
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
    
    
    public void addEdge(int u, int v){
       if (u>=0 && u<numOfAirports && v>=0 && v<numOfAirports){    
           if (!isEdge(u,v) && u!=v){
               getNeighborList(v).addToInList(u);
               getNeighborList(u).addToOutList(v);
            }
       }
       else{
           throw new IndexOutOfBoundsException();
       }
    }  
    public boolean isEdge(int u, int v){
        if (u>=0 && u<numOfAirports && v>=0 && v<numOfAirports){            
            if (u != v){
               return getNeighborList(u).getOutList().contains(v);
            }
            else{
                return true;
            }        
        }
        else{
            throw new IndexOutOfBoundsException();
        }
    }         
    public DirectedNodeList getNeighborList(int u){
        return dGraph.get(u);
    }   
    
        
    public void depthFirstTraversal(){
        int max=0;
        
        for (int i=numOfAirports-1; i>=0; i--){
           int u = finishingNumber[i];
           
           if(!marked[u]){
               numOfSCC++;
               maxNumOfCC = 0;
               scc = new ArrayList<>();
               dFT(u);
               
               for (Integer k: scc)
               components[k] = u;
               strongCC.put(u,scc);
           }
           
           if (maxNumOfCC > max){
               max = maxNumOfCC;
            }
        }
        
        System.out.println("max SCC size: " + max);
    }   
    public void dFT(int v){
        scc.add(v);
        maxNumOfCC += 1;
        marked[v] = true;
            
        for (Integer u: dGraph.get(v).getOutList()){
            if(!marked[u]){
                dFT(u);   
            }
        }
    }

    
    public void postOrderDepthFirstTraversal(boolean reverse){
        for (int i=0; i<numOfAirports; i++){
           if (!marked[i]){
               postOrderDFT(i,reverse);
           }
        }
    }   
    public void postOrderDFT(int v,boolean reverse){           
        marked[v] = true;
        
        if (reverse) {
            for (Integer u: dGraph.get(v).getInList()){
                if (!marked[u]){
                    postOrderDFT(u,reverse);
                }
            }
        }
        else {
            for (Integer u:dGraph.get(v).getOutList()){
                  if (!marked[u]){
                      postOrderDFT(u,reverse);
                  }
            }
        }
        
        finishingNumber[finalCount++] = v;
    }
    
    
    public void buildHubs(){
        SCC p = new SCC();
        p.readAndStoreGraph("airports-IATA-codes.txt","routes.txt");
        AirportRoutes dirGraph = new AirportRoutes(p.numVertex);
        
        for (Edge e: p.edgeSet){
           dirGraph.addEdge(e.v1,e.v2);
        }
        
        dirGraph.postOrderDepthFirstTraversal(true);
        dirGraph.marked= new boolean[p.numVertex];
        dirGraph.depthFirstTraversal();
        System.out.println(dirGraph.numOfSCC);
        
        HashedDirectedGraph hashedDGraph = p.buildReducedGraph(dirGraph); 
        System.out.println("size: " + hashedDGraph.getNumVertex());
        System.out.println("edges: " + hashedDGraph.getNumEdges());
        rd = hashedDGraph;
    }

        
    public int minRoutes(String port){ 
        int mappedVertex = labeledAirports.get(port);  
        int count = 0;
        
        for(int vertex: rd.hdg.keySet()){ 
            int size = rd.hdg.get(vertex).getInDegree();
            
            if(size == 0){
                count++;
            }
        }
        
        return count;
    }

       
    public int numFlightNetworks(){          
        ArrayList<DirectedNodeList> dg = dGraph;
        combined = combine(dg);  
        int res = recursiveDFT(dg);
          
        return res;
    }  
    public ArrayList<ArrayList<Integer>> combine(ArrayList<DirectedNodeList> d){
        ArrayList<ArrayList<Integer>> merge = new ArrayList<>();
        
        for(int i=0; i<d.size(); i++){
            ArrayList<Integer> mix = new ArrayList<>(); 
            
            for(Integer v: d.get(i).getOutList()){
                mix.add(v);
            }
            
            for(Integer v2: d.get(i).getInList()){
                mix.add(v2);
            }
            
            merge.add(mix);
        }
        
        return merge;
    }
    

    public int recursiveDFT(ArrayList<DirectedNodeList> ar){
        ArrayList<ArrayList<Integer>> weakCC = new ArrayList<>();
        boolean[] marked = new boolean[ar.size()];
        
        for (int i = 0; i < ar.size(); i++){
            if (!marked[i]){
                ArrayList<Integer> comp = new ArrayList<>();
                dftRec(i, marked, comp);
                weakCC.add(comp);
            }
        }
        
        return weakCC.size();
    }   
    public void dftRec(int idx, boolean[] mark, ArrayList<Integer> comp){
        mark[idx] = true;
        comp.add(idx);
        
        for (int v: combined.get(idx)){
            if (!mark[v]){
                dftRec(v, mark, comp);
            }
        }
    }

    
    public static void main(String[] args){
        AirportRoutes test1 = new AirportRoutes(9135);
        test1.readAndStoreData("airports-IATA-codes.txt","routes.txt");       
        test1.buildHubs(); 
        System.out.println("ORD Routes: " + test1.minRoutes("ORD")); 
         
        AirportRoutes test2 = new AirportRoutes(9135);
        test2.readAndStoreData("airports-IATA-codes.txt","routes.txt");
        System.out.println(test2.numFlightNetworks());
    }
}