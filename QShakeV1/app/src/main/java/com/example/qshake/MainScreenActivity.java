package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * Main Screen Activity
 * Start point for app
 */

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainScreenActivity extends Activity {
	private Button newGame,conGame,exitGame,createGame;
	private static final int REQUEST_CODE = 0;
	private SharedPreference prefs;
	String colour = "#D10D0D";
	private ColorDrawable colorDrawable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		createUI();
		menuInit();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		prefs = new SharedPreference();

	}

	private void createUI() {
		newGame = (Button) findViewById(R.id.newGameBT);
		conGame = (Button) findViewById(R.id.conGameBT);
		exitGame  = (Button) findViewById(R.id.exitGameBT);
		createGame = (Button)findViewById(R.id.createGameBT);
	}

	protected void onStart() {
		super.onStart();
		ConButton();
	}
	public void menuInit() {
		colorDrawable = new ColorDrawable(Color.parseColor("#267058"));
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Main Screen");
		actionBar.setBackgroundDrawable(colorDrawable);
		getActionBar().setDisplayShowHomeEnabled(false);
	}
	protected void onRestart() {
		super.onRestart();
		ConButton();
	}
	protected void onStop() {
		super.onStop();

	}

	public void finish() {
		super.finish();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			
		}
		ConButton();
	}

	/* CallBack for messages */
	public void ErrorMessage(String error) {
		Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG)
				.show();
	}

	public void ConButton(){
		if(!prefs.getNumberofPlayers(getApplicationContext())){
			conGame.setClickable(false);
			conGame.setTextColor(Color.LTGRAY);
		}else{
			conGame.setClickable(true);
			conGame.setTextColor(Color.parseColor(colour));
		}
	}
	public void game(View view) {
		switch(view.getId()){
			case R.id.newGameBT:
				Intent intent = new Intent(this, NewGameActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
				break;
			case R.id.conGameBT:
				Intent intent1 = new Intent(this, GameActivity.class);
				startActivityForResult(intent1, REQUEST_CODE);
				break;
			case R.id.createGameBT:
				Intent intent2 = new Intent(this, OwnQuestionsActivity.class);
				startActivityForResult(intent2, REQUEST_CODE);
				break;
			case R.id.exitGameBT:
				this.finish();
				break;
		}
	}

}
