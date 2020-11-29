package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Model.Articles;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    List<Articles> articles = new ArrayList<>();


    public Adapter(Context context, List<Articles> articles) {
        this.context = context;
        this.articles = articles;

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSource, tvDate;
        ShapeableImageView imageView;
        ConstraintLayout itemsLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.daysTxt);
            imageView = itemView.findViewById(R.id.image);
            itemsLayout = itemView.findViewById(R.id.items_layout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Articles a = articles.get(position);
        holder.tvTitle.setText(a.getTitle());
        holder.tvDate.setText(dateTime(a.getPublishedAt()));

        String imageUrl = a.getUrlToImage();

        Picasso.with(context).load(imageUrl).into(holder.imageView);

        holder.itemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedNews.class);
                intent.putExtra("date", dateTime(a.getPublishedAt()));
                intent.putExtra("title", a.getTitle());
                intent.putExtra("source", a.getSource().getName());
                intent.putExtra("description", a.getDescription());
                intent.putExtra("imageUrl", a.getUrlToImage());
                intent.putExtra("url", a.getUrl());
                intent.putExtra("topicSelected",MainActivity.topicString());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();

        return country.toLowerCase();
    }

    private String dateTime(String t) {
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }
}


