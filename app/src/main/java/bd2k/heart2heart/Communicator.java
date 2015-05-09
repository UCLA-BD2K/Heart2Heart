package bd2k.heart2heart;

import android.app.Activity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Brian on 5/8/2015.
 */
public class Communicator extends Thread {

    BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    Socket socket;
    PrintWriter out;
    BufferedReader br;


    public void run() {

        try {
            String host = "52.8.142.166";
            int portNumber = 8080;
            System.out.println("Creating socket to '" + host + "' on port " + portNumber);
            socket = new Socket(host, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String msg;
                while ((msg = queue.poll()) != null) {
                    out.println(msg);
                    System.out.println("message sent: " + msg);

                    final String response = br.readLine();
                    RunnerState.lastServerResponse = System.currentTimeMillis();
                    System.out.println("server says: " + response);

                    if(response == "start")
                    {
                        RunnerState.running = true;
                        continue;
                    }
                    else if(response == "finished")
                    {
                        RunnerState.running = false;
                        continue;
                    }
                    if(!RunnerState.running)
                        continue;

                    String[] frags = response.split(" ");

                    RunnerState.personalDistance = Float.valueOf(frags[0]);
                    RunnerState.relativeDistance = Float.valueOf(frags[1]);
                    RunnerState.partnerSpeed = Float.valueOf(frags[2]);

                }
            }
        }catch(IOException e){e.printStackTrace();}
    }
}
