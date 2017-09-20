package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * Stackit
 * Database Handler and Question Stack Class
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class StackIt extends SQLiteOpenHelper{
	/*Database info*/
	private static final String DATABASE_NAME = "questions.db";
	private static final int DATABASE_VERSION = 7;
	/*Table Stuff*/
	public static final String TABLE_QUESTION = "questiontable";
	public static final String QUESTION_ID = "id";
	public static final String COLUMN_QUESTION = "question";
	public static final String COLUMN_TYPE = "type";
    //Integers 0 (false) and 1 (true)
	public static final String COLUMN_STAR = "star";
	public static final String COLUMN_USER = "user";
	private ArrayList<Question> list;
	private SQLiteDatabase database;
	private Context con;
	private static final String DATABASE_CREATE = "create table "
		      + TABLE_QUESTION  + "(" + QUESTION_ID
		      + " integer primary key autoincrement, " + COLUMN_QUESTION 
		      + " text not null, " +  COLUMN_TYPE + " integer not null, " 
		      + COLUMN_STAR + " integer not null, "+ COLUMN_USER + " integer not null );";

	public StackIt(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		list = new ArrayList<Question>();
		//loadFile("g1.txt");
		//loadFile("w1.txt");
		//loadFile("r1.txt");
	}
    /*Sorts Selected Types*/
	public void dataSort(boolean[] items){
		list.clear();
		for(int x = 0 ;x <items.length;x++){
		//	Log.i("Stackit",String.valueOf(items[x]));
			if(items[x] == true){
				if(x ==0){getSelectedAll("General");}
				if(x ==1){getSelectedAll("Work");}
				if(x ==2){getSelectedAll("Relationships");}
				if(x ==3){getSelectedStar(1);}
			}
		}
		if(list.isEmpty()){
			Log.i("Stackit","isEmpty");
			getQuestionAll();
		}
        randomnList();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public void push(Question q1) {
		list.add(q1);
	}

	public Question pop() {
		if (isEmpty()) {
			return null;
		} else {
			Question q1 = list.get(0);
			list.remove(0);
			return q1;
		}
	}

	public void randomnList() {
		int size = list.size();
		Random rand = new Random();
		for (int x = 0; x < 100; x++) {
			int randnumber = rand.nextInt(size);
			Question q1 = pop();
			list.add(randnumber, q1);
		}
	}
    /*Creates Input*/
	public void createQuestion(Question q1) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_QUESTION, q1.getQuestion());
		values.put(COLUMN_TYPE, q1.getType());
		values.put(COLUMN_STAR, 0);
		values.put(COLUMN_USER, q1.getStar());
		db.insert(TABLE_QUESTION, null, values);
		db.close();
	}
    /*Gets All Database inputs*/
	public void getQuestionAll() {		
		String getAll = "SELECT * FROM " + TABLE_QUESTION + ";" ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(getAll, null);
		
		if(cursor.moveToFirst()){
			do{
				Question temp = new Question() ;
				temp.setId(cursor.getInt(0));
				temp.setQuestion(cursor.getString(1));
				temp.setType(cursor.getString(2));
				temp.setStar(cursor.getInt(3));
				this.push(temp);;
			//	Log.i(DATABASE_NAME, cursor.getString(1) + cursor.getString(2));
			}while(cursor.moveToNext());
		}
		cursor.close();
		db.close();
		db.close();
	}
    /*Search Custom type*/
	public void getSelectedAll(String str) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] cols= {QUESTION_ID,COLUMN_QUESTION,COLUMN_TYPE,COLUMN_STAR};
		Cursor cursor = db.query(TABLE_QUESTION, cols, COLUMN_TYPE + " = '" + str + "'", null, null, null, null);
		if(cursor== null){
			//Log.i(DATABASE_NAME, "NUll");
		}
		if(cursor.moveToFirst()){
			do{
				Question temp = new Question() ;
				
				temp.setId(cursor.getInt(0));
				temp.setQuestion(cursor.getString(1));
				temp.setType(cursor.getString(2));
				temp.setStar(cursor.getInt(3));
			//	Log.i(DATABASE_NAME, cursor.getString(1) + " " + cursor.getString(2));
				
				this.push(temp);;
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		db.close();
		db.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
		onCreate(db);
	}
    /*Creates inputs for database*/
	public void loadFile(String str){
		try {
			 File sdcard = Environment.getExternalStorageDirectory();
		     File file = new File(sdcard,str);

		     BufferedReader br = new BufferedReader(new FileReader(file));  
		     String line;   
		     String type = br.readLine();
		     Question de1 = new Question();
		     de1.setType(type);
		     while ((line = br.readLine()) != null) {
		              de1.setQuestion(line);
				      de1.setUser(0);
		              createQuestion(de1);
		     }
		     br.close() ;
		 }catch (IOException e) {
		    e.printStackTrace();           
		 }
	}
    /*Check for double inputs*/
    public boolean checkSelectedAll(String str) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols= {QUESTION_ID,COLUMN_QUESTION,COLUMN_TYPE,COLUMN_STAR};
        Cursor cursor = db.query(TABLE_QUESTION, cols, COLUMN_USER  + " = '" + 0 + "'", null, null, null, null);
        if(cursor== null){
            cursor.close();
            db.close();
            return false;
        }

        if(cursor.moveToFirst()){
            do{
                Question temp = new Question() ;
                if(str.compareTo(cursor.getString(1))==0){
                    cursor.close();
                    db.close();
                    db.close();
                    return true;
                }

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return false;

    }
    /*Search Favourite type*/
    public void getSelectedStar(int x) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols= {QUESTION_ID,COLUMN_QUESTION,COLUMN_TYPE,COLUMN_STAR};
        Cursor cursor = db.query(TABLE_QUESTION, cols, COLUMN_STAR + " = '" + x + "'", null, null, null, null);
        if(cursor== null){
           // Log.i(DATABASE_NAME, "NUll");
        }
        if(cursor.moveToFirst()){
            do{
                Question temp = new Question() ;
                temp.setId(cursor.getInt(0));
                temp.setQuestion(cursor.getString(1));
                temp.setType(cursor.getString(2));
                temp.setStar(cursor.getInt(3));
                this.push(temp);;

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        db.close();
    }


	/* Updating single contact*/
	public int updateQuestion(Question q1,int x ) {
		SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, q1.getQuestion());
        values.put(COLUMN_TYPE, q1.getType());
        values.put(COLUMN_STAR, x);
        values.put(COLUMN_USER, q1.getStar());
		// updating row
		return db.update(TABLE_QUESTION, values, QUESTION_ID + " = ?",
				new String[] { String.valueOf(q1.getId()) });
	}
    /*Placeholder for saftey*/
    public Question placeHolder(){
        Question q1 = new Question();
        q1.setId(0);
        q1.setType("General");
        q1.setStar(1);
        q1.setQuestion("End of List");
        return q1;
    }
}
