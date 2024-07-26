package com.group7.recommenderapp.ui.home;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.fragments.MovieFragment;
import com.group7.recommenderapp.fragments.MusicFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movieRecommendationsContainer, new MovieFragment())
                    .replace(R.id.musicRecommendationsContainer, new MusicFragment())
                    .commit();
        }
    }
}
