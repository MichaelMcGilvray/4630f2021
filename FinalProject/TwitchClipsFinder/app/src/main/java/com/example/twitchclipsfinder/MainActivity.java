package com.example.twitchclipsfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView categorySearch;
    private Button searchButton;

    private Tuple categories[];
    private String selectedCategoryID = "743";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categorySearch = findViewById(R.id.categorySearch);
        categorySearch.setThreshold(1);
        categorySearch.setOnItemClickListener(this);
        categorySearch.setHint("Category");

        // Setup the autocomplete results when new text is entered
        categorySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = categorySearch.getText().toString();

                if (query.length() > 0) {
                    Tuple[] tuples = new Tuple[0];

                    try {
                        tuples = new clipGenerator().getCategory(query);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    categories = tuples;
                    String autocompleteArr[] = new String[tuples.length];
                    for (int i = 0; i < autocompleteArr.length; i++) {
                        autocompleteArr[i] = tuples[i]._key;
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, autocompleteArr);
                    categorySearch.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Setup the search button
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    generateClips();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // When an autocomplete item is selected, it will replace the text in categorySearch
        String selected = categorySearch.getText().toString();

        // Check if selected is a category
        for(int i = 0; i < categories.length; i++) {
            if (selected.equals(categories[i]._key)) {
                // System.out.print("Selected = " + categories[i]._key + ", " + categories[i]._value + "\n");
                selectedCategoryID = categories[i]._value;
            }
        }

        // Hide the keyboard when the user picks an item
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public void openClipWatchingActivity(Clip clip) {
        Intent intent = new Intent(this, Clip_Watching.class);
        intent.putExtra("clip", clip);
        startActivity(intent);
    }

    public void generateClips() throws InterruptedException, ExecutionException, JSONException, IOException {
        LinearLayout linearLayout = findViewById(R.id.clipsList);

        // Temporarily, I will just generate 10 clips
        final int totalClips = 10;

        Clip[] arrOfClips = new clipGenerator().getClips(selectedCategoryID);

        for (int i = 0; i < totalClips; i++) {
            createClipView(arrOfClips[i]);
        }
    }

    public void createClipView(Clip clip) {
        LinearLayout linearLayout = findViewById(R.id.clipsList);

        ImageView imageView = new ImageView(this);

        // Use Picasso to load image into ImageView using a URL
        Picasso.get().load(clip._thumbnail).into(imageView);

        // LinearLayout.LayoutParams imageParameters = new LinearLayout.LayoutParams(480, 720);
        // imageView.setLayoutParams(imageParameters);

        // Make the ImageView clickable and open the clip watching screen
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClipWatchingActivity(clip);
            }
        });

        // Add the imageView to the linearLayout (scrolling section in app)
        linearLayout.addView(imageView);

        // Create title
        TextView textView = new TextView(this);
        textView.setText(clip._title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        linearLayout.addView(textView);
    }
}