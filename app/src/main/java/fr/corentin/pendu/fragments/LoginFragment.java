package fr.corentin.pendu.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.mindrot.jbcrypt.BCrypt;

import fr.corentin.pendu.MainActivity;
import fr.corentin.pendu.R;
import fr.corentin.pendu.activities.MenuActivity;
import fr.corentin.pendu.database.user.User;
import fr.corentin.pendu.database.user.UserManager;
import fr.corentin.pendu.manager.PreferencesManager;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText txtEmail;
    private EditText txtPassword;

    private Button btnLogin;
    private Button btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_blank, container, true);

        this.txtEmail = rootView.findViewById(R.id.et_login_email);
        this.txtPassword = rootView.findViewById(R.id.et_login_password);

        this.btnBack = rootView.findViewById(R.id.btn_back);
        this.btnBack.setOnClickListener(this);
        this.btnLogin = rootView.findViewById(R.id.btn_login_login);
        this.btnLogin.setOnClickListener(this);

        return rootView;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                if(getActivity() != null ){
                    Intent main = new Intent(getContext(), MainActivity.class);
                    startActivity(main);
                    getActivity().finish();
                }
                break;
            case R.id.btn_login_login :
                this.login(this.txtEmail.getText().toString(), this.txtPassword.getText().toString(), v);
                break;
        }
    }

    private void login(String email, String password, View v) {
        if(this.allFieldIsFill(email, password) && getActivity() != null){
            UserManager userManager = new UserManager(getContext());
            userManager.open();
            if(userManager.exist(email)){
                if(BCrypt.checkpw(password, userManager.getHashedPassword(email)) && getContext()!= null){
                    Intent menu = new Intent(v.getContext(), MenuActivity.class);
                    v.getContext().startActivity(menu);
                    User user = userManager.getUser(userManager.getUserId(email));
                    userManager.close();
                    PreferencesManager.saveObject(getContext(), "pref", "user", user);
                } else {
                    this.createDialog(this.idToString(R.string.title_error), this.idToString(R.string.msg_failed_login));
                }
            } else {
                this.createDialog(this.idToString(R.string.title_error), this.idToString(R.string.msg_failed_login));
            }
        } else {
            this.createDialog(this.idToString(R.string.title_error), this.idToString(R.string.msg_field));
        }
    }

    private String idToString(int id){
        return getResources().getString(id);
    }

    private boolean allFieldIsFill(String email,  String password){
        return !email.equals("")  && !password.equals("");
    }

    public void createDialog(String title, String msg){
        if(this.getContext() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setPositiveButton("OK", (dialog, which) -> {});
            builder.create().show();
        }
    }

}
