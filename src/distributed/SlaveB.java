package distributed;

import java.net.Socket;
import java.util.ArrayList;

public class SlaveB {

	public static void main(String[] args) {
		
		System.out.println("Slave B connected");
		ArrayList<String> jobs = new ArrayList<String>();
		ArrayList<String> completedJobs = new ArrayList<String>();
		Object lock = new Object();

		try (Socket socket = new Socket("127.0.0.1", 8085);) {

			ReadingThread readFromMaster = new ReadingThread(socket, jobs, lock, "Master");
			SlaveWritingThread writeToMaster = new SlaveWritingThread(socket, jobs, completedJobs, lock, "Slave B");
			
			readFromMaster.start();
			writeToMaster.start();
			
			readFromMaster.join();
			writeToMaster.join();
			
		} catch (Exception ex) {
			System.out.println("Error occurred. Please try again.");
			System.exit(0);
		}
	}
}