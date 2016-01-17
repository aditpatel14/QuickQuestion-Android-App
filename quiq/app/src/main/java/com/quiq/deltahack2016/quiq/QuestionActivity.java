package com.quiq.deltahack2016.quiq;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class QuestionActivity extends AppCompatActivity {

    private static final String EXTRA_CLASS_CODE = "extra_final";
    String newQuestionToPost = "";

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

    @InjectView(R.id.new_question_send_button)
    Button newQuestionSendButton;

    List<QuestionItem> questions;
    FireBaseManager fireManager;
    private String classCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.inject(this);
        Firebase.setAndroidContext(this);
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
        setupNewQuestionButton();
        }
private void setupFireManager() {
final QuestionActivity context = this;
        fireManager = FireBaseManager.getInstance(this);
        fireManager.addListener(new FireBaseManager.Listener() {
@Override
public void questionsLoaded(List<QuestionItem> questions) {
        Collections.sort(questions);
        context.questions = questions;
        questionsRecyclyer.setAdapter(new QuestionsAdapter(context, questions));
        }
        });
        this.classCode = getIntent().getStringExtra(EXTRA_CLASS_CODE);
        setTitle("Questions in: " + classCode);
        startRefreshThread();
        }
private boolean threadRunning = true;
private void startRefreshThread(){

final Thread thread = new Thread(new Runnable()
        {
            final int MIN_INTERVAL = 1 * 500;
            long lastMessageChecked = System.currentTimeMillis();
            @Override
            public void run(){
                int count = 1;
                while (threadRunning){
                    if(System.currentTimeMillis() - lastMessageChecked > MIN_INTERVAL){
                        lastMessageChecked = System.currentTimeMillis();
                        fireManager.getQuestions(classCode);
                        Log.d("REFRESH CHECKING THREAD", "CHECKED FOR REFRESH");
                        count = 0;
                    }
                    count++;
                }
                Log.d("REFRESH CHECKING THREAD", "STOPPED");
            }
        });
        thread.start();
        Log.d("REFRESH CHECKING THREAD", "STARTED");
    }
    @Override public void finish(){
        super.finish();
        threadRunning = false;
    }
    private void setupNewQuestionButton(){
        final Context context = this;
        newQuestionSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuestionPopup.setVisibility(View.GONE);

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                //Gets the Question from the edit textview if its not blank
                String[] profanities = {"potato"};
                if(newQuestionText.getText().toString().equals("") == false) {
                    newQuestionToPost = newQuestionText.getText().toString();
                    for(String profanity : profanities){
                        if(newQuestionToPost.contains(profanity)){
                            Toast.makeText(context, "Please use clean language", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    String nowDate = FireBaseManager.getInstanceUnsafe().getNowDate();

                    Firebase questionRef = new Firebase("https://quiq.firebaseio.com/instructors/Do Not Touch/" +
                            classCode + "/lectures/" + nowDate + "/questions/" + questions.size());

                    Map<String, Object> newQuestion = new HashMap<String, Object>();
                    newQuestion.put("index", questions.size());
                    newQuestion.put("answer", "");
                    newQuestion.put("answered", false);
                    newQuestion.put("text", newQuestionToPost);
                    newQuestion.put("votes", 1);
                    System.out.println(questionRef.getPath().toString());
                    questionRef.setValue(newQuestion, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                System.out.println("Data could not be saved. " + firebaseError.getMessage());
                            } else {
                                System.out.println("Data saved successfully.");
                            }
                        }
                    });



                    Toast.makeText(context, newQuestionToPost, Toast.LENGTH_SHORT).show();
                }
                //clears the textview after extracting the question
                newQuestionText.setText("");

            }
        });
    }

    public class QuestionsAdapter extends RecyclerView.Adapter<QuestionViewHolder>{
        Context context;
        List<QuestionItem> questions;
        public QuestionsAdapter(Context context1, List<QuestionItem> questions1){
            context = context1;
            questions = questions1;
        }

        @Override
        public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new QuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.question_item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(QuestionViewHolder holder, int position) {
            holder.fill(questions.get(position), classCode, questions.get(position).getIndex() + "");
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.question_text)
        TextView questionText;

        @InjectView(R.id.question_votes)
        TextView questionVotes;

        @InjectView(R.id.button_downvote)
        ImageView buttonDownvote;

        @InjectView(R.id.button_upvote)
        ImageView buttonUpvote;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }

        public void fill(final QuestionItem questionItem, final String courseCode, final String questionNumber){
            if(questionItem != null){
                questionText.setText(questionItem.getQuestionText());
                questionVotes.setText(questionItem.getVotes() + "");
                buttonUpvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FireBaseManager.getInstanceUnsafe().sendVote(courseCode, questionNumber, questionItem.getVotes() + 1);
                    }
                });

                buttonDownvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FireBaseManager.getInstanceUnsafe().sendVote(courseCode, questionNumber, questionItem.getVotes() - 1);


                    }
                });
                questionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(questionItem.isAnswered() == true ){
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(questionItem.getAnswer()));
                            startActivity(intent);
                        }


                    }
                });

            }
        }

    }

    public void onBuyTicketClicked(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(intent);
    }
}