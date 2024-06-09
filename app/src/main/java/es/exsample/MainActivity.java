package es.exsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText et;
    Button bt, deleteAllButton, lockButton;
    DBHelper dbHelper;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        setContentView(ll);

        et = new EditText(this);
        bt = new Button(this);
        bt.setText("Add Task");
        deleteAllButton = new Button(this);
        deleteAllButton.setText("Delete All Tasks");
        lockButton = new Button(this);
        bt.setBackgroundColor(Color.parseColor("#7FBFFF")); // 明るい青
        deleteAllButton.setBackgroundColor(Color.parseColor("#FF0000")); // 明るい赤
        lockButton.setBackgroundColor(Color.parseColor("#FFFF00")); // 明るい黄色

        listView = new ListView(this);
        dbHelper = new DBHelper(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dbHelper.getAllTodos());
        listView.setAdapter(adapter);

        ll.addView(et);
        ll.addView(bt);
        ll.addView(lockButton);
        ll.addView(deleteAllButton);
        ll.addView(listView);

        updateLockButton();

        bt.setOnClickListener(v -> {
            // データベースがロックされているか否か確認
            if (!dbHelper.isLocked()) {
                // バリデーション
                if (validateInput(et)) {
                    addTodo();
                    updateTodoList();
                } else {
                    Toast.makeText(this, "Please enter a Todo item.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Database is locked!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAllButton.setOnClickListener(v -> {
            // データベースがロックされているか否か確認
            if (!dbHelper.isLocked()) {
                dbHelper.deleteAllTodos();
                updateTodoList();
                Toast.makeText(this, "All todos deleted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Database is locked!", Toast.LENGTH_SHORT).show();
            }
        });

        lockButton.setOnClickListener(v -> {
            boolean currentLockStatus = dbHelper.isLocked();
            dbHelper.setLocked(!currentLockStatus);
            updateLockButton();
            Toast.makeText(this, !currentLockStatus ? "Database Locked" : "Database Unlocked", Toast.LENGTH_SHORT).show();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // データベースがロックされているか否か確認
            if (!dbHelper.isLocked()) {
                String todo = adapter.getItem(position);
                dbHelper.deleteTodo(todo);
                updateTodoList();
            } else {
                Toast.makeText(this, "Database is locked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ロックボタンに表示する文字を更新するメソッド
    private void updateLockButton() {
        boolean isLocked = dbHelper.isLocked();
        lockButton.setText(isLocked ? "UnLock":"Lock");
    }

    private boolean validateInput(EditText editText) {
        String inputText = editText.getText().toString().trim();
        return !inputText.isEmpty();
    }

    private void addTodo() {
        String todo = et.getText().toString().trim();
        dbHelper.addTodo(todo);
        et.setText("");
    }

    private void updateTodoList() {
        ArrayList<String> todos = dbHelper.getAllTodos();
        adapter.clear();
        adapter.addAll(todos);
        adapter.notifyDataSetChanged();
    }
}
