package com.quiq.deltahack2016.quiq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QuestionActivity extends AppCompatActivity {

    @InjectView(R.id.recycler)
    RecyclerView questionsRecyclyer;

    List<QuestionItem> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.inject(this);
        questions = new ArrayList<>();
    questionsRecyclyer.setAdapter(new QuestionsAdapter(questions));

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