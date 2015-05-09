import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String args[]) throws IOException {
		final String host = "localhost";
		final int portNumber = 8080;
		System.out.println("Creating socket to '" + host + "' on port " + portNumber);

		while (true) {
			Socket socket = new Socket(host, portNumber);
			System.out.println("connected to server!");
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
			String userInput = userInputBR.readLine();
			//while (userInput != null) {

			//}

			
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();
			out.println(userInput);
			System.out.println("server response: " + br.readLine());
			userInput = userInputBR.readLine();

			
			

			
			socket.close();
			System.out.println("socket closed!");
			// if ("exit".equalsIgnoreCase(userInput)) {
			// 	socket.close();
			// 	break;
			// }
		}
		
	}
}