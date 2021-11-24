package emulator_utils;

public class Status {

    short opcode;
    boolean S, I, E, Z;
    
    public Status() {
    	
    }

    public Status(short op, boolean start, boolean indirect_Bit, boolean e_flag, boolean zero_flag) {
        opcode=op;
        S = start;
        I = indirect_Bit;
        E = e_flag;
        Z = zero_flag; 
    }
    
    //use for log
    public String toString() {
        //generate a binary string of the control word
        String st = "Control Word:" + " Opcode=" + Integer.toString(opcode) + " S=" +(S?"1":"0") +
            " I=" +(I?"1":"0") + " E=" +(E?"1":"0") + " Z=" +(Z?"1":"0");

        return st;
    }
    

}