package com.quiq.deltahack2016.quiq;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.class_code_edittext)
    EditText classCodeText;

    @InjectView(R.id.enter_class_button)
    Button enterButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        final Activity context = this;
        enterButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        if(classCodeText.getText().toString().equals("") == false){
                            String classCode = classCodeText.getText().toString();
                            QuestionActivity.start(context, classCode);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Please enter a class code", Toast.LENGTH_SHORT).show();

                        }


                    }
                });


    }

}
