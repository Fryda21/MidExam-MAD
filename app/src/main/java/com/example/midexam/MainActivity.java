package com.example.midexam;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public static final int MIN_AGE = 17;

    EditText editTextName, editTextId;
    TextView textAge;
    SeekBar seekBarAge;
    Button btnAdd, btnView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextName = findViewById(R.id.editTextName);
        editTextId = findViewById(R.id.editTextId);
        textAge = findViewById(R.id.textAge);
        seekBarAge = findViewById(R.id.seekBarAge);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        dbHelper = new DBHelper(this);

        seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textAge.setText("Age: " + (progress + MIN_AGE));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        btnAdd.setOnClickListener(v -> addStudent());
        btnView.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ViewStudent.class)));
    }

    private void addStudent() {
        String name = editTextName.getText().toString().trim();
        String id = editTextId.getText().toString().trim();
        int age = seekBarAge.getProgress() + MIN_AGE;

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(id)) {
            editTextId.setError("Student ID is required");
            return;
        }

        if (dbHelper.addStudent(new StudentModel(id, name, age))) {
            Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
            editTextName.setText("");
            editTextId.setText("");
            seekBarAge.setProgress(0);
        } else {
            editTextId.setError("Student ID already exists");
            Toast.makeText(this, "Student ID must be unique", Toast.LENGTH_SHORT).show();
        }
    }
}