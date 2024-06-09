package es.exsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText et1, et2;
    Button bt;
    DBHelper dbHelper;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        setContentView(ll);

        et1 = new EditText(this);
        et2 = new EditText(this);
        bt = new Button(this);
        bt.setText("Add Todo");

        listView = new ListView(this);
        dbHelper = new DBHelper(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dbHelper.getAllTodos());
        listView.setAdapter(adapter);

        ll.addView(et1);
        ll.addView(et2);
        ll.addView(bt);
        ll.addView(listView);

        bt.setOnClickListener(v -> {
            addTodo();
            updateTodoList();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String todo = adapter.getItem(position);
            dbHelper.deleteTodo(todo);
            updateTodoList();
        });
    }

    private void addTodo() {
        String title = et1.getText().toString(); // タイトルは今回は使用しない
        String todo = et2.getText().toString();
        dbHelper.addTodo(todo);
        et1.setText("");
        et2.setText("");
    }

    private void updateTodoList() {
        ArrayList<String> todos = dbHelper.getAllTodos();
        adapter.clear();
        adapter.addAll(todos);
        adapter.notifyDataSetChanged();
    }
}
