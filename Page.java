package HW1;

import java.sql.Timestamp;

public class Page {
    private int referenceString;
    private int refBit = 0;
    private int dirtyBit = 0;
    private long timestamp;

    public Page() {
        this.timestamp = System.currentTimeMillis();
    }

    public int getReferenceString() {
        return referenceString;
    }

    public int getRefBit() {
        return refBit;
    }

    public int getDirtyBit() {
        return dirtyBit;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setReferenceString(int referenceString) {
        this.referenceString = referenceString;
    }

    public void setRefBit(int refBit) {
        this.refBit = refBit;
    }

    public void setDirtyBit(int dirtyBit) {
        this.dirtyBit = dirtyBit;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }
    
}
