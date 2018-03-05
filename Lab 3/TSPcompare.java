/*
 * Created by Marie Morin, Thane O'Brien, and Brandon Klise.
 * This project is the Traveling Salesman problem. We are looking to find the shortest
 * possible path connecting all cities in the map.
 */

import java.util.Scanner;
import java.util.ArrayList;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;

public class TSPcompare {
    City current = null; // current starts out as null
    City prevCity = null; // when we're on our first iteration, there will be no previous city
    ArrayList<City> unvisitedC = new ArrayList<>();
    ArrayList<City> visitedC = new ArrayList<>();
    ArrayList<Edge> mstEdges = new ArrayList<>(); //creates an array list of edges for the minumum span tree

    private class City {
        public double x, y;
        public int cityNum;

        public City(int i) {
            cityNum = i;
            this.x = (Math.random() * 100); // random x value
            this.y = (Math.random() * 100); // random y value
        }
        

        public void setPrev(City c) // sets previous city
        {
            prevCity = c;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
    
    public TSPcompare(){}
    
    public TSPcompare(TSPcompare tsp)
    {
        for(int i = 0; i < tsp.visitedC.size(); i++)
            this.visitedC.add(tsp.visitedC.get(i));
        for(int i = 0; i < tsp.unvisitedC.size(); i++)
            this.unvisitedC.add(tsp.unvisitedC.get(i));
    }

    private void makeCities(int vertNum) { // generates the cities
        for (int i = 0; i < vertNum; i++) {
            if (i == 0) {
                current = new City(i); // if first iteration, current is that city
                visitedC.add(current);
            } else {
                unvisitedC.add(new City(i)); // otherwise just makes a new city, adds to arrayList
            }
        }
    }

    private double distance(City a, City b) { // calculates distance between cities
        double xDistance = b.getX() - a.getX();
        double yDistance = b.getY() - a.getY();
        double total = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        return total;
    }

    private void travelTo(City c) // moving to the next city
    {
        prevCity = current; // current becomes previous
        current = c; // previous becomes c
        visitedC.add(current); // adds to visited list
        unvisitedC.remove(current); // removes from unvisited list
    }

    private City findClosest(City current) //finds the closest city to the current city passed in
    {
        City closest = unvisitedC.get(0);  //initially set the closest city to the first unvisited city on the list
        
        if(unvisitedC.size() == 1)      //if the unvisited city list only has one city left, then that city is the next closest city
            closest = unvisitedC.get(0);
        else    //otherwise, run through the unvisited city list and find the next closest city to the current city
        for (int i = 0; i < unvisitedC.size(); i++) {
            if(distance(current, unvisitedC.get(i)) < distance(current, closest)) //only reset the closest city if the unvisited city's distance is less than the closest city's distance
                closest = unvisitedC.get(i);
        }
            return closest; //return the closest city
    }
    
    private void twiceAround(City current)
    {
        //add the city to the visited city list before finding the next city
        this.visitedC.add(current);
        int i = 0;
        //search through the mst to find the next vertex that connects to the current vertex
        while(!mstEdges.isEmpty()) {
            int v = mstEdges.get(i).either();       //vertex v of the current edge
            int w = mstEdges.get(i).other(mstEdges.get(i).either());  //vertex w of the current edge (the other vertex)
            //if an edge is found that matches the current city, jump to the other city on that edge
            if(v == current.cityNum || w == current.cityNum) {
                mstEdges.remove(i);
                i = -1;
                this.unvisitedC.remove(current);
                if(v != current.cityNum) {
                    for (int j = 0; j < this.unvisitedC.size(); j++)
                        if(unvisitedC.get(j).cityNum == v)
                            twiceAround(this.unvisitedC.get(j));
                }
                else {
                    for (int k = 0; k < this.unvisitedC.size(); k++)
                        if(unvisitedC.get(k).cityNum == w)
                            twiceAround(this.unvisitedC.get(k));
                }
            }
            i++; //increment i to move to the next edge in the list
            if(i >= mstEdges.size())    //if no edges are found for the next city, break the recursive loop to return to the previous city
                break;
        }
    }
    
    public void travel() { // starts traveling from city to city
        do {
            travelTo(findClosest(current)); //travel to the next closest city to the current city
        } while(!unvisitedC.isEmpty()); //travel until there are no more unvisited cities left

        visitedC.add(visitedC.get(0)); //once done traveling through cities, travel back to the first city
    }

    public static void main(String args[]) {
        double counter = 0; //creates a counter variable to total up traveled distances
        long startTime, stopTime, elapsedTime;  //creates time variables to count how long the calculations take
        Scanner sc = new Scanner(System.in);
        TSPcompare greedy = new TSPcompare(); // creates instance of TSPcompare for greedy approach
        if (args.length > 0) { // if there is a value for args
            int numOfVerts = Integer.parseInt(args[0]); // reads in # of verticies
            
            /*Computes and solves the Greedy approach*/
            startTime = System.currentTimeMillis(); //start the timer
            greedy.makeCities(numOfVerts);      //generate as many cities as the argument passed in
            TSPcompare twiceAround = new TSPcompare(greedy); // creates instance of TSPcompare for twice around approach
            greedy.travel();  //do the greedy approach
            stopTime = System.currentTimeMillis(); //start the timer
            
            /*Outputs a summary of the Greedy approach*/
            System.out.println("Greedy tour:");
            while(!greedy.visitedC.isEmpty())
            {
                if(greedy.visitedC.size() > 1) {
                System.out.print(greedy.visitedC.get(0).cityNum);
                System.out.print(" -> ");
                counter += greedy.distance(greedy.visitedC.get(0), greedy.visitedC.get(1));
                }
                else
                    System.out.print(greedy.visitedC.get(0).cityNum + "\n");
                greedy.visitedC.remove(0);
            }
            System.out.printf("Total cost: %.3f\n", counter);
            elapsedTime = stopTime - startTime;
            System.out.println("Time to find: " + elapsedTime + " milliseconds\n");
            
            
            /*Computes and solves the Twice Around the Tree Approach*/
            startTime = System.currentTimeMillis(); //start the timer
            EdgeWeightedGraph graph = new EdgeWeightedGraph(Integer.parseInt(args[0])); //creates an edge weighted graph with as many vertices as passed into the argument
            twiceAround.unvisitedC.add(twiceAround.visitedC.get(0)); //puts all the cities on the unvisited city list for the twiceAround instance
            twiceAround.visitedC.remove(0);
            
            for(int i = 0; i < twiceAround.unvisitedC.size(); i++)      //generates the edge weighted graph
                for(int j = i +1; j < twiceAround.unvisitedC.size(); j++)
                    graph.addEdge(new Edge(i,j,twiceAround.distance(twiceAround.unvisitedC.get(i), twiceAround.unvisitedC.get(j))));
            
            KruskalMST mst = new KruskalMST(graph); //generates the minimum span tree
            
            for(Edge e : mst.edges()) //saves the edges of the mst to the array list of edges
            {
                twiceAround.mstEdges.add(e);
            }
            
            twiceAround.twiceAround(twiceAround.unvisitedC.get(twiceAround.unvisitedC.size()-1));
            twiceAround.visitedC.add(twiceAround.visitedC.get(0));
            stopTime = System.currentTimeMillis(); //stop the timer
            
            /*Outputs a summary of the twice around the tree approach*/
            System.out.println("Twice around the tree approach:");
            counter = 0;
            while(!twiceAround.visitedC.isEmpty())
            {
                if(twiceAround.visitedC.size() > 1) {
                System.out.print(twiceAround.visitedC.get(0).cityNum);
                System.out.print(" -> ");
                counter += twiceAround.distance(twiceAround.visitedC.get(0), twiceAround.visitedC.get(1));
                }
                else
                    System.out.print(twiceAround.visitedC.get(0).cityNum + "\n");
                twiceAround.visitedC.remove(0);
            }
            System.out.printf("Total cost: %.3f\n", counter);
            elapsedTime = stopTime - startTime;
            System.out.println("Time to find: " + elapsedTime + " milliseconds\n");
        }
    }
}