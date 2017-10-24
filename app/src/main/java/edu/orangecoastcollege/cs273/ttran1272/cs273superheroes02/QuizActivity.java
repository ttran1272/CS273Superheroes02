package edu.orangecoastcollege.cs273.ttran1272.cs273superheroes02;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "CS273 Superheros Quiz";

    private static final int NUMBER_ITEMS_IN_QUIZ = 10;
    private static final int CHOICES = 4;

    private Button[] mButtons = new Button[4];
    private LinearLayout[] mLayouts = new LinearLayout[2];

    private List<Superhero> mAllSuperheroesList; // all the superheros loaded from JSON
    private List<Superhero> mQuizSuperheroesList; // superheroes in current quiz ( just 10 of them)

    private Superhero mCorrectSuperhero; // correct superhero for the current question

    private int mTotalGuesses; // number of total guesses made
    private int mCorrectGuesses; // number of correct guesses

    private SecureRandom rng; // used to randomize the quiz

    private Handler handler; // used to delay loading next superhero

    private TextView mQuestionNumberTextView; // shows current question number
    private ImageView mSuperheroImageView; // displays picture of the superhero
    private TextView mAnswerTextView; // displays correct answer

    private String mTypeOfQuiz; // stores which superhero is selected
    private boolean superheroQuiz = true;
    private boolean superpowerQuiz = false;
    private boolean guessOneThingQuiz = false;

    // Key used in preferences.xml
    private static final String QUIZ_TYPE = "pref_typeOfQuiz";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Register the OnSharedPreferencesChangeListener
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(mListener);

        mQuizSuperheroesList = new ArrayList<>(NUMBER_ITEMS_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        // Get references to GUI components (textviews and imageviews)
        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        mSuperheroImageView = (ImageView) findViewById(R.id.superheroImageView);

        // Put all 4 buttons in the array (mButtons)
        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);

        mLayouts[0] = (LinearLayout) findViewById(R.id.row1LinearLayout);
        mLayouts[1] = (LinearLayout) findViewById(R.id.row2LinearLayout);

        // Set mQuestionNumberTextView's text to the appropriate strings.xml resource
        mQuestionNumberTextView.setText(getString(R.string.question, 1, NUMBER_ITEMS_IN_QUIZ));

        // Load all the superheroes from the JSON file using the JSONLoader
        try {
            mAllSuperheroesList = JSONLoader.loadJSONFromAsset(this);
            Log.e(TAG, "JSONLoader successfully with size=" + mAllSuperheroesList.size());
        }catch (IOException e){
            Log.e(TAG, "Error loading JSON file", e);
        }

        // Set the default choice
        mTypeOfQuiz = preferences.getString(QUIZ_TYPE, "Superhero Name");

        // Update type of quiz, which is among "Guess the Superhero", "Guess Superpower",
        // or "Guess The One Thing"
        updateTypeOfQuiz();

        // start the quiz
        resetQuiz();

    }

    /**
     * Sets up and starts a new quiz
     */

    public void resetQuiz() {

        // Reset the number of correct guesses made
        mCorrectGuesses = 0;

        // Reset the total number of guesses the user made
        mTotalGuesses = 0;

        // clear list of quiz superhero (for prior games played)
        mQuizSuperheroesList.clear();

        // Randomly add SUPERHERO_NAME_IN_QUIZ (10) superheroes from the mAllSuperheroesList
        // into the mQuizSuperheroesList

        int size = mAllSuperheroesList.size();
        int randomPosition;
        Superhero randomSuperhero;

        while (mQuizSuperheroesList.size() < NUMBER_ITEMS_IN_QUIZ){
            // Generate random position
            randomPosition = rng.nextInt(size);
            randomSuperhero = mAllSuperheroesList.get(randomPosition);

            if (!mQuizSuperheroesList.contains(randomSuperhero))
                mQuizSuperheroesList.add(randomSuperhero);
        }

        loadNextGuess();

    }

    /**
     * Method initiates the process of loading the next superhero for the quiz, showing the
     * superhero's image and then 4 buttons, one of which contains the correct answer
     * This method is called by function resetQuiz()
     */
    private void loadNextGuess() {

        // Initialize the mCorrectSuperhero by removing the item at position 0 in the mQuizSuperheroesList
        mCorrectSuperhero = mQuizSuperheroesList.remove(0);

        // Clear the mAnswerTextView so that id doesn't show text from the previous question
        mAnswerTextView.setText("");

        // Display current question number in the mQuestionNumberTextView
        int questionNumber = NUMBER_ITEMS_IN_QUIZ - mQuizSuperheroesList.size();

        mQuestionNumberTextView.setText(getString(R.string.question, questionNumber, NUMBER_ITEMS_IN_QUIZ));

        // Use AssetManager to load next image from assets folder
        AssetManager am = getAssets();

        try {
            // Get an InputStream to the asset representing the next superhero
            // and try to use the InputStream to create a Drawable
            // The file name can be retrieved from the correct superhero's file name
            // Set the image drawable to the correct superhero

            InputStream stream = am.open(mCorrectSuperhero.getFileName());
            Drawable image = Drawable.createFromStream(stream, mCorrectSuperhero.getName());
            mSuperheroImageView.setImageDrawable(image);

        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + mCorrectSuperhero.getFileName(), e);
        }

        // Shuffle the order of all the superheroes
        do {
            Collections.shuffle(mAllSuperheroesList);
        }
        while (mAllSuperheroesList.subList(0, CHOICES).contains(mCorrectSuperhero));

        TextView quizGuessInfo = (TextView) findViewById(R.id.guessQuizTextView);

        // After the loop, randomly replace one of the 4 buttons with the name of the correct information
        // based on the type of quiz and update the quiz information
        if (superheroQuiz) {
            // Update the type of quiz
            quizGuessInfo.setText(R.string.guess_superhero);

            // Loop through all 4 buttons, enable them all and set them to the first 4 superheroes in the all superheroes list
            for (int i = 0; i < CHOICES; i++) {
                mButtons[i].setEnabled(true);
                mButtons[i].setText(mAllSuperheroesList.get(i).getName());
            }
            mButtons[rng.nextInt(CHOICES)].setText(mCorrectSuperhero.getName());

        } else if (superpowerQuiz) {
            // Update the type of quiz
            quizGuessInfo.setText(R.string.guess_superpower);

            // Loop through all 4 buttons, enable them all and set them to the first 4 superheroes in the all superheroes list
            for (int i = 0; i < CHOICES; i++) {
                mButtons[i].setEnabled(true);
                mButtons[i].setText(mAllSuperheroesList.get(i).getSuperpower());
            }
            mButtons[rng.nextInt(CHOICES)].setText(mCorrectSuperhero.getSuperpower());

        } else {
            // Update the type of quiz
            quizGuessInfo.setText(R.string.guess_onething);

            // Loop through all 4 buttons, enable them all and set them to the first 4 superheroes in the all superheroes list
            for (int i = 0; i < CHOICES; i++) {
                mButtons[i].setEnabled(true);
                mButtons[i].setText(mAllSuperheroesList.get(i).getOneThing());
            }
            mButtons[rng.nextInt(CHOICES)].setText(mCorrectSuperhero.getOneThing());
        }
    }


        /**
         * Handles the click event of one of the 4 buttons indicating the guess of a superhero's name
         * to match the picture image displayed. If the guess is correct, the superhero's name (in GREEN)
         * will be shown, followed by a slight delay of 2 seconds, then the next superhero will be loaded.
         * Otherwise, the word "Incorrect Guess"  will be shown in RED and the button will be disabled.
         * @param v
         */

    public void makeGuess(View v) {

        // Downcast the View v into a Button (since it's one of the 4 buttons)
        Button clickedButton = (Button) v;

        // Get the superhero's name from the text of the button
        String guess = clickedButton.getText().toString();

        mTotalGuesses++;

        // Based on the type of quiz to do the comparison
        String superheroInfo;

        if (superheroQuiz)
            superheroInfo = mCorrectSuperhero.getName();
        else if (superpowerQuiz)
            superheroInfo = mCorrectSuperhero.getSuperpower();
        else
            superheroInfo = mCorrectSuperhero.getOneThing();

        // If the quess matches the correct superhero's information( name or superpower, or one thing:
        if (guess.equals(superheroInfo)) {

            // Disable all buttons (don't let user guess again)
            for (Button b : mButtons)
                b.setEnabled(false);

            // increment the number of correct guesses
            mCorrectGuesses++;

            // then display correct answer in green text.
            mAnswerTextView.setText(superheroInfo);
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_answer));

            if (mCorrectGuesses < NUMBER_ITEMS_IN_QUIZ) {
                // Wait two seconds, then load next superhero
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextGuess();
                    }
                }, 2000);
            } else {
                // if the user has completed all 10 questions
                // Show an AlertDialog with the statistics and an option to reset quiz
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.results, mTotalGuesses,
                        (double) mCorrectGuesses / mTotalGuesses));
                builder.setPositiveButton(getString(R.string.reset_quiz),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resetQuiz();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        }
        // Else, the answer is incorrect, so display "Incorrect Guess!" in red
        // and disable jus the incorrect button
        else {
            clickedButton.setEnabled(false);
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_answer));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Make a new Intent going to SettingsActivity
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        return super.onOptionsItemSelected(item);
    }


    // Update the type of quiz
    private void updateTypeOfQuiz() {

        // If the type of quiz is "Superhero Name"
        if (mTypeOfQuiz.equals("Superhero Name")) {
            superheroQuiz = true;
            superpowerQuiz = false;
            guessOneThingQuiz = false;
        } else if (mTypeOfQuiz.equals("Guess Superpower")) {
            superpowerQuiz = true;
            superheroQuiz = false;
            guessOneThingQuiz = false;
        } else {
            guessOneThingQuiz = true;
            superheroQuiz = false;
            superpowerQuiz = false;
        }
    }




    // Create the mListener to get the selected type of quiz
    SharedPreferences.OnSharedPreferenceChangeListener mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            mTypeOfQuiz = sharedPreferences.getString(key, "Superhero Name");
            Log.e(TAG, "Type of Quiz = " + mTypeOfQuiz);

            updateTypeOfQuiz();
            resetQuiz();

            Toast.makeText(QuizActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();

        }
    };
}
