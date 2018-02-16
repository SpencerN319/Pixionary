package SaveData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by fastn on 2/15/2018.
 */

public final class UserPassToDatabase {

    private UserPassToDatabase() {}

    public static class UserPassEntry implements BaseColumns {
        public static final String TABLE_NAME = "login";
        public static final String COLUMN_NAME_USER = "username";
        public static final String COLUMN_NAME_PASS = "password";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserPassEntry.TABLE_NAME + " (" +
            UserPassEntry.COLUMN_NAME_USER + " TEXT," +
            UserPassEntry.COLUMN_NAME_PASS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserPassEntry.TABLE_NAME;

    public class UserPassDbHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "userdata.db";

        public UserPassDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldV, int newV) {
            onUpgrade(db, oldV, newV);
        }
    }
}
