package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * OwnQuestion
 * Data Input Class for custom Questions
 * Dependents: Shakeit;
 */
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OwnQuestionsActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private ColorDrawable colorDrawable;
    private EditText createValue;
    private Button createBtn;
    private ArrayAdapter<String> dataType;
    private Spinner typeSpinner;
    private TextView textInput,textQ;
    private String[] questionTypes = { " General ", " Work ",
            " Relationships ", " Favourite " };
    private StackIt stackIt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_questions);
        this.stackIt = new StackIt(getApplicationContext());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ui();
        menuInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public void menuInit() {
        colorDrawable = new ColorDrawable(Color.parseColor("#267058"));
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Create User Questions");
        actionBar.setBackgroundDrawable(colorDrawable);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    public void ui(){
       /*Spinners Input*/
        textInput = (TextView)findViewById(R.id.textUser);
        textQ = (TextView)findViewById(R.id.textType);
        typeSpinner = (Spinner) findViewById(R.id.spinnertype);
        dataType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,questionTypes);
        dataType.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        typeSpinner.setAdapter(dataType);
        typeSpinner.setSelection(1);
        createBtn = (Button) findViewById(R.id.createBT);
        createValue = (EditText)findViewById(R.id.textT);
		/*Text Watcher editText*/
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {

                } catch (NullPointerException v1) {
			        /*Catch for null values*/

                }
                ;
            }
        };
        createValue.addTextChangedListener(textWatcher);
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    /*Error to message*/
    public void ErrorMessage(String error){
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    public void finish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onClick(View v) {

            Question de1 = new Question();
            String str = String.valueOf(createValue.getText());

            if(!str.isEmpty()){
                de1.setQuestion(str);
                de1.setType(typeSpinner.getSelectedItem().toString());
                de1.setUser(1);
                de1.setStar(0);
                Log.i("test", str);
                if(!stackIt.checkSelectedAll(str)){
                    stackIt.createQuestion(de1);
                    this.ErrorMessage(de1.toStringAll());
                }else {
                    this.ErrorMessage("Already created");

                }
            }else{
                this.ErrorMessage("Need an Input!");
            }

    }
}
