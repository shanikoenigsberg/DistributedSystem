package distributed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ReadingThread extends Thread {
	private BufferedReader reader;
	private ArrayList<String> readingList;
	public Object lock;
	private String message;

	public ReadingThread(Socket socket, ArrayList<String> readingList, Object lock, String message) {
		this.readingList = readingList;
		this.lock = lock;
		this.message = message;

		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			System.out.println("Error occurred. Please try again.");
		}
	}

	@Override
	public void run() {
		String jobWithSource;
		
		while (true) {

			String readingIn = null;
			try {
				jobWithSource = reader.readLine();
				readingIn = jobWithSource.substring(0, jobWithSource.length() - 1);

				if (readingIn == null || readingIn.isEmpty() || readingIn.isBlank()) {
					continue;
				} else {
					synchronized (lock) {
						readingList.add(jobWithSource);
					}
				}
				if (readingIn.length() > 8 && readingIn.substring(0,8).equals("Complete")) {
					System.out.println("Job " + readingIn.substring(8) + " completed");
				}
				else
					System.out.println(readingIn + " received from " + message);
				
			} catch (Exception e) {
				System.out.println("Error occurred. Please try again.");
				System.exit(0);
			}
			try {
				sleep(100);
			} catch(InterruptedException e) {
				System.out.println("Error occurred. Please try again.");
				System.exit(0);
			}
		}

	}

}
