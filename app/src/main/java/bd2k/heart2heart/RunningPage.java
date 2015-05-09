package bd2k.heart2heart;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hannah on 5/8/2015.
 */
public class RunningPage extends ActionBarActivity{

    Button finishRunButton;

    TextView mySpeedView;
    TextView myMileTimeView;
    TextView friendSpeedView;
    TextView friendMileTimeView;
    TextView distanceRunView;
    TextView relativeDistanceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_mode);
        finishRunButton = (Button) findViewById(R.id.button5);
        mySpeedView = (TextView) findViewById(R.id.textView3);
        myMileTimeView = (TextView) findViewById(R.id.textView5);
        friendSpeedView = (TextView) findViewById(R.id.textView17);
        friendMileTimeView = (TextView) findViewById(R.id.textView19);
        distanceRunView = (TextView) findViewById(R.id.textView7);
        relativeDistanceView = (TextView) findViewById(R.id.textView15);
        addListenerOnButtons();


        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                /*
                Every time the location changes it recalculates the speed, displays it and sends it to the server.
                 */

                if(!RunnerState.running)
                    return;

                location.getLatitude();
                float speed = location.getSpeed();
                if(speed < 0.5) speed = 0;
                float mileTime = new Float(1 / (speed * 0.0372822715));

                mySpeedView.setText("" + speed);
                myMileTimeView.setText("" + mileTime);
                friendSpeedView.setText("" + RunnerState.partnerSpeed);
                friendMileTimeView.setText("" + RunnerState.mileTime(RunnerState.partnerSpeed));
                distanceRunView.setText("" + RunnerState.personalDistance);
                relativeDistanceView.setText("" + RunnerState.relativeDistance);
                //mainText.setText("Current speed: " + speed + " m/s\nMile time: " + mileTime + " minutes");

                RunnerState.sender.queue.add(RunnerState.runnerID + "," + speed + "," + System.currentTimeMillis());
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

    public void addListenerOnButtons() {

        final Context context = this;
        finishRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                RunnerState.sender.queue.add(RunnerState.runnerID + "," + "done");//tied to stop button
                startActivity(intent);
            }
        });
    }
}
