import edu.princeton.cs.algs4.Particle;

/**
 * Created by Marie Morin, Brandon Klise, and Thane O'Brien.
 * CSCI 232 Lab 1: MDSimulation
 * Finished on 2/4/2017
 * <p>
 * This class is the events (collisions between particles and walls).
 * We implemented Comparable so that we could compare an event to another event,
 * in order to sort them in the priority queue.
 * <p>
 * This class implements the Particle.java class as provided for us in the
 * algs4.jar file.
 */
public class Event implements Comparable<Event> {

    private double time;
    private Particle a, b;
    private int collisionCount_A;
    private int collisionCount_B;

    public Event(double t, Particle a, Particle b) {
        this.a = a;
        this.b = b;
        this.time = t;

        // sets the collision counts of the two particles equal to the current collision count of the particle if the particle is not null
        if (a != null)
            collisionCount_A = a.count();
        else
            collisionCount_A = -1;
        if (b != null)
            collisionCount_B = b.count();
        else
            collisionCount_B = -1;
    }

    public double getTime() { // returns time to event
        return time;
    }

    public Particle getParticle1() { // returns first particle
        return a;
    }

    public Particle getParticle2() { // returns second particle
        return b;
    }

    // compares the amount of time left in two events
    @Override
    public int compareTo(Event e) {
        if (time > e.getTime())
            return 1;
        else if (time < e.getTime())
            return -1;
        else
            return 0;
    }

    // if the two particles have identical collision counts, the event is valid
    public boolean wasSuperveningEvent() {
        if (a != null && a.count() != collisionCount_A)
            return true;
        else if (b != null && b.count() != collisionCount_B)
            return true;
        else
            return false;
    }
}