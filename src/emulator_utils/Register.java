package emulator_utils;

public class Register {

    //Use short because we assume upto 16-bit registers
    private short value;
    private short mask;
 
    public Register(int bits) {
        this.mask = (short) ((1 << bits) - 1);     //helpful if bits is < 16
        this.value = 0;
    }

    public void load(short in) {
        value = (short) (in & mask);
    } 

    public void increment() {
        value = (short) ((value + 1) & mask);
    }

    public void clear() {
        value = 0;
    }

    public short getValue() {
        return value;
    }

    public void update(short in, boolean L, boolean I, boolean C) {
        //count number of active signals
        int count = (L?1:0) + (I?1:0) + (C?1:0);
        //only one of the signals should be one
        if (count <=1) {
            if (L) load(in);
            else if (I) increment();
            else if (C) clear();
        }
    }

}