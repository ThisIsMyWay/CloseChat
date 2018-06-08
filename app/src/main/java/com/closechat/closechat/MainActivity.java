package com.closechat.closechat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {


    EditText loginEditText;
    Button logInActionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectWithView();
        addActionToViews();
    }

    private void connectWithView() {
        loginEditText =  this.findViewById(R.id.login_edit_text);
        logInActionBtn = this.findViewById(R.id.log_in_btn);
    }

    private void addActionToViews() {

        logInActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginEditText.getText().toString().trim().length() > 0) {
                    Toast.makeText( MainActivity.this, "not empty", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText( MainActivity.this, "empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
