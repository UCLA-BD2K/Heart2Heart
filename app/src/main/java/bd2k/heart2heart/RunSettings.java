package bd2k.heart2heart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hannah on 5/8/2015.
 */
public class RunSettings extends ActionBarActivity {

    String username;
    String friendName;

    TextView friendNameView;
    TextView friendLabel;
    TextView settingsLabel;

    Button syncOrBeginButton;

    TextView countDownText;
    TextView colorView;

    public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_settings);
        getStoredProfile();
        //enableLocation();
        addListenerOnButtons();
    }

    public void getStoredProfile() { // to send to the other device
        SharedPreferences profileSettings = getSharedPreferences("ProfileInfo", MODE_PRIVATE);
        username = profileSettings.getString("username", "Root");
    }

    // TODO:
    public void sendRequest() {

    }

    public void enableLocation() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
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

        syncOrBeginButton = (Button) findViewById(R.id.button4);
        countDownText = (TextView) findViewById(R.id.textView23);
        colorView = (TextView) findViewById(R.id.textView25);

        syncOrBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                friendNameView = (TextView) findViewById(R.id.editText4);
                friendName = friendNameView.getText().toString();

                SharedPreferences profileSettings = getSharedPreferences("ProfileInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = profileSettings.edit();
                editor.putString("friendname", friendName);

                editor.commit();

                Context context = getApplicationContext();
                CharSequence text = "Connected! Count down begins now!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                toast.show();

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(friendNameView.getWindowToken(), 0);

                new CountDownTimer(1500, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        //hideText();
                        displayCountDown();
                    }
                }.start();
            }
        });
    }

    public void hideText() {
        friendLabel.setVisibility(View.INVISIBLE);
        settingsLabel.setVisibility(View.INVISIBLE);
        syncOrBeginButton.setVisibility(View.INVISIBLE);

    }

    public void displayCountDown() {
        final Context context = this;
        colorView.setVisibility(View.VISIBLE);
        countDownText.setVisibility(View.VISIBLE);
        countDownText.bringToFront();

        new CountDownTimer(2800, 500) {
            public void onTick(long millisUntilFinished) {
                if(counter == 2) //switch to yellow
                {
                    colorView.setBackgroundColor(Color.YELLOW);
                    countDownText.setText("SET");
                }
                else if(counter == 4) //switch to green
                {
                    colorView.setBackgroundColor(Color.GREEN);
                    countDownText.setText("GO!");
                }
                counter++;
            }
            public void onFinish() {
                Intent intent = new Intent(context, RunningPage.class);
                startActivity(intent);
            }
        }.start();
    }
}
