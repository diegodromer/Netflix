package com.diegolima.netflix.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.diegolima.netflix.R;

public class CadastroActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);

		configClicks();
	}

	private void configClicks(){
		findViewById(R.id.btnEntrar).setOnClickListener(view -> finish());
	}
}