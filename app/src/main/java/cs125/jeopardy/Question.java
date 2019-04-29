package cs125.jeopardy;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String question;
    private String correctAnswer;
    private ArrayList<String> answerChoices;
    private int questionValue;

    public Question(String setQuestion, String setAnswer, ArrayList<String> setChoices) {
        question = setQuestion;
        correctAnswer = setAnswer;
        answerChoices = setChoices;
        questionValue = 0;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public int getQuestionValue() {
        return questionValue;
    }
    public ArrayList<String> getAnswerChoices() {
        return answerChoices;
    }

    public void setQuestionValue(int setValue) {
        questionValue = setValue;
    }
}
