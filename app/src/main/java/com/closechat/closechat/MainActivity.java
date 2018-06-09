package com.closechat.closechat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.closechat.closechat.imageshare.apimodel.ImageResponse;
import com.closechat.closechat.imageshare.apimodel.Upload;
import com.closechat.closechat.imageshare.service.UploadService;
import com.closechat.closechat.utils.Utils;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity {

    ImageButton chooseAvatarBtn;
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
        chooseAvatarBtn = this.findViewById(R.id.choose_avatar_btn);
        loginEditText =  this.findViewById(R.id.login_edit_text);
        logInActionBtn = this.findViewById(R.id.log_in_btn);
    }


    private void addActionToViews() {
        logInActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginStr = loginEditText.getText().toString().trim();
                if (loginStr.length() > 0) {
                    Intent myIntent = new Intent(MainActivity.this, UserList.class);
                    myIntent.putExtra("login", loginStr);
                    if (pathToImage == null) {
                        myIntent.putExtra("avatarFromRes", R.drawable.avatar_icon);
                    } else {
                        chosenFile = new File(pathToImage);
                        myIntent.putExtra("avatarFromFile", pathToImage);
                    }
                    uploadChoosenAvatar();
                } else {
                    Toast.makeText( MainActivity.this, "Write your login, please.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    private Upload upload;
    private File chosenFile;

    private void uploadChoosenAvatar() {
        if (chosenFile == null) return;
        createUpload(chosenFile);

        new UploadService(this).Execute(upload, new UiCallback());
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = String.valueOf("avatar of ") + loginEditText.getText();
    }


    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
//                    MainActivity.this.startActivity(myIntent);

        }

        @Override
        public void failure(RetrofitError error) {
            if (error == null) {
                Toast.makeText( MainActivity.this, "No internet connection.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void chooseAvatar(View view) {
            retrieveAvatar();
    }

    String pathToImage = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            pathToImage = image.getPath();
            chooseAvatarBtn.setImageDrawable(Drawable.createFromPath(pathToImage));
        }
    }

    private void retrieveAvatar() {
        ImagePicker.cameraOnly().start(this);
    }
}
