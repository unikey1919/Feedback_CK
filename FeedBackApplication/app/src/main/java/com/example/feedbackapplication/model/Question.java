package com.example.feedbackapplication.model;

public class Question {
    private int TopicID;
    private String TopicName;
    private int QuestionID;
    private String QuestionContent;

    public Question() {

    }

    public Question(int topicID, String topicName, int questionID, String questionContent) {
        TopicID = topicID;
        TopicName = topicName;
        QuestionID = questionID;
        QuestionContent = questionContent;
    }

    public int getTopicID() {
        return TopicID;
    }

    public void setTopicID(int topicID) {
        TopicID = topicID;
    }

    public String getTopicName() {
        return TopicName;
    }

    public void setTopicName(String topicName) {
        TopicName = topicName;
    }

    public int getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(int questionID) {
        QuestionID = questionID;
    }

    public String getQuestionContent() {
        return QuestionContent;
    }

    public void setQuestionContent(String questionContent) {
        QuestionContent = questionContent;
    }
}
