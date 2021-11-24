package emulator_utils;

public class Memory {

    private short[] data;

    public Memory(int size) {
        data = new short[size];
    }

    public short read(int addr) {
        return data[addr];
    }
    //this is c style
    public void write(int addr, short val) {
        data[addr] = val;
    }

    public void fill(short[] in, int start) {
        int i;
        for (i = start; i < in.length; i++)
            data[i] = in[i];
        for (i=in.length; i < data.length; i++)
            data[i] = 0;
    }
    
    //fill brings the first program from memory and put zeroes at the end of the memroy
    public void clear() {
        for (int i = 0; i < data.length; i++)
            data[i] = 0;
    }

}