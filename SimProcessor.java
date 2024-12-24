/** Bracha Drillick
 *  T00497525
 *  Context Switch Simulation
 */

import java.util.Random;

public class SimProcessor {
	private SimProcess currProcess;
	private int register1;
	private int register2;
	private int register3;
	private int register4;
	private int currInstruction;
	
	public SimProcessor() {
		Random randValue = new Random();
		register1 = randValue.nextInt();
		register2 = randValue.nextInt();
		register3 = randValue.nextInt();
		register4 = randValue.nextInt();
	}
	
	//getters, than setters
	
	public SimProcess getCurrProcess() {
		return currProcess;
	}
	public int getRegister1() {
		return register1;
	}
	public int getRegister2() {
		return register2;
	}
	public int getRegister3() {
		return register3;
	}
	public int getRegister4() {
		return register4;
	}
	public int getCurrInstruction() {
		return currInstruction;
	}
	public void setCurrProcess(SimProcess currProcess) {
		this.currProcess = currProcess;
	}
	public void setRegister1(int register1) {
		this.register1 = register1;
	}
	public void setRegister2(int register2) {
		this.register2 = register2;
	}
	public void setRegister3(int register3) {
		this.register3 = register3;
	}
	public void setRegister4(int register4) {
		this.register4 = register4;
	}
	public void setCurrInstruction(int currInstruction) {
		this.currInstruction = currInstruction;
	}
	
	public ProcessState executeNextInstruction() {
		ProcessState currState = currProcess.execute(currInstruction);
		currInstruction++;
		
		Random randValue = new Random();
		register1 = randValue.nextInt();
		register2 = randValue.nextInt();
		register3 = randValue.nextInt();
		register4 = randValue.nextInt();

		return currState;
	}
	
	

}