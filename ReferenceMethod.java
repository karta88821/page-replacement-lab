package HW1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ReferenceMethod
 */
public class ReferenceMethod {

    private List<Integer> referenceStrings;
    private int numberOfFrames;

    public ReferenceMethod(List<Integer> referenceStrings, int numberOfFrames) {
        this.referenceStrings = referenceStrings;
        this.numberOfFrames = numberOfFrames;
    }

    /**
     * FIFO Replacement Algorithm
     */
    public Result FIFO(Page[] mem) {
        int memIndex = 0;
        boolean isMemFull = false;
        Set<Integer> set = new HashSet<Integer>();
        Result result = new Result();

        for (int i = 0; i < referenceStrings.size(); i++) {
            Integer currentRefString = referenceStrings.get(i);

            if (!set.contains(currentRefString)) { // 如果這memory目前不包含這個reference string

                result.addOnePageFault(); // 找不到要的page，有page fault
                result.addOneInterrupt(); // 要從disk搬一個frame，通知OS幫忙做I/O

                // 若memory滿了，則從全部的frame中，找出最久沒被用到的frame做替換
                // 若memory還有空位，則依序找空位填入
                if (isMemFull) {

                    int victimIndex = findMinTimestamp(mem);
                    Page page = mem[victimIndex];

                    // 有修改過的page，在被replace前要先寫回disk
                    if (page.getDirtyBit() == 1) {
                        result.addOneDiskWrite();
                    }

                    // old reference string從set中移除，new reference string加入set中
                    set.remove(page.getReferenceString());
                    set.add(currentRefString);

                    page.setReferenceString(currentRefString);
                    page.updateTimestamp();

                    // 從0~1隨機產生一個數，若小於預設的disk write probability，則將dirty bit設為1
                    page = setDirtyWithRandomProb(page);

                } else {
                    // 從disk搬一個page上來memory
                    Page newPage = new Page();
                    newPage.setReferenceString(currentRefString);
                    set.add(currentRefString);

                    mem[memIndex] = newPage;

                    memIndex++;

                    // 若每個frame都有東西了，代表memory滿了
                    isMemFull = isMemoryFull(memIndex, numberOfFrames);
                }
            }
        }
        return result;
    }

    private Page setDirtyWithRandomProb(Page page) {
        Page temp = page;
        if (Math.random() < Constant.DISK_WRITE_PROBABILITY) {
            temp.setDirtyBit(1);
        } else {
            temp.setDirtyBit(0);
        }
        return temp;
    }

    /**
     * Optimal Replacement Algorithm
     */
    public Result optimal(Page[] mem) {
        int memIndex = 0;
        boolean isMemFull = false;
        Set<Integer> set = new HashSet<Integer>();
        Result result = new Result();

        for (int i = 0; i < referenceStrings.size(); i++) {
            Integer currentRefString = referenceStrings.get(i);

            if (!set.contains(currentRefString)) { // 如果這memory目前不包含這個reference string

                result.addOnePageFault(); // 找不到要的page，有page fault
                result.addOneInterrupt(); // 要從disk搬一個frame，通知OS幫忙做I/O

                if (isMemFull) {

                    // 查看mem中哪個page未來最晚會被refer
                    int victimIndex = predict(mem, i + 1);

                    Page page = mem[victimIndex];

                    if (page.getDirtyBit() == 1) {
                        result.addOneDiskWrite();
                    }

                    set.remove(page.getReferenceString());
                    page.setReferenceString(currentRefString);
                    page.updateTimestamp();
                    set.add(currentRefString);

                    // 從0~1隨機產生一個數，若小於預設的disk write probability，則將dirty bit設為1
                    page = setDirtyWithRandomProb(page);

                } else {
                    // 從disk搬一個page上來memory
                    Page newPage = new Page();
                    newPage.setReferenceString(currentRefString);
                    set.add(currentRefString);

                    mem[memIndex] = newPage;

                    memIndex++;

                    // 若每個frame都有東西了，代表memory滿了
                    isMemFull = isMemoryFull(memIndex, numberOfFrames);
                }
            }
        }
        return result;
    }

    private int predict(Page[] mem, int index) {
        int res = -1, farthest = index;

        //                                               目前reference string的index+1
        //                                                             |
        // 從mem[0] ~ mem[numberOfFrames - 1]一個個和referenceStrings[index] ~ referenceStrings[N - 1]比較
        for (int i = 0; i < numberOfFrames; i++) {
            int j;
            for (j = index; j < referenceStrings.size(); j++) {
                if (mem[i].getReferenceString() == referenceStrings.get(j)) {
                    if (j > farthest) {
                        farthest = j;
                        res = i;
                    }
                    break;
                }
            }

            if (j == numberOfFrames) {
                return i;
            }
        }
        return (res == -1) ? 0 : res;
    }

    /**
     * ARB Replacement Algorithm
     */
    public Result ARB(Page[] mem) {
        boolean isMemFull = false;
        int memIndex = 0;
        int clear = 1000;
        int[] arb = new int[numberOfFrames]; // register for each page
        Set<Integer> set = new HashSet<Integer>();
        Result result = new Result();

        for (int i = 0; i < referenceStrings.size(); i++) {
            Integer currentRefString = referenceStrings.get(i);

            if (!set.contains(currentRefString)) { // memory裡找不到這個refernce string

                result.addOnePageFault(); // 找不到要的page，有page fault
                result.addOneInterrupt(); // 要從disk搬一個frame，通知OS幫忙做I/O

                if (isMemFull) {
                    List<Integer> indexsOfMinValue = getIndexsOfMinValue(arb); // 找出8bits數值最小的那個

                    int victimIndex = -1;

                    if (indexsOfMinValue.size() == 1) {
                        victimIndex = indexsOfMinValue.get(0);
                    } else {
                        long timestamp = System.currentTimeMillis() + 10000;
                        for (Integer index : indexsOfMinValue) {
                            if (mem[index].getTimestamp() < timestamp) {
                                timestamp = mem[index].getTimestamp();
                                victimIndex = index;
                            }
                        }
                    }

                    Page page = mem[victimIndex];

                    // 有修改過的page，在被replace前要先寫回disk
                    if (page.getDirtyBit() == 1) {
                        result.addOneDiskWrite();
                    }

                    // old reference string從set中移除，new reference string加入set中
                    set.remove(page.getReferenceString());
                    set.add(currentRefString);

                    page.setReferenceString(currentRefString);
                    page.updateTimestamp();

                    // 從0~1隨機產生一個數，若小於預設的disk write probability，則將dirty bit設為1
                    page = setDirtyWithRandomProb(page);
                } else {
                    // 從disk搬一個page上來memory
                    Page newPage = new Page();
                    newPage.setReferenceString(currentRefString);
                    newPage.setRefBit(1);
                    set.add(currentRefString);

                    mem[memIndex] = newPage;

                    memIndex++;

                    isMemFull = isMemoryFull(memIndex, numberOfFrames);
                }
            } else {
                for(int j = 0; j < numberOfFrames; j++) {
                    Page page = mem[j];
                    if (page == null) { break; }
                    if (page.getReferenceString() == currentRefString) {
                        page.setRefBit(1);
                        break;
                    }
                }
            }

            clear--;

            // 每1000次memory references，就進行一次
            if (clear == 0) {
                clear = 1000;

                // 所有page的register皆向右shift 1 bit
                arb = shiftARB(arb);

                // copy reference bit到register的最高為元
                for (int j = 0; j < numberOfFrames; j++) {
                    Page page = mem[j];
                    if (page == null) { break; }
                    int refBit = page.getRefBit();
                    arb[j] = arb[j] | (refBit << 7);
                }

                // 所有page的reference bit清為0
                for (int j = 0; j < numberOfFrames; j++) {
                    Page page = mem[j];
                    if (page == null) { break; }
                    page.setRefBit(0);
                }

                // 通知OS將reference bit清掉，產生interrupt
                result.addOneInterrupt();
            }
        }
        return result;
    }

    /**
     * My Replacement Algorithm
     */
    public Result MyReplacementAlgo(Page[] mem) {
        int memIndex = 0;
        boolean isMemFull = false;
        Set<Integer> set = new HashSet<Integer>();
        Result result = new Result();

        for (int i = 0; i < referenceStrings.size(); i++) {
            Integer currentRefString = referenceStrings.get(i);

            if (!set.contains(currentRefString)) { // 如果這memory目前不包含這個reference string

                result.addOnePageFault(); // 找不到要的page，有page fault
                result.addOneInterrupt(); // 要從disk搬一個frame，通知OS幫忙做I/O

                // 若memory滿了，則從全部的frame中，找出最久沒被用到的frame做替換
                // 若memory還有空位，則依序找空位填入
                if (isMemFull) {

                    int victimIndex = -1;

                    // 從mem找出dirty bit和reference bit皆為0的page
                    for (int j = 0; j < numberOfFrames; j++) {
                        Page page = mem[j];
                        if (page.getDirtyBit() == 0) {
                            victimIndex = j;
                            break;
                        }
                    }

                    // 若找不到，則用FIFO找出victim page
                    if (victimIndex == -1) {
                        victimIndex = findMinTimestamp(mem);
                    }

                    Page page = mem[victimIndex];

                    // 有修改過的page，在被replace前要先寫回disk
                    if (page.getDirtyBit() == 1) {
                        result.addOneDiskWrite();
                    }

                    // old reference string從set中移除，new reference string加入set中
                    set.remove(page.getReferenceString());
                    set.add(currentRefString);

                    page.setReferenceString(currentRefString);
                    page.updateTimestamp();

                    // 從0~1隨機產生一個數，若小於預設的disk write probability，則將dirty bit設為1
                    page = setDirtyWithRandomProb(page);

                } else {
                    // 從disk搬一個page上來memory
                    Page newPage = new Page();
                    newPage.setReferenceString(currentRefString);
                    set.add(currentRefString);

                    mem[memIndex] = newPage;

                    memIndex++;

                    // 若每個frame都有東西了，代表memory滿了
                    isMemFull = isMemoryFull(memIndex, numberOfFrames);
                }
            } else {
                for(int j = 0; j < numberOfFrames; j++) {
                    Page page = mem[j];
                    if (page == null) { break; }
                    if (page.getReferenceString() == currentRefString) {
                        page.setRefBit(1);
                        break;
                    }
                }
            }
        }
        return result;
    }

    private List<Integer> getIndexsOfMinValue(int[] array) {
        List<Integer> res = new ArrayList<>();

        int min = 0b100000000;

        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                res.clear();
                res.add(i);
            } else if (array[i] == min) {
                res.add(i);
            }
        }
        return res;
    }

    private int[] shiftARB(int[] array) {
        int[] shiftArray;
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i] >>> 1;
        }
        shiftArray = array;
        return shiftArray;
    }

    private boolean isMemoryFull(int memIndex, int numberOfFrames) {
        return memIndex == numberOfFrames;
    }

    private int findMinTimestamp(Page[] mem) {
        int index = -1;
        long minTimestamp = System.currentTimeMillis() + 100000;
        for (int i = 0; i < mem.length; i++) {
            long timestamp = mem[i].getTimestamp();
            if (timestamp < minTimestamp) {
                index = i;
                minTimestamp = timestamp;
            }
        }
        return index;
    }
}