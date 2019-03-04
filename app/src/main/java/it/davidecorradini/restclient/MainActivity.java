package it.davidecorradini.restclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String serverURL = "http://10.8.8.238:8080";
    private LinearLayout searchLayout, addLayout, getLayout;
    private TextView getTitle, getAuthor, getReleaseDate, getPrice, addResult;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    searchLayout.setVisibility(View.VISIBLE);
                    addLayout.setVisibility(View.GONE);
                    getLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_add:
                    addLayout.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.GONE);
                    getLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_get:
                    getLayout.setVisibility(View.VISIBLE);
                    addLayout.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchLayout = findViewById(R.id.searchLayout);
        addLayout = findViewById(R.id.addLayout);
        getLayout = findViewById(R.id.getLayout);

        searchLayout.setVisibility(View.VISIBLE);
        addLayout.setVisibility(View.GONE);
        getLayout.setVisibility(View.GONE);

        getTitle = findViewById(R.id.textViewGetTitle);
        getAuthor = findViewById(R.id.textViewGetAuthor);
        getReleaseDate = findViewById(R.id.textViewGetReleaseDate);
        getPrice = findViewById(R.id.textViewGetPrice);
        addResult = findViewById(R.id.textViewAddResult);


        Button getButton = findViewById(R.id.buttonGet);
        getButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getTitle.setText("Loading...");
                EditText inputId = findViewById(R.id.editTextID);
                getBookRequest(Long.parseLong(inputId.getText().toString()));
            }
        });

        Button addButton = findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addResult.setText("Adding...");
                EditText addTitle, addAuthor, addReleaseDate, addPrice;
                addTitle = findViewById(R.id.editTextAddTitle);
                addAuthor = findViewById(R.id.editTextAddAuthor);
                addReleaseDate = findViewById(R.id.editTextAddReleaseDate);
                addPrice = findViewById(R.id.editTextAddPrice);
                addBookRequest(addTitle.getText().toString(), addAuthor.getText().toString(),
                        addReleaseDate.getText().toString(), Double.parseDouble(addPrice.getText().toString()));
            }
        });
    }


    private void getBookRequest(long id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverURL + "/book?id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json;
                try {
                    json = new JSONObject(response);
                } catch (Exception e) {
                    json = null;
                    e.printStackTrace();
                }
                if (json != null) {
                    try {
                        getTitle.setText(json.getString("title"));
                        getAuthor.setText(json.getString("author"));

                        SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        SimpleDateFormat dateToString = new SimpleDateFormat("yyyy-MM-dd");
                        Date releaseDate = stringToDate.parse(json.getString("releaseDate"));
                        getReleaseDate.setText(dateToString.format(releaseDate));
                        getPrice.setText(String.valueOf(json.getDouble("price")) + "$");
                    } catch (Exception e) {
                        getTitle.setText("Error in JSON.");
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getTitle.setText("That didn't work!");
                Log.d("NET", error.toString());
            }
        });
        queue.add(stringRequest);
    }


    private void addBookRequest(String title, String author, String releaseDateString, double price) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                serverURL + "/book?title=" + title + "&author=" + author + "&releaseDate=" + releaseDateString + "&price=" + Double.toString(price),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("true")) {
                    addResult.setText("Done");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addResult.setText("That didn't work!");
                Log.d("NET", error.toString());
            }
        });
        queue.add(stringRequest);
    }
}