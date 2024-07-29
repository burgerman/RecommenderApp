package com.group7.recommenderapp.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.util.DatabaseManager;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter presenter;
    private TextView userInfoTextView;
    private Button createTestUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfoTextView = findViewById(R.id.userInfoTextView);
        createTestUserButton = findViewById(R.id.createTestUserButton);

        DatabaseManager dbManager = DatabaseManager.getSharedInstance(this);
        UserDao userDao = new UserDao(dbManager.getUserCollection());
        presenter = new MainPresenter(this, userDao);

        createTestUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.createTestUser();
            }
        });

        presenter.start();
    }

    @Override
    public void showUserInfo(User user) {
        if (user != null) {
            String userInfo = "Username: " + user.getUserName() + "\n" +
                    "Email: " + user.getEmail() + "\n" +
                    "Document ID: " + user.getDocumentId();
            userInfoTextView.setText(userInfo);
        } else {
            userInfoTextView.setText("No user found");
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
