package fr.corentin.pendu.database.object;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import fr.corentin.pendu.database.user.UserManager;

/**
 * Class {@link MySQLite}
 * @author Corentin Dupont
 */

public class MySQLite extends SQLiteOpenHelper {

    private static final String DB_NAME = "pendu";
    private static final int DB_VERSION = 1;

    public MySQLite(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserManager.CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
