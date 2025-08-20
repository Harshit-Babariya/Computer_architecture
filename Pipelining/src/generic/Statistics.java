package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int number_of_times_OF_stalled_for_data_locks=0; 
	static int numberOfCycles;
	static int number_of_times_instructions_on_wrong_path=0;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("number_of_times_OF_stalled_for_data_locks = " + number_of_times_OF_stalled_for_data_locks);
			writer.println("number_of_times_instructions_on_wrong_path = " + number_of_times_instructions_on_wrong_path);
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}
	public static int getnumberofcycles()
	{
		return Statistics.numberOfCycles;
	}
	public static void setnumber_of_times_OF_stalled_for_data_locks(int number_of_times_OF_stalled_for_data_locks) {
		Statistics.number_of_times_OF_stalled_for_data_locks = number_of_times_OF_stalled_for_data_locks;
	}
	public static int getnumber_of_times_OF_stalled_for_data_locks() {
		return Statistics.number_of_times_OF_stalled_for_data_locks;
	}
	public static void setnumber_of_times_instructions_on_wrong_path(int number_of_times_instructions_on_wrong_path) {
		Statistics.number_of_times_instructions_on_wrong_path = number_of_times_instructions_on_wrong_path;
	}
	public static int getnumber_of_times_instructions_on_wrong_path() {
		return Statistics.number_of_times_instructions_on_wrong_path;
	}
}
