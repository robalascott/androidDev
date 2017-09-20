package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * PlayerListAdapter
 * Controller for listview Items of users
 */

import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerListAdapter extends ArrayAdapter<Players>{
	private final Activity context;
	private final List<Players> playersObject;
	private final Integer[] imgid = {R.drawable.user_profile_blue,R.drawable.user_profile_purple,R.drawable.user_profile_red,R.drawable.user_profile_yellow};
	 
	public PlayerListAdapter(Activity context,List<Players> objects) {
		super(context,  R.layout.listview, objects);
		this.context=context;
		this.playersObject=objects;
	}

	public View getView(int position,View view,ViewGroup parent) {
		 LayoutInflater inflater=context.getLayoutInflater();
		 View rowView=inflater.inflate(R.layout.listview, null,true);
		 
				ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
				TextView txtName = (TextView) rowView.findViewById(R.id.name);
				txtName.setText(playersObject.get(position).getName());
				TextView txtPlayer = (TextView) rowView.findViewById(R.id.info);
				txtPlayer.setText(playersObject.get(position).getPicID());
				switch(position%4){
					case 0:imageView.setImageResource(imgid[position%4]);break;
					case 1:imageView.setImageResource(imgid[position%4]);break;
					case 2:imageView.setImageResource(imgid[position%4]);break;
					case 3:imageView.setImageResource(imgid[position%4]);break;
				}

		 return rowView;
		 
	 };
	
}
