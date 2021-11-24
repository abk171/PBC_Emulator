package emulator_utils;

public class DataUnit {
	Memory m;
	Register AR;
	Register PC;
	Register DR;
	Register E;
	Register AC;
	Register IR;
	Register S;
	Register SC;
	Register Z;
	
	
	
	public DataUnit(Memory mr) {
		m = mr;
		AR = new Register(11);
		//add resets here 
		PC = new Register(12);
		DR = new Register(16);
		E = new Register(1);
		AC = new Register(16);
		IR = new Register(16);
		//initialize to NOP
		S = new Register(1);
		SC = new Register(3);
		AR.clear();
		PC.clear();
		DR.clear();
		E.clear();
		AC.clear();
		IR.clear();
//		S.clear();
		SC.clear();
		
		//Initializing IR to NOP
		IR.update((short) 0XF000, true, false, false); // 1111 1000 0000 0000 >> 12 = 1000 0000 0000 0111
		S.increment();
		//initialize the IR to a NOP if possible?
		//we need to have a clock in the emulator
		
		//What is S
		// s is start and is only switched off if halt
		//How to load wisely as an emulation?
		//I still have to implement the controlUnit
		//bugs may happen in the cotrolUnit
		
	}
	//we may use this as a flag
	
	//The data unit provides the following methods:
	public Status getStatus() {
		//returns the values of the status bits
		Status status = new Status();
		status.opcode = (short) ((IR.getValue() >>> 12) % 16); 
		//please change this breh --------------------------------------------------------------------
		
		status.S = S.getValue() == 1 ? true : false;
		status.I =  ((short) IR.getValue() & (short) (1 << 11))  == (short) 1 << 11 ? true : false;
		
		//shift left and shift right
		status.E = E.getValue() == 1 ? true : false;
		status.Z = AC.getValue() == 0 ? true : false;//Z is only based on the AC
//		System.out.println(status.toString());
		return status;
	}
	public void updateState(CW control_word) {
		//updates the registers and flip-flops based on a control word
		//to be implemented
		//an example
		//SC
		CW cw = control_word;
		
		short payload;
		Register[] bus = new Register[5];
		bus[0] = AR;
		bus[1] = PC;
		bus[2] = DR;
		bus[3] = AC;
		bus[4] = IR;
		short sum = 0;
		sum += cw.s0 ? 1 : 0;
		sum += cw.s1 ? 1 << 1 : 0;
		sum += cw.s2 ? 1 << 2 : 0;
		sum -= 2;
		SC.update((short) 0, cw.SC_ld, cw.SC_inc, cw.SC_clr);
		S.update((short) 0, cw.S_ld, cw.S_set, cw.S_clr);
			//this is ALU
			if(sum == -2) {
				ALU(cw.ADD,cw.AND,cw.SHL,cw.SHR,cw.TRANSFER,cw.COMPLEMENT);
//					Z.update((short)0, false, false, AC.getValue() != 0);
				return;
			}
			//we need to find the alu payload ehere
			if(sum == -1) {
				payload = m.read(AR.getValue());
			}
			else {
				payload = bus[sum].getValue();
			}
			if(cw.MEM_write) m.write(AR.getValue(), payload);
			AR.update(payload, cw.AR_ld, cw.AR_inc, cw.AR_clr);
			PC.update(payload, cw.PC_ld, cw.PC_inc, cw.PC_clr);
			DR.update(payload, cw.DR_ld, cw.DR_inc, cw.DR_clr);
//			AC.update(payload, cw.AC_ld, cw.AC_inc, cw.AC_clr);
			IR.update(payload, cw.IR_ld, cw.IR_inc, cw.IR_clr);
			
			
			//how to handle indirect? -------------------------------------------------
			//how to handle skips------------------------------------------------------
			//ISZ How to handle?
			//two ways:
			//handle it here
		
	}
	
	public void add() {
		//here I will load the E value because if I do it later then i affect the AC
		int ac = (int) AC.getValue();
		int dr = (int) DR.getValue();
		short e = (short) ((ac + dr) >> 16); 
		AC.load((short) (AC.getValue() + DR.getValue()));
		SC.clear();
		//add e? ----------------------------------------------------------------------
		//where is the e input? 
		//the add operatoin when there is a carry
		//most significant bit of ac
		
	}
	public void and() {
		AC.load((short) (AC.getValue() & DR.getValue()));
		SC.clear();
	}
	public void cil() {
		short val = E.getValue();
		E.load((short) (AC.getValue() >> 15));
		AC.load((short) (AC.getValue() << 1 + val));
		SC.clear();
	}
	public void cir() {
		short val =  E.getValue();
		E.load((short) (AC.getValue() & 1));
		AC.load((short) (AC.getValue() >> 1 + val << 16));
		SC.clear();
	}
	
	public void lda() {
		AC.load(DR.getValue());
		SC.clear();
	}
	
	public void cma() {
		AC.load((short)~AC.getValue());
		SC.clear();
		
	}
	
	public void ALU(boolean add, boolean and, boolean cil, boolean cir, boolean lda, boolean cma) {
		if(add) add();
		if(and) and();
		if(cil) cil();
		if(cir) cir();
		if(lda) lda();
		if(cma) cma();
	}
}


//needs to fix the data unit

//the control unit
//It produces the control word
