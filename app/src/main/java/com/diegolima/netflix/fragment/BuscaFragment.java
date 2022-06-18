package com.diegolima.netflix.fragment;

import android.app.Activity;
import android.app.TaskInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.security.keystore.StrongBoxUnavailableException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diegolima.netflix.R;
import com.diegolima.netflix.adapter.AdapterBusca;
import com.diegolima.netflix.helper.FirebaseHelper;
import com.diegolima.netflix.model.Categoria;
import com.diegolima.netflix.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuscaFragment extends Fragment {

	private AdapterBusca adapterBusca;

	List<Post> postList = new ArrayList<>();

	private SearchView searchView;
	private RecyclerView rvPosts;
	private ProgressBar progressBar;
	private TextView textInfo;
	private TextView textBuscasPopulares;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_busca, container, false);

	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		iniciaComponentes(view);
		configRv(postList);
		recuperaPost();
		configSearchView();
	}

	private void configSearchView(){
		textBuscasPopulares.setText("Buscas populares");
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String pesquisa) {
				if (pesquisa.length() >= 3){
					buscarPosts(pesquisa);
					textBuscasPopulares.setText("Buscas populares");
				}else{
					ocultarTeclado();
					Toast.makeText(getContext(), "Mínimo 3 caracteres", Toast.LENGTH_SHORT).show();
				}
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(view -> {
			ocultarTeclado();
			recuperaPost();

			EditText edt = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
			edt.getText().clear();

			searchView.requestFocus();

			InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		});
	}

	private void buscarPosts(String pesquisa){

		List<Post> postListBusca = new ArrayList<>();

		for (Post post : postList){
			if(post.getTitulo().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT))){
				postListBusca.add(post);
			}
		}

		if (!postListBusca.isEmpty()){
			configRv(postListBusca);
			textInfo.setText("");
			textBuscasPopulares.setText("Buscas populares");
		}else{
			textBuscasPopulares.setText("");
			configRv(new ArrayList<>());
			textInfo.setText("Nenhum post encontradocom o nome pesquisado.");
		}
	}

	private void recuperaPost() {
		DatabaseReference postRef = FirebaseHelper.getDatabaseReference()
				.child("posts");
		postRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()){
					postList.clear();
					for (DataSnapshot ds : snapshot.getChildren()){
						postList.add(ds.getValue(Post.class));
						textInfo.setText("");
					}
				}else{
					textInfo.setText("Nenhum post cadastrado");
				}
				progressBar.setVisibility(View.GONE);

				configRv(postList);
				adapterBusca.notifyDataSetChanged();

			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}


	private void configRv(List<Post> postList) {
		rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
		rvPosts.setHasFixedSize(true);
		adapterBusca = new AdapterBusca(postList, getContext());
		rvPosts.setAdapter(adapterBusca);
		adapterBusca.notifyDataSetChanged();
	}

	private void iniciaComponentes(View view) {
		searchView = view.findViewById(R.id.searchView);
		rvPosts = view.findViewById(R.id.rvPosts);
		progressBar = view.findViewById(R.id.progressBar);
		textInfo = view.findViewById(R.id.textInfo);
		textBuscasPopulares = view.findViewById(R.id.textBuscasPopulares);
	}

	private void ocultarTeclado() {
		InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}