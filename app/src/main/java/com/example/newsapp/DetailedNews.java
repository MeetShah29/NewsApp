package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedNews extends AppCompatActivity {

    private TextView date, source, title, description, read_more, share_now, first_letter;
    private ImageView img;
    private String Date, Source, Title, Description, ImageUrl, WebUrl, TopicSelected, first_char;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        date = findViewById(R.id.dateTxt);
        source = findViewById(R.id.sourceName);
        title = findViewById(R.id.completeTitle);
        description = findViewById(R.id.contentTxt);
        read_more = findViewById(R.id.readMore);
        share_now = findViewById(R.id.shareNow);
        img = findViewById(R.id.newsImg);
        first_letter = findViewById(R.id.mark);

        Intent intent = getIntent();
        Date = intent.getStringExtra("date");
        Source = intent.getStringExtra("source");
        Title = intent.getStringExtra("title");
        Description = intent.getStringExtra("description");
        ImageUrl = intent.getStringExtra("imageUrl");
        WebUrl = intent.getStringExtra("url");
        TopicSelected = intent.getStringExtra("topicSelected");

        first_char = String.valueOf(Source.charAt(0));
        first_letter.setText(first_char);

        date.setText(Date);
        source.setText(Source);
        Picasso.with(DetailedNews.this).load(ImageUrl).into(img);


        TopicSelected = mainActivity.topicString();
        System.out.println("Value" + Title);

        String replaceWith = "<u>" + TopicSelected + "</u>";
        System.out.println("Msg" + replaceWith);
        String modifiedTitleTxt = Title.replaceAll(TopicSelected, replaceWith);
        String modifiedDescriptionTxt = Description.replaceAll(TopicSelected, replaceWith);
        title.setText(Html.fromHtml(modifiedTitleTxt));
        description.setText(Html.fromHtml(modifiedDescriptionTxt));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void readMoreClick(View view) {
        Intent intent = new Intent(DetailedNews.this, DetailedNewsWebView.class);
        intent.putExtra("url", WebUrl);
        startActivity(intent);
    }

    public void shareNowClick(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "News Cast" + "\n" + Title + "\n" + WebUrl);
        Intent.createChooser(sendIntent, "Share via");
        startActivity(sendIntent);
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}