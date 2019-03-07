package it.davidecorradini.restclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String serverURL = "http://192.168.28.66:8080";
    private LinearLayout searchLayout, addLayout, getLayout;
    private TextView searchResults, getTitle, getAuthor, getReleaseDate, getPrice, getResult, addResult;
    private EditText searchQuery, inputId,editTitle, editAuthor, editReleaseDate, editPrice;
    private Button getButton, addButton, deleteButton, editButton, searchButton;
    private RequestQueue queue;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchLayout = findViewById(R.id.searchLayout);
        addLayout = findViewById(R.id.addLayout);
        getLayout = findViewById(R.id.getLayout);

        searchLayout.setVisibility(View.VISIBLE);
        addLayout.setVisibility(View.GONE);
        getLayout.setVisibility(View.GONE);

        searchQuery = findViewById(R.id.editTextSearchQuery);
        inputId = findViewById(R.id.editTextID);
        getTitle = findViewById(R.id.textViewGetTitle);
        getAuthor = findViewById(R.id.textViewGetAuthor);
        getReleaseDate = findViewById(R.id.textViewGetReleaseDate);
        getPrice = findViewById(R.id.textViewGetPrice);
        getResult = findViewById(R.id.textViewGetResult);
        addResult = findViewById(R.id.textViewAddResult);
        searchResults = findViewById(R.id.textViewSearchResults);

        editTitle = findViewById(R.id.editTextEditTitle);
        editAuthor = findViewById(R.id.editTextEditAuthor);
        editReleaseDate = findViewById(R.id.editTextEditReleaseDate);
        editPrice = findViewById(R.id.editTextEditPrice);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //mAdapter = new MyAdapter();
        //recyclerView.setAdapter(mAdapter);



        queue = Volley.newRequestQueue(this);

        getButton = findViewById(R.id.buttonGet);
        getButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getResult.setText("Loading...");
                getTitle.setVisibility(View.VISIBLE);
                getAuthor.setVisibility(View.VISIBLE);
                getReleaseDate.setVisibility(View.VISIBLE);
                getPrice.setVisibility(View.VISIBLE);
                editTitle.setVisibility(View.GONE);
                editAuthor.setVisibility(View.GONE);
                editReleaseDate.setVisibility(View.GONE);
                editPrice.setVisibility(View.GONE);

                getBookRequest(Long.parseLong(inputId.getText().toString()));
            }
        });

        addButton = findViewById(R.id.buttonAdd);
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

        deleteButton = findViewById(R.id.buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputId = findViewById(R.id.editTextID);
                deleteBookRequest(Long.parseLong(inputId.getText().toString()));
            }
        });

        editButton = findViewById(R.id.buttonEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editButton.getText().toString().equals("Edit")) {
                    getTitle.setVisibility(View.GONE);
                    getAuthor.setVisibility(View.GONE);
                    getReleaseDate.setVisibility(View.GONE);
                    getPrice.setVisibility(View.GONE);
                    editTitle.setVisibility(View.VISIBLE);
                    editAuthor.setVisibility(View.VISIBLE);
                    editReleaseDate.setVisibility(View.VISIBLE);
                    editPrice.setVisibility(View.VISIBLE);
                    editButton.setText("Save");
                } else if (editButton.getText().toString().equals("Save")) {
                    getResult.setText("Saving...");
                    editButton.setEnabled(false);
                    editBookRequest(Long.parseLong(inputId.getText().toString()), editTitle.getText().toString(), editAuthor.getText().toString(),
                            editReleaseDate.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                }
            }
        });

        searchButton = findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResults.setText("Loading...");
                searchBookRequest(searchQuery.getText().toString());
            }
        });
    }


    private void getBookRequest(long id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverURL + "/book?id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("")) {
                    getResult.setText("Book not found.");
                    return;
                }
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
                        editTitle.setText(json.getString("title"));
                        getAuthor.setText(json.getString("author"));
                        editAuthor.setText(json.getString("author"));
                        SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        SimpleDateFormat dateToString = new SimpleDateFormat("yyyy-MM-dd");
                        Date releaseDate = stringToDate.parse(json.getString("releaseDate"));
                        getReleaseDate.setText(dateToString.format(releaseDate));
                        editReleaseDate.setText(dateToString.format(releaseDate));
                        getPrice.setText(String.valueOf(String.format("%.2f$", json.getDouble("price"))));
                        editPrice.setText(String.valueOf(json.getDouble("price")));
                        editButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        getResult.setText("Book found!");
                    } catch (Exception e) {
                        getResult.setText("Error in JSON.");
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getResult.setText("That didn't work!");
                Log.d("NET", error.toString());
            }
        });
        queue.add(stringRequest);
    }


    private void addBookRequest(String title, String author, String releaseDateString, double price) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                serverURL + "/book?title=" + title + "&author=" + author + "&releaseDate=" + releaseDateString + "&price=" + Double.toString(price),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (Long.parseLong(response) > 0) {
                    addResult.setText("Book added with ID " + response);
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


    private void deleteBookRequest(long id) {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                serverURL + "/book?id=" + Long.toString(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("true")) {
                    getResult.setText("Deleted!");
                    getTitle.setText("-");
                    editTitle.setText("");
                    getAuthor.setText("-");
                    editAuthor.setText("");
                    getReleaseDate.setText("-");
                    editReleaseDate.setText("");
                    getPrice.setText("-");
                    editPrice.setText("");
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                } else if (response.equals("false")) {
                    getResult.setText("Could not delete. (ID does not exist)");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getResult.setText("Could not delete. (Network)");
                Log.d("NET", error.toString());
            }
        });
        queue.add(stringRequest);
    }


    private void editBookRequest(long id, String title, String author, String releaseDateString, double price) {
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH,
                serverURL + "/book?id=" + Long.toString(id) + "&title=" + title + "&author=" + author + "&releaseDate=" + releaseDateString + "&price=" + Double.toString(price),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("true")) {
                            getResult.setText("Saved");
                            getBookRequest(Long.parseLong(inputId.getText().toString()));
                            editButton.setText("Edit");
                            editButton.setEnabled(true);
                            getTitle.setVisibility(View.VISIBLE);
                            getAuthor.setVisibility(View.VISIBLE);
                            getReleaseDate.setVisibility(View.VISIBLE);
                            getPrice.setVisibility(View.VISIBLE);
                            editTitle.setVisibility(View.GONE);
                            editAuthor.setVisibility(View.GONE);
                            editReleaseDate.setVisibility(View.GONE);
                            editPrice.setVisibility(View.GONE);
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


    private void searchBookRequest(String query) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                serverURL + "/books?query=" + query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsona;
                String out = "";
                try {
                    jsona = new JSONArray(response);
                    if (jsona.length() < 1) {
                        searchResults.setText("No result found.");
                    } else {
                        for (int i = 0; i < jsona.length(); i++) {
                            JSONObject o = jsona.getJSONObject(i);
                            out += "[" + o.getLong("id") + "] " + o.getString("title") + " - " + o.getString("author") + "\n";
                        }
                        searchResults.setText(out);
                    }
                } catch (Exception e) {
                    searchResults.setText("json exception " + response);
                    jsona = null;
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchResults.setText("That didn't work!");
                Log.d("NET", error.toString());
            }
        });
        queue.add(stringRequest);
    }
}