package cs125.jeopardy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class gameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get information from intent and set name for player 1 and player 2
        Intent intent = getIntent();
        TextView player1_name = findViewById(R.id.player1_text);
        TextView player2_name = findViewById(R.id.player2_text);
        player1_name.setText(intent.getStringExtra("player1"));
        player2_name.setText(intent.getStringExtra("player2"));

        int[] questionButtonsID = {R.id.point_200_1, R.id.point_200_2, R.id.point_200_3, R.id.point_200_4,
                R.id.point_400_1, R.id.point_400_2, R.id.point_400_3, R.id.point_400_4,
                R.id.point_600_1, R.id.point_600_2, R.id.point_600_3, R.id.point_600_4,
                R.id.point_800_1, R.id.point_800_2, R.id.point_800_3, R.id.point_800_4,
                R.id.point_1000_1, R.id.point_1000_2, R.id.point_1000_3, R.id.point_1000_4};
        for (int ButtonID : questionButtonsID) {
            //set all the $valueButton to listen for a click and open the questionActivity.
            Button questionButton = findViewById(ButtonID);
            questionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuestionActivity();
                }
            });

        }
    }

    void openQuestionActivity() {
        trivalApiCall();
        Intent intent = new Intent(this, questionActivity.class);
        startActivity(intent);
        finish();
    }

    private void trivalApiCall() {

    }


}
