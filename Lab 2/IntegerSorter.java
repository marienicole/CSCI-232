/**
 * CSCI 232 Lab 2, Bin Packing.
 * Created by Marie Morin, Thane O'Brien, and Brandon Klise.
 * This program is supposed to take files and place them on disks of size 1 GB.
 * We are going to attempt to put the files on disks, using the smallest amount of disks possible.
 */

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import edu.princeton.cs.algs4.MaxPQ;

public class IntegerSorter {
    MaxPQ<Integer> mp = new MaxPQ<>();
    //creates an array list of ints (these are the sound files)
    ArrayList<Integer> intList = new ArrayList<>();

    private void readFile(String[] args) {
        if (args.length > 0) { // code provided as standard input
            FileInputStream is = null;
            try {
                is = new FileInputStream(new File(args[0]));
            } catch (Exception ex) {
                System.err.println(ex);
            }
            System.setIn(is);
        }

        //creates a scanner to scan through the input file
        Scanner scan = new Scanner(System.in);

        //scans through the list of ints and adds the ints to the array list until it is at the end of the file
        while(scan.hasNextInt())
        {
            intList.add(scan.nextInt());
        }

    }

    private void sortList()
    {
        //initializes a maximum oriented priority queue of sound files
        for(int i = 0;i<intList.size(); i++)
            mp.insert(intList.get(i));
    }

    public static void main(String args[]) {
        //creates an instance of the class
        IntegerSorter is = new IntegerSorter();

        //calls readFile to read the text file that will be piped into WorstFit
        is.readFile(args);

        //sorts the sound files
        is.sortList();

        //prints out the sorted list of sound files
        int size = is.mp.size();

        for(int i = 0; i<size; i++)
        {
            System.out.println(is.mp.delMax());
        }

    }
}
