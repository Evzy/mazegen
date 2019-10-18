package mazeGenerator;

import maze.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class KruskalGenerator implements MazeGenerator {

   /* each arraylist to store nodes, edges, and connected nodes */
   ArrayList<Edge> edges = new ArrayList<Edge>();
   ArrayList<Node> nodes = new ArrayList<Node>();
   ArrayList<nodeSet> connectedNodes = new ArrayList<nodeSet>();

   @Override
   public void generateMaze(Maze maze){

      for(int i = 0; i < maze.sizeR; i++){
	         for(int j = 0; j < maze.sizeC; j++){
	               nodes.add(new Node(maze.map[i][j]));
	         }
	     }

	   /* Next, create a new edge for each connected cells. A special case if the maze is a tunnel maze.*/
	   for(Node node : nodes){
	      /* Creating new edge for each direction */
	      for(int h = 0; h < maze.NUM_DIR; h++){
	         if(node.getCell().neigh[h] != null){
	            Cell toCell = node.getCell().neigh[h];
	            Node toNode = null;
	            for(Node sNode : nodes){ // Searching for the destinated node to store in edge class
	               if(sNode.getCell() == toCell){
	                  toNode = sNode;
	                  break;
	               }
	            }
	            if(toNode != null){
	               Edge newEdges = new Edge(node, toNode, h);
	               edges.add(newEdges);
	               node.addEdge(newEdges, h);
	            }
	         }
	      }
	         if(node.getCell().tunnelTo != null){
	            Node tunnelDest = null;
	            for(Node sNode : nodes){ // Searching for destinated node from the tunnel.
	               if(sNode.getCell() == node.getCell().tunnelTo){
	                  tunnelDest = sNode;
	                  break;
	               }
	            }
	            node.setTunnel(tunnelDest);
	            /* checkConnection function is called so that no duplicated connection is made.
	             * If not, then it will create a new connection. */
               if(!checkConnection(node, tunnelDest)){
   	            nodeSet tunnelNodeSet = new nodeSet();
                  tunnelNodeSet.connectedNodes.put(tunnelDest.getKey(), tunnelDest);
                  tunnelNodeSet.connectedNodes.put(node.getKey(), node);
                  connectedNodes.add(tunnelNodeSet);
               }
	         }
      }

	   /* Next, do a loop for all the listed edges in random order and depending if the cell is already connected or not, create a new connection. */
	   while(!edges.isEmpty()){
	      Random random = new Random();
	      int index = random.nextInt(edges.size());
         int direction = edges.get(index).getDirection();

         /* checkConnection returns true if there at least one connection found. */
         if(!checkConnection(edges.get(index).getFrom(), edges.get(index).getTo())){
            /* Add connectedEdge for both of the nodes. */
            edges.get(index).getFrom().addConnectedEdge(direction);
            int reverse;
            if(direction > 2) // mathematically, this is logical.
               reverse = direction - 3;
            else
               reverse = direction + 3;
            edges.get(index).getTo().addConnectedEdge(reverse);
            edges.get(index).getFrom().getCell().wall[direction].present = false;

            nodeSet fromSet = null;
            nodeSet toSet = null;
            /* Search nodeSet for the current node to the destinated node. */
            for(nodeSet nodeSet : connectedNodes){
               if(nodeSet.searchNode(edges.get(index).getFrom()))
                  fromSet = nodeSet;
               if(nodeSet.searchNode(edges.get(index).getTo()))
                  toSet = nodeSet;
            }
            /* If they both don't have any connection, create a new nodeSet and add both node to the connectedNodes list. */
            if(fromSet == null && toSet == null){
               nodeSet newNodeSet = new nodeSet();
               newNodeSet.connectedNodes.put(edges.get(index).getFrom().getKey(), edges.get(index).getFrom());
               newNodeSet.connectedNodes.put(edges.get(index).getTo().getKey(), edges.get(index).getTo());
               connectedNodes.add(newNodeSet);
            }
            /* If only one of them have connection, merge it. */
            else if( fromSet == null)
               toSet.connectedNodes.put(edges.get(index).getFrom().getKey(), edges.get(index).getFrom());
            else
               fromSet.connectedNodes.put(edges.get(index).getTo().getKey(), edges.get(index).getTo());
         }
         /* If both nodes have connection, check for a loop by calling isLoop function. */
         else{
            if(!isLoop(edges.get(index).getFrom(), edges.get(index).getTo())){
               /* Add connectedEdge for both of the nodes. */
               edges.get(index).getFrom().addConnectedEdge(direction);
               int reverse;
               if(direction > 2)
                  reverse = direction - 3;
               else
                  reverse = direction + 3;
               edges.get(index).getTo().addConnectedEdge(reverse);
               edges.get(index).getFrom().getCell().wall[direction].present = false;

               nodeSet fromSet = null;
               nodeSet toSet = null;
               for(nodeSet nodeSet : connectedNodes){
                  /* Search nodeSet for the current node to the destinated node. */
                  if(nodeSet.searchNode(edges.get(index).getFrom()))
                     fromSet = nodeSet;
                  if(nodeSet.searchNode(edges.get(index).getTo()))
                     toSet = nodeSet;
               }
               /* Create a new nodeSet and merge both nodeSet of the current node and destinated node,
                * then add it to the connectedNodes list while removing the old nodeSet. */
               nodeSet newNodeSet = new nodeSet();
               newNodeSet.connectedNodes.putAll(fromSet.connectedNodes);
               newNodeSet.connectedNodes.putAll(toSet.connectedNodes);
               connectedNodes.remove(fromSet);
               connectedNodes.remove(toSet);
               connectedNodes.add(newNodeSet);
            }
         }
         edges.remove(index);
	   }
	} // end of generateMaze()

   public boolean checkConnection(Node from, Node to){
      nodeSet fromSet = null;
      nodeSet toSet = null;
      if(connectedNodes.size() == 0)
         return false;
      for(nodeSet nodeSet : connectedNodes){
         if(nodeSet.searchNode(from))
            fromSet = nodeSet;
         if(nodeSet.searchNode(to))
            toSet = nodeSet;
      }
      if(fromSet == null || toSet == null)
         return false;
      else
         return true;
   }

   public boolean isLoop(Node from, Node to){
      nodeSet toSet = null;
      for(nodeSet nodeSet : connectedNodes){
         if(nodeSet.searchNode(to))
            toSet = nodeSet;
      }
      if(toSet.connectedNodes.containsValue(from))
         return true;
      return false;
     }

	private class nodeSet{
      private HashMap<String, Node> connectedNodes = new HashMap<String, Node>();
      public boolean searchNode(Node node){
         if(connectedNodes.containsValue(node))
            return true;
         else
            return false;
      }
	}

	private class Node{
	   private String key;
	   private Cell cell;
	   private Node tunnel;
	   private Edge[] edges = new Edge[6];
	   private Edge[] connectedEdges = new Edge[6];

	   public Node(Cell cell){
	      this.cell = cell;
	      key = cell.c + "&" + cell.r;
	   }
	   public String getKey(){
	      return key;
	   }
	   public void addEdge(Edge edge, int direction){
	      edges[direction] = edge;
	   }
	   public void addConnectedEdge(int direction){
	      connectedEdges[direction] = edges[direction];
	   }
	   public Node getTunnel(){
	      return tunnel;
	   }
	   public void setTunnel(Node node){
	      tunnel = node;
	   }
	   public Cell getCell(){
	      return cell;
	   }
	}

	private class Edge{
	   private Node from;
	   private Node to;
	   private int direction;

	   public Edge(Node from, Node to, int direction){
	      this.from = from;
	      this.to = to;
	      this.direction = direction;
	   }
	   public int getDirection(){
	      return direction;
      }
	   public Node getFrom(){
         return from;
      }
	   public Node getTo(){
         return to;
      }
	}
} // end of class KruskalGenerator
