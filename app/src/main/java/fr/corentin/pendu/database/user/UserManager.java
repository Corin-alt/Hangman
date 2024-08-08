package fr.corentin.pendu.database.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

import fr.corentin.pendu.database.object.MySQLite;

/**
 * Class {@link UserManager}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class UserManager implements Serializable {

    private static final String TABLE_NAME = "user";

    private static final String KEY_ID_USER = "usr_id";
    private static final String KEY_EMAIL_USER = "usr_email";
    private static final String KEY_PSEUDO_USER ="usr_pseudo";
    private static final String KEY_PASSWORD_USER = "usr_password";
    private static final String KEY_WIN_USER = "usr_win";
    private static final String KEY_LOSE_USER = "usr_lose";

    public static String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_EMAIL_USER + " VARCHAR, "
            + KEY_PSEUDO_USER + " TEXT, "
            + KEY_PASSWORD_USER + " VARCHAR, "
            + KEY_WIN_USER + " BIGINT, "
            + KEY_LOSE_USER + " BIGINT)";

    private final MySQLite dbHelper;
    private SQLiteDatabase database;

    public UserManager(Context context) {
        this.dbHelper = new MySQLite(context);
    }

    public void open() throws SQLException {
        this.database = dbHelper.getWritableDatabase();
        this.dbHelper.onOpen(this.database);
    }

    public void close() {
        this.dbHelper.close();
    }

    public void addUser(User user){
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL_USER, user.getEmail());
        values.put(KEY_PSEUDO_USER, user.getPseudo());
        values.put(KEY_PASSWORD_USER, user.getPassword());
        values.put(KEY_WIN_USER, user.getWin());

        this.database.insert(TABLE_NAME, null, values);
    }

    public User getUser(int id){
        User u = new User(0, "", "", "",0, 0);
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID_USER + "=" + id, null);
        if(c.moveToFirst()){
            u.setId(c.getInt(c.getColumnIndex(KEY_ID_USER)));
            u.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL_USER)));
            u.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD_USER)));
            u.setPseudo(c.getString(c.getColumnIndex(KEY_PSEUDO_USER)));
            u.setWin(c.getInt(c.getColumnIndex(KEY_WIN_USER)));
            u.setLose(c.getInt(c.getColumnIndex(KEY_LOSE_USER)));
            c.close();
        }
        return u;
    }

    public int getUserId(String email){
        int id = 0;
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_EMAIL_USER + " = ?", new String[]{email});
        if(c.moveToFirst()){
            id = c.getInt(c.getColumnIndex(KEY_ID_USER));
            c.close();
        }
        return id;
    }

    public int getUserWin(String email){
        int nbWin = 0;
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_EMAIL_USER + " = ?", new String[]{email});
        if(c.moveToFirst()){
            nbWin = c.getInt(c.getColumnIndex(KEY_WIN_USER));
            c.close();
        }
        return nbWin;
    }

    public int getUserLose(String email){
        int nbLose = 0;
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_EMAIL_USER + " = ?", new String[]{email});
        if(c.moveToFirst()){
            nbLose = c.getInt(c.getColumnIndex(KEY_LOSE_USER));
            c.close();
        }
        return nbLose;
    }


    public void addWin(User user){
        ContentValues values = new ContentValues();
        values.put(KEY_WIN_USER , this.getUserWin(user.getEmail())+1);
        String where = KEY_ID_USER+" = ?";
        String[] whereArgs = {user.getId()+""};
        database.update(TABLE_NAME, values, where, whereArgs);
    }

    public void addLose(User user){
        ContentValues values = new ContentValues();
        values.put(KEY_LOSE_USER , this.getUserLose(user.getEmail())+1);
        String where = KEY_ID_USER+" = ?";
        String[] whereArgs = {user.getId()+""};
        database.update(TABLE_NAME, values, where, whereArgs);
    }



    public String getHashedPassword(String email){
        String hashedPassword = "";
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_EMAIL_USER + " = ?" , new String[]{email});
        if(c.moveToFirst()){
            hashedPassword = c.getString(c.getColumnIndex(KEY_PASSWORD_USER));
            c.close();
        }
        return hashedPassword;
    }

    public Cursor getUsers() {
        return database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    public boolean exist(String userEmail){
        Cursor cursor = database.rawQuery("SELECT " + KEY_EMAIL_USER + " FROM " + TABLE_NAME, null);
        String existEmail;
        if (cursor.moveToFirst()) {
            do {
                existEmail = cursor.getString(cursor.getColumnIndex(KEY_EMAIL_USER));
                if (existEmail.equals(userEmail)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }
}
