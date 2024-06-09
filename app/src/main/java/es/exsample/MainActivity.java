package es.exsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    EditText et1, et2;
    Button bt;
    String title, todo;
    int pos;
    Intent it;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        setContentView(ll);

        et1 = new EditText(this);
        et2 = new EditText(this);
        bt = new Button(this);
        bt.setText("OK");

        it = getIntent();
        title = it.getStringExtra("Title");
        todo = it.getStringExtra("Todo");
        pos = it.getIntExtra("Pos",0);
        et1.setText(title);
        et2.setText(todo);

        ll.addView(et1);
        ll.addView(et2);
        ll.addView(bt);

        bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                it.putExtra("Title",et1.getText().toString());
                it.putExtra("Todo",et2.getText().toString());
                it.putExtra("Pos",pos);
                setResult(RESULT_OK,it);
                finish();
            }
        });
    }
}