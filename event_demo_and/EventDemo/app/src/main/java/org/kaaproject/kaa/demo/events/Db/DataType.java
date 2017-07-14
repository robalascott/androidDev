package org.kaaproject.kaa.demo.events.Db;

/**
 * Created by robscott on 2017-06-29.
 */

public class DataType {
    private int Devices;
    private int Diff;
    public DataType(int devices,int diff){
        this.Devices = devices;
        this.Diff=diff;
    }

    public int getDevices() {
        return Devices;
    }

    public int getDiff() {
        return Diff;
    }

    public void setDiff(int diff) {
        Diff = diff;
    }
}
