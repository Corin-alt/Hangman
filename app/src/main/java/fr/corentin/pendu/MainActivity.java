package fr.corentin.pendu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;

import fr.corentin.pendu.activities.RulesActivity;
import fr.corentin.pendu.activities.account.LoginActivity;
import fr.corentin.pendu.activities.account.RegisterActivity;
import fr.corentin.pendu.receiver.BatteryReceiver;
import fr.corentin.pendu.manager.LanguageManager;
import fr.corentin.pendu.manager.NotificationManager;
import fr.corentin.pendu.manager.PreferencesManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static MainActivity mainActivity;

    static HashMap<AppCompatActivity, Boolean> listActivites= new HashMap<>();

    private Button btnRegister, btnRules, btnQuit, btnLogin;

    private BatteryReceiver batteryReceiver;

    private Toolbar toolbar;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);

        MainActivity.listActivites.put(this, true);

        mainActivity = this;

        this.btnRegister = findViewById(R.id.btn_register);
        this.btnRegister.setOnClickListener(this);

        this.btnLogin = findViewById(R.id.btn_login);
        this.btnLogin.setOnClickListener(this);

        this.btnQuit = findViewById(R.id.btn_quit);
        this.btnQuit.setOnClickListener(this);

        this.btnRules = findViewById(R.id.btn_rules);
        this.btnRules.setOnClickListener(this);

        this.batteryReceiver = new BatteryReceiver();
    }


    private void loadLanguage(){
        String lang = PreferencesManager.getSavedLanguage(this, "pref");
        if(lang != null) LanguageManager.changeLocale(this.getResources(), lang);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intent = new IntentFilter();
        intent.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(this.batteryReceiver, intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.batteryReceiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mn = getMenuInflater();
        mn.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar_settings) {
            this.createLanguageDialog();
        }
        return false;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                Intent register = new Intent(v.getContext(), RegisterActivity.class);
                v.getContext().startActivity(register);
                this.finish();
                break;
            case R.id.btn_login:
                Intent login = new Intent(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(login);
                this.finish();
                break;
            case R.id.btn_rules:
                Intent rule = new Intent(v.getContext(), RulesActivity.class);
                v.getContext().startActivity(rule);
                this.finish();
                break;
            case R.id.btn_quit:
//                listActivites.forEach((act, bool)->{if(bool) act.finish();});
                System.exit(0);

                break;
        }
    }

    public void createLanguageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(this.idToString(R.string.lang));
        builder.setMessage(this.idToString(R.string.msg_setting));


        builder.setPositiveButton(this.idToString(R.string.setting_lang_en), (dialog, which) -> {
            PreferencesManager.saveLanguage(this, "pref", "en");
            LanguageManager.changeLocale(this.getResources(), "en");
            NotificationManager.sendNotification(getApplicationContext(),
                    idToString(R.string.lang),
                    idToString(R.string.notif_lang_msg) + " " + idToString(R.string.setting_lang_en));
            reload();
        });

        builder.setNegativeButton(this.idToString(R.string.setting_lang_fr), (dialog, which) -> {
            PreferencesManager.saveLanguage(this, "pref", "fr");
            LanguageManager.changeLocale(this.getResources(), "fr");
            NotificationManager.sendNotification(getApplicationContext(),
                    idToString(R.string.lang),
                    idToString(R.string.notif_lang_msg) + " " + idToString(R.string.setting_lang_fr));
            reload();
        });

        builder.create().show();
    }

    private void reload(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private String idToString(int id){
        return getResources().getString(id);
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static HashMap<AppCompatActivity, Boolean> getListActivites() {
        return listActivites;
    }

}
