package com.quiq.deltahack2016.quiq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Vanshil on 2016-01-16.
 */
public class QuestionItem {
    @SerializedName("text")
    @Expose
    private String questionText;

    @SerializedName("votes")
    @Expose
    private List<Integer> votes;
    public QuestionItem(String questionText, List<Integer> votes) {
        this.questionText = questionText;
        this.votes = votes;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Integer> getVotes() {
        return votes;
    }

    public void setVotes(List<Integer> votes) {
        this.votes = votes;
    }
}
