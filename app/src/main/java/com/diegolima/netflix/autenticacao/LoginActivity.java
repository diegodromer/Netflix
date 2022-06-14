package com.diegolima.netflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.diegolima.netflix.R;
import com.diegolima.netflix.activity.MainActivity;
import com.diegolima.netflix.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

	private EditText edtEmail;
	private EditText edtSenha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		iniciaComponentes();
		configClicks();

	}

	private void validaDados() {
		String email = edtEmail.getText().toString().trim();
		String senha = edtSenha.getText().toString().trim();

		if (!email.isEmpty()) {
			if (!senha.isEmpty()) {
				loginFirebase(email, senha);
			} else {
				edtEmail.requestFocus();
				edtEmail.setError("Informe uma senha.");
			}
		} else {
			edtEmail.requestFocus();
			edtEmail.setError("Informe um e-mail.");
		}
	}

	private void loginFirebase(String email, String senha) {
		FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha)
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()){
						finish();
					}else{
						Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void configClicks(){
		findViewById(R.id.btnCadastro).setOnClickListener(view -> {
			startActivity(new Intent(this, CadastroActivity.class));
		});
		findViewById(R.id.btnEntrar).setOnClickListener(view -> validaDados());

	}

	private void iniciaComponentes() {
		edtEmail = findViewById(R.id.edtEmail);
		edtSenha = findViewById(R.id.edtSenha);
	}
}