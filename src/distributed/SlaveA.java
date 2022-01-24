package distributed;

import java.net.Socket;
import java.util.ArrayList;

public class SlaveA {

	public static void main(String[] args) {
		System.out.println("Slave A connected");
		ArrayList<String> jobs = new ArrayList<String>();
		ArrayList<String> completedJobs = new ArrayList<String>();
		Object lock = new Object();

		try (Socket socket = new Socket("127.0.0.1", 9090);) {

			ReadingThread readFromMaster = new ReadingThread(socket, jobs, lock, "Master");
			SlaveWritingThread writeToMaster = new SlaveWritingThread(socket, jobs, completedJobs, lock, "Slave A");
			
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
