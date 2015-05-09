package bd2k.heart2heart;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hannah on 5/8/2015.
 */
public class ProfileInfo extends ActionBarActivity{

    String username;
    String name;
    String email;
    String phone;
    String sex;
    String age;

    TextView usernameView;
    TextView nameView;
    TextView phoneView;
    TextView emailView;
    TextView ageView;
    RadioButton maleButton;
    RadioButton femaleButton;
    RadioButton otherButton;

    Button saveButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);
        getStoredProfile();
        fillProfile();
        addListenerOnButtons();
    }

    public void getStoredProfile() {
        SharedPreferences profileSettings = getSharedPreferences("ProfileInfo", MODE_PRIVATE);
        username = profileSettings.getString("username", "Root");
        name = profileSettings.getString("name", "First and Last");
        email = profileSettings.getString("email", "example@test.com");
        phone = profileSettings.getString("phone", "1234567890");
        sex = profileSettings.getString("sex", "Other");
        age = profileSettings.getString("age", "123");
    }

    public void fillProfile() {
        usernameView = (TextView) findViewById(R.id.editText2);
        nameView = (TextView) findViewById(R.id.editText3);
        phoneView = (TextView) findViewById(R.id.editText6);
        emailView = (TextView) findViewById(R.id.editText5);
        ageView = (TextView) findViewById(R.id.editText);

        maleButton = (RadioButton) findViewById(R.id.radioButton);
        femaleButton = (RadioButton) findViewById(R.id.radioButton2);
        otherButton = (RadioButton) findViewById(R.id.radioButton3);

        switch(determineSex()) {
            case 1:
                maleButton.toggle();
                break;
            case 2:
                femaleButton.toggle();
                break;
            case 3:
                otherButton.toggle();
                break;
            default:
                maleButton.setChecked(false);
                femaleButton.setChecked(false);
                otherButton.setChecked(false);
                break;
        }

        usernameView.setText(username);
        nameView.setText(name);
        phoneView.setText(phone);
        emailView.setText(email);
        ageView.setText(age);
    }

    public int determineSex() {
        if(sex.equals("Male")) {
            return 1;
        } else if(sex.equals("Female")) {
            return 2;
        } else { // then "Other"
            return 3;
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

        saveButton = (Button) findViewById(R.id.button6);
        cancelButton = (Button) findViewById(R.id.button7);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                username = usernameView.getText().toString();
                name = nameView.getText().toString();
                email = emailView.getText().toString();
                phone = phoneView.getText().toString();
                age = ageView.getText().toString();
                if(maleButton.isChecked()) {
                    sex = "Male";
                } else if(femaleButton.isChecked()) {
                    sex = "Female";
                } else {
                    sex = "Other";
                }

                SharedPreferences profileSettings = getSharedPreferences("ProfileInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = profileSettings.edit();
                editor.putString("username", username);
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("phone", phone);
                editor.putString("age", age);
                editor.putString("sex", sex);

                editor.commit();

                Context context = getApplicationContext();
                CharSequence text = "Your profile has been saved!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
