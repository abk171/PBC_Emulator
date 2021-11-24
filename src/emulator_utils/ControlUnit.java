package emulator_utils;


//updates:
//changed opcodes
//changed isz
//fixed some incrementing bugs

public class ControlUnit {
	
	//can be implemented using either hardwired or microprogrammed control unit. Microprogrammed is harder, because we will have to design the appropriate microinstruction set.
		//The control unit provides the following method:
		int sequenceCounter;
		
		public ControlUnit() {
			sequenceCounter = 0;
		}
		
		public void reset() {
			sequenceCounter = 0;
		}
		
		public void increment() {
			sequenceCounter++;
		}
		
		public CW getCW2(Status s) {
			CW cw = new CW();
			//this one is emulated
			
			
			boolean D[] = new boolean[16];
			boolean T[] = new boolean[7];
			
			for(int i = 0; i < 16; i++) {
				D[i] = false;
			}
			
			for(int i = 0; i < 7; i++) {
				T[i] = false;
			}
			
			D[s.opcode] = true;
			T[sequenceCounter] = true;
			
			//handle bus
			cw.s0 = T[0] || T[1] || (s.I && T[3]) || (D[1] && T[4]) || (D[2] && T[4]) || (D[3] && T[4]) || (D[4] && T[4]) || (D[5] && T[4]) || (D[7] && T[4]);
			cw.s1 = T[0] || T[2] || (D[6] && T[4]) || (D[7] && T[4]) || (D[7] && T[5]);
			cw.s2 = (T[4] && D[2] || (D[5] && T[6])) || T[2];
			
			//handle memory read
			cw.MEM_read = T[3] && s.I;
			
			//handle loads
			cw.AR_ld = T[0] || T[2] || (T[3] && s.I);
			cw.IR_ld = T[1];
			cw.DR_ld = (D[1] && T[4]) || (D[3] && T[4]) || (D[4] && T[4]) || (D[5] && T[4]);
			cw.MEM_write = (D[2] && T[4]) || (D[5] && T[6]) || (D[7] && T[4]);
			
			
			
			//handle increments
			cw.PC_inc = (((D[5] && T[6]) || (D[13] && T[3]) ) && s.Z) || ( (D[14] && T[3]) && !s.E) || T[1];
			cw.DR_inc = D[5] && T[5];
			cw.AR_inc = D[7] && T[5];
			cw.SC_inc = !(((D[5] && T[6]) || (D[13] && T[3]) ) && s.Z) && !( (D[14] && T[3]) && !s.E);
			if(cw.SC_inc) increment();
			
			//handle alu
			cw.TRANSFER = D[1] && T[5];
			cw.AND = D[3] && T[5];
			cw.ADD = D[4] && T[5];                             
			cw.COMPLEMENT = D[10] && T[3];                     
			cw.SHL = D[11] && T[3];                            
			cw.SHR = D[12] && T[4];                            
			
			//handle resets
			cw.SC_clr = (D[0] && T[4]) || (D[1] && T[5]) || (D[2] && T[4]) || (D[3] && T[5]) || (D[4] && T[5]) || (D[5] && T[6]) || (D[6] && T[4]) || (D[7] && T[5]) || (D[8] && T[3]) || (D[9] && T[3]) || (D[10] && T[3]) || (D[11] && T[3]) || (D[12] && T[3]) || (D[13] && T[3]) || (D[14] && T[3]) || (D[15] && T[3]);
			if(cw.SC_clr) reset();
			if(cw.SC_clr) cw.SC_inc = false; 
			cw.S_clr = D[0];
			
			return cw;
		}
		
		public CW getCW(Status status) {
			//returns the control word for the current time step based on status
			CW cw = new CW();
			
			if(sequenceCounter == 0) {
				//fetch
				cw.AR_ld=true;
				cw.IR_ld=true;
				cw.MEM_read=true;
				cw.PC_inc=true;
				cw.s0 = true;
				cw.s1 = true;
				increment();
				increment();
				//SC will be one. but in the if statements, there is no operation for the sc==1;
			}
			
			else if(sequenceCounter == 2) {
				//decode
				cw.AR_ld=true;
//				cw.I_ld=true; -------------------- this is not a signal
				cw.s1 = true;
				cw.s2 = true;	
				if(!status.I)
					increment();
			}
			
			else if(sequenceCounter == 3) {
				if(status.I) {
					//indirect
					cw.AR_ld=true;
					cw.MEM_read=true;
					cw.s0 = true;
					increment();
				}
				if(status.opcode == 8) {
					//opcode = 8 <CLA>
					cw.AR_clr=true;
					cw.SC_clr=true;
					reset();
				}
				else if(status.opcode == 9) {
					//opcode= 9 <CLE>
					cw.E_clr=true;
					cw.SC_clr=true;			
					reset();
				}
				else if(status.opcode == 10) {
					//opcode= 10, CMA
					cw.AC_ld=true;
					cw.COMPLEMENT=true;
					cw.SC_clr=true;			
					reset();
				}
				else if(status.opcode == 11) {
					//opcode= 11, cil
					cw.SHL=true;
					cw.E_ld=true;
					cw.AC_ld=true;
					cw.SC_clr=true;					
					reset();
				}
				else if(status.opcode == 12) {
					//opcode=12, cir
					cw.SHR=true;
					cw.E_ld=true;
					cw.AC_ld=true;
					cw.SC_clr=true;	
					reset();
				}
				else if(status.opcode == 13 ) {
					//opcode = 13, sza
					if(status.Z) {
						cw.PC_inc=true;
						cw.SC_clr=true;
						reset();
					}
				}
				else if(status.opcode == 14) {
					//opcode =14, sze
					if(!status.E)cw.PC_inc=true;
					cw.SC_clr=true;				
					reset();
				}
				else if(status.opcode == 15) {
					//opcode=15, nop
					cw.SC_clr=true;
					reset();
				}
				
			}
			
			else if(sequenceCounter == 4) {
				if(status.opcode == 0) {
					//hlt
					cw.S_clr=true;
					cw.SC_clr=true;
					reset();
				}
				else if(status.opcode == 2) {
					//opcode= 2, sta
					cw.MEM_write=true;
					cw.s0 = true;
					cw.s2 = true;	
					cw.SC_clr=true;
					reset();
				}	
				else if(status.opcode == 1 || status.opcode == 3 || status.opcode == 4 || status.opcode == 5) {
					//lda - first instruction
					//and - first instruction
					//add - first instruction
					//isz - first instruction
					cw.DR_ld=true;
					cw.s0 = true;
					cw.MEM_read=true;
					increment();
				}
				else if(status.opcode == 6) {
					//opcode = 6, bun
					cw.PC_ld=true;
					cw.s1 = true;
					cw.SC_clr=true;				
					reset();
				}
				else if(status.opcode == 7) {
					//opcode=7, bsa 
					cw.MEM_write=true;
					cw.s1=true;
					cw.s0=true;
					cw.AR_inc=true;
					increment();
				}
				
			}
			
			else if(sequenceCounter == 5) {
				if(status.opcode == 1) {
					//lda - second instruction
					cw.AC_ld=true;
					cw.SC_clr=true;
					cw.TRANSFER=true;
					reset();
				}
				if(status.opcode == 3) {
					//AND second instruction
					cw.AC_ld=true;
					cw.ADD=true;
					cw.SC_clr=true;
					cw.E_ld=true;
					reset();
				}
				else if(status.opcode == 4) {
					//ADD - second instruction
					cw.AC_ld=true;
					cw.ADD=true;
					cw.SC_clr=true;
					cw.E_ld=true;
					reset();
				}
				else if(status.opcode == 5) {
					//isz - second instruction
					cw.DR_inc=true;
					increment();
//					reset();
					//there should be an incremement here? -----------------------
				}
				else if(status.opcode == 7) {
					//bsa - second instruction
					cw.PC_ld=true;
					cw.s1=true;
					cw.SC_clr=true;
					reset();
				}
			}
			
			else if(sequenceCounter == 6) {
				//isz - third condition
				if(status.Z) {
					cw.PC_inc=true;
					
				}
				else {
					cw.MEM_write=true;
					//load AC also so we can get the Z flag
					cw.AC_ld = true;
					cw.s2=true;
					cw.SC_clr=true;
				}
				reset();
				
				
				
			}
			
			return cw;
			//Creates the cw
			//calls the dataUnit.updateState()
		}
}
