package com.example.l7;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserActivity extends AppCompatActivity {
    EditText nameBox, yearBox;
    Button delButton, saveButton;
    DbHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        nameBox = findViewById(R.id.name);
        yearBox = findViewById(R.id.year);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        sqlHelper = new DbHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            userId = extras.getLong("id");
        if (userId > 0) {
            userCursor = db.rawQuery("SELECT * FROM" + DbHelper.TABLE
                    + " WHERE" + DbHelper.COLUMN_ID + "=?", new String[] {String.valueOf(userId)});
            userCursor.moveToFirst();
            nameBox.setText(userCursor.getString(1));
            yearBox.setText(String.valueOf(userCursor.getInt(2)));
            userCursor.close();
        }
        else
            delButton.setVisibility(View.GONE);
    }

    public void save(View view) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DbHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));

        if (userId > 0)
            db.update(DbHelper.TABLE, cv, DbHelper.COLUMN_ID + "=" + userId, null);
        else
            db.insert(DbHelper.TABLE, null, cv);
        goHome();
    }
    public void delete(View view) {
        db.delete(DbHelper.TABLE, "_id=?", new String[]{String.valueOf(userId)});
        goHome();
    }
    public void goHome() {
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}