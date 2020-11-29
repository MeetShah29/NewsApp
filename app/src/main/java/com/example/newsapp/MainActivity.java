package com.example.newsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.Model.Articles;
import com.example.newsapp.Model.Headlines;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String API_KEY = "0c3c7f6d962747d4ac9309c27aa65d99";
    String query = "bitcoin";
    private static String topic, topics;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Articles> articles = new ArrayList<>();
    private TextView dateTxt;
    private EditText etQuery;
    private Button searchBtn;
    private ImageView imageViewCounter, politics, movie, sports, crime;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retriveJson(query, API_KEY);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dateTxt = findViewById(R.id.dateTxt);
        dateTxt.setText(getCurrentDateWithMonthAndYear());

        etQuery = findViewById(R.id.seachEditTxt);
        searchBtn = findViewById(R.id.btnSearch);
        politics = findViewById(R.id.mic);
        movie = findViewById(R.id.movies);
        sports = findViewById(R.id.sports);
        crime = findViewById(R.id.crime);
        topic = "bitcoin";

        shimmerFrameLayout = findViewById(R.id.shrimmer_layout);
        shimmerFrameLayout.startShimmer();

    }

    @Override
    protected void onStart() {
        isConnected();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    //API call
    public void retriveJson(String query, String apiKey) {
        Call<Headlines> call = ApiClient.getInstance().getApi().getHeadlines(query, apiKey);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this, articles);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                isConnected();
                recyclerView.setVisibility(View.GONE);
                if (t instanceof SocketTimeoutException) {
                    retriveJson(query, API_KEY);
                } else if (t instanceof IOException) {
                    // "Timeout";
                } else {
                    //Call was cancelled by user
                    if (call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    //Coversion of date and time
    private String getCurrentDateWithMonthAndYear() {
        return new SimpleDateFormat("dd LLL, yyyy", Locale.getDefault()).format(new Date());
    }

    //Search Box
    public void searchQuery(View view) {
        retriveJson(etQuery.getText().toString(), API_KEY);
        topic = etQuery.getText().toString().toUpperCase();
        closeKeyboard();
    }

    //To hide keyboard
    private void closeKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
    }


    // To check which category i.e Politics,Movies,Sports,Crime is clicked
    public void categoryClick(View view) {
        imageViewCounter = (ImageView) view;

        int tappedCategory = Integer.parseInt(imageViewCounter.getTag().toString());
//        micBtn.setBackgroundColor(Color.GREEN);
        if (tappedCategory == 1) {
//            politics.setColorFilter(Color.RED);
            retriveJson("politics", API_KEY);
            topic = "politics";
        } else if (tappedCategory == 2) {
//            movie.setColorFilter(Color.RED);
            retriveJson("movies", API_KEY);
            topic = "movie";
        } else if (tappedCategory == 3) {
//            sports.setColorFilter(Color.RED);
            retriveJson("sports", API_KEY);
            topic = "sports";
        } else {
//            crime.setColorFilter(Color.RED);
            retriveJson("crime", API_KEY);
            topic = "crime";
        }
    }

    public static String topicString() {
        return topic;
    }


    //DialogBox if internet is not connected
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("No internet connection available")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        recreate();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
                System.exit(0);
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        builder.show();

    }

    //To check Internet connection
    private void isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = connectivityManager.getActiveNetworkInfo();

        if (connection == null || !connection.isConnected() || !connection.isAvailable()) {
            showCustomDialog();
        }
    }
}