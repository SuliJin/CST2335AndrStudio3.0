package sulijin.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Suli Jin on 10/11/2017.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public final static String ACTIVITY_NAME = "ChatDatabaseHelper";
    public final static String DATABASE_NAME = "Lab5.db";
    public final static int VERSION_NUM = 3;
    public final static String KEY_ID = "_id";
    public final static String KEY_MESSAGE = "MESSAGE";
    public final static String TABLE_NAME = "lab5Table";

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MSG = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_MESSAGE + " TEXT )";
        db.execSQL(CREATE_TABLE_MSG);
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion= " + oldVer
                + " newVersion=" + newVer);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //delete what was there previously
        onCreate(db);
      //  Log.i("ChatDatabaseHelper", "Calling onCreate");
        Log.i("ChatDatabaseHelper", "Calling onDowngrade, newVersion=" + newVersion + "oldVersion=" + oldVersion);

    }
}