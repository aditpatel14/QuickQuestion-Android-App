package com.quiq.deltahack2016.quiq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.Path;

/**
 * Created by Vanshil on 2016-01-16.
 */
public interface FirebaseService {

    @GET("/instructors/Do%20Not%20Touch/{lectureName}/lectures/{lectureDay}.json")
    Call<QuestionsListResponse> getQuestions(@Path("lectureName") String lectureName, @Path("lectureDay") String lectureDay) ;

    @PATCH("/instructors/Do%20Not%20Touch/{lectureName}/lectures/{lectureDay}/questions/{questionNumber}.json")
    Call<String> sendVote (@Path("lectureName") String lectureName, @Path("lectureDay") String lectureDay, @Path("questionNumber") String questionNumber, @Body VoteItem vote);

}
class QuestionsListResponse{
    @SerializedName("questions")
    @Expose
    List<QuestionItem> questions;

    public List<QuestionItem> getQuestions() {
        return questions;
    }
}
class VoteItem{
    int votes;
    public VoteItem(int votes){
        this.votes = votes;
    }
}