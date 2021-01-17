package com.example.lutescensapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TaskMenu extends AppCompatActivity {

    // Creating the listView that will define the action (or task) menu in the application. Each element in the list have his own image,
    // title and brief description. Than, when the user taps on the element, the activity of that element get showed.
    ListView list;
    String titles[] = {"I won't use my car", "I won't buy bottled water", "I will use my portable coffee cup", "I will collect rubbish on the street",
            "I will save water", "I will save electricity", "I will plant a tree"};
    String descriptions[] = {"Start using a bike or public transports and get rid of your vehicle",
            "Drink tap water and don't buy water in plastic bottles", "Ask coffee shop baristas to make coffee directly in your own cup and don't use their ones",
            "Collect rubbish that you find while walking around", "Save water during your daily routine", "Save electricity during your daily routine", "Plant a tree somewhere"};
    int imgs[] = {R.drawable.nocar, R.drawable.plasticbottle, R.drawable.coffeecup, R.drawable.trashcollection, R.drawable.water, R.drawable.light, R.drawable.tree};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmenu);

        list = findViewById(R.id.listViewAction);

        // Create and set instance of the adapter
        Adpt adpt = new Adpt(this, titles, imgs, descriptions);
        list.setAdapter(adpt);

        // Handle clicks on the items listed in the view (the action/task menu)
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 0){
                    Intent intent = new Intent(TaskMenu.this, CarTask.class);
                    startActivity(intent);
                }
                if (position == 1){
                    Intent intent = new Intent(TaskMenu.this, BottledWaterTask.class);
                    startActivity(intent);
                }
                if (position == 2){
                    Intent intent = new Intent(TaskMenu.this, CoffeeTask.class);
                    startActivity(intent);
                }
                if (position == 3){
                    Intent intent = new Intent(TaskMenu.this, RubbishTask.class);
                    startActivity(intent);
                }
                if (position == 4){
                    Intent intent = new Intent(TaskMenu.this, SaveWaterTask.class);
                    startActivity(intent);
                }
                if (position == 5) {
                    Intent intent = new Intent(TaskMenu.this, SaveElectricityTask.class);
                    startActivity(intent);
                }
                if (position == 6){
                    Intent intent = new Intent(TaskMenu.this, TreeTask.class);
                    startActivity(intent);
                }

            }
        });
    }

    class Adpt extends ArrayAdapter<String>{

        Context context;
        String myTitles[];
        String myDescriptions[];
        int[] imgs;

        Adpt(Context c, String[] titles, int[] imgs, String[] descriptions){
            super(c, R.layout.row_action, R.id.titleAction, titles);
            this.context = c;
            this.imgs = imgs;
            this.myTitles = titles;
            this.myDescriptions = descriptions;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // Defines the component of the row of the listView
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_action, parent, false);
            ImageView imageView = row.findViewById(R.id.logoAction);
            TextView myTitle = row.findViewById(R.id.titleAction);
            TextView myDescription = row.findViewById(R.id.descriptionAction);
            imageView.setImageResource(imgs[position]);
            myTitle.setText(titles[position]);
            myDescription.setText(descriptions[position]);

            return row;
        }
    }
}
