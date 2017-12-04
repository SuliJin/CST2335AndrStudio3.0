package sulijin.androidlabs;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    protected static final String AUTHOR_NAME ="SULI JIN";
    private String newMessage = "You selected item 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "replace with your action", Snackbar.LENGTH_LONG)
                        .setAction("action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_one:
                Log.d("Toolbar", "Choice 1 selected");
                Snackbar.make(findViewById(R.id.action_one),
                        newMessage, Snackbar.LENGTH_LONG)
                        .setAction(R.string.actionOne, null).show();
                break;
            //Start an activity…
            case R.id.action_two:
                Log.d("Toolbar", "Choice 2 selected");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Do you want to go back?");
                // Add the buttons
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent start = new Intent(TestToolbar.this, StartActivity.class);
                        startActivity(start);
                    }
                });
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                // Create the AlertDialog
                AlertDialog dialog1 = builder1.create();

                dialog1.show();

                break;
            //Start an activity…
            case R.id.action_three:
                Log.d("Toolbar", "Choice 3 selected");
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Set new message");
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog, null);
                builder2.setView(dialogView);
                // Add the buttons
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText text = (EditText) dialogView.findViewById(R.id.newMessageText);
                        newMessage = text.getText().toString();
                    }
                });
                builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                // Create the AlertDialog
                AlertDialog dialog2 = builder2.create();

                dialog2.show();
                break;

            //Start an activity…
            case R.id.about:
                Context context = getApplicationContext();
                CharSequence text = "Version 1.0 by " + AUTHOR_NAME;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
        }

        return true;
    }

}
