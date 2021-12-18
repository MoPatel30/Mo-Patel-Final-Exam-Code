/**
 * Write a description of class SCC here.
 *
 * @author Mohammed Patel
 * @version Take-home Final 12/18
 */
import java.util.*;
import java.io.*;

public class SCC{
    static ArrayList<Edge> edgeSet;
    static int numVertex;
    HashedDirectedGraph reduced;
    static HashMap<String, Integer> AirportAsInts;
    
    
    public SCC(){                
        edgeSet= new ArrayList<>();
        numVertex =0;
        reduced = new HashedDirectedGraph();
        AirportAsInts = new HashMap<>();
    }
    
    
    public HashedDirectedGraph buildReducedGraph(AirportRoutes dg){
        reduced = new HashedDirectedGraph(dg.numOfSCC);
        
        for (int c: dg.strongCC.keySet())
            reduced.addVertex(c);
        
        for (Edge e:edgeSet) {
            if (e.v1!=e.v2) {
                int c1=dg.components[e.v1];
                int c2= dg.components[e.v2];
                if (c1!=c2){
                  reduced.addEdge(c1,c2);
                  
                }
            }
        }      
               
        return reduced;
    }
    
    
    public static void readAndStoreGraph(String airports, String routes) {
        try{
            Scanner sc1 = new Scanner(new File(airports));
            Scanner sc2 = new Scanner(new File(routes));
            String s1;
            String s2;
            
            int i = 0;
            int maxV = 0;
            int count = 0;
            edgeSet = new ArrayList<>();
       
            while (sc1.hasNextLine()){
                s1 = sc1.nextLine();
                AirportAsInts.put(s1, count);
                count++;
            }
            
            while(sc2.hasNextLine()){
                s2 = sc2.nextLine();
                
                if(s2.isEmpty()) continue;
                
                String[] line = s2.split(" ");
                int v1 = AirportAsInts.get(line[0]);
                int v2 = AirportAsInts.get(line[1]);
                edgeSet.add(new Edge(v1, v2));
                int p = Math.max(v1, v2);
                
                if(p > maxV){
                    maxV = p;
                }
                
                i++;
            }
            numVertex = maxV + 1;
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
}