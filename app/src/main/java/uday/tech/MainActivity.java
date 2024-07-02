package uday.tech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private AdapterList myAdapter;
    private List<ItemList> itemList;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //initialiaze ui
        recyclerView = findViewById(R.id.recycle_view);
        floatingActionButton = findViewById(R.id.floatAddNews);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        //setup recyclerview
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        myAdapter = new AdapterList(itemList);
        recyclerView.setAdapter(myAdapter);

        //floatingactionbutton
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddPage = new Intent(MainActivity.this, NewsAdd.class);
                startActivity(toAddPage);
            }
        });
        myAdapter.setOnItemClickListener(new AdapterList.onItemClickListener() {
            @Override
            public void onItemClick(ItemList item) {
                Intent intent = new Intent(MainActivity.this, NewsDetail.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("nama", item.getJudul());
                intent.putExtra("desc", item.getDeskripsi());
                intent.putExtra("imageUrl", item.getImgUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        progressDialog.show();
        db.collection("news").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    itemList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ItemList item = new ItemList(
                                document.getString("nama"),
                                document.getString("desc"),
                                document.getString("imageUrl")
                        );
                        item.setId(document.getId());
                        itemList.add(item);
                        Log.d("data", document.getId() + " => " + document.getData());
                    }
                    myAdapter.notifyDataSetChanged();
                } else {
                    Log.w("data", "Error getting documents.", task.getException());
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            Intent toLogin = new Intent(MainActivity.this, Login.class);
            startActivity(toLogin);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}