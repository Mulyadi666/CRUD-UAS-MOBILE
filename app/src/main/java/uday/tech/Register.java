package uday.tech;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //initialize firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.passwordConfirmation);
        nameEditText = findViewById(R.id.name);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        registerButton.setOnClickListener(v -> {
            if (nameEditText.getText().length() > 0 && emailEditText.getText().length() > 0 && passwordEditText.getText().length() > 0 && confirmPasswordEditText.getText().length() > 0) {
                if (passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                    register(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString());
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Lengkapi semua Lomko!@!@!@", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    FirebaseUser user = task.getResult().getUser();
                    if (user != null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    } else {
                        Toast.makeText(Register.this, "Gagal Register", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(Register.this, "Gagal Register " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    Log.v("errornyatuuu", task.getException().getMessage());
                }
            }
        });

    }

    private void reload() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }

}