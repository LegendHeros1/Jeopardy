package cs125.jeopardy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Random;


public class gameActivity extends AppCompatActivity {

    private Player playerOne;
    private Player playerTwo;
    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    private static String display = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_game);

        //get information from intent and set information for player 1 and player 2
        Intent intent = getIntent();
        TextView player1_name = findViewById(R.id.player1_text);
        TextView player2_name = findViewById(R.id.player2_text);
        TextView player1_score = findViewById(R.id.player1_score);
        TextView player2_score = findViewById(R.id.player2_score);

        playerOne = (Player)intent.getSerializableExtra("playerOneClass");
        playerTwo = (Player)intent.getSerializableExtra("playerTwoClass");

        player1_name.setText(playerOne.getName());
        player2_name.setText(playerTwo.getName());
        player1_score.setText(String.valueOf(playerOne.getScore()));
        player2_score.setText(String.valueOf(playerTwo.getScore()));

        final TextView mTextViewResult = findViewById(R.id.testing_box);
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
                    //trivalAPICall("https://opentdb.com/api.php?amount=1&category=9&difficulty=easy&type=multiple");
                    //mTextViewResult.setText(display);

                }
            });

        }
    }

    /**
     * @param category name of the category
     * Category IDs
     * general knowledge: 9
     * vehicles: 28
     *
     * entertainment: 10 - 16, 29, 31 - 32
     * mythology: 20
     * art: 25
     *
     * science: 17 - 19, 30
     * animals: 27
     *
     * Geography: 22
     * History: 23
     * Politics: 24
     *
     *
     * @param difficult difficulty level - easy, medium, hard
     * @return something like this https://opentdb.com/api.php?amount=1&category=9&difficulty=easy&type=multiple
     */

    public String getAPIurl(String category, String difficult) {
        String baseUrl = "https://opentdb.com/api.php?amount=1&";
        String categoryPart = "category="; //need to include id
        String difficultyPart = "&difficulty=" + difficult;
        String questionType = "&type=multiple";

        int[] category1_ids = new int[] {17, 18, 19, 30};
        int[] category2_ids = new int[] {22, 23, 24};
        int[] category3_ids = new int[] {10, 11, 12, 13, 14, 15, 16, 29, 31, 32};
        int[] category4_ids = new int[] {9, 28};

        //randomly pick a id from the respective category array
        Random random = new Random();
        int randomNumber;
        switch (category) {
            case "category1":
                //pick a number between 0 and 3 inclusive
                randomNumber = random.nextInt(category1_ids.length);
                categoryPart += category1_ids[randomNumber];
                break;
            case "category2":
                //pick a number between 0 and 2 inclusive
                randomNumber = random.nextInt(category2_ids.length);
                categoryPart += category2_ids[randomNumber];
                break;
            case "category3":
                //pick a number between 0 and 9 inclusive
                randomNumber = random.nextInt(category3_ids.length);
                categoryPart += category3_ids[randomNumber];
                break;
            case "category4":
                //pick a number between 0 and 1 inclusive
                randomNumber = random.nextInt(category4_ids.length);
                categoryPart += category4_ids[randomNumber];
                break;
             //hopefully doesn't run
            default:
                categoryPart += 9;
                break;
        }
        return baseUrl + categoryPart + difficultyPart + questionType;


    }

    void openQuestionActivity() {
        Intent intent = new Intent(this, questionActivity.class);
        intent.putExtra("playerOneClass", playerOne);
        intent.putExtra("playerTwoClass", playerTwo);
        startActivity(intent);
        finish();
    }

    /**
     * attempt to call api with the url
     * @param url trivial category url
     */
    void trivalAPICall(String url) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    parseJson(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //ignore
                }
            });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void parseJson(JSONObject response) {
        try {
            JSONArray results = response.getJSONArray("results");
            JSONObject firstQuestionSet = results.getJSONObject(0);

            String question = firstQuestionSet.getString("question");
            String correct_Answer = firstQuestionSet.getString("correct_answer");
            JSONArray incorrect_Answers = firstQuestionSet.getJSONArray("incorrect_answers");

            display = incorrect_Answers.getString(2);
        } catch (JSONException ignored) {
            //do nothing
        }
    }
}
