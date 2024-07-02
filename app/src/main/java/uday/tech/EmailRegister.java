package uday.tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailRegister extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Ganti dengan ID layout kamu

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");

        // Tombol Daftar
        findViewById(R.id.registerButton).setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim(); // Ganti dengan ID EditText kamu
            String password = passwordEditText.getText().toString().trim(); // Ganti dengan ID EditText kamu

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Masukkan email dan password", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Pendaftaran berhasil
                                FirebaseUser user = auth.getCurrentUser();
                                Toast.makeText(EmailRegister.this, "Pendaftaran berhasil: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                // Navigasi ke aktivitas login atau lakukan tindakan lain
                                startActivity(new Intent(EmailRegister.this, Login.class));
                                finish();
                            } else {
                                // Pendaftaran gagal
                                Log.w("RegisterActivity", "Pendaftaran gagal", task.getException());
                                Toast.makeText(EmailRegister.this, "Pendaftaran gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}