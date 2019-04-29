package cs125.jeopardy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class questionActivity extends AppCompatActivity {

    private Player playerOne;
    private Player playerTwo;
    private Question questionClass;
    private String correctAnswer;
    private int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //getting information
        Intent intent = getIntent();
        playerOne = (Player)intent.getSerializableExtra("playerOneClass");
        playerTwo = (Player)intent.getSerializableExtra("playerTwoClass");
        questionClass = (Question)intent.getSerializableExtra("questionClass");
        turn = intent.getIntExtra("turnCount", -1);


        //setting up the activity
        TextView question_textbox = findViewById(R.id.question_textbox);
        question_textbox.setText(questionClass.getQuestion());

        correctAnswer = questionClass.getCorrectAnswer();
        ArrayList<String> answerChoices= questionClass.getAnswerChoices();
        Button choiceA = findViewById(R.id.answer_A);
        choiceA.setText(answerChoices.get(0));
        Button choiceB = findViewById(R.id.answer_B);
        choiceB.setText(answerChoices.get(1));
        Button choiceC = findViewById(R.id.answer_C);
        choiceC.setText(answerChoices.get(2));
        Button choiceD = findViewById(R.id.answer_D);
        choiceD.setText(answerChoices.get(3));

        Button[] choiceButtons = {choiceA, choiceB, choiceC, choiceD};
        for (Button choiceButton : choiceButtons) {
            choiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    String btn_text = btn.getText().toString();

                    btn.setBackgroundColor(Color.RED);
                    if (btn_text.equals(correctAnswer)) {
                        btn.setBackgroundColor(Color.GREEN);
                        if (turn % 2 == 1) {
                            playerOne.increaseScore(questionClass.getQuestionValue());
                        } else {
                            playerTwo.increaseScore(questionClass.getQuestionValue());
                        }
                    }
                    backToGameActivity();
                }
            });
        }
    }
    void backToGameActivity() {
        Intent intent = new Intent(this, gameActivity.class);
        intent.putExtra("playerOneClass", playerOne);
        intent.putExtra("playerTwoClass", playerTwo);
        intent.putExtra("turnCount", turn + 1);
        startActivity(intent);
        finish();
    }
}
