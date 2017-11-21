package sulijin.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    private Button sendButton;
    private EditText editText;
    private ListView listView;
    private ArrayList<String> chatMessageList = new ArrayList();
    private SQLiteDatabase writeableDB;
    private Boolean isLandscape;
    private FrameLayout landscapeFrameLayout;
    private Cursor cursor;
    private int requestCode = 1;
    private ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        sendButton = (Button)findViewById(R.id.sendButton);
        editText = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.chatView);
        landscapeFrameLayout = (FrameLayout) findViewById(R.id.landscapeFrameLayout);

        if(landscapeFrameLayout == null){
            isLandscape = false;
            Log.i(ACTIVITY_NAME, "The phone is on portrait layout.");

        }
        else {
            isLandscape = true;
            Log.i(ACTIVITY_NAME, "The phone is on landscape layout.");
        }

        final ChatAdapter messageAdapter =new ChatAdapter( this );
        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);
        writeableDB = chatDatabaseHelper.getWritableDatabase();
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String input = editText.getText().toString();
                chatMessageList.add(input);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");

                ContentValues newData = new ContentValues();
                newData.put(ChatDatabaseHelper.KEY_MESSAGE, input);
                writeableDB.insert(ChatDatabaseHelper.TABLE_NAME, "" , newData);
            }
        });

       cursor = writeableDB.rawQuery("select * from lab5Table",null );
       final int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
       final Intent intent = new Intent(this, MessageDetailActivity.class);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String message = messageAdapter.getItem(position);
                long idInDb =  messageAdapter.getItemId(position);

                Bundle bundle = new Bundle();
                bundle.putLong("id",idInDb);
                bundle.putString("message", message);
                bundle.putBoolean("isLandscape", isLandscape);

                if(isLandscape == true){
                    MessageFragment messageFragment = new MessageFragment();

                    messageFragment.setArguments(bundle);
                    FragmentManager fragmentManager =getFragmentManager();
                    //remove previous fragment
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                        fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.landscapeFrameLayout, messageFragment).addToBackStack(null).commit();
                }
                else{
                    intent.putExtra("bundle", bundle);
                    startActivityForResult(intent, requestCode);
                }
            }
        });

        cursor.moveToFirst();//resets the iteration of results

        while(!cursor.isAfterLast() ) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:"
                    +cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            chatMessageList.add(cursor.getString(messageIndex));
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount() );

        for(int i = 0; i <cursor.getColumnCount();i++){
            System.out.println(cursor.getColumnName(i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && data != null) {
            Long id = data.getLongExtra("id", -1);
            writeableDB.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "=" + id, null);
        }
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){

            return chatMessageList.size();
        }
        public String getItem(int position){

            return chatMessageList.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

        public long getItemId(int position){
           cursor.moveToPosition(position);
           return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
           }

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        writeableDB.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}