package emulator_utils;

public class ControlUnit {
	//can be implemented using either hardwired or microprogrammed control unit. Microprogrammed is harder, because we will have to design the appropriate microinstruction set.
	//The control unit provides the following method:
	public CW getCW(Status status) {
		//returns the control word for the current time step based on status
		return new CW();
	}
}
