import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {



	public static void main(String args[]) throws IOException {
		final int portNumber = 8080;
		System.out.println("Creating server socket on port " + portNumber);
		ServerSocket serverSocket = new ServerSocket(portNumber);

		AtomicReferenceArray<Double> speed_list = new AtomicReferenceArray<Double>(5);
		AtomicReferenceArray<Double> distance_list = new AtomicReferenceArray<Double>(5);
		AtomicReferenceArray<Long> time_list = new AtomicReferenceArray<Long>(5);
		AtomicIntegerArray ready = new AtomicIntegerArray(5);
		AtomicInteger done = new AtomicInteger();
		for (int i = 0; i < 5; i++) {
			speed_list.set(i, new Double("0"));
			distance_list.set(i, new Double("0"));
			time_list.set(i, new Long("0"));
			ready.set(i, new Integer("0"));
			done.set(new Integer("0"));
		}


		int id = 1;
		while (true) {
			ServerWorker w;
			w = new ServerWorker(serverSocket.accept(), id, speed_list, distance_list, time_list, ready, done);
			Thread t = new Thread(w);
			System.out.println("initialized thread " + id);
			//id++;
			t.start();
		}
	}
}