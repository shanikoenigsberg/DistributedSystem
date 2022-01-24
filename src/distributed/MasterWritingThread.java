package distributed;

import java.net.Socket;
import java.util.ArrayList;
import java.io.*;

public class MasterWritingThread extends Thread {
	private PrintWriter writerClient1;
	private PrintWriter writerClient2;
	private ArrayList<String> jobs;
	private Object lock;

	public MasterWritingThread(Socket socketClient1, Socket socketClient2, ArrayList<String> jobs, Object lock) {
		try {
			writerClient1 = new PrintWriter(socketClient1.getOutputStream(), true);
			writerClient2 = new PrintWriter(socketClient2.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.jobs = jobs;
		this.lock = lock;
	}

	@Override
	public void run() {
		String jobWithSource;
		String job;
		while (true) {
			jobWithSource = null;
			job = null;

			if (!jobs.isEmpty()) {
				synchronized (lock) {
					if (!jobs.isEmpty()) {
						jobWithSource = jobs.get(0);
						job = jobWithSource.substring(0, jobWithSource.length() - 1);
						jobs.remove(jobWithSource);
					}

				}
				if (job != null) {
					//substring to get which client job is going to. Client "1" or "2"
					if (jobWithSource.substring(jobWithSource.length() - 1).equals("1")) {
						writerClient1.println("Complete" + jobWithSource);
					} else {
						writerClient2.println("Complete" + jobWithSource);
					}

					System.out.println("Sent job " + job + " to Client " + jobWithSource.substring(jobWithSource.length() - 1));
				}
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
