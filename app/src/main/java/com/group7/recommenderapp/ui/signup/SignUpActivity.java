package com.group7.recommenderapp.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.ui.preference.PreferenceSelectionActivity;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {
    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private SignUpContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        UserService userService = UserService.getUserServiceInstance(this);
        presenter = new SignUpPresenter(this, userService);
    }

    public void onSignUpTapped(View view) {
        String username = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        presenter.attemptSignUp(username, email, password, confirmPassword);
    }

    @Override
    public void showSignUpSuccess(User newUser) {
        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PreferenceSelectionActivity.class);
        intent.putExtra("USER_ID", newUser.getDocumentId());
        startActivity(intent);
        finish();
    }

    @Override
    public void showSignUpFailure(String message) {
        Toast.makeText(this, "Sign up failed: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidationError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
