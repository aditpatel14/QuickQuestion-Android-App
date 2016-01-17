package com.quiq.deltahack2016.quiq;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Vanshil on 2016-01-16.
 */
public class FireBaseManager {
    public static FireBaseManager instance;
    private List<Listener> listeners;
    public static synchronized FireBaseManager getInstanceUnsafe(){
        return instance;
    }
    public static synchronized FireBaseManager getInstance(Context context){
        if(instance == null){
            instance = new FireBaseManager(context);
        }
        return instance;
    }

    private FirebaseService fireService;
    private String nowDate;
    private Retrofit retrofit;

    private FireBaseManager(Context context){
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        listeners = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.FIREBASE_PATH))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        DateTime now = DateTime.now();
        nowDate = String.format("%02d", now.getDayOfMonth()) + "-" +  String.format("%02d", now.getMonthOfYear()) + "-" + DateTime.now().getYear();
        fireService = retrofit.create(FirebaseService.class);

    }

    public void getQuestions(String courseName){
        if(courseName.length() > 0){
            Call<QuestionsListResponse> fireCall =  fireService.getQuestions(courseName, nowDate);
            fireCall.enqueue(new Callback<QuestionsListResponse>() {
                @Override
                public void onResponse(Response<QuestionsListResponse> response, Retrofit retrofit) {
                    Log.d("FireBaseManager", response.raw() + "");
                    notifyQuestionsLoaded(response.body().getQuestions());
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("FireBaseManager", t.getMessage() + "");
                }
            });
        }
    }

    public void sendVote(String courseName, String questionName, int vote){
        Call<String> fireCall = fireService.sendVote(courseName, nowDate, questionName, new VoteItem(vote));
        fireCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void addListener(Listener listener){
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    private void notifyQuestionsLoaded(List<QuestionItem> questions){
        for(Listener listener : listeners){
            listener.questionsLoaded(questions);
        }
    }

    public String getNowDate() {
        return nowDate;
    }

    interface Listener{
        void questionsLoaded(List<QuestionItem> questions);
    }

}
