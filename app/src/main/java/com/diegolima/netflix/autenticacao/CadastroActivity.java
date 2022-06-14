package com.diegolima.netflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.diegolima.netflix.R;
import com.diegolima.netflix.activity.MainActivity;
import com.diegolima.netflix.helper.FirebaseHelper;

public class CadastroActivity extends AppCompatActivity {

	private EditText edtEmail;
	private EditText edtSenha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);

		iniciaComponentes();
		configClicks();
	}

	private void validaDados() {
		String email = edtEmail.getText().toString().trim();
		String senha = edtSenha.getText().toString().trim();

		if (!email.isEmpty()) {
			if (!senha.isEmpty()) {
				cadastroFirebase(email, senha);
			} else {
				edtEmail.requestFocus();
				edtEmail.setError("Informe uma senha.");
			}
		} else {
			edtEmail.requestFocus();
			edtEmail.setError("Informe um e-mail.");
		}
	}

	private void cadastroFirebase(String email, String senha) {
		FirebaseHelper.getAuth().createUserWithEmailAndPassword(email, senha)
			.addOnCompleteListener(task -> {
				if (task.isSuccessful()){
					finish();
					startActivity(new Intent(this, MainActivity.class));
				}else{
					Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
				}
			});
	}

	private void configClicks() {
		findViewById(R.id.btnEntrar).setOnClickListener(view -> finish());
		findViewById(R.id.btnCadastro).setOnClickListener(view -> validaDados());
	}

	private void iniciaComponentes() {
		edtEmail = findViewById(R.id.edtEmail);
		edtSenha = findViewById(R.id.edtSenha);
	}
}