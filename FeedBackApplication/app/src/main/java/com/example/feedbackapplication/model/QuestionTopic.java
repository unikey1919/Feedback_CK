package com.example.feedbackapplication.model;

import java.util.List;

public class QuestionTopic {
    private String Topic;
    private List<QuestionContent> subQuestion;

    public QuestionTopic(){

    }

    public QuestionTopic(String Topicx, List<QuestionContent> subQuestionx) {
        subQuestion = subQuestionx;
        Topic = Topicx;
    }

    public List<QuestionContent> getQuestion() {
        return subQuestion;
    }

    public void setQuestion(List<QuestionContent> Questionx) {
        subQuestion = Questionx;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String Topicx) {
        Topic = Topicx;
    }
}
