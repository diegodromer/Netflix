package com.diegolima.netflix.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.diegolima.netflix.R;
import com.diegolima.netflix.autenticacao.LoginActivity;
import com.diegolima.netflix.helper.FirebaseHelper;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler(getMainLooper()).postDelayed(() -> {
			finish();
			if (FirebaseHelper.getAutenticado()){
				startActivity(new Intent(this, MainActivity.class));
			}else{
				startActivity(new Intent(this, LoginActivity.class));
			}
		}, 1500);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			hideSystemUI();
		}
	}

	private void hideSystemUI() {
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_IMMERSIVE
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN);
	}
}