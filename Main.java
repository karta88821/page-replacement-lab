package HW1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main
 */
public class Main {

    private static final List<Integer> randomRefStrings = ReferenceString.random();
    private static final List<Integer> localityRefStrings = ReferenceString.locality();
    private static final List<Integer> myRefStrings = ReferenceString.myRef();
    private static final int[] sizeOfPhysicalMem = { 30, 60, 90, 120, 150 };

    public static void main(String[] args) {
        demo();
    }

    static void demo() {
        int[] sizeOfPhysicalMem = { 30, 60, 90, 120, 150 };

        List<Integer> refStrings = new ArrayList<>();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("How to pick?");
            System.out.println("(1)Random pick (2)Locality pick (3)My pick (4)Exit");
            int choice = sc.nextInt();

            switch (choice) {
            case 1:
                refStrings = new ArrayList<>(randomRefStrings);
                break;
            case 2:
                refStrings = new ArrayList<>(localityRefStrings);
                break;
            case 3:
                refStrings = new ArrayList<>(myRefStrings);
                break;
            default:
                break;
            }

            for (int i = 0; i < sizeOfPhysicalMem.length; i++) {
                int frameSize = sizeOfPhysicalMem[i];
                ReferenceMethod methods = new ReferenceMethod(refStrings, frameSize);

                Page[] mem = new Page[frameSize];
                Result FIFO = methods.FIFO(mem);
                mem = new Page[frameSize];
                Result optimal = methods.optimal(mem);
                mem = new Page[frameSize];
                Result ARB = methods.ARB(mem);
                mem = new Page[frameSize];
                Result myAlgo = methods.MyReplacementAlgo(mem);

                System.out.println(
                        "==========================================================================================");
                System.out.println("Frame size = " + frameSize);
                System.out.format("         %12s %12s %12s\n", "Page faults", "Interrupts", "Disk writes");
                System.out.format("FIFO     %12d %12d %12d\n", FIFO.getNumberOfPageFaults(),
                        FIFO.getNumberOfInterrupts(), FIFO.getNumberOfDiskWrites());
                System.out.format("Optimal  %12d %12d %12d\n", optimal.getNumberOfPageFaults(),
                        optimal.getNumberOfInterrupts(), optimal.getNumberOfDiskWrites());
                System.out.format("ARB      %12d %12d %12d\n", ARB.getNumberOfPageFaults(), ARB.getNumberOfInterrupts(),
                        ARB.getNumberOfDiskWrites());
                System.out.format("MyAlgo   %12d %12d %12d\n", myAlgo.getNumberOfPageFaults(),
                        myAlgo.getNumberOfInterrupts(), myAlgo.getNumberOfDiskWrites());
            }
        }
    }

    static void writeAll() {
        Result[] FIFO_random = new Result[5];
        Result[] Optimal_random = new Result[5];
        Result[] ARB_random = new Result[5];
        Result[] myalgo_random = new Result[5];

        Result[] FIFO_locality = new Result[5];
        Result[] Optimal_locality = new Result[5];
        Result[] ARB_locality = new Result[5];
        Result[] myalgo_locality = new Result[5];
        
        Result[] FIFO_myref = new Result[5];
        Result[] Optimal_myref = new Result[5];
        Result[] ARB_myref = new Result[5];
        Result[] myalgo_myref = new Result[5];

        for (int i = 0; i < sizeOfPhysicalMem.length; i++) {
            int frameSize = sizeOfPhysicalMem[i];
            ReferenceMethod random = new ReferenceMethod(randomRefStrings, frameSize);
            ReferenceMethod locality = new ReferenceMethod(localityRefStrings, frameSize);
            ReferenceMethod myRef = new ReferenceMethod(myRefStrings, frameSize);

            System.out.println("Frame size: " + frameSize + " is starting.");

            Page[] mem = new Page[frameSize];
            Result FIFO_random_result = random.FIFO(mem);
            FIFO_random[i] = FIFO_random_result;
            mem = new Page[frameSize];
            Result FIFO_locality_result = locality.FIFO(mem);
            FIFO_locality[i] = FIFO_locality_result;
            mem = new Page[frameSize];
            Result FIFO_myref_result = myRef.FIFO(mem);
            FIFO_myref[i] = FIFO_myref_result;

            mem = new Page[frameSize];
            Result optimal_random_result = random.optimal(mem);
            Optimal_random[i] = optimal_random_result;
            mem = new Page[frameSize];
            Result optimal_locality_result = locality.optimal(mem);
            Optimal_locality[i] = optimal_locality_result;
            mem = new Page[frameSize];
            Result optimal_myref_result = myRef.optimal(mem);
            Optimal_myref[i] = optimal_myref_result;

            mem = new Page[frameSize];
            Result ARB_random_result = random.ARB(mem);
            ARB_random[i] = ARB_random_result;
            mem = new Page[frameSize];
            Result ARB_locality_result = locality.ARB(mem);
            ARB_locality[i] = ARB_locality_result;
            mem = new Page[frameSize];
            Result ARB_myref_result = myRef.ARB(mem);
            ARB_myref[i] = ARB_myref_result;

            mem = new Page[frameSize];
            Result my_random_result = random.MyReplacementAlgo(mem);
            myalgo_random[i] = my_random_result;
            mem = new Page[frameSize];
            Result my_locality_result = locality.MyReplacementAlgo(mem);
            myalgo_locality[i] = my_locality_result;
            mem = new Page[frameSize];
            Result my_myref_result = myRef.MyReplacementAlgo(mem);
            myalgo_myref[i] = my_myref_result;
        }

        File csvOutputFile = new File("pages.csv");
        String[] titles = { "FIFO_random,", "Optimal_random,", "ARB_random,", "myalgo_random,",
                            "FIFO_locality,", "Optimal_locality,", "ARB_locality,", "myalgo_locality,",
                            "FIFO_myref,", "Optimal_myref,", "ARB_myref,", "myalgo_myref," };

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {

            StringBuffer sb = new StringBuffer();

            // Page faults
            for (int i = 0; i < titles.length; i++) {
                String title = titles[i];
                switch (i) {
                case 0:
                    sb.append(title + FIFO_random[0].getNumberOfPageFaults() + ","
                            + FIFO_random[1].getNumberOfPageFaults() + "," 
                            + FIFO_random[2].getNumberOfPageFaults()+ "," 
                            + FIFO_random[3].getNumberOfPageFaults() + ","
                            + FIFO_random[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 1:
                    sb.append(title + Optimal_random[0].getNumberOfPageFaults() + ","
                            + Optimal_random[1].getNumberOfPageFaults() + ","
                            + Optimal_random[2].getNumberOfPageFaults() + "," 
                            + Optimal_random[3].getNumberOfPageFaults() + ","
                            + Optimal_random[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 2:
                    sb.append(title + ARB_random[0].getNumberOfPageFaults() + ","
                            + ARB_random[1].getNumberOfPageFaults() + "," 
                            + ARB_random[2].getNumberOfPageFaults() + ","
                            + ARB_random[3].getNumberOfPageFaults() + "," 
                            + ARB_random[4].getNumberOfPageFaults()
                            + "\r\n");
                    break;
                case 3:
                    sb.append(title + myalgo_random[0].getNumberOfPageFaults() + ","
                            + myalgo_random[1].getNumberOfPageFaults() + ","
                            + myalgo_random[2].getNumberOfPageFaults() + ","
                            + myalgo_random[3].getNumberOfPageFaults() + ","
                            + myalgo_random[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 4:
                    sb.append(title + FIFO_locality[0].getNumberOfPageFaults() + ","
                            + FIFO_locality[1].getNumberOfPageFaults() + ","
                            + FIFO_locality[2].getNumberOfPageFaults() + ","
                            + FIFO_locality[3].getNumberOfPageFaults() + ","
                            + FIFO_locality[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 5:
                    sb.append(title + Optimal_locality[0].getNumberOfPageFaults() + ","
                            + Optimal_locality[1].getNumberOfPageFaults() + "," 
                            + Optimal_locality[2].getNumberOfPageFaults() + "," 
                            + Optimal_locality[3].getNumberOfPageFaults() + ","
                            + Optimal_locality[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 6:
                    sb.append(title + ARB_locality[0].getNumberOfPageFaults() + ","
                            + ARB_locality[1].getNumberOfPageFaults() + "," 
                            + ARB_locality[2].getNumberOfPageFaults() + ","
                            + ARB_locality[3].getNumberOfPageFaults() + "," 
                            + ARB_locality[4].getNumberOfPageFaults()
                            + "\r\n");
                    break;
                case 7:
                    sb.append(title + myalgo_locality[0].getNumberOfPageFaults() + ","
                            + myalgo_locality[1].getNumberOfPageFaults() + "," 
                            + myalgo_locality[2].getNumberOfPageFaults() + "," 
                            + myalgo_locality[3].getNumberOfPageFaults() + ","
                            + myalgo_locality[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 8:
                    sb.append(title + FIFO_myref[0].getNumberOfPageFaults() + "," 
                            + FIFO_myref[1].getNumberOfPageFaults() + "," 
                            + FIFO_myref[2].getNumberOfPageFaults() + "," 
                            + FIFO_myref[3].getNumberOfPageFaults() + "," 
                            + FIFO_myref[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 9:
                    sb.append(title + Optimal_myref[0].getNumberOfPageFaults() + ","
                            + Optimal_myref[1].getNumberOfPageFaults() + "," 
                            + Optimal_myref[2].getNumberOfPageFaults() + "," 
                            + Optimal_myref[3].getNumberOfPageFaults() + ","
                            + Optimal_myref[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 10:
                    sb.append(title + ARB_myref[0].getNumberOfPageFaults() + ","
                            + ARB_myref[1].getNumberOfPageFaults() + ","
                            + ARB_myref[2].getNumberOfPageFaults() + ","
                            + ARB_myref[3].getNumberOfPageFaults() + ","
                            + ARB_myref[4].getNumberOfPageFaults() + "\r\n");
                    break;
                case 11:
                    sb.append(title + myalgo_myref[0].getNumberOfPageFaults() + ","
                            + myalgo_myref[1].getNumberOfPageFaults() + "," 
                            + myalgo_myref[2].getNumberOfPageFaults() + "," 
                            + myalgo_myref[3].getNumberOfPageFaults() + ","
                            + myalgo_myref[4].getNumberOfPageFaults() + "\r\n");
                    break;
                }
            }

            // Interrupts
            for (int i = 0; i < titles.length; i++) {
                String title = titles[i];
                switch (i) {
                case 0:
                    sb.append(title + FIFO_random[0].getNumberOfInterrupts() + ","
                            + FIFO_random[1].getNumberOfInterrupts() + "," 
                            + FIFO_random[2].getNumberOfInterrupts()+ "," 
                            + FIFO_random[3].getNumberOfInterrupts() + ","
                            + FIFO_random[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 1:
                    sb.append(title + Optimal_random[0].getNumberOfInterrupts() + ","
                            + Optimal_random[1].getNumberOfInterrupts() + ","
                            + Optimal_random[2].getNumberOfInterrupts() + "," 
                            + Optimal_random[3].getNumberOfInterrupts() + ","
                            + Optimal_random[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 2:
                    sb.append(title + ARB_random[0].getNumberOfInterrupts() + ","
                            + ARB_random[1].getNumberOfInterrupts() + "," 
                            + ARB_random[2].getNumberOfInterrupts() + ","
                            + ARB_random[3].getNumberOfInterrupts() + "," 
                            + ARB_random[4].getNumberOfInterrupts()
                            + "\r\n");
                    break;
                case 3:
                    sb.append(title + myalgo_random[0].getNumberOfInterrupts() + ","
                            + myalgo_random[1].getNumberOfInterrupts() + ","
                            + myalgo_random[2].getNumberOfInterrupts() + ","
                            + myalgo_random[3].getNumberOfInterrupts() + ","
                            + myalgo_random[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 4:
                    sb.append(title + FIFO_locality[0].getNumberOfInterrupts() + ","
                            + FIFO_locality[1].getNumberOfInterrupts() + ","
                            + FIFO_locality[2].getNumberOfInterrupts() + ","
                            + FIFO_locality[3].getNumberOfInterrupts() + ","
                            + FIFO_locality[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 5:
                    sb.append(title + Optimal_locality[0].getNumberOfInterrupts() + ","
                            + Optimal_locality[1].getNumberOfInterrupts() + "," 
                            + Optimal_locality[2].getNumberOfInterrupts() + "," 
                            + Optimal_locality[3].getNumberOfInterrupts() + ","
                            + Optimal_locality[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 6:
                    sb.append(title + ARB_locality[0].getNumberOfInterrupts() + ","
                            + ARB_locality[1].getNumberOfInterrupts() + "," 
                            + ARB_locality[2].getNumberOfInterrupts() + ","
                            + ARB_locality[3].getNumberOfInterrupts() + "," 
                            + ARB_locality[4].getNumberOfInterrupts()
                            + "\r\n");
                    break;
                case 7:
                    sb.append(title + myalgo_locality[0].getNumberOfInterrupts() + ","
                            + myalgo_locality[1].getNumberOfInterrupts() + "," 
                            + myalgo_locality[2].getNumberOfInterrupts() + "," 
                            + myalgo_locality[3].getNumberOfInterrupts() + ","
                            + myalgo_locality[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 8:
                    sb.append(title + FIFO_myref[0].getNumberOfInterrupts() + "," 
                            + FIFO_myref[1].getNumberOfInterrupts() + "," 
                            + FIFO_myref[2].getNumberOfInterrupts() + "," 
                            + FIFO_myref[3].getNumberOfInterrupts() + "," 
                            + FIFO_myref[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 9:
                    sb.append(title + Optimal_myref[0].getNumberOfInterrupts() + ","
                            + Optimal_myref[1].getNumberOfInterrupts() + "," 
                            + Optimal_myref[2].getNumberOfInterrupts() + "," 
                            + Optimal_myref[3].getNumberOfInterrupts() + ","
                            + Optimal_myref[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 10:
                    sb.append(title + ARB_myref[0].getNumberOfInterrupts() + ","
                            + ARB_myref[1].getNumberOfInterrupts() + ","
                            + ARB_myref[2].getNumberOfInterrupts() + ","
                            + ARB_myref[3].getNumberOfInterrupts() + ","
                            + ARB_myref[4].getNumberOfInterrupts() + "\r\n");
                    break;
                case 11:
                    sb.append(title + myalgo_myref[0].getNumberOfInterrupts() + ","
                            + myalgo_myref[1].getNumberOfInterrupts() + "," 
                            + myalgo_myref[2].getNumberOfInterrupts() + "," 
                            + myalgo_myref[3].getNumberOfInterrupts() + ","
                            + myalgo_myref[4].getNumberOfInterrupts() + "\r\n");
                    break;
                }
            }

            // Disk writes
            for (int i = 0; i < titles.length; i++) {
                String title = titles[i];
                switch (i) {
                case 0:
                    sb.append(title + FIFO_random[0].getNumberOfDiskWrites() + ","
                            + FIFO_random[1].getNumberOfDiskWrites() + "," 
                            + FIFO_random[2].getNumberOfDiskWrites()+ "," 
                            + FIFO_random[3].getNumberOfDiskWrites() + ","
                            + FIFO_random[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 1:
                    sb.append(title + Optimal_random[0].getNumberOfDiskWrites() + ","
                            + Optimal_random[1].getNumberOfDiskWrites() + ","
                            + Optimal_random[2].getNumberOfDiskWrites() + "," 
                            + Optimal_random[3].getNumberOfDiskWrites() + ","
                            + Optimal_random[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 2:
                    sb.append(title + ARB_random[0].getNumberOfDiskWrites() + ","
                            + ARB_random[1].getNumberOfDiskWrites() + "," 
                            + ARB_random[2].getNumberOfDiskWrites() + ","
                            + ARB_random[3].getNumberOfDiskWrites() + "," 
                            + ARB_random[4].getNumberOfDiskWrites()
                            + "\r\n");
                    break;
                case 3:
                    sb.append(title + myalgo_random[0].getNumberOfDiskWrites() + ","
                            + myalgo_random[1].getNumberOfDiskWrites() + ","
                            + myalgo_random[2].getNumberOfDiskWrites() + ","
                            + myalgo_random[3].getNumberOfDiskWrites() + ","
                            + myalgo_random[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 4:
                    sb.append(title + FIFO_locality[0].getNumberOfDiskWrites() + ","
                            + FIFO_locality[1].getNumberOfDiskWrites() + ","
                            + FIFO_locality[2].getNumberOfDiskWrites() + ","
                            + FIFO_locality[3].getNumberOfDiskWrites() + ","
                            + FIFO_locality[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 5:
                    sb.append(title + Optimal_locality[0].getNumberOfDiskWrites() + ","
                            + Optimal_locality[1].getNumberOfDiskWrites() + "," 
                            + Optimal_locality[2].getNumberOfDiskWrites() + "," 
                            + Optimal_locality[3].getNumberOfDiskWrites() + ","
                            + Optimal_locality[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 6:
                    sb.append(title + ARB_locality[0].getNumberOfDiskWrites() + ","
                            + ARB_locality[1].getNumberOfDiskWrites() + "," 
                            + ARB_locality[2].getNumberOfDiskWrites() + ","
                            + ARB_locality[3].getNumberOfDiskWrites() + "," 
                            + ARB_locality[4].getNumberOfDiskWrites()
                            + "\r\n");
                    break;
                case 7:
                    sb.append(title + myalgo_locality[0].getNumberOfDiskWrites() + ","
                            + myalgo_locality[1].getNumberOfDiskWrites() + "," 
                            + myalgo_locality[2].getNumberOfDiskWrites() + "," 
                            + myalgo_locality[3].getNumberOfDiskWrites() + ","
                            + myalgo_locality[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 8:
                    sb.append(title + FIFO_myref[0].getNumberOfDiskWrites() + "," 
                            + FIFO_myref[1].getNumberOfDiskWrites() + "," 
                            + FIFO_myref[2].getNumberOfDiskWrites() + "," 
                            + FIFO_myref[3].getNumberOfDiskWrites() + "," 
                            + FIFO_myref[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 9:
                    sb.append(title + Optimal_myref[0].getNumberOfDiskWrites() + ","
                            + Optimal_myref[1].getNumberOfDiskWrites() + "," 
                            + Optimal_myref[2].getNumberOfDiskWrites() + "," 
                            + Optimal_myref[3].getNumberOfDiskWrites() + ","
                            + Optimal_myref[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 10:
                    sb.append(title + ARB_myref[0].getNumberOfDiskWrites() + ","
                            + ARB_myref[1].getNumberOfDiskWrites() + ","
                            + ARB_myref[2].getNumberOfDiskWrites() + ","
                            + ARB_myref[3].getNumberOfDiskWrites() + ","
                            + ARB_myref[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                case 11:
                    sb.append(title + myalgo_myref[0].getNumberOfDiskWrites() + ","
                            + myalgo_myref[1].getNumberOfDiskWrites() + "," 
                            + myalgo_myref[2].getNumberOfDiskWrites() + "," 
                            + myalgo_myref[3].getNumberOfDiskWrites() + ","
                            + myalgo_myref[4].getNumberOfDiskWrites() + "\r\n");
                    break;
                }
            }

            pw.write(sb.toString());

            System.out.println("Write to cvs file successfully.");

            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}