package com.example.squad.atg;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.squad.atg.Interface.getData;
import com.example.squad.atg.adapter.CustomAdapter;
import com.example.squad.atg.adapter.PhotoApi;
import com.example.squad.atg.adapter.RecyclerTouchListener;
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
import static com.example.squad.atg.Constants.method;
import static com.example.squad.atg.Constants.nojsoncallback;
import static com.example.squad.atg.Constants.per_page;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosPage extends Fragment {

    List<PhotoApi> photoList = new ArrayList<>();
    ProgressDialog progressDoalog;
    RecyclerView recyclerView;
    CustomAdapter adapter;

    int page;

    public PhotosPage() {
        // Required empty public constructor
    }

    public static PhotosPage newInstance() {
        PhotosPage fragment = new PhotosPage();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos_page, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CustomAdapter(getContext(), photoList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    getDataPics();
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        PhotoApi temp = photoList.get(position);

                        Intent ans = new Intent(getContext(), picture.class);
                        ans.putExtra("url", temp.getUrl() );
                        ans.putExtra("title",temp.getTitle());
                        startActivity(ans);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );
        recyclerView.setAdapter(adapter);


        getDataPics();

    }

    public void getDataPics() {
        getData service = RetrofitClient.getRetrofitInstance().create(getData.class);
        Call<String> call = service.getAllPhotos(method, per_page, page, api_key, format, nojsoncallback, extras);
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

                            for (int i = 0; i < photo.length(); i++) {
                                JSONObject in = photo.getJSONObject(i);
                                photoList.add(new PhotoApi(in.getString("url_s"),in.getString("title")));
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
