package com.example.plantie.help;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.example.plantie.domen.Plant;
import com.example.plantie.domen.Reminder;
import com.example.plantie.domen.User;

import java.util.ArrayList;

public class DatabaseBroker extends SQLiteOpenHelper {

    private static final String DB_NAME = "plantie.db";
    private static final int DB_VERSION = 1;
    public static DatabaseBroker instance;
    
    public static DatabaseBroker getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseBroker(context);
        return instance;
    }

    public DatabaseBroker(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "CREATE TABLE user ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "firstName TEXT, "+
                "lastName TEXT," +
                "username TEXT," +
                "email TEXT, " +
                "profilePicture TEXT)";

        String query2 = "CREATE TABLE plant ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "commonName TEXT, " +
                "familyCommonName TEXT," +
                "scientificName TEXT," +
                "slug TEXT," +
                "imageURL TEXT)";

        String query3 = "CREATE TABLE reminder (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "plantID INTEGER," +
                "date TEXT NOT NULL," +
                "FOREIGN KEY (plantID) references plant (id))";

        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS plant");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS reminder");

        this.onCreate(sqLiteDatabase);
    }

    public boolean addUser(User u) {
        boolean successful = false;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("firstName", u.getFirstName());
        values.put("lastName", u.getLastName());
        values.put("username", u.getUsername());
        values.put("email", u.getEmail());
        values.put("profilePicture", u.getProfilePicture().toString());

        try {
            db.insert("user", null, values);
            db.close();
            successful = true;
        } catch (Exception e) {
            successful = false;
            e.printStackTrace();
        }
        return successful;
    }

    public boolean addPlant(Plant newPlant) {
        boolean successful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("commonName", newPlant.getCommonName());
        values.put("slug", newPlant.getSlug());
        values.put("familyCommonName", newPlant.getFamilyCommonName());
        values.put("scientificName", newPlant.getScientificName());
        values.put("imageURL", newPlant.getImageURL());

        try {
            db.insert("plant", null, values);
            db.close();
            successful = true;
        } catch (Exception e) {
            successful = false;
            e.printStackTrace();
        }
        return successful;
    }

    public ArrayList<Plant> getAllPlants() {
        ArrayList<Plant> plants = new ArrayList<>();
        String query = "select * from plant";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Plant plant = null;
        while (cursor.moveToNext()) {
            plant = new Plant();
            plant.setId(cursor.getInt(0));
            plant.setCommonName(cursor.getString(1));
            plant.setSlug(cursor.getString(2));
            plant.setFamilyCommonName(cursor.getString(3));
            plant.setScientificName(cursor.getString(4));
            plant.setImageURL(cursor.getString(5));

            plants.add(plant);
        }

        return plants;
    }

    public boolean addReminder(Reminder reminder) {
        boolean successful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("plantID", reminder.getPlant().getId());
        values.put("date", reminder.getDate());

        try {
            db.insert("reminder", null, values);
            db.close();
            successful = true;
        } catch (Exception e) {
            successful = false;
            e.printStackTrace();
        }
        return successful;
    }

    public ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> reminders = new ArrayList<>();
        String query = "select p.id, p.commonName, p.slug, p.familyCommonName, p.scientificName, p.imageURL, r.id, r.date " +
                "from reminder r join plant p on (r.plantid = p.id) order by r.date";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Reminder reminder = null;
        Plant plant = null;
        while (cursor.moveToNext()) {
            plant = new Plant();
            reminder = new Reminder();
            plant.setId(cursor.getInt(0));
            plant.setCommonName(cursor.getString(1));
            plant.setSlug(cursor.getString(2));
            plant.setFamilyCommonName(cursor.getString(3));
            plant.setScientificName(cursor.getString(4));
            plant.setImageURL(cursor.getString(5));
            reminder.setId(cursor.getInt(6));
            reminder.setDate(cursor.getString(7));
            reminder.setPlant(plant);

            reminders.add(reminder);
        }

        return reminders;
    }

    public boolean deleteReminder(Reminder reminder) {
        boolean uspesno = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String upit = "DELETE FROM reminder WHERE id =" +reminder.getId();
            db.execSQL(upit);
            db.close();
            uspesno = true;
        } catch (Exception e) {
            e.printStackTrace();
            uspesno = false;
        }
       return uspesno;
    }

    public boolean removePlant(Plant currentPlant) {
        boolean uspesno = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String upit = "DELETE FROM plant WHERE id =" +currentPlant.getId();
            db.execSQL(upit);
            db.close();
            uspesno = true;
        } catch (Exception e) {
            e.printStackTrace();
            uspesno = false;
        }
        return uspesno;
    }
}
