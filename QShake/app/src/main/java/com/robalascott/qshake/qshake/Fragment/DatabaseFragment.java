package com.robalascott.qshake.qshake.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robalascott.qshake.qshake.Database.Question;

import com.robalascott.qshake.qshake.R;

/**
 * Created by robscott on 2017-09-21.
 */

public class DatabaseFragment extends Fragment implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private final static String key = "question";
    public Button btnUpdate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4_database_menu, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnUpdate = (Button)rootView.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        return rootView;
    }

    private void writeNewQuestion(int id, String question, String type) {
        Question q1 = new  Question(1,question,type);
        mDatabase.child(key).child(type).setValue(q1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                this.writeNewQuestion(1,"Hello","family");
                break;

            default:
                break;
        }
    }
}
