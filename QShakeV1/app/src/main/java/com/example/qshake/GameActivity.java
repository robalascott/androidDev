package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * GameActivity
 * Main Class for gameplay
 * Dependents: ShakeListener, GameEngine & SharedPreference;
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.example.qshake.GameEngine.CallBack;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements CallBack {
	private ImageView playerImage;
	private RelativeLayout layout;
	private Bitmap[] test = new Bitmap[8];
	private TextView playerQuestion, playerName;
	private ImageButton starImage;
	private Vibrator vibe;
	private ShakeListener mShaker;
	private GameEngine engine;
	private SharedPreference prefs;
	private ColorDrawable colorDrawable;
	private static int MAX_SIZE = 7;
	/* Select things */
	private AlertDialog dialog;
	private String tempString = null;
	private String userprofile ="user_profile.bmp";
    private Question tempQuestion = new Question();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuInit();
		setContentView(R.layout.activity_game);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		prefs = new SharedPreference();
		this.engine = new GameEngine(this,
				prefs.collectPlayers(getApplicationContext()), this,prefs.getGameType(getApplicationContext()),prefs.getQuestionType(getApplicationContext()));
		vibeInit();
		screenSize();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        tempQuestion.setStar(0);
        setButon(tempQuestion);
	}
    /*Populates bitmap with correct size and inits*/
	public void screenSize() {
		InputStream open = null;
		AssetManager manager = getAssets();
		try {

			int size = returnSize();
			Log.i("GameScreen",Integer.toString(engine.getPlayerSize()));
			for(int x = 0;x<engine.getPlayerSize();x++ )
			{
				Bitmap bitmap = null;
				String temp = engine.getPlayerPicID(x);
				if(temp.compareTo(userprofile)==0 || x == 0){
					Log.i("GameScreen",Integer.toString(size));
					open = manager.open(userprofile);
					bitmap = BitmapFactory.decodeStream(open);
					bitmap = getResizedBitmap(bitmap, size, size);
					test[x]= bitmap;
				}else{
					bitmap = BitmapFactory.decodeFile(getOutputMediaFileType(engine.getPlayerPicID(x)));
					bitmap = getResizedBitmap(bitmap, size, size);
					test[x]= bitmap;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		starImage = (ImageButton) findViewById(R.id.star);
		Drawable t = starImage.getBackground();
		int x = android.R.drawable.star_off;
		starImage.setBackgroundResource(x);
		playerImage = (ImageView) findViewById(R.id.player_image);
		playerImage.setImageBitmap(test[0]);
		playerName = (TextView) findViewById(R.id.player_name);
		playerQuestion = (TextView) findViewById(R.id.quest);
		layout = (RelativeLayout)findViewById(R.id.game_layout);
	}

	/* CallBack for messages */
	public void ErrorMessage(String error) {
		Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onResume() {
		mShaker.resume();
		super.onResume();
	}

	protected void onStop() {
		super.onStop();
		prefs.savePlayers(getApplicationContext(), engine.getPlayer());
        prefs.setGameRules(getApplicationContext(), engine.getGameType());
		prefs.setQuestionRules(getApplicationContext(), engine.getQType());
	}

	@Override
	public void onPause() {
		mShaker.pause();
		prefs.savePlayers(getApplicationContext(), engine.getPlayer());
        prefs.setGameRules(getApplicationContext(), engine.getGameType());
        prefs.setQuestionRules(getApplicationContext(), engine.getQType());
		super.onPause();
	}


	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.typeBtn:
			engine.selectGameType(dialog);
			prefs.setGameRules(getApplicationContext(), engine.gameType);
            return true;
		case R.id.filterbtn:
			engine.setQuestions(dialog);
            prefs.setGameRules(getApplicationContext(), engine.getGameType());
            return true;
		case R.id.addplayer:
			if (engine.getPlayerSize() > MAX_SIZE) {
				this.ErrorMessage("Max of 6 Players");
			} else {
				prefs.savePlayers(getApplicationContext(), engine.getPlayer());
				prefs.setGameRules(getApplicationContext(), engine.getGameType());
				prefs.setQuestionRules(getApplicationContext(), engine.getQType());
				Intent intent = new Intent(this, NewUserActivity.class);
				startActivityForResult(intent, 0);
			}
            return true;
		case R.id.removeplayer:
            engine.removeUser(dialog);
			engine.setPlayerAll(prefs.collectPlayers(getApplicationContext()));
			screenSize();
            return true;    
		}
		
		return false;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			engine.setPlayerAll(prefs.collectPlayers(getApplicationContext()));
			screenSize();
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void menuInit() {
		colorDrawable = new ColorDrawable(Color.parseColor("#267058"));
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Game");
		actionBar.setBackgroundDrawable(colorDrawable);
		getActionBar().setDisplayShowHomeEnabled(false);
	}

	public void vibeInit() {
		vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(100);
                mShaker.pause();
                Players temp = engine.turnResolve();
                playerName.setText("Player: " + temp.getName());
                playerImage.setImageBitmap(test[engine.getPos(temp.getName())]);
                mShaker.resume();
            }
        });
	}

	@Override
	public void methodToCallBack(Question q1) {
	    if(q1 == null){
            engine.setDataSort();
        }else{
             tempQuestion = q1;
             playerQuestion.setText(tempQuestion.getQuestion() + "\n" + tempQuestion.getType());
             setBackground(tempQuestion.getType(), tempQuestion.getStar());
             setButon(tempQuestion);
      }
    }
    /*Background colours handler*/
	private void setBackground(String str, int x){
		String temp;
		switch(str.charAt(0)){
			case 'G':/*general*/
				layout.setBackgroundColor(Color.parseColor("#025763"));
				break;
			case 'W':/*Work*/
				layout.setBackgroundColor(Color.parseColor("#2E1487"));
				break;
			case 'R':/*Relationships*/
				layout.setBackgroundColor(Color.parseColor("#BC071C"));
				break;

		}
        if(x == 1){
            /*Favourites*/
            temp = "#FFB735";
            layout.setBackgroundColor(Color.parseColor(temp));
        }

	}

    /*Resizing tool for Images*/
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;

	}
	/* http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels*/
	public int returnSize() {
		int measuredWidth = 0;
		int measuredHeight = 0;
		WindowManager w = getWindowManager();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			w.getDefaultDisplay().getSize(size);
			measuredWidth = size.x;
			measuredHeight = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			measuredWidth = d.getWidth();
			measuredHeight = d.getHeight();
		}
		int bestSize = Math.max(measuredHeight,measuredWidth);
		Log.i("Game", Integer.toString(bestSize));
		if (bestSize <= 1280 &&  bestSize > 960 ) {
			return 300;
		}else if (bestSize <= 960 &&  bestSize > 640 ) {
			return 200;
		} else if (bestSize <= 640 && bestSize >= 470 ){
			return 150 ;
		}else if (bestSize <= 470 &&bestSize >= 320){
			return 120;
		}
		return 112;
	}

	private String getOutputMediaFileType(String str){
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
				+ "/Android/data/"
				+ getApplicationContext().getPackageName()
				+ "/Files");
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				return null;
			}
		}
		String temp = Environment.getExternalStorageDirectory()
				+ "/Android/data/"
				+ getApplicationContext().getPackageName()
				+ "/Files"+File.separator + str;
		return temp;
	}

	public void clickStartype(View view){
        if(tempQuestion.getStar() == 0){
            tempQuestion.setStar(1);
            engine.updateDb(tempQuestion, 1);
        }else{
            tempQuestion.setStar(0);
            engine.updateDb(tempQuestion, 0);
        }
        setButon(tempQuestion);
	}

    /*Enable/disable  Starbutton*/
    public void setButon(Question q1){
        if(q1.getStar()==0){
            Drawable t = starImage.getBackground();
            starImage.setBackgroundResource(android.R.drawable.star_off);
        }else{
            Drawable t = starImage.getBackground();
            starImage.setBackgroundResource(android.R.drawable.star_on);
        }
    }
}
