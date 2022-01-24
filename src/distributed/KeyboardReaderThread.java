package distributed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class KeyboardReaderThread extends Thread {

	private ArrayList<String> jobsList;
	private BufferedReader input;
	private Object readingLOCK;
	protected static int aJobCount = 1;
	protected static int bJobCount = 1;
	private String message;

	public KeyboardReaderThread(ArrayList<String> jobsList, Object readingLOCK, String message) {
		input = new BufferedReader(new InputStreamReader(System.in));
		this.jobsList = jobsList;
		this.readingLOCK = readingLOCK;
		this.message=message;

	}

	public void run() {
		String job;

		try {
			do {
				do {
					System.out.println("Please input job types A/B or Q to quit");
					job = input.readLine().toUpperCase();

					if (!job.equals("A") && !job.equals("B") && !job.equals("Q")) {
						System.out.println("You can only input A/B. Please try again. ");
						job = input.readLine().toUpperCase();
					}
				} while (!job.equals("A") && !job.equals("B"));

				synchronized (readingLOCK) {
					if (job.equals("A")) {
						jobsList.add(job + aJobCount + message);
						aJobCount++;
					} else {
						jobsList.add(job + bJobCount + message);
						bJobCount++;
					}

				}
				
				sleep(100);

			} while (!job.equals("Q"));
			
		} catch (IOException | InterruptedException ex) {
			System.out.println("Error occurred. Please try again.");
			System.exit(0);
		}

		System.out.println("Goodbye...");
	}

}
