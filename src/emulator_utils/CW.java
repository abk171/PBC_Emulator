package emulator_utils;

public class CW {
    //registers
    boolean MEM_read, MEM_write;
    boolean AR_ld, AR_inc, AR_clr;
    boolean PC_ld, PC_inc, PC_clr;
    boolean DR_ld, DR_inc, DR_clr;
    boolean AC_ld, AC_inc, AC_clr;
    boolean IR_ld, IR_inc, IR_clr;
    boolean SC_ld, SC_inc, SC_clr;

    //flags
    boolean S_ld, S_set, S_clr;  
    boolean E_ld, E_set, E_clr;

    //bus
    boolean s0, s1, s2;

    //ALU
    boolean AND, ADD, TRANSFER, COMPLEMENT, SHR, SHL;

    //use for log
    public String toString() {
        //generate a binary string of the control word
        String signals = (MEM_read?"MEM_read ":"") + (MEM_write?"MEM_write ":"") + 
            (AR_ld?"AR_ld ":"") +  (AR_inc?"AR_inc ":"") +  (AR_clr?"AR_clr ":"") + 
            (PC_ld?"PC_ld ":"") +  (PC_inc?"PC_inc ":"") +  (PC_clr?"PC_clr ":"") + 
            (DR_ld?"DR_ld ":"") +  (DR_inc?"DR_inc ":"") +  (DR_clr?"DR_clr ":"") + 
            (AC_ld?"AC_ld ":"") +  (AC_inc?"AC_inc ":"") +  (AC_clr?"AC_clr ":"") + 
            (IR_ld?"IR_ld ":"") +  (IR_inc?"IR_inc ":"") +  (IR_clr?"IR_clr ":"") + 
            (SC_ld?"SC_ld ":"") +  (SC_inc?"SC_inc ":"") +  (SC_clr?"SC_clr ":"") + 
            (S_ld?"S_ld ":"")   +  (S_set?"S_set ":"")    +  (S_clr?"S_clr ":"")    + 
            (E_ld?"E_ld ":"")   +  (E_set?"E_set ":"")    +  (E_clr?"E_clr ":"")    + 
            "Bus: " + (s2?"1":"0") +  (s1?"1":"0") +  (s0?"1 ":"0 ") + 
            "ALU_" + (AND?"and ":"") + (ADD?"add ":"") + (AND?"transfer ":"") +
            (COMPLEMENT?"complement ":"") + (SHR?"shr ":"") + (SHL?"shl ":"");

        return signals;
    }

}