package cs125.jeopardy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class questionActivity extends AppCompatActivity {

    private Player playerOne;
    private Player playerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        playerOne = (Player)intent.getSerializableExtra("playerOneClass");
        playerTwo = (Player)intent.getSerializableExtra("playerTwoClass");

        int[] questionChoiceID = {R.id.answer_A, R.id.answer_B, R.id.answer_C, R.id.answer_D};
        for (int ChoiceID : questionChoiceID) {
            Button choiceButton = findViewById(ChoiceID);
            choiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToGameActivity();
                }
            });
        }
    }
    void backToGameActivity() {
        Intent intent = new Intent(this, gameActivity.class);
        playerOne.increaseScore(20);
        intent.putExtra("playerOneClass", playerOne);
        intent.putExtra("playerTwoClass", playerTwo);
        startActivity(intent);
        finish();
    }
    public void hello() {
        System.out.println("hi");
    }
}
