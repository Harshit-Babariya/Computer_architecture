package generic;
import java.io.FileInputStream;
import java.io.IOException;
import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		try(FileInputStream objfile = new FileInputStream(assemblyProgramFile))
		{
			byte[] bytes = new byte[4];
			objfile.read(bytes);
			int firstcodeaddress=(bytes[0]&0xFF)*(1<<24);
			firstcodeaddress+=(bytes[1]&0xFF)*(1<<16);
			firstcodeaddress+=(bytes[2]&0xFF)*(1<<8);
			firstcodeaddress+=(bytes[3]&0xFF);
			int data;
			int i=0;
			while(objfile.read(bytes)!=-1)
			{
				data=(bytes[0]&0xFF)*(1<<24);
				data+=(bytes[1]&0xFF)*(1<<16);
				data+=(bytes[2]&0xFF)*(1<<8);
				data+=(bytes[3]&0xFF);
				processor.getMainMemory().setWord(i,data);
				i+=1;
			}
			processor.getRegisterFile().setProgramCounter(firstcodeaddress);
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1, 65535);
			processor.getRegisterFile().setValue(2, 65535);
			
		}
		catch(IOException e)
		{
			System.out.println("Error reading the object file: ");
		}
	}
	
	public static void simulate()
	{
		int noofcycles=0;
		while(simulationComplete == false)
		{				
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			noofcycles+=1;	
		}
		Statistics.setNumberOfCycles(noofcycles);
	}
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
