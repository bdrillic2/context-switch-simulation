/** Bracha Drillick
 *  T00497525
 *  Context Switch Simulation
 */

import java.util.Random;

public class SimProcess {
	private int pid;
	private String procName;
	private int totalInstructions;
	
	public SimProcess(int pid, String procName, int totalInstructions) {
		this.pid = pid;
		this.procName = procName;
		this.totalInstructions = totalInstructions;
	}
	
	public int getPid() {
		return pid;
	}
	
	public ProcessState execute(int i) {
		System.out.print("Proc " + procName + ", PID: " + pid + ", executing instruction: " + i);
		
		if(i >= totalInstructions) {
			return ProcessState.valueOf("FINISHED");
		}
			
		Random randNum = new Random();
		int num = randNum.nextInt(100);
		if(num < 15) {
			return ProcessState.valueOf("BLOCKED");
		}
		
		return ProcessState.valueOf("READY");
	}
}