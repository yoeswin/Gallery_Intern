package com.example.squad.atg;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.squad.atg.Interface.getData;
import com.example.squad.atg.adapter.CustomAdapter;
import com.example.squad.atg.adapter.PhotoApi;
import com.example.squad.atg.adapter.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.squad.atg.Constants.api_key;
import static com.example.squad.atg.Constants.extras;
import static com.example.squad.atg.Constants.format;
import static com.example.squad.atg.Constants.methods;
import static com.example.squad.atg.Constants.nojsoncallback;


public class Search extends Fragment {
    SearchView searchView;
    ProgressDialog progressDoalog;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    List<PhotoApi> photoList = new ArrayList<>();

    public Search() {
        // Required empty public constructor
    }


    public static Search newInstance() {
        Search fragment = new Search();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recyclerView1);
        adapter = new CustomAdapter(getContext(), photoList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                progressDoalog = new ProgressDialog(getActivity());
                progressDoalog.setMessage("Loading....");
                progressDoalog.show();
                getDataPics(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void getDataPics(String text) {
        getData service = RetrofitClient.getRetrofitInstance().create(getData.class);
        Call<String> call = service.getAllPhotos(methods, api_key, format, nojsoncallback, extras, text);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDoalog.dismiss();
                try {
                    JSONObject obj = null;
                    if (response.body() != null) {
                        obj = new JSONObject(response.body());

                        if (obj.getString("stat").equals("ok")) {
                            JSONArray photo = obj.getJSONObject("photos").getJSONArray("photo");
                            ArrayList<PhotoApi> url = new ArrayList<>();

                            photoList.clear();
                            for (int i = 0; i < photo.length(); i++) {
                                JSONObject in = photo.getJSONObject(i);
                                photoList.add(new PhotoApi(in.getString("url_s"), in.getString("title")));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
