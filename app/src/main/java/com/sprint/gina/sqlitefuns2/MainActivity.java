package com.sprint.gina.sqlitefuns2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// a database is like an excel workbook
// a database table is like an excel sheet
// a table has rows and columns
// the rows are uniquely identified by an id
// the columns are uniquely identified by fields (name + type)

// 2 classes to know
// 1. SQLiteOpenHelper: we will subclass this class and implement
// some SQL methods
// CRUD: create, read, update, and delete operations
// 2. SQLiteOpenHelper: a reference type to the database

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivityTag";

    ContactOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        // set up GUI programmatically
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.purple_200));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CustomAdapter adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
        setContentView(recyclerView);

        helper = new ContactOpenHelper(this);
        Contact contact = new Contact("Spike the Bulldog",
                "509-509-5095");
        helper.insertContact(contact);
        List<Contact> contacts = helper.getSelectAllContacts();
        Log.d(TAG, "onCreate: " + contacts);
        Contact updatedContact = new Contact(1, "SPIKE",
                "208-208-2082", -1);
        helper.updateContactById(updatedContact);
        // TODO: deleting all the contacts means that when we insert again
        // our ids and our positions are not off by 1 anymore (BIG BUG!!!)
        helper.deleteAllContacts();
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView text1;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
            }

            public void updateView(Contact c) {
                text1.setText(c.toString());
            }
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            // get the Contact at position
            Contact contact = helper.getSelectContactById(position + 1); // BIG BUG!!!!
            // TODO: fix the bug... there isn't going to be a perfect plus 1 mapping
            // between position and ids when you start deleting
             holder.updateView(contact);
        }

        @Override
        public int getItemCount() {
            // get number of Contacts
            return helper.getSelectAllContacts().size(); // not very efficient!!
        }
    }
}