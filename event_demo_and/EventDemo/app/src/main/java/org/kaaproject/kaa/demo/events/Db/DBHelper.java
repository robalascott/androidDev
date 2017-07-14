package org.kaaproject.kaa.demo.events.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import org.apache.avro.generic.GenericData;
import org.kaaproject.kaa.demo.events.FSM.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

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
    @ColorInt
    private static final int COLOR_OTHER = 0xff004600;


    public DBHelper(Context context, String DATABASE_NAME){
        super(context,DATABASE_NAME,null,12);
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
        Log.i(Constant.FSMLOG,"Clear");
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
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, COLLECTION_TABLE_NAME);
        return numRows;
    }
    public TextView getAllDiff(TextView mTextChatLogs) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + COLLECTION_TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            String x  =(res.getString(1)+ " diff "+ res.getString(4) + " Devices " + res.getString(5));
            final SpannableString coloredEntry = new SpannableString("\n" + x);
            coloredEntry.setSpan(new ForegroundColorSpan(COLOR_OTHER),
                    1, x.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mTextChatLogs.append(coloredEntry);
            res.moveToNext();
        }
        mTextChatLogs.append("\n" +this.numberOfRows() + "Number of messages");
        return mTextChatLogs;
    }

    public ArrayList<DataType> getAllDiff(ArrayList<DataType> e ) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + COLLECTION_TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            //es.getInt(5) for devices
            DataType temp = new DataType(res.getInt(5),res.getInt(4));
            e.add(temp);
            res.moveToNext();
        }
        return e;
    }
}
