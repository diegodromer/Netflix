package com.diegolima.netflix.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diegolima.netflix.R;
import com.diegolima.netflix.adapter.AdapterCategoria;
import com.diegolima.netflix.adapter.AdapterPost;
import com.diegolima.netflix.helper.FirebaseHelper;
import com.diegolima.netflix.model.Categoria;
import com.diegolima.netflix.model.Download;
import com.diegolima.netflix.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetalhePostActivity extends AppCompatActivity {

	private List<Post> postList = new ArrayList<>();
	private List<String> downloadList = new ArrayList<>();
	private AdapterPost adapterPost;

	private TextView textTitulo;
	private ImageView imagemPost;
	private ImageView imageFake;
	private TextView textAno;
	private TextView textDuracao;
	private TextView textElenco;
	private ConstraintLayout btnAssistir;
	private ConstraintLayout btnBaixar;
	private TextView textSinopse;
	private RecyclerView rvPosts;

	private Post post;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalhe_post);

		iniciaComponentes();
		configRv();
		configDados();
		configCliques();
		recuperaPost();
		recuperaDownloads();
	}

	private void configDados() {
		post = (Post) getIntent().getSerializableExtra("postSelecionado");

		textTitulo.setText(post.getTitulo());

		imageFake.setVisibility(View.GONE);

		Picasso.get().load(post.getImagem()).into(imagemPost);

		textAno.setText(post.getAno());
		textDuracao.setText(getString(R.string.text_duracao, post.getDuracao()));
		textSinopse.setText(post.getSinopse());
		textElenco.setText(post.getElenco());
	}

	private void configCliques() {
		findViewById(R.id.ibVoltar).setOnClickListener(view -> {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		});
		findViewById(R.id.btnBaixar).setOnClickListener(view -> efetuarDownload());
	}

	private void efetuarDownload() {

		if (!downloadList.contains(post.getId())){
			downloadList.add(post.getId());
			Download.salvar(downloadList);
			Toast.makeText(getBaseContext(), "Download efetuado com sucesso!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getBaseContext(), "Download já efetuado anteriormente!", Toast.LENGTH_SHORT).show();
		}
	}

	private void recuperaDownloads() {
		DatabaseReference downloadRef = FirebaseHelper.getDatabaseReference()
				.child("downloads");
		downloadRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot ds : snapshot.getChildren()){
					downloadList.add(ds.getValue(String.class));
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}


	private void recuperaPost() {
		DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
				.child("posts");
		postRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot ds : snapshot.getChildren()){
					postList.add(ds.getValue(Post.class));
				}
				adapterPost.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void configRv(){
		rvPosts.setLayoutManager(new GridLayoutManager(this, 3));
		rvPosts.setHasFixedSize(true);
		adapterPost = new AdapterPost(postList, this);
		rvPosts.setAdapter(adapterPost);
	}

	private void iniciaComponentes() {
		textTitulo = findViewById(R.id.textTitulo);
		imagemPost = findViewById(R.id.imagemPost);
		imageFake = findViewById(R.id.imageFake);
		textAno = findViewById(R.id.textAno);
		textDuracao = findViewById(R.id.textDuracao);
		textElenco = findViewById(R.id.textElenco);
		btnAssistir = findViewById(R.id.btnAssistir);
		btnBaixar = findViewById(R.id.btnBaixar);
		textSinopse = findViewById(R.id.textSinopse);
		rvPosts = findViewById(R.id.rvPosts);
	}
}