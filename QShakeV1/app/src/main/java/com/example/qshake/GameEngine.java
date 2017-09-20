package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * GameEngine
 * Data Class for gameplay
 * Dependents: Shakeit;
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

public class GameEngine {

	private List<Players> player = new ArrayList<Players>();
	private Context context;
	private int highest;
	private StackIt stack;
	public CallBack callback;
	/**/
	final CharSequence[] gameTypes = { " Normal Random ", " Round Robin " };
	final public CharSequence[] questionTypes = { " General ", " Work ",
			" Relationships ", " Favourite " };
	public boolean gameType;
	private boolean[] questionType = new boolean[4];
    private Question tempholder;


	interface CallBack {
	    void methodToCallBack(Question q1);
	}
	
	public GameEngine(Context con, List<Players> player,CallBack call,boolean gT, boolean[] qT) {
		this.context = con;
		this.setPlayer(player);
		this.callback = call;
		this.gameType = gT;
		this.questionType = qT;
		stack = new StackIt(con);
	    onResume();
	}

	public int getPlayerSize(){
		return player.size();
	}
	public void setPlayerAll(List<Players> player1){
		this.player = player1;
	}

	public String getPlayerPicID(int pos){
		return player.get(pos).getPicID();
	}
    public void onResume(){
        highest = getHighestValue(player);
        stack.dataSort(questionType);
        tempholder = new Question();
        tempholder = stack.pop();
    }

    public void setDataSort(){
        stack.dataSort(questionType);
    }

	public int getPos(String string){
		for(int x = 0;x< player.size();x++){
			if(string.compareTo(player.get(x).getName())==0){
				return x;
			}
		}
		return 0;
	}

	public boolean getGameType(){
		return this.gameType;
	}
	public boolean[] getQType(){
		return this.questionType;
	}
	/* Messages */
	public void ErrorMessage(String error) {
		Toast.makeText(context, error, Toast.LENGTH_LONG).show();
	}

	public List<Players> getPlayer() {
		return player;
	}

	public void setPlayer(List<Players> player) {
		this.player = player;
	}
    /*Returns Highest value for turns*/
	public int getHighestValue(List<Players> player) {
		int max = 0;
		for (int i = 0; i < player.size(); i++) {
			if (player.get(i).getTurn() > max) {
				max = player.get(i).getTurn();
			}
		}
		if(max == 0){
			return 1;
		}else{
			return max;
		}
	}

	public Players turnResolve() {
		int size = player.size();
		Log.i("randsize", Integer.toString(size));
		Random rand = new Random();
		int randnumber = 0;
		if(!gameType){
			/*Round Robin*/
            int counter = 0;
            for(int x = 1;x < size;x++){
                if(highest == player.get(x).getTurn()){counter++;}
            }
            if(counter== size-1){highest++;
                tempholder=stack.pop();
            }
            do {
                randnumber = rand.nextInt(size);
                Log.i("rand-RoundRobin", Integer.toString(randnumber));
            } while (highest == player.get(randnumber).getTurn() || randnumber == 0);
            player.get(randnumber).setTurn(highest);
            callback.methodToCallBack(tempholder);
            return player.get(randnumber);
		}else{
			do {
				randnumber = rand.nextInt(size);
				Log.i("rand-Standard ", Integer.toString(randnumber));
			} while (highest == player.get(randnumber).getTurn() || randnumber == 0);
			highest++;
			player.get(randnumber).setTurn(highest);
			callback.methodToCallBack(stack.pop());
			return player.get(randnumber);
		}

	}
	public void setQuestions(AlertDialog dialog) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Select The Question Types");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }  );
                  builder.setMultiChoiceItems(questionTypes, questionType,
                          new DialogInterface.OnMultiChoiceClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog,
                                                  int indexSelected, boolean isChecked) {
                                  /*I am not pround of this line of code*/
                                  questionType[indexSelected] = isChecked;
                              }
                          }).setPositiveButton("OK",
                          new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int id) {
                                  stack.dataSort(questionType);
                              }
                          });

		dialog = builder.create();
		dialog.show();
	}
	public void updateDb(Question tempQuestion, int x) {
        stack.updateQuestion(tempQuestion,x);
	}


	public void selectGameType(AlertDialog dialog) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		int holder = 0;
		builder.setTitle("Select Game Type");
		if (gameType == false) {
			holder = 1;
		}
		builder.setSingleChoiceItems(gameTypes, holder,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						/*I am not pround of this line of code*/
                        if (which == 0) {
							gameType = true;
						} else {
                            tempholder = stack.pop();
							gameType = false;
						}
					}
				}).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
		dialog = builder.create();
		dialog.show();

	}
    public boolean removeUser(AlertDialog dialog) {
        List<String> user = new ArrayList<String>();
        if(player.size()<=3){
            ErrorMessage("At least two players needed to play");
            return false;
        }
        for(int i = 1; i<player.size();i++){
            user.add(player.get(i).getName());
        }
        final CharSequence[] listUser = user.toArray(new String[user.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems( listUser,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*I am not pround of this line of code*/
                        player.remove(which+1);
                    }
                }).setPositiveButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        dialog = builder.create();
        dialog.show();
        return true;
    }
}
