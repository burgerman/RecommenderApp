package com.group7.recommenderapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.ui.home.HomeActivity;
import com.group7.recommenderapp.ui.signup.SignUpActivity;
import com.group7.recommenderapp.util.DatabaseManager;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private EditText usernameOrEmailInput;
    private EditText passwordInput;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameOrEmailInput = findViewById(R.id.usernameOrEmailInput);
        passwordInput = findViewById(R.id.passwordInput);
        presenter = new LoginPresenter(this);
    }

    public void onLoginTapped(View view) {
        String usernameOrEmail = usernameOrEmailInput.getText().toString();
        String password = passwordInput.getText().toString();
        presenter.login(usernameOrEmail, password);
    }

    public void onSignUpTapped(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoginSuccess() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
