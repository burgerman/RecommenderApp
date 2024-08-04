package com.group7.recommenderapp.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.ui.login.LoginActivity;
import com.group7.recommenderapp.util.DatabaseManager;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {
    private EditText usernameOrEmailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameOrEmailInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

    }

    public void onSignUpTapped(View view) {
        String usernameOrEmail = usernameOrEmailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        presenter.signUp(usernameOrEmail, password, confirmPassword);
    }

    @Override
    public void showSignUpSuccess() {
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showSignUpError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
