package com.group7.recommenderapp.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.couchbase.lite.Blob;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.ui.login.LoginActivity;
import com.group7.recommenderapp.util.DatabaseManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.View {

    private UserProfileContract.UserActionsListener mActionListener;

    TextView uniqueIdText;
    EditText nameInput;
    EditText ageInput;
    EditText genderInput;
    TextView emailInput;
    TextView preferencesText;
    RecyclerView likedItemsRecyclerView;
    ImageView imageView;

    private LikedItemsAdapter likedItemsAdapter;

    ActivityResultLauncher<Intent> mainActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                                imageView.setImageBitmap(bitmap);
                            } catch (IOException ex) {
                                Log.i("SelectPhoto", ex.getMessage());
                            }
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        uniqueIdText = findViewById(R.id.uniqueIdText);
        nameInput = findViewById(R.id.nameInput);
        ageInput = findViewById(R.id.ageInput);
        genderInput = findViewById(R.id.genderInput);
        emailInput = findViewById(R.id.emailInput);
        preferencesText = findViewById(R.id.preferencesText);
        likedItemsRecyclerView = findViewById(R.id.likedItemsRecyclerView);
        imageView = findViewById(R.id.imageView);

        setupLikedItemsRecyclerView();

        mActionListener = new UserProfilePresenter(this, this);

        runOnUiThread(() -> mActionListener.fetchProfile());
    }

    private void setupLikedItemsRecyclerView() {
        likedItemsAdapter = new LikedItemsAdapter();
        likedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        likedItemsRecyclerView.setAdapter(likedItemsAdapter);
    }

    public void onUploadPhotoTapped(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mainActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    public void onLogoutTapped(View view) {
        DatabaseManager.getSharedInstance(this).closeDatabaseForUser();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onSaveTapped(View view) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("name", nameInput.getText().toString());
        profile.put("age", Integer.parseInt(ageInput.getText().toString()));
        profile.put("gender", genderInput.getText().toString());

        byte[] imageViewBytes = getImageViewBytes();
        if (imageViewBytes != null) {
            profile.put("imageData", new Blob("image/jpeg", imageViewBytes));
        }

        mActionListener.saveProfile(profile);
    }

    private byte[] getImageViewBytes() {
        byte[] imageBytes = null;
        BitmapDrawable bmDrawable = (BitmapDrawable) imageView.getDrawable();
        if (bmDrawable != null) {
            Bitmap bitmap = bmDrawable.getBitmap();
            if (bitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageBytes = baos.toByteArray();
            }
        }
        return imageBytes;
    }

    @Override
    public void showProfile(Map<String, Object> profile) {
        uniqueIdText.setText("ID: " + (String) profile.get("uniqueId"));
        nameInput.setText((String) profile.get("name"));
        ageInput.setText(String.valueOf((Integer) profile.get("age")));
        genderInput.setText((String) profile.get("gender"));
        emailInput.setText((String) profile.get("email"));

        String preferences = "Movie Preferences: " + profile.get("moviePreferences") +
                "\nMusic Preferences: " + profile.get("musicPreferences");
        preferencesText.setText(preferences);

        List<String> likedItems = (List<String>) profile.get("likedItems");
        likedItemsAdapter.setLikedItems(likedItems);

        Blob imageBlob = (Blob) profile.get("imageData");
        if (imageBlob != null) {
            Drawable d = Drawable.createFromStream(imageBlob.getContentStream(), "res");
            imageView.setImageDrawable(d);
        }
    }

    @Override
    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
