package com.diegolima.netflix.fragment;

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
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class BuscaFragment extends Fragment {

	private AdapterBusca adapterBusca;
	private List<Post> postList = new ArrayList<>();

	private SearchView searchView;
	private RecyclerView rvPosts;
	private ProgressBar progressBar;
	private TextView textInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_busca, container, false);

	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		iniciaComponentes(view);
		configRv();
		recuperaPost();
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
				adapterBusca.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}


	private void configRv() {
		rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
		rvPosts.setHasFixedSize(true);
		adapterBusca = new AdapterBusca(postList, getContext());
		rvPosts.setAdapter(adapterBusca);
	}

	private void iniciaComponentes(View view) {
		searchView = view.findViewById(R.id.searchView);
		rvPosts = view.findViewById(R.id.rvPosts);
		progressBar = view.findViewById(R.id.progressBar);
		textInfo = view.findViewById(R.id.textInfo);
	}
}