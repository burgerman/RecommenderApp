package com.group7.recommenderapp.ui.help;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.group7.recommenderapp.R;

public class HelpActivity extends AppCompatActivity implements HelpContract.View {
    private HelpContract.Presenter presenter;
    private TextView helpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpTextView = findViewById(R.id.helpTextView);
        presenter = new HelpPresenter(this);
        presenter.loadHelpContent();
    }

    @Override
    public void displayHelpContent(String content) {
        helpTextView.setText(content);
    }
}
