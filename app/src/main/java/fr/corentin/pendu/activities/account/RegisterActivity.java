package fr.corentin.pendu.activities.account;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fr.corentin.pendu.MainActivity;
import fr.corentin.pendu.R;
import fr.corentin.pendu.manager.LanguageManager;
import fr.corentin.pendu.manager.PreferencesManager;

/**
 * Class {@link RegisterActivity}
 * @author Corentin Dupont
 * @version For project Info0306
 */
public class RegisterActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_register);
        MainActivity.getListActivites().put(this, true);
    }

    private void loadLanguage(){
        String lang = PreferencesManager.getSavedLanguage(this, "pref");
        if(lang != null) LanguageManager.changeLocale(this.getResources(), lang);
    }

}
