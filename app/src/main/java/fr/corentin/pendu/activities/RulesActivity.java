package fr.corentin.pendu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import fr.corentin.pendu.MainActivity;
import fr.corentin.pendu.R;
import fr.corentin.pendu.manager.ChargeWebPageManager;
import fr.corentin.pendu.manager.LanguageManager;
import fr.corentin.pendu.manager.PreferencesManager;

/**
 * Class {@link RulesActivity}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class RulesActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBack;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_rules);

        this.btnBack = findViewById(R.id.btn_rules_back);
        this.btnBack.setOnClickListener(this);

        this.webView = findViewById(R.id.rule_page);

        new ChargeWebPageManager(this.webView, (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).loadNetwork(getCurrentFocus());

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_rules_back){
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            this.finish();
        }
    }

    private void loadLanguage(){
        String lang = PreferencesManager.getSavedLanguage(this, "pref");
        if(lang != null) LanguageManager.changeLocale(this.getResources(), lang);
    }

}