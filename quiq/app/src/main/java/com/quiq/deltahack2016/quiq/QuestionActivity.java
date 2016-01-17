package com.quiq.deltahack2016.quiq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QuestionActivity extends AppCompatActivity {
    private static final String EXTRA_CLASS_CODE = "extra_final";
    String newQuestionToPost = "";

    @InjectView(R.id.recycler)
    RecyclerView questionsRecyclyer;

    @InjectView(R.id.new_question_fab)
    FloatingActionButton newQuestionFab;

    @InjectView(R.id.popup_new_question)
    FrameLayout newQuestionPopup;

    @InjectView(R.id.new_question_text)
    EditText newQuestionText;

    @InjectView(R.id.new_question_send_button)
    Button newQuestionSendButton;

    List<QuestionItem> questions;

    public static void start(Context context, String classCode){
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(EXTRA_CLASS_CODE, classCode);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.inject(this);
        questions = new ArrayList<>();
        questionsRecyclyer.setAdapter(new QuestionsAdapter(questions));
        newQuestionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuestionPopup.setVisibility(View.VISIBLE);
            }
        });

        newQuestionSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuestionPopup.setVisibility(View.GONE);

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                //Gets the Question from the edit textview if its not blank
                if(newQuestionText.getText().toString().equals("") == false ) {
                    newQuestionToPost = newQuestionText.getText().toString();
                    Toast.makeText(QuestionActivity.this, newQuestionToPost, Toast.LENGTH_SHORT).show();
                }
                //clears the textview after extracting the question
                newQuestionText.setText("");

            }
        });


    }
}
class QuestionsAdapter extends RecyclerView.Adapter<QuestionViewHolder>{
    List<QuestionItem> questions;
    public QuestionsAdapter(List<QuestionItem> questions){
        this.questions = questions;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.fill(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class QuestionViewHolder extends RecyclerView.ViewHolder{

    @InjectView(R.id.question_text)
    TextView questionText;

    @InjectView(R.id.question_votes)
    TextView questionVotes;

    @InjectView(R.id.button_downvote)
    ImageButton buttonDownvote;

    @InjectView(R.id.button_upvote)
    ImageButton buttonUpvote;

    public QuestionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void fill(QuestionItem questionItem){
        questionText.setText(questionItem.getQuestionText());
        questionVotes.setText(questionItem.getVotes() + "");
    }

}