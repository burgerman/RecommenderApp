package com.group7.recommenderapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.profile.UserProfileActivity;
import com.group7.recommenderapp.ui.preference.PreferenceSelectionActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private EditText usernameInput;
    private EditText passwordInput;
    private AppCompatImageView imageView;
    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        imageView = findViewById(R.id.imageViewLogo);

        presenter = new LoginPresenter(this, this);

        // Makes logging in easier for testing
        imageView.setOnClickListener(v -> {
            usernameInput.setText("demo@example.com");
            passwordInput.setText("password");
        });
    }

    public void onLoginTapped(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        presenter.handleLogin(username, password);
    }

    public void onSignUpTapped(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        presenter.handleSignUp(username, password);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToUserProfile() {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void navigateToPreferenceSelection() {
        Intent intent = new Intent(getApplicationContext(), PreferenceSelectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
