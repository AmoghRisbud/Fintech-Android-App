package com.example.test2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Adapter;
import com.example.test2.AlphaVantageApi;
import com.example.test2.MainActivity;
import com.example.test2.NewsDetail;
import com.example.test2.R;
import com.example.test2.Utils;
import com.example.test2.api.ApiClient;
import com.example.test2.api.ApiInterface;
import com.example.test2.models.Article;
import com.example.test2.models.News;
import com.example.test2.models.Source;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment  {

    private AlphaVantageApi alphaVantageApi;

    Boolean isScrolling=false;
    ApiInterface apiInterface;
    public static final String API_KEY="e055443a32f0471398420ff3ca57bd4d";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles=new ArrayList<>();
    private Adapter adapter;

    int current_items,total_items,scrolled_items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        recyclerView=view.findViewById(R.id.recyclerview_news);
        layoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        //recyclerView.getRecycledViewPool().clear();
        adapter = new Adapter(articles, view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        ConnectivityManager manager=(ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork=manager.getActiveNetworkInfo();
        if(activenetwork!=null){
            LoadJson();
        }else {
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }




    return view;
    }
    public void LoadJson(){
        ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        String country = Utils.getCountry();
         Call<News> call;
         call=apiInterface.getNews("in","business",API_KEY);
         call.enqueue(new Callback<News>() {
             @Override
             public void onResponse(Call<News> call, Response<News> response) {
                 if (response.isSuccessful() && response.body().getArticles() !=null){
                     /*if (articles.isEmpty()){
                         articles.clear();
                     }*/
                     News news=response.body();
                     List<Article> articles =news.getArticles();

                     Log.d("TAG","Response = "+articles);
                     //recyclerView.getRecycledViewPool().clear();
                     adapter=new Adapter(articles,getActivity());

                     recyclerView.setAdapter(adapter);
                     adapter.notifyDataSetChanged();

                     adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                         @Override
                         public void onItemClick(View v, int position) {
                             Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
                             ImageView imageView = v.findViewById(R.id.newsaimg);
                             Intent intent = new Intent(getActivity(), NewsDetail.class);


                             Article article = articles.get(position);
                             intent.putExtra("url", article.getUrl());
                             intent.putExtra("title", article.getTitle());
                             intent.putExtra("img",  article.getUrlToImage());
                             intent.putExtra("date",  article.getPublishedAt());
                             intent.putExtra("source",  article.getSource().getName());
                             intent.putExtra("author",  article.getAuthor());

                             getContext().startActivity(intent);

                         }
                     });
                 }
             }

             @Override
             public void onFailure(Call<News> call, Throwable t) {
                 Toast.makeText(getContext(),"response failed \n"+t,Toast.LENGTH_SHORT).show();
             }
         });
    }




}
