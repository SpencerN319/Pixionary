package SaveData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sb_3.pixionary.Utilities.POJO.User;

/**
 * Created by fastn on 2/15/2018.
 */

public final class UserDataDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserData.db";
    private static final String TABLE_NAME = "User";
    private static final String KEY_GET = "get";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GAMES_PLAYED = "games_played";
    private static final String KEY_SCORE = "score";
    private static final String KEY_CATEGORY_COUNT = "category_count";
    private static final String KEY_IMAGE_COUNT = "image_count";
    private static final String KEY_USERTYPE = "user_type";
    private static final String[] COLUMNS = { KEY_GET, KEY_USERNAME, KEY_PASSWORD, KEY_ID, KEY_USERTYPE, KEY_GAMES_PLAYED, KEY_SCORE, KEY_CATEGORY_COUNT, KEY_IMAGE_COUNT };

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_GET + " TEXT," +
            KEY_USERNAME + " TEXT," +
            KEY_PASSWORD + " TEXT," +
            KEY_ID + " TEXT," +
            KEY_USERTYPE + " TEXT," +
            KEY_GAMES_PLAYED + " TEXT," +
            KEY_SCORE + " TEXT," +
            KEY_CATEGORY_COUNT + " TEXT," +
            KEY_IMAGE_COUNT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public UserDataDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        this.onCreate(db);
    }

    public void deleteOne(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(user.getId())});
        db.close();
    }

    public void deleteOne(int get) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "get = ?", new String[] { String.valueOf(get)});
        db.close();
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GET, 0);
        contentValues.put(KEY_ID, user.getId());
        contentValues.put(KEY_USERNAME, user.getUsername());
        contentValues.put(KEY_PASSWORD, user.getPassword());
        contentValues.put(KEY_USERTYPE, user.getUserType());
        contentValues.put(KEY_GAMES_PLAYED, user.getGamesPlayed());
        contentValues.put(KEY_SCORE, user.getScore());
        contentValues.put(KEY_CATEGORY_COUNT, user.getCategoryCount());
        contentValues.put(KEY_IMAGE_COUNT, user.getImageCount());

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
    public User getUserById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " id = ?",
                new String[] { String.valueOf(id)}, null,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            User user = new User(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4),Integer.parseInt(cursor.getString(5)),
                    Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
                    Integer.parseInt(cursor.getString(8)));
            return user;
        }
        return null;

    }

    public User getUser(String get) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " get = ?",
                new String[] { String.valueOf(get)}, null,
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            User user = new User(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4),Integer.parseInt(cursor.getString(5)),
                    Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
                    Integer.parseInt(cursor.getString(8)));
            return user;
        }
        return null;

    }

    public User getUserByName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " username = ?",
                new String[] { String.valueOf(username)}, null, null,
                null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        User user = new User(cursor.getString(1),
                cursor.getString(2), cursor.getString(3),
                cursor.getString(4),Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
                Integer.parseInt(cursor.getString(8)));
        return user;
    }

    //Update.
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USERNAME, user.getUsername());
        contentValues.put(KEY_PASSWORD, user.getPassword());
        contentValues.put(KEY_USERTYPE, user.getUserType());

        int i = db.update(TABLE_NAME, contentValues, "id = ?",
                new String[] { String.valueOf(user.getId())});

        db.close();
        return i;
    }
}
