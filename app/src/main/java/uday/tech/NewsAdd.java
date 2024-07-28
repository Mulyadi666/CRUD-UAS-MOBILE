package uday.tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class NewsAdd extends android.app.Activity {
    String id = "", judul, deskripsi, image;
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nama, desc;
    private ImageView imageView;
    private Button saveNews, chooseImage;
    private Uri imageUri;

    private FirebaseFirestore dbNews;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_add);

        //initialize firebase

        dbNews = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        //initialize ui
        nama = findViewById(R.id.nama);
        desc = findViewById(R.id.desc);
        imageView = findViewById(R.id.imageView);
        saveNews = findViewById(R.id.btnAdd);
        chooseImage = findViewById(R.id.btnChooseImage);

        progressDialog = new ProgressDialog(NewsAdd.this);
        progressDialog.setTitle("Loading...");

        Intent updateOption = getIntent();
        if (updateOption != null) {
            id = updateOption.getStringExtra("id");
            judul = updateOption.getStringExtra("nama");
            deskripsi = updateOption.getStringExtra("desc");
            image = updateOption.getStringExtra("imageUrl");

            nama.setText(judul);
            desc.setText(deskripsi);
            Glide.with(this).load(image).into(imageView);
        }
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newsTitle = nama.getText().toString().trim();
                String newsDecs = desc.getText().toString().trim();

                if (newsTitle.isEmpty() || newsDecs.isEmpty()) {
                    Toast.makeText(NewsAdd.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                if (imageUri != null) {
                    uploadImageToStorage(newsTitle, newsDecs);
                } else {
                    saveData(newsTitle, newsDecs, image);
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("*image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadImageToStorage(String newsTitle, String newsDecs) {
        if (imageUri != null) {
            StorageReference storageRef = storage.getReference().child("news_images/" + System.currentTimeMillis() + ".jpg");
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveData(newsTitle, newsDecs, imageUrl);
                    }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "Failed to upload image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveData(String newsTitle, String newsDecs, String imageUrl) {
        Map<String, Object> newsData = new HashMap<>();
        newsData.put("nama", newsTitle);
        newsData.put("desc", newsDecs);
        newsData.put("imageUrl", imageUrl);
        if (id != null) {
            dbNews.collection("news").document(id)
                    .update(newsData)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "News updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "Failed to update news" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w("NewsAdd", "Error updating document", e);
                    });
        } else {
            dbNews.collection("news")
                    .add(newsData)
                    .addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "News added successfully", Toast.LENGTH_SHORT).show();
                        nama.setText("");
                        desc.setText("");
                        imageView.setImageResource(0);

                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(NewsAdd.this, "Failed to add news" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w("NewsAdd", "Error adding document", e);
                    });

        }

    }
}