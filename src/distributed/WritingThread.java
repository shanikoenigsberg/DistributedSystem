package distributed;

import java.net.Socket;
import java.util.ArrayList;
import java.io.*;

public class WritingThread extends Thread {
	private PrintWriter writer;
	private ArrayList<String> jobs;
	private Object lock;
	private String message;

	public WritingThread(Socket socket, ArrayList<String> jobs, Object lock, String message) {
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.jobs = jobs;
		this.lock = lock;
		this.message = message;
	}

	@Override
	public void run() {
		String jobWithSource;
		String job;
		while (true) {
			
			job = null;
			if (jobs.size() > 0) {
				synchronized (lock) {
					jobWithSource = jobs.get(0);
					job=jobWithSource.substring(0,jobWithSource.length()-1);
					jobs.remove(jobWithSource);
				}
			
				writer.println(jobWithSource);
				System.out.println("Sent job " + job + " to " + message);
				job = null;
			}
			
			try {
				sleep(100);
			} catch(InterruptedException ex) {
				System.out.println("Error occurred. Please try again.");
				System.exit(0);
			}
		}
	}
}
