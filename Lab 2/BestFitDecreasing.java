/**
 * CSCI 232 Lab 2, Bin Packing.
 * Created by Marie Morin, Thane O'Brien, and Brandon Klise.
 * This program is supposed to take files and place them on disks of size 1 GB.
 * We are going to attempt to put the files on disks, using the smallest amount of disks possible.
 */

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import edu.princeton.cs.algs4.BST;


public class BestFitDecreasing {
    BST<Integer, ArrayList<Disk>> bst = new BST<>();

    public void createDisks(ArrayList<Integer> soundFiles) {
        //sorts through the sound files and puts the disks together until there are no more sound files
        while (!soundFiles.isEmpty()) {
            //reads the largest sound in the sound list
            int currentSound = soundFiles.get(0);

            //removes sound from the list after it is read
            soundFiles.remove(0);

            Disk disk = null;

            //if there exists a disk with enough available space
            try {
                if(bst.ceiling(currentSound) == null)
                    bst.delete(bst.ceiling(currentSound));
                int key = bst.ceiling(currentSound);
                disk = bst.get(key).get(0);
                bst.get(key).remove(0);
                disk.addFile(currentSound);

                if(bst.get(key).isEmpty())
                    bst.delete(key);
            } catch (NoSuchElementException | IllegalArgumentException e) {
                disk = new Disk(currentSound);
            }

            if(bst.contains(disk.room()))
                bst.get(disk.room()).add(disk);
            else {
                ArrayList<Disk> newList = new ArrayList<>();
                newList.add(disk);
                bst.put(disk.room(), newList);
            }

        }

        ArrayList<Disk> allDisks = new ArrayList<>();

        while(!bst.isEmpty())
        {
            int key = bst.max();
            for(int i = 0; i < bst.get(key).size(); i++) {
                allDisks.add(bst.get(key).get(i));
            }
            bst.deleteMax();
        }

        //creates a variable for the total size of all of the sound files
        double fileSizeSum = 0;

        //sums up all the sound files on the disk
        for (int i = 0; i < allDisks.size(); i++) {
            for(int j = 0; j< allDisks.get(i).al.size();j++)
                fileSizeSum += allDisks.get(i).returnFile(j);
        }
        //converts the sum from KBs to GBs
        fileSizeSum = fileSizeSum / 1000000;

        //prints a summary of the output
        System.out.println("Sum of all files = " + fileSizeSum + " GB");
        System.out.println("Disks used\t = " + allDisks.size());

        //prints out the resulting list of disks
        for (int i = (allDisks.size()-1); i >=0; i--) {
            //displays the current disk
            System.out.print("\t"+ (7-i) + " ");
            System.out.format("%6d", allDisks.get(i).room()); // prints out according to instructions, correct spacing
            System.out.print(" : ");

            //prints all the files on the current instance of the disk
            for (int j = 0; j < allDisks.get(i).al.size(); j++) {
                System.out.print(allDisks.get(i).returnFile(j) + " ");
            }
            System.out.println();
        }

    }


    //main method
    public static void main(String args[]) {
        //creates an array list of sorted ints (these are the sound files)
        ArrayList<Integer> sortedInts = new ArrayList<>();

        if (args.length > 0) { // code provided as standard input
            FileInputStream is = null;
            try {
                if (args.length == 1)
                    is = new FileInputStream(new File(args[0]));
                else
                    for (int i = 0; i < args.length; i++) {
                        sortedInts.add(Integer.parseInt(args[i]));
                    }
            } catch (Exception ex) {
                System.err.println(ex);
            }
            System.setIn(is);
        }

        //if the sound files are being piped from IntegerSorter, then take the sorted ints and call the create disks method on them
        if (sortedInts.size() != 0) {
            //runs the program on the sorted ints
            BestFitDecreasing fit = new BestFitDecreasing();
            fit.createDisks(sortedInts);
        } else    //if there are no sorted ints, then scan the input file and call create disks on the unsorted ints
        {
            //creates a scanner to scan through the input file
            Scanner scan = new Scanner(System.in);

            //creates an array list of unsorted ints (these are the sound files)
            ArrayList<Integer> unsortedInts = new ArrayList<>();

            //scans through the ints and adds them to the unsorted array list until it is at the end of the file
            while (scan.hasNextInt()) {
                unsortedInts.add(scan.nextInt());
            }

            //runs the program on the unsorted list of ints
            BestFitDecreasing fit = new BestFitDecreasing();
            fit.createDisks(unsortedInts);
        }

    }


}
