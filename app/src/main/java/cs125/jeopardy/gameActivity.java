package cs125.jeopardy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class gameActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Jeopardy:gameActivity";
    private TextView mTextViewResult;
    private RequestQueue mQueue;


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

        TextView mTextViewResult = findViewById(R.id.testing_box);
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
                    trivalApiCall();
                    //openQuestionActivity();
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
        String url = "https://opentdb.com/api.php?amount=10&type=multiple";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("results");
                    JSONObject firstSet = result.getJSONObject(0);

                    //get question, answer, incorrect multiple choice
                    String question = firstSet.getString("question");
                    String correctAnswer = firstSet.getString("correct_answer");
                    JSONArray incorrectAnswers = firstSet.getJSONArray("incorrect_answers");
                    mTextViewResult.append(question + " " + correctAnswer);
                    Log.d(TAG, "WORKING");
                    Log.d(TAG, question + " " + correctAnswer);

                } catch (JSONException e) {
                    mTextViewResult.append("Not working!");
                    Log.d(TAG, "NOT WORKING");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
