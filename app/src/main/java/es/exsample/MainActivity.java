package es.exsample;

import android.content.Context;
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
    Button bt,deleteAllButton;
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
        bt.setText("Add Todo");
        deleteAllButton = new Button(this);
        deleteAllButton.setText("Delete All Todos");

        listView = new ListView(this);
        dbHelper = new DBHelper(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dbHelper.getAllTodos());
        listView.setAdapter(adapter);

        ll.addView(et);
        ll.addView(bt);
        ll.addView(deleteAllButton);
        ll.addView(listView);

        bt.setOnClickListener(v -> {
            if (validateInput(et)) {  // ToDoの入力を検証
                addTodo();
                updateTodoList();
            } else {
                Toast.makeText(this, "Please enter a Todo item.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAllButton.setOnClickListener(v -> {
            dbHelper.deleteAllTodos();
            updateTodoList();
            Toast.makeText(this, "All todos deleted.", Toast.LENGTH_SHORT).show();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String todo = adapter.getItem(position);
            dbHelper.deleteTodo(todo);
            updateTodoList();
        });
    }

    private boolean validateInput(EditText editText) {
        String inputText = editText.getText().toString().trim();
        return !inputText.isEmpty();
    }

    private void addTodo() {
        String todo = et.getText().toString().trim();
        dbHelper.addTodo(todo);
        et.setText(""); // ToDo入力フィールドをクリア
    }

    private void updateTodoList() {
        ArrayList<String> todos = dbHelper.getAllTodos();
        adapter.clear();
        adapter.addAll(todos);
        adapter.notifyDataSetChanged();
    }
}
