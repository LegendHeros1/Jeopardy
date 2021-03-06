package cs125.jeopardy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class gameActivity extends AppCompatActivity {

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;


    private Player playerOne;
    private Player playerTwo;
    private Question questionClass;
    private int questionValue;

    private TextView player1_name;
    private TextView player2_name;

    /**
     * odd number - player's one turn
     * even number - player's two turn
     */
    private static int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_game);

        //save states
        final SharedPreferences prefs = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);

        //get information from intent and set information for player 1 and player 2
        Intent intent = getIntent();
        player1_name = findViewById(R.id.player1_text);
        player2_name = findViewById(R.id.player2_text);
        TextView player1_score = findViewById(R.id.player1_score);
        TextView player2_score = findViewById(R.id.player2_score);


        playerOne = (Player)intent.getSerializableExtra("playerOneClass");
        playerTwo = (Player)intent.getSerializableExtra("playerTwoClass");
        turn = intent.getIntExtra("turnCount", 1);

        player1_name.setText(playerOne.getName());
        player2_name.setText(playerTwo.getName());
        player1_score.setText(String.valueOf(playerOne.getScore()));
        player2_score.setText(String.valueOf(playerTwo.getScore()));


        int[] questionButtonsID = {R.id.point_200_1, R.id.point_200_2, R.id.point_200_3, R.id.point_200_4,
                R.id.point_400_1, R.id.point_400_2, R.id.point_400_3, R.id.point_400_4,
                R.id.point_600_1, R.id.point_600_2, R.id.point_600_3, R.id.point_600_4,
                R.id.point_800_1, R.id.point_800_2, R.id.point_800_3, R.id.point_800_4,
                R.id.point_1000_1, R.id.point_1000_2, R.id.point_1000_3, R.id.point_1000_4};
        for (int ButtonID : questionButtonsID) {
            //set all the $valueButton to listen for a click and open the questionActivity.
            Button questionButton = findViewById(ButtonID);
            String buttonStringID = getButtonIdString(questionButton.getId());

            if (turn == 1) {
                prefs.edit().putBoolean(buttonStringID, true).apply();

            }
            questionButton.setVisibility(prefs.getBoolean(buttonStringID, true) ? View.VISIBLE : View.INVISIBLE);
            questionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //using the name of the button to get the pointValue of the question - happen in trivalAPICall
                    Button btn = (Button) v;
                    String btn_text = btn.getText().toString();
                    String buttonStringID = getButtonIdString(btn.getId());

                    questionValue = Integer.valueOf(btn_text.substring(1));
                    btn.setVisibility(View.INVISIBLE);
                    prefs.edit().putBoolean(buttonStringID, false).apply();
                    trivalAPICall(api_information(buttonStringID));
                }
            });

        }
        highlightPlayerTurn(turn);

    }

    public String getButtonIdString(int buttonID) {
        return ((Button) findViewById(buttonID)).getResources().getResourceEntryName(buttonID);
    }
    public void highlightPlayerTurn(int turn) {
        if (turn % 2 == 1) {
            //player 1 turn
            player1_name.setTextColor(Color.GREEN);
            player2_name.setTextColor(Color.BLACK);
        } else {
            player2_name.setTextColor(Color.GREEN);
            player1_name.setTextColor(Color.BLACK);
        }
    }

    public String api_information(String buttonStringID) {
        String[] splitArr = buttonStringID.split("_");
        String categoryID = "category" + splitArr[2];;
        int questionValue = Integer.valueOf(splitArr[1]);

        if (questionValue <= 400) {
            return getAPIurl(categoryID, "easy");
        }
        else if (questionValue <= 800) {
            return getAPIurl(categoryID, "medium");
        } else {
            return getAPIurl(categoryID, "hard");
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
     * @param difficult difficulty level - easy(200,400), medium(2600,800), hard(1000)
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
        //increase the turn count;
        Intent intent = new Intent(this, questionActivity.class);
        intent.putExtra("playerOneClass", playerOne);
        intent.putExtra("playerTwoClass", playerTwo);
        intent.putExtra("questionClass", questionClass);
        intent.putExtra("turnCount", turn);
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

            JSONArray incorrects = firstQuestionSet.getJSONArray("incorrect_answers");
            //add the incorrect_answers to the arrayList
            //add the correct answer to the arrayList at a random chosen index(0,1,2,3)
            ArrayList<String> answer_Choices = new ArrayList<>();
            for (int i = 0; i < incorrects.length(); i++) {
                answer_Choices.add(incorrects.getString(i));
            }
            Random random = new Random();
            int randomIndex = random.nextInt(answer_Choices.size());
            answer_Choices.add(randomIndex, correct_Answer);

            questionClass = new Question(question, correct_Answer, answer_Choices);
            questionClass.setQuestionValue(questionValue);
            openQuestionActivity();
        } catch (JSONException ignored) {
            //do nothing
        }
    }
}
