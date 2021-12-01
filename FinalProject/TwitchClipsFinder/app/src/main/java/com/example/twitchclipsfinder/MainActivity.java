package com.example.twitchclipsfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.app.ActionBar;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView categorySearch;
    private AutoCompleteTextView streamerSearch;

    private DatePicker datePicker;
    private RadioGroup radioGroup;
    private Button searchButton;

    private Tuple categories[];
    private Tuple streamers[];

    private String selectedCategoryID = "";
    private String selectedStreamerID = "";
    private String selectedStartDate = "";
    private String selectedEndDate = "";
    private String selectedTimeInterval = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        streamerSearch = findViewById(R.id.streamerSearch);
        streamerSearch.setThreshold(1);
        streamerSearch.setOnItemClickListener(new streamerSearchListener(streamerSearch));
        streamerSearch.setHint("Streamer");

        categorySearch = findViewById(R.id.categorySearch);
        categorySearch.setThreshold(1);
        categorySearch.setOnItemClickListener(new categorySearchListener(categorySearch));
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

        // Setup the autocomplete results when new text is entered
        streamerSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = streamerSearch.getText().toString();

                if (query.length() > 0) {
                    Tuple[] tuples = new Tuple[0];

                    try {
                        tuples = new clipGenerator().getStreamer(query);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    streamers = tuples;
                    String autocompleteArr[] = new String[tuples.length];
                    for (int i = 0; i < autocompleteArr.length; i++) {
                        autocompleteArr[i] = tuples[i]._key;
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, autocompleteArr);
                    streamerSearch.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        datePicker = findViewById(R.id.datePicker);


        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                selectedTimeInterval = radioButton.getText().toString().toLowerCase(Locale.ROOT);
                // System.out.print("Selected " + selectedTimeInterval + "\n");
            }
        });
        



        // Setup the search button
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(selectedTimeInterval.equals("none")) && !(selectedTimeInterval.equals(""))) {
                    try {
                        calculateStartAndEndDate();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    generateClips();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    displayMessage("No clips could be found using the given search criteria.");
                    // e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                closeKeyboard();
            }
        });
    }



    public class categorySearchListener implements AdapterView.OnItemClickListener {
        AutoCompleteTextView _autoCompleteTextView;

        public categorySearchListener(AutoCompleteTextView autoCompleteTextView) {
            _autoCompleteTextView = autoCompleteTextView;
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
        }
    }

    public class streamerSearchListener implements AdapterView.OnItemClickListener {
        AutoCompleteTextView _autoCompleteTextView;

        public streamerSearchListener(AutoCompleteTextView autoCompleteTextView) {
            _autoCompleteTextView = autoCompleteTextView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // When an autocomplete item is selected, it will replace the text in streamerSearch
            String selected = streamerSearch.getText().toString();

            // Check if selected is a streamer
            for(int i = 0; i < streamers.length; i++) {
                if (selected.equals(streamers[i]._key)) {
                    // System.out.print("Selected = " + streamers[i]._key + ", " + streamers[i]._value + "\n");
                    selectedStreamerID = streamers[i]._value;
                }
            }
        }
    }



    public void openClipWatchingActivity(Clip clip) {
        Intent intent = new Intent(this, Clip_Watching.class);
        intent.putExtra("clip", clip);
        startActivity(intent);
    }

    public void generateClips() throws InterruptedException, ExecutionException, JSONException, IOException {
        LinearLayout linearLayout = findViewById(R.id.clipsList);

        Tuple ret = new Tuple();
        Clip[] arrOfClips = new Clip[0];
        String pagination = "";

        int totalClipsViews = 0;
        final int MAX_CLIPS = 20;
        int numberOfRequests = 0;
        final int MAX_REQUESTS = 10;

        // If no fields entered
        if ((selectedCategoryID == "") && (selectedStreamerID == "")) {
            displayMessage("Please specify a filtering method.");
            return;
        }

        while ((totalClipsViews < MAX_CLIPS) && (numberOfRequests < MAX_REQUESTS)) {
            // System.out.print("number of requests = " + numberOfRequests + "\n");

            // If only one field is entered
            if ((selectedCategoryID != "") && (selectedStreamerID == "")) {
                ret = new clipGenerator().getClipsFromCategory(selectedCategoryID, pagination, selectedStartDate, selectedEndDate);
                numberOfRequests++;
                arrOfClips = ret._clips;
                pagination = ret._key;

                for (int i = 0; (i < arrOfClips.length) && (i < MAX_CLIPS); i++) {
                    createClipView(arrOfClips[i]);
                    totalClipsViews++;
                }
            }
            else if ((selectedCategoryID == "") && (selectedStreamerID != "")) {
                ret = new clipGenerator().getClipsFromStreamer(selectedStreamerID, pagination, selectedStartDate, selectedEndDate);
                numberOfRequests++;
                arrOfClips = ret._clips;
                pagination = ret._key;

                for (int i = 0; (i < arrOfClips.length) && (i < MAX_CLIPS); i++) {
                    createClipView(arrOfClips[i]);
                    totalClipsViews++;
                }
            }

            // If a combination of the fields are entered
            if ((selectedCategoryID != "") && (selectedStreamerID != "")) {
                ret = new clipGenerator().getClipsFromStreamer(selectedStreamerID, pagination, selectedStartDate, selectedEndDate);
                numberOfRequests++;
                arrOfClips = ret._clips;
                pagination = ret._key;

                // Then check if the arrOfClips are also in the category
                for (int i = 0; (i < arrOfClips.length) && (i < MAX_CLIPS); i++) {
                    if (arrOfClips[i]._game_id.equals(selectedCategoryID)) {
                        createClipView(arrOfClips[i]);
                        totalClipsViews++;
                    }
                }
            }
        }

        // "Error" messages
        System.out.print("number of requests = " + numberOfRequests + "\n");
        System.out.print("Total clip views = " + totalClipsViews + "\n");
        if ((numberOfRequests >= MAX_REQUESTS) && (totalClipsViews <= 0)) {
            displayMessage("No clips could be found using the given search criteria.");
        } else if (numberOfRequests >= MAX_REQUESTS) {
            displayMessage("These are all the clips that could be found using the given search criteria.");
        }
        
        // Reset search criteria
        selectedStreamerID = "";
        selectedCategoryID = "";
        categorySearch.setText("");
        streamerSearch.setText("");
    }

    public void createClipView(Clip clip) {
        LinearLayout linearLayout = findViewById(R.id.clipsList);

        ImageView imageView = new ImageView(this);

        // Use Picasso to load image into ImageView using a URL
        Picasso.get().load(clip._thumbnail).into(imageView);

        LinearLayout.LayoutParams imageParameters = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 500);
        imageView.setLayoutParams(imageParameters);

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
        TextView title = new TextView(this);
        title.setText(clip._title);
        title.setTextColor(Color.BLACK);
        title.setPadding(100,10, 0, 0);
        title.setTextSize(18);
        linearLayout.addView(title);

        // Create streamer name
        TextView streamer = new TextView(this);
        streamer.setText(clip._broadcaster_name);
        streamer.setTextColor(Color.BLACK);
        streamer.setPadding(100,0, 0, 0);
        streamer.setTextSize(14);
        linearLayout.addView(streamer);

        // Create views
        TextView views = new TextView(this);
        views.setText((NumberFormat.getInstance().format(clip._views)) + " views");
        views.setTextColor(Color.BLACK);
        views.setPadding(100,0, 0, 50);
        views.setTextSize(14);
        linearLayout.addView(views);
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void displayMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.mainActivityLayout), message, 5000);
        snackbar.show();
    }

    public void calculateStartAndEndDate() throws ParseException {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;  // Months are indexed starting at 0 in Android DatePicker
        int year = datePicker.getYear();
        // System.out.print("Unconverted start date = " + year + "-" + month + "-" + day + "\n");
        selectedStartDate = convertDateToRFC(year, month, day);

        String temp = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
        SimpleDateFormat simpleDateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormater.parse(temp));


        if (selectedTimeInterval.equals("day")) {
            calendar.add(calendar.DATE, 1);
        } else if (selectedTimeInterval.equals("week")) {
            calendar.add(calendar.DATE, 7);
        } else if (selectedTimeInterval.equals("month")) {
            calendar.add(calendar.DATE, 30);
        }

        // System.out.print("Unconverted end date = " + calendar.get(calendar.YEAR) + "-" + calendar.get(calendar.MONTH) + "-" + calendar.get(calendar.DAY_OF_MONTH) + "\n");
        selectedEndDate = convertDateToRFC(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1, calendar.get(calendar.DAY_OF_MONTH));
    }

    public String convertDateToRFC(int year, int month, int day) {
        String _year = String.valueOf(year);
        String _month = String.valueOf(month);
        if (_month.length() == 1) {
            _month = "0" + _month;
        }
        String _day = String.valueOf(day);
        if (_day.length() == 1) {
            _day = "0" + _day;
        }
        String ret = _year + "-" + _month + "-" + _day + "T00:00:01.52Z";
        return ret;
    }
}


/*
curl -X GET 'https://api.twitch.tv/helix/clipscurl -X GET 'https://api.twitch.tv/helix/clips?game_id=32982' -H 'Authorization: Bearer hlpj3tm0717ydxtihrn78kwywsup6i' -H 'Client-Id: k61784vtuptn3u2aes0xf6ss1e2nf5'' -H 'Authorization: Bearer hlpj3tm0717ydxtihrn78kwywsup6i' -H 'Client-Id: k61784vtuptn3u2aes0xf6ss1e2nf5'
- Returns 20 clips

curl -X GET 'https://api.twitch.tv/helix/clips?broadcaster_id=95873995' -H 'Authorization: Bearer hlpj3tm0717ydxtihrn78kwywsup6i' -H 'Client-Id: k61784vtuptn3u2aes0xf6ss1e2nf5'
- Returns 19 clips

curl -X GET 'https://api.twitch.tv/helix/clips?broadcaster_id=26301881' -H 'Authorization: Bearer hlpj3tm0717ydxtihrn78kwywsup6i' -H 'Client-Id: k61784vtuptn3u2aes0xf6ss1e2nf5'


Sodapoppin ID = 26301881
Just chatting ID = 509658




GetClips with pagination:
curl -X GET 'https://api.twitch.tv/helix/clips?broadcaster_id=95873995&after=2' -H 'Authorization: Bearer hlpj3tm0717ydxtihrn78kwywsup6i' -H 'Client-Id: k61784vtuptn3u2aes0xf6ss1e2nf5'
Cursor = eyJiIjpudWxsLCJhIjp7IkN1cnNvciI6Ik1qQT0ifX0



GetClips by date
curl -X GET 'https://api.twitch.tv/helix/clips?broadcaster_id=95873995&started_at=2021-10-03T00:00:01.52Z' -H 'Authorization: Bearer hlpj3tm0717ydxtihrn78kwywsup6i' -H 'Client-Id: k61784vtuptn3u2aes0xf6ss1e2nf5'


GetClips with start and end date
curl -X GET 'https://api.twitch.tv/helix/clips?game_id=32982&started_at=2021-10-03T00:00:01.52Z&ended_at=2021-11-03T00:00:01.52Z' -H 'Authorization: Bearer hlpj3tm0717ydxtihrn78kwywsup6i' -H 'Client-Id: k61784vtuptn3u2aes0xf6ss1e2nf5'
 */