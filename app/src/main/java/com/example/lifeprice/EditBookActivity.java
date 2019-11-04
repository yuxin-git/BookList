package com.example.lifeprice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {

    private EditText editTextBookName;
    private Button buttonOk,buttonCancel;
    private int editPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editPosition=getIntent().getIntExtra("edit_position",0);

        editTextBookName=(EditText)findViewById(R.id.edit_text_name);
        editTextBookName.setText(getIntent().getStringExtra("edit_name"));

        buttonOk=(Button)findViewById(R.id.button_ok);
        buttonCancel=(Button)findViewById(R.id.button_cancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditBookActivity.this.finish();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                //将要传递的值附加到Intent对象
                intent.putExtra("edit_position",editPosition);
                intent.putExtra("book_name",editTextBookName.getText().toString().trim());
                setResult(RESULT_OK,intent);

                EditBookActivity.this.finish();

            }
        });
    }
}
