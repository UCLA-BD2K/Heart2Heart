package bd2k.heart2heart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    Button beginRun;
    Button history;
    Button profileInfo;
    ImageView logoView;
    TextView welcomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoView = (ImageView) findViewById(R.id.imageView);
        logoView.setAlpha(50);
        welcomeView = (TextView) findViewById(R.id.textView);
        welcomeView.bringToFront();
        addListenerOnButtons();
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

        beginRun = (Button) findViewById(R.id.button);
        history = (Button) findViewById(R.id.button2);
        profileInfo = (Button) findViewById(R.id.button3);

        beginRun.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, RunSettings.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, History.class);
                startActivity(intent);
            }
        });

        profileInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, ProfileInfo.class);
                startActivity(intent);
            }
        });

    }

}
