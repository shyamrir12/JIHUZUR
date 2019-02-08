package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.awizom.jihuzur.MenuActivity;
import com.example.awizom.jihuzur.R;

public class CustomerCommentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton sendBtn,backBtn;
    private AutoCompleteTextView receiverName;
    private EditText messageCommentText;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity_layout);
        initView();
    }

    private void initView() {

        sendBtn = findViewById(R.id.sendBtn);
        receiverName = findViewById(R.id.receverName);
        messageCommentText = findViewById(R.id.messageEditText);
        backBtn = findViewById(R.id.backArrowBtn);
        sendBtn.setOnClickListener(this);
        String messages = messageCommentText.getText().toString().trim();
        if (messages.isEmpty()) {
            messageCommentText.requestFocus();
            return;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendBtn:

                break;
            case R.id.backArrowBtn:
               intent = new Intent(CustomerCommentActivity.this, MenuActivity.class);
               startActivity(intent);
                break;
        }
    }
}
