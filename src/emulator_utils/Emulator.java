

package emulator_utils;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import java.util.Scanner;
//
public class Emulator {

	
	
	private DataUnit mDataUnit;
	private ControlUnit mControlUnit;
	private CW mCw;
	private Status mStatus;
	private Memory memory;
	
	public Emulator(short[] instructions, int start) {
		memory = new Memory(2048);
		memory.fill(instructions, start);
		mDataUnit=new DataUnit(memory);
		mControlUnit=new ControlUnit();
		mDataUnit.SC.clear();
		//start2();
	}
	public void start2() {
	//mDataUnit.S.up
		mDataUnit.S.load((short)0x1);
		mDataUnit.PC.load((short) 0x0000);

		emulate();
	}
	public void emulate() {
		while(mDataUnit.S.getValue() != 0) {
			mStatus=mDataUnit.getStatus();
			mCw=mControlUnit.getCW2(mStatus);
			mDataUnit.updateState(mCw);
			System.out.println("SC: "+ mDataUnit.SC.getValue() + "\n\tStatus: "+mStatus.toString()+"\n\tCW: "+mCw.toString()+"\n\n");
		}
		System.out.println(mDataUnit.getStatus());

	}


	public void readFile(String fileName) {
		 File file = new File("C:\\Users\\Admin\\Documents\\Eclipse-Workspace\\CMPE363-Project\\src\\"+fileName);
		    if (file.exists()) {	
		    	try {
		    		Scanner myReader = new Scanner(file);
		    		int i = 0;
			        while (myReader.hasNextLine()) {
				          memory.write(i++, myReader.nextShort());
				          
				        }
			        myReader.close();
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		    } else {
		      System.out.println("The file does not exist.");
		    }
	}


public void readMemory(int start, int end) {
	Memory m = new Memory(2048);
	for (int i = start; i <= end; i++) {
		System.out.println(i + ":	" + m.read(i));
	}
	
}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		 //create emulator object.
		//call emulator.start()
		short[] i=new short[2048];
		
		new Emulator(i, 0);
		//emulator.start();
		
		//------------------------------------------------
		 Emulator emulator = new Emulator(i, 0);
		 
		 
		 System.out.println("Welcome to my PBC emulator!");
		 System.out.println("Your machine language input program should be provided in Decimal");
		 System.out.println("Please enter the file name for the input program:");
		 Scanner file = new Scanner (System.in);
		 String fileName = file.nextLine();
		 
		 emulator.readFile(fileName);
		 
		 System.out.println("Started emulation your program execution . . .");
		
		 // Start execution
		 emulator.start2();
		 
		 System.out.println("Finished emulating your program execution.");
		 System.out.println("Let’s print your memory.");
		 
		 System.out.println("Enter the beginning of the memory range you like to see: ");
		 int start = file.nextInt();
		 System.out.println("Enter the end of the memory range you like to see: ");
		 int end = file.nextInt();
		 
		 System.out.println("Your memory contents: ");
		 
		 // Display the contents
		
		 emulator.readMemory(start, end);
		 
		 System.out.println("Thanks for using my PBC emulator! ");
	}
	
}

//
////get the state from the data unit
////get the control word using the state
////update the state using cw



