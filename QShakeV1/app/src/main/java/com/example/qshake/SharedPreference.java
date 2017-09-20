package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * SharedPreference class
 * Controller for Shared Prefs
 */
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPreference {
	public static final String PREFS_NAME = "player_prefs";
	public static final String GAME_TYPE = "game_type";
	public static final String PIC_TYPE = "pic_type";
	private final String imageString = "user_profile.bmp";
	public SharedPreference() {
		super();
	}
	/* Save PLayer*/
	public void savePlayers(Context context, List<Players> player) {
		SharedPreferences settings;
		Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		editor = settings.edit();
		editor.putInt("size", player.size());
		for (int i = 0; i < player.size(); i++) {
			editor.putString("playerName" + "_" + i, player.get(i).getName());
			editor.putString("playerID" + "_" + i, player.get(i).getPicID());
			editor.putInt("playerTurn" + "_" + i, player.get(i).getTurn());
		}
		editor.commit();
		Log.i("shared", "save");
	}

	/*Collect Player*/
	public List<Players> collectPlayers(Context context) {
		SharedPreferences settings;
		List<Players> player = new ArrayList<Players>();
		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		int size = settings.getInt("size", 0);
		Log.i("shared", Integer.toString(size));
		for (int i = 0; i < size; i++) {
			Players temp = new Players(settings.getString("playerName" + "_"+ i, null), settings.getString("playerID" + "_" + i, null),settings.getInt("playerTurn" + "_" + i, 0));
			Log.i("shared",temp.getName() + temp.getPicID());
			player.add(temp);
		}
		return player;
	}

	public void clearSharedPreference(Context context) {
		SharedPreferences settings;
		Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		editor = settings.edit();
		editor.clear();
		editor.commit();
	}
	/*Number of Players*/
	public boolean getNumberofPlayers(Context context){
		SharedPreferences settings;
		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		int size = settings.getInt("size", 0);
		if (size >2){
			return true;
		}else{
			return false;
		}
	}
	
	public void setGameRules(Context context,boolean gameType){
		SharedPreferences settings;
		Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		editor = settings.edit();
		editor.putBoolean(GAME_TYPE, gameType);
		editor.commit();

		Log.i("shared", "saveGAMETYPE");
	}
	public boolean getGameType(Context context){
		SharedPreferences settings;
		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		boolean x = settings.getBoolean(GAME_TYPE, true);
		return x;
	}
	public void setQuestionRules(Context context,boolean[] qType){
		SharedPreferences settings;
		Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		editor = settings.edit();
		int size = qType.length;
		for (int i = 0; i < size; i++) {
			editor.putBoolean("qType" + "_"+ i, qType[i]);
		}
		editor.commit();
	
	}
	public boolean[] getQuestionType(Context context){
		SharedPreferences settings;
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		int size = 4;
		boolean[] x = new boolean[4];
		for (int i = 0; i < size; i++) {
			 x[i] = settings.getBoolean("qType" + "_"+ i, true);
		}
		return x;
	}

	public void setImageType(Context context,String str) {
		SharedPreferences settings;
		Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		editor = settings.edit();
		editor.putString(PIC_TYPE, str);
		editor.commit();
		Log.i("shared", "saveIMAgeTYPE");
	}
	public String getImageType(Context context) {
		SharedPreferences settings;
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		String x = settings.getString(PIC_TYPE, imageString);;
		return x;

	}
}
