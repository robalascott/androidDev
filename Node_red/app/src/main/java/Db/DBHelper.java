package Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by robscott on 2017-06-27.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Collection.db";
    public static final String COLLECTION_TABLE_NAME = "Dbase";
    public static final String COLLECTION_COLUMN_ID = "id";
    public static final String COLLECTION_COLUMN_NAME = "name";
    public static final String COLLECTION_COLUMN_SENT = "sent";
    public static final String COLLECTION_COLUMN_REV = "rev";
    public static final String COLLECTION_COLUMN_DIFF = "diff";
    public static final String COLLECTION_COLUMN_DEV = "dev";
    private static final int VERSION = 1;

    public DBHelper(Context context, String DATABASE_NAME){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Dbase " +
                        "(id integer primary key, name text,sent int,rec int, diff int, dev int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ COLLECTION_TABLE_NAME);
        onCreate(db);
    }

    public void delete() {
        SQLiteDatabase db =  this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ COLLECTION_TABLE_NAME);
        Log.i("FSMLOG","Clear");
        db.close();
    }


    public boolean insertContact (String name, int sent, int rec, int diff,int dev ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("sent", sent);
        contentValues.put("rec", rec);
        contentValues.put("diff", diff);
        contentValues.put("dev", dev);
        db.insert(COLLECTION_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, COLLECTION_TABLE_NAME);
        return numRows;
    }





}
