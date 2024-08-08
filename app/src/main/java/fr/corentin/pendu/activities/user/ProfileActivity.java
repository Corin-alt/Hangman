package fr.corentin.pendu.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.corentin.pendu.MainActivity;
import fr.corentin.pendu.R;
import fr.corentin.pendu.activities.MenuActivity;
import fr.corentin.pendu.database.user.User;
import fr.corentin.pendu.database.user.UserManager;
import fr.corentin.pendu.manager.LanguageManager;
import fr.corentin.pendu.manager.PreferencesManager;

/**
 * Class {@link ProfileActivity}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvPseudo, tvVictory, tvDefeat;

    private Button btnBack;

    private User user;
    private UserManager userManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_profile);
        MainActivity.getListActivites().put(this, true);


        this.userManager = new UserManager(this);
        this.userManager.open();

        this.user = PreferencesManager.getSavedObject(this, "pref", "user", User.class);

        this.tvPseudo = findViewById(R.id.title_profile);
        this.tvPseudo.setText(this.user.getPseudo());

        this.tvVictory = findViewById(R.id.profile_nb_victory);
        this.tvVictory.setText(this.idToString(R.string.profile_victory) + ": " + this.userManager.getUserWin(this.user.getEmail()));

        this.tvDefeat = findViewById(R.id.profile_nb_defeat);
        this.tvDefeat.setText(this.idToString(R.string.profile_defeat) + ": " + this.userManager.getUserLose(this.user.getEmail()));

        this.btnBack = findViewById(R.id.btn_profile_back);
        this.btnBack.setOnClickListener(this);

    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_profile_back) {
            Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(menu);
            this.userManager.close();
            this.finish();
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