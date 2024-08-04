package com.group7.recommenderapp.ui.help;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.helpicon) {
            presenter.showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showHelpDialog(String authorNames, String version, String instructions) {
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("Authors: " + authorNames + "\nVersion: " + version + "\n\nInstructions:\n" + instructions)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}