package com.quiq.deltahack2016.quiq;

/**
 * Created by Vanshil on 2016-01-16.
 */
public class QuestionItem {
    private String questionText;
    private int votes;

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
}
