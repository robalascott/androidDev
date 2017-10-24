package com.robalascott.todolist.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


enum enumFunctions{
    ADD,EDIT,DELETE,ALL;
}

public class MainActivity extends AppCompatActivity implements TaskInfomer{
    private RecyclerView recyclerView ;
    private TodoAdapter adapter;
    private List<TaskObject> dropListArray = new ArrayList<>();
    private final static String url =  "https://timesheet-1172.appspot.com/5730ac99/notes";
    private final String failed =  "404";
    private final String success =  "204";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlert(null);
            }
        });
        //placeHolder for List creating
        dropListArray.add(new TaskObject(1,"Wow","Such Empty"));

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        adapter = new TodoAdapter(dropListArray,this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClick(new TodoAdapter.ItemClickListener (){
            @Override
            public void onItemClick(View v, int pos,String style) {
                 //This is dirty add new interface or enums
                 if(style.contains(enumFunctions.EDIT.toString())) {
                     addAlert(dropListArray.get(pos));
                }else if(style.contains(enumFunctions.DELETE.toString())){
                     deleteEditAlert(dropListArray.get(pos).getId());
                }else{
                     makeToast("Error in Selection");
                 }
            }
        });
    }
    //lead-in function for Asynctasks
    private void taskFunction(enumFunctions functiontype, int pos, String[] value) {
       if(checkInternetConnection(this)){
           switch (functiontype){
               case ADD:new AllTask(this).execute(new String[]{functiontype.toString(),value[0],String.valueOf(pos),value[1],value[2]});break;
               case DELETE: new AllTask(this).execute(new String[]{functiontype.toString(),url,String.valueOf(pos)});break;
               case EDIT: new AllTask(this).execute(new String[]{functiontype.toString(),url,String.valueOf(pos),value[1],value[2]});break;
               case ALL: new AllTask(this).execute(new String[]{functiontype.toString(),url});break;
               default:makeToast("Something went wrong with selection:" + functiontype.toString());break;
           }
       }else{
           makeToast("No network currently");

       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            makeToast("A single click for edit and long click to remove items");
            return true;}
        else if(id == R.id.about_settings){
            makeToast("Made by Robert Scott");
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean deleteEditAlert(final int pos1){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Do you want to Delete this entry??");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        taskFunction(enumFunctions.DELETE,pos1,null);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        return false;
    }

    public boolean addAlert(final TaskObject object){
        LayoutInflater layout = LayoutInflater.from(this);
        View mView = layout.inflate(R.layout.layoutpromts,null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(mView);
        alertDialog.setTitle("Note");
        alertDialog.setMessage("Add a new Note");
        final EditText userTitle = (EditText) mView.findViewById(R.id.titleInput);
        final EditText userDescp = (EditText) mView.findViewById(R.id.descpInput);
        if(object!=null){
            userTitle.setText(object.getTitle());
            userDescp.setText(object.getDescription());
        }
        alertDialog
                .setCancelable(false)
                .setPositiveButton("add",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogBox, int id ){
                            if (userTitle.getText().length() != 0 || userDescp.getText().length() != 0) {
                                String[] temp = {url, userTitle.getText().toString(), userDescp.getText().toString()};
                                //PlaceHolder
                                new TaskObject(1,"Wow","Such Empty");
                                if (object==null){
                                    taskFunction(enumFunctions.ADD, 0, temp);
                                }else if(!userTitle.getText().toString().equals(object.getTitle()) ||!userDescp.getText().toString().equals(object.getDescription()) ) {
                                    taskFunction(enumFunctions.EDIT, object.getId(), temp);
                                }else{
                                    //This be could dangerous lock in the main thread
                                    makeToast("Failed to edit");
                                }
                            }
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogBox, int id ){
                          dialogBox.cancel();
                    }
                });
        alertDialog.show();
        return false;
    }

    //General update for the whole list
    public synchronized void onTaskAllupdate(ArrayList<TaskObject> object) {
        Collections.reverse(object);
        this.dropListArray.clear();
        this.dropListArray.addAll(object);
        this.adapter.notifyDataSetChanged();
    }

    public synchronized void addArrayObject(TaskObject object) {
        this.dropListArray.add(object);
        this.adapter.notifyDataSetChanged();
    }

    public synchronized void updateArrayObject(TaskObject object,Object value) {
        this.dropListArray.set(getPos(object.getId()),(TaskObject) value);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Startup view
        taskFunction(enumFunctions.ALL, 0, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Startup view
        taskFunction(enumFunctions.ALL, 0, null);
    }

    @Override
    public void onTaskAll(Object value) {
       try {
            if(value instanceof String){
                if(value.toString().equalsIgnoreCase(failed)){
                    makeToast("Failed to delete");
                }else if(value.toString().equalsIgnoreCase(success)) {
                    //not effective but so low end
                    new AllTask(this).execute(new String[]{enumFunctions.ALL.toString(),url});
                }
            }else if(value instanceof TaskObject){
                //not effective but so low end
                TaskObject task = (TaskObject) value;
                if(getPos(task.getId())==-1){
                    addArrayObject(task);
                }else{
                   updateArrayObject(task,value);
                }
                this.adapter.notifyDataSetChanged();
            }else if (value instanceof ArrayList){
                //not certain about this a long term good idea;
                onTaskAllupdate((ArrayList<TaskObject>) value);
            }
       }catch (ClassCastException e){
            makeToast("Wrong Class Method");
       }catch (NullPointerException e){
           makeToast("Incorrect pointer");
       }
    }
    //Search List for positions
    private int getPos(int id) {
        int x = 0;
        for(TaskObject e: dropListArray){
            if(e.getId() == id){
                return x;
            }
            x++;
        }
        return -1;
    }



    public void makeToast(String str ){
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }

    // passive Check for the Internet connection
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else if(Build.VERSION.SDK_INT >= 21){
            Network[] info = connectivity.getAllNetworks();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i] != null && connectivity.getNetworkInfo(info[i]).isConnected()) {
                        return true;
                    }
                }
            }
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
            final NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
