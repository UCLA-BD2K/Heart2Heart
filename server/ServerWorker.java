import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicInteger;

class ServerWorker implements Runnable {
	private Socket socket;
	private int id;
	private AtomicReferenceArray<Double> speed_list;
	private AtomicReferenceArray<Double> distance_list;
	private AtomicReferenceArray<Long> time_list;
	private AtomicIntegerArray ready;
	private AtomicInteger done;

	ServerWorker(Socket socket, int id, AtomicReferenceArray<Double> speed_list, AtomicReferenceArray<Double> distance_list, AtomicReferenceArray<Long> time_list, AtomicIntegerArray ready, AtomicInteger done) {
		this.socket = socket;
		this.id = id;
		this.speed_list = speed_list;
		this.distance_list = distance_list;
		this.time_list = time_list;
		this.ready = ready;
		this.done =done;
	}

	private Double calculate(int index, Double speed, Long time) {
		Double avg = (speed_list.get(index) + speed)/2;
		System.out.println("avg: " + avg);
		speed_list.set(index, speed);
		if (time_list.get(index) != 0) {
			Double delta = avg * ((time - time_list.get(index))/1000);
			distance_list.set(index, distance_list.get(index) + delta);
			System.out.println("delta: " + delta);
		}
		time_list.set(index, time);
		System.out.println("total distance: " + distance_list.get(index));


		return distance_list.get(index);
	}

	public void run() {

		try {
			System.out.println("connected to client");

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String str = br.readLine();

			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);

			while(str != null) {
				//System.out.println("here2");

				while (str != null) {
					System.out.println("received: " + str);
					if (str.charAt(2) == 'r') {
						int client = Integer.parseInt(str.substring(0,1));
						ready.set(client, 1);
						System.out.println("client " + client + " ready!");
						break;
					}
					System.out.println("incorrect ready mesage");
					str = br.readLine();
				}

				while(true) {
					if (ready.get(1) == 1 && ready.get(2) == 1) {
						pw.println("start");
						System.out.println("clients ready, sent start message");
						break;
					}
				}


				str = br.readLine();

				while (str != null) {

					if (done.get() == 1) {
						pw.println("finish");
						System.out.println("finishing run; resetting");
						for (int i = 0; i < 5; i++) {
							speed_list.set(i, new Double("0"));
							distance_list.set(i, new Double("0"));
							time_list.set(i, new Long("0"));
							ready.set(i, new Integer("0"));
						}
						break;
					}

					System.out.println("\n---------MESSAGE---------");
					System.out.println("received: " + str);

					int index = Integer.parseInt(str.substring(0,1));
					System.out.println("client: " + index);

					if (str.charAt(2) == 'd') {
						done.set(1);
						System.out.println("client " + index + " done!");
						for (int i = 0; i < 5; i++) {
							speed_list.set(i, new Double("0"));
							distance_list.set(i, new Double("0"));
							time_list.set(i, new Long("0"));
							ready.set(i, new Integer("0"));
						}
						break;
					}

					String speed_string = "";
					int i = 2;
					for (; str.charAt(i) != ','; i++) {
						speed_string = speed_string + str.charAt(i);
					}
					i++;
					Double speed = Double.parseDouble(speed_string);
					System.out.println("speed: " + speed);

					String time_string = str.substring(i);
					Long time = Long.parseLong(time_string);
					System.out.println("time: " + time);


					//distance, other distance, other speed


					
					Double distance = calculate(index, speed, time);
					System.out.println("sent response to client " + index + ":");
					Double diff;
					if (index == 1) {
						diff = distance - distance_list.get(0);
						if (diff > 50.0)
							diff = 50.0;
						else if (diff < -50.0)
							diff = -50.0;
						pw.println(distance + " " + diff + " " + speed_list.get(0));
						System.out.println(distance + " " + diff + " " + speed_list.get(0));
					}
					else {
						diff = distance - distance_list.get(1);
						if (diff > 50.0)
							diff = 50.0;
						else if (diff < -50.0)
							diff = -50.0;
						pw.println(distance + " " + diff + " " + speed_list.get(1));
						System.out.println(distance + " " + diff + " " + speed_list.get(1));
					}

					
					

					str = br.readLine();
				}

				//System.out.println("here");
				str = br.readLine();



			}
			pw.close();
			socket.close();
			System.out.println("socket closed!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}