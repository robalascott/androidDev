package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * NewGameEngine
 * Data Input Class
 */
import java.util.ArrayList;
import java.util.List;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewGameActivity extends ListActivity {
	private ColorDrawable colorDrawable;
	private List<Players> player = new ArrayList<Players>();
	private PlayerListAdapter adapter;
	private ListView list;
	private SharedPreference prefs;
	private static int MAX_SIZE = 7;
	private static final int REQUEST_CODE = 1;
	private ImageButton filterBtn, gameBtn;
	private TextView filterText, gameText, filter, game;
	/* Select things */
	private AlertDialog dialog;
	final CharSequence[] gameTypes = { " Normal Random ", " Round Robin " };
	final CharSequence[] questionTypes = { " General ", " Work ",
			" Relationships ", " Favourite " };
	final ArrayList<CharSequence> seletedItems = new ArrayList<CharSequence>();
	private boolean gameType;
	private boolean[] questionType = new boolean[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuInit();
		setContentView(R.layout.activity_new_game);
		ui();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//createPlaceholder();
		updateAdapter(player);
		prefs = new SharedPreference();
	}

	public void ui() {
		list = (ListView) findViewById(android.R.id.list);
		filterBtn = (ImageButton) findViewById(R.id.filter);
		filterBtn.setBackgroundResource(android.R.drawable.ic_menu_add);
		filterText = (TextView) findViewById(R.id.filter_name);
		gameBtn = (ImageButton) findViewById(R.id.gametype);
		gameBtn.setBackgroundResource(android.R.drawable.ic_menu_add);
		filterText = (TextView) findViewById(R.id.gametype_name);
		filter = (TextView) findViewById(R.id.fselect);
		game = (TextView) findViewById(R.id.fgame);
	}

	public void menuInit() {
		colorDrawable = new ColorDrawable(Color.parseColor("#267058"));
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Game Setup");
		actionBar.setBackgroundDrawable(colorDrawable);
		getActionBar().setDisplayShowHomeEnabled(false);
	}

	protected void onStart() {
		super.onStart();
		player.addAll(prefs.collectPlayers(getApplicationContext()));
		updateAdapter(player);
		adapter.notifyDataSetChanged();
		gameType = prefs.getGameType(getApplicationContext());
		questionType = prefs.getQuestionType(getApplicationContext());
		game.setText(getGameType());
		filter.setText(getQuestionType(questionType));
	}

	protected void onStop() {
		super.onStop();
		prefs.savePlayers(getApplicationContext(), player);
		prefs.setGameRules(getApplicationContext(), gameType);
		prefs.setQuestionRules(getApplicationContext(), questionType);
	}

	public void finish() {
		prefs.savePlayers(getApplicationContext(), player);
		prefs.setGameRules(getApplicationContext(), gameType);
		prefs.setQuestionRules(getApplicationContext(), questionType);
		super.finish();
	}

	private void updateAdapter(List<Players> alarm) {
		adapter = new PlayerListAdapter(this, alarm);
		list.setAdapter(adapter);
	}

	private void createPlaceholder() {
		Players tempPlayer = new Players("CREATE NEW", "Player");
		player.add(tempPlayer);
	}

	/* CallBack for messages */
	public void ErrorMessage(String error) {
		Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG)
				.show();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		if (position == 0) {
			if (player.size() > MAX_SIZE) {
				this.ErrorMessage("Max of 6 Players");
			} else {
				prefs.savePlayers(getApplicationContext(), player);
				prefs.setGameRules(getApplicationContext(), gameType);
				prefs.setQuestionRules(getApplicationContext(), questionType);

				Intent intent = new Intent(this, NewUserActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}

		} else if (position < player.size()) {
			selectUserRemove(player.get(position).getName(), position);
		}
	}

	private void insert(String a1, String b1) {
		Players tempPlayer = new Players(a1, b1);
		player.add(tempPlayer);
		adapter.notifyDataSetChanged();
	}

	public void clickGametype(View v) {
		switch (v.getId()) {
		case R.id.gametype:
			selectGameType();
			break;
		case R.id.filter:
			setQuestions(questionType);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			player.addAll(prefs.collectPlayers(getApplicationContext()));
			updateAdapter(player);
			adapter.notifyDataSetChanged();
		}
	}
	private void setQuestions(boolean[] qType) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select The Question Types");
		builder.setMultiChoiceItems(questionTypes, qType,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
										int indexSelected, boolean isChecked) {
						Log.i("Ckeck", String.valueOf(isChecked));
						questionType[indexSelected] = isChecked;
					}
				}).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						prefs.setQuestionRules(getApplicationContext(), questionType);
						filter.setText(getQuestionType(questionType));
					}
				});

		dialog = builder.create();
		dialog.show();
	}

	private void selectGameType() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		int holder = 0;
		builder.setTitle("Select Game Type");
		if (gameType == false) {
			holder = 1;
		}
		builder.setSingleChoiceItems(gameTypes, holder,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							gameType = true;
						} else {
							gameType = false;
						}
					}
				}).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						prefs.setGameRules(getApplicationContext(), gameType);
						game.setText(getGameType());
					}
				});
		dialog = builder.create();
		dialog.show();
	}
	private void selectUserRemove(String str,final int pos) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Remove User " + str);
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				player.remove(pos);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}

		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}
	public String getQuestionType(boolean[] list) {
		StringBuilder x = new StringBuilder();
		int x1 = 0;
		int v = 0;
		String str = " ";
		do {
			if (list[x1]) {
				x.append(questionTypes[x1]);
				x.append(str);
			} else {
				v++;
			}
			x1++;
		} while (x1 < list.length && x1 < questionTypes.length);
		if (v == list.length) {
			return "None";
		}

		return x.toString();
	}
	public CharSequence getGameType() {
		/* If true normal Randomn else Round Robin */
		if (gameType) {
			return gameTypes[0];
		}
		return gameTypes[1];
	}

}