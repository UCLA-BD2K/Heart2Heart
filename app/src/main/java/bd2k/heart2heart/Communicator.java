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
    TextView responseText;
    Activity mainActivity;
    RunnerState myState;

    public Communicator(RunnerState myState, Activity mainActivity, TextView responseText)
    {
        this.myState = myState;
        this.responseText = responseText;
        this.mainActivity = mainActivity;
    }

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
                    myState.lastServerResponse = System.currentTimeMillis();
                    System.out.println("server says: " + response);

                    if(response == "start")
                    {
                        myState.running = true;
                        continue;
                    }
                    if(!myState.running)
                        continue;

                    String[] frags = response.split(" ");

                    myState.personalDistance = Float.valueOf(frags[0]);
                    myState.relativeDistance = myState.personalDistance - Float.valueOf(frags[1]);
                    myState.partnerSpeed = Float.valueOf(frags[2]);

                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //stuff that updates ui
                            responseText.setText("total distance: " + myState.personalDistance + " meters\nrelative distance: " + myState.relativeDistance + " meters\npartner's speed: " + myState.partnerSpeed);
                        }
                    });

                }
            }
        }catch(IOException e){e.printStackTrace();}
    }
}
