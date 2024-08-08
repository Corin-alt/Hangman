package fr.corentin.pendu.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.mindrot.jbcrypt.BCrypt;

import fr.corentin.pendu.MainActivity;
import fr.corentin.pendu.R;
import fr.corentin.pendu.activities.account.LoginActivity;
import fr.corentin.pendu.database.user.User;
import fr.corentin.pendu.database.user.UserManager;
import fr.corentin.pendu.utils.Regex;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    private EditText txtEmail;
    private EditText txtPseudo;
    private EditText txtPassword;
    private EditText txtConfirmPassword;


    private Button btnRegister;
    private Button btnBack;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_blank, container, false);

        this.txtEmail = rootView.findViewById(R.id.et_register_email);
        this.txtPseudo = rootView.findViewById(R.id.et_register_pseudo);
        this.txtPassword = rootView.findViewById(R.id.et_register_password);
        this.txtConfirmPassword = rootView.findViewById(R.id.et_confirm_password);

        this.btnBack = rootView.findViewById(R.id.btn_back);
        this.btnBack.setOnClickListener(this);
        this.btnRegister = rootView.findViewById(R.id.btn_register_register);
        this.btnRegister.setOnClickListener(this);

        return rootView;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back:
                if(getActivity() != null ){
                    Intent main = new Intent(getContext(), MainActivity.class);
                    startActivity(main);
                    getActivity().finish();
                }
                break;
            case R.id.btn_register_register:
                this.register(this.txtEmail.getText().toString(),
                        this.txtPseudo.getText().toString(),
                        this.txtPassword.getText().toString(),
                        this.txtConfirmPassword.getText().toString()
                );
                break;
        }
    }

    private void register(String email, String pseudo, String password, String password2){
        if(this.allFieldIsFill(email, pseudo, password)){
            if(this.checkFormatEmail(email)){
                UserManager userManager = new UserManager(getContext());
                userManager.open();
                if(!userManager.exist(email)) {
                    if(password.equals(password2)){
                        String pwdHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
                        int id = userManager.getUsers().getCount() + 1;
                        userManager.addUser(new User(id, email, pseudo, pwdHash, 0, 0));
                        this.createDialog(this.idToString(R.string.title_success),  this.idToString(R.string.msg_success_register), true);
                        userManager.close();
                    } else {
                        this.createDialog(idToString(R.string.title_error), idToString(R.string.msg_match_password), false);
                    }
                } else {
                    this.createDialog(idToString(R.string.title_error), idToString(R.string.msg_email_already_use), false);
                }
            } else {
                this.createDialog(idToString(R.string.title_error), idToString(R.string.msg_email), false);
            }
        } else {
            this.createDialog(idToString(R.string.title_error), idToString(R.string.msg_field), false);
        }
    }


    private boolean checkFormatEmail(String email) {
        return email.matches(Regex.EMAIL.getReg());
    }

    private boolean allFieldIsFill(String email, String pseudo, String password){
        return !email.equals("") && !pseudo.equals("") && !password.equals("");
    }

    private String idToString(int id){
        return getResources().getString(id);
    }

    public void createDialog(String title, String msg, boolean success){
        if(this.getContext() != null ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setPositiveButton("OK", (dialog, which) -> {
                if(success && getActivity() != null){
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                    getActivity().finish();
                }
            });
            builder.create().show();
        }
    }
}
