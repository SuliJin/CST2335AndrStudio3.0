package sulijin.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Suli Jin on 11/19/2017.
 */

public class MessageFragment extends Fragment {
    private SQLiteDatabase writeableDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);
        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(getActivity());
        writeableDB = chatDatabaseHelper.getWritableDatabase();

        Bundle bundle = this.getArguments();
        final long id = bundle.getLong("id");
        String msg = bundle.getString("message");
        final boolean isLandscape = bundle.getBoolean("isLandscape");
        TextView idView = view.findViewById(R.id.fragmentId);
        idView.setText(""+id);
        TextView msgView = view.findViewById(R.id.fragmentMsg);
        msgView.setText(msg);

        Button del = view.findViewById(R.id.deleteButton);
        del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {
                if (isLandscape) {
                    writeableDB.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "=" + id, null);
                } else {
                    Intent ret = new Intent();
                    ret.putExtra("id", id);
                    getActivity().setResult(Activity.RESULT_OK, ret);
                    getActivity().finish();
                }
            }
        });
        return view;
    }
}