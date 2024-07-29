package com.group7.recommenderapp.ui.music;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.fragments.MusicFragment;
import com.group7.recommenderapp.ui.base.BaseActivity;

public class MusicActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.musicContainer, new MusicFragment())
                    .commit();
        }

        FloatingActionButton addMusicButton = findViewById(R.id.addMusicButton);
        addMusicButton.setOnClickListener(v -> {
            // Handle add music action
            Toast.makeText(this, "Add music clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query change
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_details) {
            // Handle details action
            return true;
        } else if (itemId == R.id.action_delete) {
            // Handle delete action
            return true;
        } else if (itemId == R.id.action_favorites) {
            // Handle view favorites action
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
