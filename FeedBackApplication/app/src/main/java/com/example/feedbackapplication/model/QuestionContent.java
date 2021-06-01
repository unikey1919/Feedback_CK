package com.example.feedbackapplication.model;

public class QuestionContent {
    private String Question;
    private String QuestionID;

    public QuestionContent(String Questionx, String QuestionxID) {
        Question = Questionx;
        QuestionID = QuestionxID;

    }

    public String getQuestion() {
        return Question;
    }
    public String getQuestionID() {
        return QuestionID;
    }


    public void setQuestion(String Questionx) {
        Question = Questionx;
    }
    public void setQuestionID(String QuestionxId) {
        QuestionID = QuestionxId;
    }

}
