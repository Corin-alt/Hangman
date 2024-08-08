package fr.corentin.pendu.activities.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.corentin.pendu.MainActivity;
import fr.corentin.pendu.R;
import fr.corentin.pendu.activities.MenuActivity;
import fr.corentin.pendu.database.user.User;
import fr.corentin.pendu.database.user.UserManager;
import fr.corentin.pendu.manager.LanguageManager;
import fr.corentin.pendu.manager.PreferencesManager;

/**
 * Class {@link PenduActivity}
 * @author Corentin Dupont
 * @version For project Info0306
 */
public class PenduActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout container;
    private Button btnPropose, btnBack;
    private TextView lettersTapees;
    private ImageView image;
    private EditText etLetter;

    private User user;

    private String word;
    private int found;
    private int error;
    private List<Character> listOfLetters = new ArrayList<>();
    private boolean win;

    private Bundle mState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_pendu);
        MainActivity.getListActivites().put(this, true);

        this.user = PreferencesManager.getSavedObject(this, "pref", "user", User.class);

        this.lettersTapees = findViewById(R.id.tv_letters_tapees);
        this.container = findViewById(R.id.word_container);
        this.btnPropose = findViewById(R.id.btn_send);
        this.btnBack = findViewById(R.id.btn_game_back);
        this.etLetter = findViewById(R.id.et_letter);
        this.image = findViewById(R.id.iv_pendu);

        this.btnPropose.setOnClickListener(this);
        this.btnBack.setOnClickListener(this);

        isSaved();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        super.onRestoreInstanceState(saved);

        this.user = (User) saved.getSerializable("user");

        this.word = saved.getString("word");
        this.found  = saved.getInt("find");
        this.error  = saved.getInt("error");
        this.setImage(this.error);
        this.win = saved.getBoolean("win");

        this.init();

        String letterTapees = saved.getString("letterTapees");
        for(int i = 0; i<letterTapees.length(); i++){
            this.listOfLetters.add(letterTapees.charAt(i));
            this.checkLetterInWord(String.valueOf(letterTapees.charAt(i)), this.word, true);
        }
        this.lettersTapees.setText(letterTapees);

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        this.mState = outState;
        outState.putSerializable("user", this.user);
        outState.putString("word", getWord());
        outState.putInt("find", this.found);
        outState.putInt("error", this.error);
        outState.putBoolean("win", this.win);
        outState.putString("letterTapees", this.lettersTapees.getText().toString());

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void isSaved(){
        if (this.mState != null) {
            this.onRestoreInstanceState(this.mState);
        } else {
            this.initDefaultValues();
            this.init();
        }
    }


    private void initDefaultValues(){
        this.word = this.getRandomWord();
        this.win = false;
        this.error = 0;
        this.found = 0;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_game_back:
                Intent menu = new Intent(v.getContext(), MenuActivity.class);
                v.getContext().startActivity(menu);
                this.finish();
                break;
            case R.id.btn_send:
                String letterFomInput = this.etLetter.getText().toString().toUpperCase();
                etLetter.setText("");
                if(letterFomInput.length() > 0){
                    if(!this.letterAlreadyUsed(letterFomInput.charAt(0), listOfLetters)){
                        this.listOfLetters.add(letterFomInput.charAt(0));
                        this.checkLetterInWord(letterFomInput, this.word, false);
                    }

                    if(this.found == this.word.length()){
                        UserManager userManager = new UserManager(this);
                        userManager.open();
                        userManager.addWin(this.user);
                        this.win = true;
                        this.createDialog(true);
                        userManager.close();
                    }

                    if(!this.word.contains(letterFomInput)){
                        this.error++;
                        setImage(this.error);
                    }

                    if(this.error == 6){
                        UserManager userManager = new UserManager(this);
                        userManager.open();
                        userManager.addLose(this.user);
                        this.win = false;
                        this.createDialog(false);
                        userManager.close();
                    }

                    this.showAllLetter(this.listOfLetters);
                    this.closeKeyboard();
                }
                break;
        }
    }


    /**
     * Method that initializes the game
     */
    public void init(){
        this.lettersTapees.setText("");
        this.image.setBackgroundResource(R.drawable.first);

        if(this.listOfLetters != null && !this.listOfLetters.isEmpty()) this.listOfLetters.clear();
        this.container.removeAllViews();

        for(int i = 0; i<word.length(); i++){
            TextView placeholder_letter = (TextView) getLayoutInflater().inflate(R.layout.placeholder_letter, null);
            this.container.addView(placeholder_letter);
        }
    }


    private List<String> getListOfWords(){
        List<String> words = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader((new InputStreamReader(getAssets().open(this.getFileName()))));
            String line;
            while ((line = br.readLine()) != null){
                words.add(line);
            }
            br.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

        return words;
    }


    private String getFileName(){
        String lang =PreferencesManager.getSavedObject(this, "pref", "lang", String.class);
        if ("fr".equals(lang)) {
            return "dictionary_FR.txt";
        }
        return "dictionary_EN.txt";
    }


    private String getRandomWord(){
        return this.getListOfWords().get((int) (Math.random() * this.getListOfWords().size())).trim();
    }


    /**
     * Checks if the entered letter has not already been entered
     * @param letter letter entered
     * @param listOfLetters list of letters already entered
     * @return {@link Boolean}
     */
    private boolean letterAlreadyUsed(char letter, List<Character> listOfLetters){
        for(int i = 0; i < listOfLetters.size(); i++){
            if(listOfLetters.get(i) == letter){
                Toast.makeText(getApplicationContext(), R.string.letter_already_use, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the entered letter is contained in the word.
     * @param letter the entered letter
     * @param word the word
     */
    private void checkLetterInWord(String letter, String word, boolean replace){
        for(int i = 0; i < word.length(); i++){
            String letter_word = String.valueOf(word.charAt(i));
            if(letter.equals(letter_word)){
                TextView tv = (TextView) this.container.getChildAt(i);
                tv.setText(letter_word);
                if(!replace) this.found++;
            }
        }
    }

    /**
     * Show the list of letters entered
     * @param listOfLetters list of entered letter
     */
    private void showAllLetter(List<Character> listOfLetters){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < listOfLetters.size(); i++){
            sb.append(listOfLetters.get(i));
        }
        this.lettersTapees.setText(sb.toString());
    }

    private void setImage(int error){
        switch (error){
            case 1: this.image.setBackgroundResource(R.drawable.second);
                break;
            case 2: this.image.setBackgroundResource(R.drawable.third);
                break;
            case 3: this.image.setBackgroundResource(R.drawable.fourth);
                break;
            case 4: this.image.setBackgroundResource(R.drawable.fifth);
                break;
            case 5: this.image.setBackgroundResource(R.drawable.sixth);
                break;
            case 6: this.image.setBackgroundResource(R.drawable.seventh);
                break;
        }
    }

    public void createDialog(boolean win){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (win){
            builder.setTitle(this.idToString(R.string.victory));
        }
        else {
            builder.setTitle(this.idToString(R.string.defeat));
            builder.setMessage(this.idToString(R.string.awnser) + " " + this.word);
        }

        builder.setPositiveButton(this.idToString(R.string.restart), (dialog, which) -> {
            this.mState = null;
            this.isSaved();
        });

        builder.setNegativeButton(this.idToString(R.string.logout), (dialog, which) -> {
            PreferencesManager.saveObject(this, "pref", "user", null);
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            this.finish();
        });
        builder.create().show();
    }


    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadLanguage(){
        String lang = PreferencesManager.getSavedLanguage(this, "pref");
        if(lang != null) LanguageManager.changeLocale(this.getResources(), lang);
    }


    private String idToString(int id){
        return getResources().getString(id);
    }

    public String getWord() {
        return word;
    }
}