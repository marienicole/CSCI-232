/**
 * CSCI 232 Lab 2, Bin Packing.
 * Created by Marie Morin.
 * This program is supposed to take files and place them on disks of size 1 GB.
 * We are going to attempt to put the files on disks, using the smallest amount of disks possible.
 * <p>
 * This class is the disk class, which will hold the files.
 * I created a file class as well, and an arrayList to hold the files on the disk.
 */

import java.util.ArrayList;

public class Disk implements Comparable<Disk> {

    private class File {
        private int size;

        public File(int size) // File constructor
        {
            this.size = size;
        }

        public int getSize() // returns size of File
        {
            return size;
        }
    }

    ArrayList<File> al = new ArrayList<>();
    public int fullSize = 1000000; // the total data the Disk can hold
    public int size = 0; // initial size

    public Disk(int initial) // disk constructor
    {
        size = initial;
        al.add(new File(initial));
    }

    public int room() // returns how much space is left on the disk
    {
        return fullSize - size;
    }

    public void addFile(int fileSize) // adds file to the disk
    {
        size += fileSize;
        al.add(new File(fileSize)); // adds the file to the List for the disk

    }
    
    public int returnFile(int index) // returns the sound file from the disk
    {
        return al.get(index).size;
    }

    public int compareTo(Disk d) {
        if (d.room() > this.room()) // if comparator is greater than this one return one
        {
            return 1;
        } else if (d.room() < this.room()) // if room on comparator is less than this one return 0
        {
            return 0;
        } else return 1; // if they're equal, return 1 to add to first because it doesn't really matter
    }
}