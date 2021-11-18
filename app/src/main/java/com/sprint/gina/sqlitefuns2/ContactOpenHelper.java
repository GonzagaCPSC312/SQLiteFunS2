package com.sprint.gina.sqlitefuns2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContactOpenHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "contactsDatabase.db";
    static final int DATABASE_VERSION = 1;

    static final String CONTACTS_TABLE = "tableContacts";
    static final String ID = "_id"; // by convention
    static final String NAME = "name";
    static final String PHONE_NUMBER = "phoneNumber";
    static final String IMAGE_RESOURCE_ID = "imageResourceId";

    public ContactOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // this where we create our database tables
        // our database will have one table to store contacts
        // AKA records AKA rows
        // this method only executes one time
        // right before the first call to getWriteableDatabase()
        String sqlCreate = "CREATE TABLE " + CONTACTS_TABLE + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                PHONE_NUMBER + " TEXT, " +
                IMAGE_RESOURCE_ID + " INTEGER)";
        // construct the SQL (structured query language) statement
        // and execute it
        Log.d(MainActivity.TAG, "onCreate: " + sqlCreate);
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertContact(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, contact.getName());
        contentValues.put(PHONE_NUMBER, contact.getPhoneNumber());
        contentValues.put(IMAGE_RESOURCE_ID, contact.getImageResourceId());

        // get a writeable ref to the database
        SQLiteDatabase db = getWritableDatabase();
        db.insert(CONTACTS_TABLE, null, contentValues);
        // close the writeable ref when done!!
        db.close();
    }

    // select (read of crud)
    // helper method
    public Cursor getSelectAllCursor() {
        // we need to construct a query to get a cursor
        // to step through records
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(CONTACTS_TABLE, new String[]{ID,
                                                    NAME,
                                                    PHONE_NUMBER,
                                                    IMAGE_RESOURCE_ID},
                null, null, null,
                null, null);
        return cursor;
    }

    public List<Contact> getSelectAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = getSelectAllCursor();
        // the cursor starts "before" the first record
        // in case there is no first record
        while (cursor.moveToNext()) { // returns false when no more records to process
            // parse the field values
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phoneNumber = cursor.getString(2);
            int imageResourceId = cursor.getInt(3);
            Contact contact = new Contact(id, name, phoneNumber, imageResourceId);
            contacts.add(contact);
        }
        return contacts;
    }

    public Contact getSelectContactById(int idParam) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(CONTACTS_TABLE, new String[]{ID,
                        NAME,
                        PHONE_NUMBER,
                        IMAGE_RESOURCE_ID},
                ID + "=?", new String[]{"" + idParam}, null,
                null, null);
        Contact contact = null;
        if (cursor.moveToNext()) { // returns false when no more records to process
            // parse the field values
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phoneNumber = cursor.getString(2);
            int imageResourceId = cursor.getInt(3);
            contact = new Contact(id, name, phoneNumber, imageResourceId);
        }
        return contact;
    }

    public void updateContactById(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, contact.getName());
        contentValues.put(PHONE_NUMBER, contact.getPhoneNumber());
        contentValues.put(IMAGE_RESOURCE_ID, contact.getImageResourceId());

        SQLiteDatabase db = getWritableDatabase();
        db.update(CONTACTS_TABLE, contentValues, ID + "=?",
                new String[]{"" + contact.getId()});
        db.close();
    }

    public void deleteAllContacts() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CONTACTS_TABLE, null, null);
        db.close();
    }
}
