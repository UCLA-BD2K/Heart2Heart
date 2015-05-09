package bd2k.heart2heart;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {


    final RunnerState myState = new RunnerState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //This is in the onCreate method
        LinearLayout lView = new LinearLayout(this);
        lView.setOrientation(LinearLayout.VERTICAL);

        final TextView mainText = new TextView(this);
        mainText.setText("Ready, set, GO!");

        final TextView responseText = new TextView(this);
        responseText.setText("Waiting for response from server...");

        lView.addView(mainText);
        lView.addView(responseText);

        setContentView(lView);

        final String runnerID;

        final String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println(androidId);
        //these aren't the right unique IDs
        switch(androidId)
        {
            case "921b5535f5bbeead"://this is the emulator
                runnerID = "1";
                break;
            case "d4ec30ea085d913e"://this is tevfik's phone
                runnerID = "2";
                break;
            default:
                runnerID = "1";
                break;
        }

        final Communicator sender = new Communicator(myState, this, responseText);
        sender.start();

        sender.queue.add(runnerID + "," + "ready");//tied to start button
        //sender.queue.add(runnerID + "," + "done");//tied to stop button

        final SoundThread sounds = new SoundThread(myState, this);
        sounds.start();



        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                /*
                Every time the location changes it recalculates the speed, displays it and sends it to the server.
                 */

                if(!myState.running)
                    return;

                location.getLatitude();
                float speed = location.getSpeed();
                if(speed < 0.5) speed = 0;
                float mileTime = new Float(1 / (speed * 0.0372822715));
                mainText.setText("Current speed: " + speed + " m/s\nMile time: " + mileTime + " minutes");
                sender.queue.add(runnerID + "," + speed + "," + System.currentTimeMillis());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
