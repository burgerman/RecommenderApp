package com.group7.recommenderapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.couchbase.lite.Collection;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.ui.signup.SignUpActivity;
import com.group7.recommenderapp.util.DatabaseManager;
import com.group7.recommenderapp.ui.preference.PreferenceSelectionActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private LoginPresenter loginPresenter;
    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        Collection userCollection = DatabaseManager.getSharedInstance(this).getUserCollection();
        if (userCollection == null) {
            Log.e(TAG, "User collection is null. Check database initialization.");
            Toast.makeText(this, "Error initializing database. Please try again later.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        loginPresenter = new LoginPresenter(this, userCollection);
    }

    public void onLoginTapped(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        loginPresenter.handleLogin(username, password);
    }

    public void onLoginSuccess(User user) {
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PreferenceSelectionActivity.class);
        intent.putExtra("USER_ID", user.getDocumentId());
        startActivity(intent);
        finish();
    }


    public void onLoginFailure() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
    }

    public void onSignUpTapped(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
