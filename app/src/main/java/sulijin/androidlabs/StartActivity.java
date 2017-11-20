package sulijin.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
//    static final int PICK_ACTIVITY_REQUEST = 10;
//    static final int RESULT_OK = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        final Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this,ListItemActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        final Button button2 = (Button)findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Log.i(ACTIVITY_NAME,"User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this,ChatWindow.class);
                startActivity(intent);
            }
        });

        final Button weatherButton = (Button)findViewById(R.id.weatherButton);

        weatherButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Log.i(ACTIVITY_NAME,"User clicked Weather Forecast Button");
                Intent intent = new Intent(StartActivity.this,WeatherForecast.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data){
        if (requestCode == 10 && responseCode == Activity.RESULT_OK); {
            Log.i(ACTIVITY_NAME,"Returned to StartActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            CharSequence text = "ListItemActivity passed: ";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), text+messagePassed, duration); //this is the ListActivity
            toast.show();
        }
    }

    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }

    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart()");
    }

    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");

    }
}