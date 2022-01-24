package distributed;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Master {

	public static void main(String[] args) {

		System.out.println("Master connected");

		ArrayList<String> slaveAJobs = new ArrayList<String>();
		ArrayList<String> slaveBJobs = new ArrayList<String>();

		ArrayList<String> completedJobs = new ArrayList<String>();
		
		Object lock = new Object();

		try (ServerSocket serverSocketClient1 = new ServerSocket(8080);
				Socket socketClient1 = serverSocketClient1.accept();
				ServerSocket serverSocketClient2 = new ServerSocket(9085);
				Socket socketClient2 = serverSocketClient2.accept();
				ServerSocket slaveASocket = new ServerSocket(9090);
				Socket slaveA = slaveASocket.accept();
				ServerSocket slaveBSocket = new ServerSocket(8085);
				Socket slaveB = slaveBSocket.accept();
				) {
			
			
			Object lockA = new Object();
			Object lockB = new Object(); 

			MasterReadingThread readFromClient1 = new MasterReadingThread(socketClient1, lockA, lockB, slaveAJobs, slaveBJobs,"Client 1");
			MasterReadingThread readFromClient2 = new MasterReadingThread(socketClient2, lockA, lockB, slaveAJobs, slaveBJobs, "Client 2");
			
			WritingThread writingThreadA = new WritingThread(slaveA, slaveAJobs, lockA, "Slave A");
			WritingThread writingThreadB = new WritingThread(slaveB, slaveBJobs, lockB, "Slave B");
			
			ReadingThread readingThreadA = new ReadingThread(slaveA, completedJobs, lock, "Slave A");
			ReadingThread readingThreadB = new ReadingThread(slaveB, completedJobs, lock, "Slave B");
			
			MasterWritingThread writeToClient1 = new MasterWritingThread(socketClient1, socketClient2, completedJobs, lock);
			MasterWritingThread writeToClient2 = new MasterWritingThread(socketClient1, socketClient2, completedJobs, lock);
			
			//START THREADS
			readFromClient1.start();			
			readFromClient2.start();
			
			writingThreadA.start();
			writingThreadB.start();
			
			readingThreadA.start();
			readingThreadB.start();
			
			writeToClient1.start();
			writeToClient2.start();
			
			//JOIN THREADS
			readFromClient1.join();
			readFromClient2.join();
			
			writingThreadA.join();
			writingThreadB.join();

			readingThreadA.join();
			readingThreadB.join();
			
			writeToClient1.join();
			writeToClient2.join();
			

		} catch (Exception ex) {
			System.out.println("Error occurred. Please try again.");
			System.exit(0);
		}

	}

}