package cs125.jeopardy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.start_btn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    String getTextIn(final int editor) {
        return ((EditText) findViewById(editor)).getText().toString();
    }

    void startGame() {
        Intent intent = new Intent(this, gameActivity.class);
        Player playerOne = new Player(getTextIn(R.id.edit_player1_name), 0);
        Player playerTwo = new Player(getTextIn(R.id.edit_player2_name), 0);
        intent.putExtra("playerOneClass", playerOne);
        intent.putExtra("playerTwoClass", playerTwo);
        startActivity(intent);
        finish();
    }
}
