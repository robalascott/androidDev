package com.robalascott.todolist.todolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robscott on 2017-10-22.
 */

class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder>{
    private List<TaskObject> todoList;
    private Context con;
    private ItemClickListener listner;
    public interface ItemClickListener{
        public void onItemClick(View v, int pos,String style );
    }
    public void setOnClick(ItemClickListener listner){
        this.listner = listner;
    }

    public TodoAdapter(List<TaskObject> dropListArray, Context context) {
            this.todoList = dropListArray;
            this.con = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todoitems, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TaskObject taskObject = todoList.get(position);
        holder.idView.setText(Integer.toString(taskObject.getId()));
        holder.titleView.setText(taskObject.getTitle());
        holder.descriptionView.setText(taskObject.getDescription());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView idView,titleView,descriptionView;
        private ImageButton imgButton;
        public MyViewHolder(View v){
            super(v);
            idView = (TextView) itemView.findViewById(R.id.idTextView);
            titleView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionView = (TextView) itemView.findViewById(R.id.decriptionTextView);


            v.setOnClickListener (new View.OnClickListener (){
                @Override
                public void onClick(View v) {
                    if(listner!=null){
                        int position = getAdapterPosition ();
                        if(position!=RecyclerView.NO_POSITION){
                            listner.onItemClick (v,position,enumFunctions.EDIT.toString());
                        }
                    }
                }

            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listner!=null){
                        int position = getAdapterPosition ();
                        if(position!=RecyclerView.NO_POSITION){
                            listner.onItemClick (v,position,enumFunctions.DELETE.toString());
                        }
                    }
                    return true;
                }
            });
        }
    }
}
