package uday.tech;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewsDetail extends android.app.Activity {
    TextView newsNama, newsDesc;
    ImageView newsImage;
    Button edit, hapus;

    private FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        //ui
        newsNama = findViewById(R.id.newsTitle);
        newsDesc = findViewById(R.id.newsSubtitle);
        newsImage = findViewById(R.id.newsImage);
        edit = findViewById(R.id.editButton);
        hapus = findViewById(R.id.deleteButton);
        db = FirebaseFirestore.getInstance();

        //get data
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String nama = intent.getStringExtra("nama");
        String desc = intent.getStringExtra("desc");
        String imageUrl = intent.getStringExtra("imageUrl");

        //button edit
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetail.this, NewsAdd.class);
                intent.putExtra("id", id);
                intent.putExtra("nama", nama);
                intent.putExtra("desc", desc);
                intent.putExtra("imageUrl", imageUrl);
                startActivity(intent);
            }

        });
        //button hapus
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("news").document(id).delete().addOnSuccessListener(aVoid -> {
                    Toast.makeText(NewsDetail.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewsDetail.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(NewsDetail.this, "Gagal dihapus", Toast.LENGTH_SHORT).show();
                    Log.w("NewsDetail", "Gagal dihapus", e);
                });
            }
        });
        //set data ui component
        newsNama.setText(nama);
        newsDesc.setText(desc);
        Glide.with(NewsDetail.this).load(imageUrl).into(newsImage);
    }
}
