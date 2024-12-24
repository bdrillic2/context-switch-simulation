/** Bracha Drillick
 *  T00497525
 *  Context Switch Simulation
 */

import java.util.ArrayList;
import java.util.Random;

public class Simulator {
	public static void main(String[] args) {
		//create and fill ready and blocked processes array lists
		ArrayList<ProcessControlBlock> readyProcesses = instantiateProcesses();
		ArrayList<ProcessControlBlock> blockedProcesses = new ArrayList<>();
		
		//initialize processor with no-arg default constructor - it is totally empty
		SimProcessor processor = new SimProcessor();
		
		//initialize quantum variable
		final int QUANTUM = 5;
		
				
		//initialize and set currPCB variable to first ready process's PCB
		ProcessControlBlock currPCB = readyProcesses.get(0);
		
		//place process and currInstruction of first PCB on ready list into processor with ProcessState READY and 0 ticks
		processor.setCurrProcess(currPCB.getProcess());
		processor.setCurrInstruction(currPCB.getCurrInstruction());
		ProcessState processState = ProcessState.READY;
		int ticks = 0;
		
		for(int i = 0; i < 3000; i++) {	
			//check if there are no ready processes, only blocked ones
			if(readyProcesses.size() == 0 && processState == ProcessState.BLOCKED) {
				System.out.print("\nStep " + (i + 1) + " Processor is idling");
			//check if there is only one process but it is being moved to blocked or finished
			} else if(readyProcesses.size() == 1 && (processState == ProcessState.FINISHED || processState == ProcessState.BLOCKED)) {
				System.out.print("\nStep " + (i + 1) + " Processor is idling");
				//if it is meant to be blocked, move it
				if(processState == ProcessState.BLOCKED) {
					blockedProcesses.add(currPCB);
					readyProcesses.remove(0);
				}
			//check for context switches in three scenarios
			//check if process is finished
			} else if(processState == ProcessState.FINISHED) {
				processor = contextSwitch(processor, readyProcesses, processState, i);
				currPCB = readyProcesses.get(0);
				processState = ProcessState.READY;
				ticks = 0;
			//check if process is blocked
			} else if(processState == ProcessState.BLOCKED) {
				//add to blocked list
				blockedProcesses.add(currPCB);
				processor = contextSwitch(processor, readyProcesses, processState, i);
				currPCB = readyProcesses.get(0);
				processState = ProcessState.READY;
				ticks = 0;
			//check if process ran for a full quantum
			} else if (ticks == QUANTUM) {
				System.out.print("\n*** Quantum expired ***");
				//place in back of ready list
				readyProcesses.add(currPCB);
				processor = contextSwitch(processor, readyProcesses, processState, i);
				currPCB = readyProcesses.get(0);
				processState = ProcessState.READY;
				ticks = 0;
			} else {
				//if not, execute next instruction
				System.out.print("\nStep " + (i + 1) + " ");
				processState = processor.executeNextInstruction();
				if(processState == ProcessState.FINISHED) {
					System.out.print("\n*** Process completed ***");
				}
				if(processState == ProcessState.BLOCKED) { 
					System.out.print("\n*** Process blocked ***");
				}
				ticks++;
			}
			
			processState = unblockProcesses(blockedProcesses, readyProcesses, processState);
			
		}
		
	}
	
	public static ArrayList<ProcessControlBlock> instantiateProcesses() {
		//create ready processes ArrayLists, and add all PCBs
		ArrayList<ProcessControlBlock> readyProcesses = new ArrayList<>();
		
		readyProcesses.add(new ProcessControlBlock(new SimProcess(1, "proc1", 150)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(2, "proc2", 200)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(3, "proc3", 300)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(4, "proc4", 250)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(5, "proc5", 300)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(6, "proc6",150)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(7, "proc7", 400)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(8, "proc8", 150)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(9, "proc9", 150)));
		readyProcesses.add(new ProcessControlBlock(new SimProcess(10, "proc10", 200)));

		return readyProcesses;
	}
	
	public static SimProcessor contextSwitch(SimProcessor processor, ArrayList<ProcessControlBlock> readyProcesses, 
											 ProcessState processState, int i) {
		//initialize, set, and remove variable currPCB from readyProcesses
		ProcessControlBlock currPCB = readyProcesses.get(0);
		readyProcesses.remove(currPCB);
		
		if(processState != ProcessState.FINISHED) {
			//save currInstruction and registers to PCB if process has not been completed
			currPCB.setCurrInstruction(processor.getCurrInstruction());
			currPCB.setRegister1(processor.getRegister1());
			currPCB.setRegister2(processor.getRegister2());
			currPCB.setRegister3(processor.getRegister3());
			currPCB.setRegister4(processor.getRegister4());
		}
		
		//load processor with new process, instructions, and registers
		processor.setCurrProcess(readyProcesses.get(0).getProcess());
		processor.setCurrInstruction(readyProcesses.get(0).getCurrInstruction());
		processor.setRegister1(readyProcesses.get(0).getRegister1());
		processor.setRegister2(readyProcesses.get(0).getRegister2());
		processor.setRegister3(readyProcesses.get(0).getRegister3());
		processor.setRegister4(readyProcesses.get(0).getRegister4());
		
		//Display context switch information
		System.out.print("\nStep " + (i + 1) + " CONTEXT SWITCH");
		if(processState != ProcessState.FINISHED) {
			System.out.print("\n\tSaving process " + currPCB.getProcess().getPid() + "\t");
			System.out.print("Instruction: " + currPCB.getCurrInstruction() + " " + 
					" R1: " + currPCB.getRegister1() + " R2: " + currPCB.getRegister2() +
					" R3: " + currPCB.getRegister3() + " R4: " + currPCB.getRegister4());
		}
		if(readyProcesses.size() > 1 || processState != ProcessState.BLOCKED) {
			System.out.print("\n\tRestoring process " + processor.getCurrProcess().getPid() + "\t");
			System.out.print("Instruction: " + processor.getCurrInstruction() + " " + 
					" R1: " + processor.getRegister1() + " R2: " + processor.getRegister2() +
					" R3: " + processor.getRegister3() + " R4: " + processor.getRegister4());
		}
		return processor;
	}
	
	public static ProcessState unblockProcesses(ArrayList<ProcessControlBlock> blockedProcesses, 
										ArrayList<ProcessControlBlock> readyProcesses, ProcessState processState) {
		//create random variable to be used to unblock processes
		Random randNum = new Random();
		
		//Loop through blocked processes and wake up with 30% probability
		for(int b = 0; b < blockedProcesses.size(); b++) {
			int prob = randNum.nextInt(100);
			if(prob < 30) {
				ProcessControlBlock wakingProcess = blockedProcesses.get(b);
				readyProcesses.add(wakingProcess);
				blockedProcesses.remove(b);
				//if this process added brings the readyProcess size to 1, meaning it was previously empty, change processState to READY
				if(readyProcesses.size() == 1) {
					processState = ProcessState.READY;
				}
			}
		}
		return processState;
		
	}
}