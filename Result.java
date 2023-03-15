package HW1;

public class Result {
    private int numberOfPageFaults;
    private int numberOfInterrupts;
    private int numberOfDiskWrites;

    public int getNumberOfPageFaults() {
        return numberOfPageFaults;
    }

    public void addOnePageFault() {
        this.numberOfPageFaults++;
    }

    public int getNumberOfInterrupts() {
        return numberOfInterrupts;
    }

    public void addOneInterrupt() {
        this.numberOfInterrupts++;
    }

    public int getNumberOfDiskWrites() {
        return numberOfDiskWrites;
    }

    public void addOneDiskWrite() {
        this.numberOfDiskWrites++;
    }

}
