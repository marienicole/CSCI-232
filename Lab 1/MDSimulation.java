import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Particle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;

/**
 * Created by Marie Morin, Brandon Klise, and Thane O'Brien.
 * CSCI 232 Lab 1: MDSimulation
 * Finished on 2/4/2017
 * <p>
 * In this program, the methods defined below cause different sized balls to bounce off
 * of the walls of the JFrame, as well as each other. They utilize priority Queues, which
 * hold the impending events, sorted by which one is going to happen in the least amount
 * of time.
 * <p>
 * We utilized the Particle.java, StdDraw.java, and MinPQ.java classes provided for us in
 * the algs4.jar bundle.
 */
public class MDSimulation {
    // creates an arraylist of particles
    private ArrayList<Particle> particles = new ArrayList<Particle>();
    // creates a priority queue for the events
    private MinPQ<Event> queue = new MinPQ<Event>();
    // creates a time variable to save the time from the most recent event
    private double deltaT = 0;
    // creates a variable called length of type int that will be used to determine how many particles the input file contains
    private int length = 0;

    // constructor for MDSimulation class, sets up the particle array
    public MDSimulation() {
        this.particles = particles;
    }

    // main method
    public static void main(String[] args) {

        // creates a frame and enables double buffering by utilizing the StdDraw class
        StdDraw.setCanvasSize(800, 800);
        StdDraw.enableDoubleBuffering();

        // creates an instance of the MDSimulation class
        MDSimulation sim = new MDSimulation();

        // calls the readFile method on the sim instance to begin the program
        sim.readFile(args);

    }

    // reads the input text file
    private void readFile(String[] args) {
        particles = new ArrayList<Particle>(Integer.valueOf(StdIn.readLine()));
        String eachParticle;

        // this if statement looks at the first line to determine how many particles will be simulated
        while ((eachParticle = StdIn.readLine()) != null) {
            // this section takes in a line, then tokenizes it into different parts so that these parts can be saved to an instance of a particle
            // this loop happens until there is no more text to read
            String[] tokens = eachParticle.split(" ");
            double rx = Double.valueOf(tokens[1]);
            double ry = Double.valueOf(tokens[2]);
            double vx = Double.valueOf(tokens[3]);
            double vy = Double.valueOf(tokens[4]);
            double radius = Double.valueOf(tokens[5]);
            double mass = Double.valueOf(tokens[6]);
            int R = Integer.valueOf(tokens[7]);
            int G = Integer.valueOf(tokens[8]);
            int B = Integer.valueOf(tokens[9]);
            Color color = new Color(R, G, B);
            Particle particle = new Particle(rx, ry, vx, vy, radius, mass, color);
            particles.add(particle);
        }
        // begins the drawing and simulation part of the program
        doSimulation();
    }

    private void doSimulation() {
        // updates the queue
        for (int i = 0; i < length; i++) {
            updateQueue(particles.get(i));
        }
        // inserts an empty event into the queue so that the queue actually calls the draw method below
        queue.insert(new Event(0, null, null));

        // does the animation only while the queue is not empty
        while (!queue.isEmpty()) {
            Event curEvent = queue.delMin();

            // updates the simulation only if the current event is valid
            if (!curEvent.wasSuperveningEvent()) {
                Particle a = curEvent.getParticle1();
                Particle b = curEvent.getParticle2();

                // updates the positions of the particles
                for (int i = 0; i < length; i++) {
                    particles.get(i).move(curEvent.getTime() - deltaT);
                }

                if (a != null && b != null)
                    a.bounceOff(b);
                else if (a == null && b == null)
                    draw();
                else if (b == null)
                    a.bounceOffVerticalWall();
                else if (a == null)
                    b.bounceOffHorizontalWall();


                // updates deltaT to the time of the most current event
                deltaT = curEvent.getTime();

                // updates the queue for all possibilities of particles a and b
                updateQueue(a);
                updateQueue(b);
            }
        }

    }

    // updates the priority queue for all the possibilities of particle p
    private void updateQueue(Particle p) {
        if (p == null) return;

        for (int i = 0; i < particles.size(); i++) {
            double time = p.timeToHit(particles.get(i));
            if (time != Double.MAX_VALUE)
                queue.insert(new Event(deltaT + time, p, particles.get(i)));
        }

        queue.insert(new Event(deltaT + p.timeToHitVerticalWall(), p, null));
        queue.insert(new Event(deltaT + p.timeToHitHorizontalWall(), null, p));

    }

    // draws all the particles to the screen
    private void draw() {
        StdDraw.clear();
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).draw();
        }
        StdDraw.show();
        StdDraw.pause(20);

        queue.insert(new Event(deltaT + 1.0 / 2, null, null));
    }

}