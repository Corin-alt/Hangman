package fr.corentin.pendu.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import fr.corentin.pendu.MainActivity;
import fr.corentin.pendu.R;
import fr.corentin.pendu.activities.game.PenduActivity;
import fr.corentin.pendu.activities.user.ProfileActivity;
import fr.corentin.pendu.database.user.User;
import fr.corentin.pendu.manager.LanguageManager;
import fr.corentin.pendu.manager.PreferencesManager;

/**
 * Class {@link MenuActivity}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOnePlayer, btnLogout, btnProfile;
    private User user;

    private Bundle mState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_menu);

        MainActivity.getListActivites().put(this, true);
        this.btnOnePlayer = findViewById(R.id.btn_one_player);
        this.btnOnePlayer.setOnClickListener(this);
        this.btnLogout = findViewById(R.id.btn_menu_logout);
        this.btnLogout.setOnClickListener(this);
        this.btnProfile = findViewById(R.id.btn_profile);
        this.btnProfile.setOnClickListener(this);

        isSaved();
    }


    private void isSaved(){
        if (this.mState != null) {
            this.onRestoreInstanceState(this.mState);
        } else {
            this.user = PreferencesManager.getSavedObject(this, "pref", "user", User.class);
            if(this.user != null) Toast.makeText(getApplicationContext(), this.idToString(R.string.welcome_back ) + ", " + user.getPseudo(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        super.onRestoreInstanceState(saved);
        this.user = (User) saved.getSerializable("user");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        this.mState = outState;
        outState.putSerializable("user", this.user);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one_player:
                Intent pendu = new Intent(getApplicationContext(), PenduActivity.class);
                startActivity(pendu);
                this.finish();
                break;
            case R.id.btn_menu_logout:
                PreferencesManager.saveObject(this, "pref", "user", null);
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                this.finish();
                break;
            case R.id.btn_profile:
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profile);
                this.finish();
                break;

        }
    }

    private void loadLanguage(){
        String lang = PreferencesManager.getSavedLanguage(this, "pref");
        if(lang != null) LanguageManager.changeLocale(this.getResources(), lang);
    }


    private String idToString(int id){
        return getResources().getString(id);
    }
}