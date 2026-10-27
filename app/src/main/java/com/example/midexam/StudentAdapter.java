package com.example.midexam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private final List<StudentModel> students;
    private final Context context;
    private final DBHelper dbHelper;

    public StudentAdapter(List<StudentModel> students, Context context) {
        this.students = students;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel student = students.get(position);
        holder.textViewId.setText("ID: " + student.getStudentId());
        holder.editTextName.setText(student.getName());

        holder.seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                holder.textAge.setText("Age: " + (progress + MainActivity.MIN_AGE));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        holder.seekBarAge.setProgress(student.getAge() - MainActivity.MIN_AGE);
        holder.textAge.setText("Age: " + student.getAge());

        holder.btnEdit.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            String newName = holder.editTextName.getText().toString().trim();
            if (newName.isEmpty()) {
                holder.editTextName.setError("Name is required");
                return;
            }
            int newAge = holder.seekBarAge.getProgress() + MainActivity.MIN_AGE;
            StudentModel updated = new StudentModel(students.get(pos).getStudentId(), newName, newAge);
            dbHelper.updateStudent(updated);
            students.set(pos, updated);
            Toast.makeText(context, "Student updated", Toast.LENGTH_SHORT).show();
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            dbHelper.deleteStudent(students.get(pos).getStudentId());
            students.remove(pos);
            notifyItemRemoved(pos);
            Toast.makeText(context, "Student deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId, textAge;
        EditText editTextName;
        SeekBar seekBarAge;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textAge = itemView.findViewById(R.id.textAge);
            editTextName = itemView.findViewById(R.id.editTextName);
            seekBarAge = itemView.findViewById(R.id.seekBarAge);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}