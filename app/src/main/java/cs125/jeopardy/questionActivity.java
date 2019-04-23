package cs125.jeopardy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class questionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

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
        startActivity(intent);
        finish();
    }
    public void hello() {
        System.out.println("hi");
    }
}
