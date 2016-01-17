package com.quiq.deltahack2016.quiq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vanshil on 2016-01-16.
 */
public class QuestionItem implements Comparable<QuestionItem>{
    @SerializedName("text")
    @Expose
    private String questionText;
    @SerializedName("votes")
    @Expose
    private int votes;

    private int index;

    private String answer;
    private boolean answered;
    public QuestionItem(String questionText, int votes) {
        this.questionText = questionText;
        this.votes = votes;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public int compareTo(QuestionItem another) {
        int diff = another.getVotes() - this.getVotes();
        if(diff != 0) {
            return diff / Math.abs(diff);
        }
        return diff;
    }

    public int getIndex() {
        return index;
    }
}
