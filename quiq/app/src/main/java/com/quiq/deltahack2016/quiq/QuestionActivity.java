package com.quiq.deltahack2016.quiq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QuestionActivity extends AppCompatActivity {

    private static final String EXTRA_CLASS_CODE = "extra_final";

    public static void start(Context context, String classCode){
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(EXTRA_CLASS_CODE, classCode);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @InjectView(R.id.recycler)
    RecyclerView questionsRecyclyer;

    @InjectView(R.id.new_question_fab)
    FloatingActionButton newQuestionFab;

    @InjectView(R.id.popup_new_question)
    FrameLayout newQuestionPopup;

    @InjectView(R.id.new_question_text)
    EditText newQuestionText;

    List<QuestionItem> questions;
    FireBaseManager fireManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.inject(this);
        questions = new ArrayList<>();
        questionsRecyclyer.setLayoutManager(new LinearLayoutManager(this));
        questionsRecyclyer.setAdapter(new QuestionsAdapter(this, questions));

        newQuestionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuestionPopup.setVisibility(View.VISIBLE);
            }
        });
        setupFireManager();

    }
    private void setupFireManager(){
        final Activity context = this;
        fireManager = FireBaseManager.getInstance(this);
        fireManager.addListener(new FireBaseManager.Listener() {
            @Override
            public void questionsLoaded(List<QuestionItem> questions) {
                questionsRecyclyer.setAdapter(new QuestionsAdapter(context, questions));
            }
        });
        fireManager.getQuestions(getIntent().getStringExtra(EXTRA_CLASS_CODE));

    }
}
class QuestionsAdapter extends RecyclerView.Adapter<QuestionViewHolder>{
    Context context;
    List<QuestionItem> questions;
    public QuestionsAdapter(Context context, List<QuestionItem> questions){
        this.context = context;
        this.questions = questions;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.question_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.fill(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
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
        if(questionItem.getVotes() != null){
            questionVotes.setText(questionItem.getVotes().size() + "");
        }
        else{
            questionVotes.setText(0 + "");
        }
    }

}