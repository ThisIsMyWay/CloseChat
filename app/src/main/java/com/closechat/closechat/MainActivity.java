package com.closechat.closechat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.closechat.closechat.imageshare.apimodel.ImageResponse;
import com.closechat.closechat.imageshare.apimodel.Upload;
import com.closechat.closechat.imageshare.service.UploadService;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import java.io.File;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity {
    public static final String LOGIN_EXTRA_DATA_ID = "login";
    public static final String AVATAR_FROM_RES_EXTRA_DATA_ID = "avatarFromRes";
    public static final String AVATAR_FROM_HTTP_LINK_EXTRA_DATA_ID = "avatarFromHttp";
    public static final String AVATAR_FROM_PATH_EXTRA_DATA_ID = "avatarFromPath";


    ImageButton chooseAvatarBtn;
    EditText loginEditText;
    Button logInActionBtn;

    FriendsNearby friendsNearby = new FriendsNearby();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectWithView();
        addActionToViews();
    }

    private Handler uiCallback = new Handler() {
        public void handleMessage (Message msg) {

        }
    };

    private void connectWithView() {
        chooseAvatarBtn = this.findViewById(R.id.choose_avatar_btn);
        loginEditText =  this.findViewById(R.id.login_edit_text);
        logInActionBtn = this.findViewById(R.id.log_in_btn);
    }


    private void addActionToViews() {
        logInActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginEditText.getText().toString().trim().length() > 0) {
                    sharePictureAndNavigate();
                } else {
                    Toast.makeText( MainActivity.this, "Write your login, please.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //Retrieving avatar from camera
    public void chooseAvatar(View view) {
        ImagePicker.cameraOnly().start(this);
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

    // upload photo to imugr
    private Upload upload;
    private File chosenFile;

    private void sharePictureAndNavigate() {
        chosenFile = new File(pathToImage);

        if (havePictureToShare()) {
            createUpload(chosenFile);
            new UploadService(this).Execute(upload, new UiCallback());
        } else {
            navigateToUserList(null);
        }
    }

    private boolean havePictureToShare() {
        return chosenFile != null;
    }


    private void createUpload(File image) {
        upload = new Upload();
        upload.image = image;
        upload.title = String.valueOf("avatar of ") + loginEditText.getText();
    }


    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            navigateToUserList(imageResponse);
        }

        @Override
        public void failure(RetrofitError error) {
            if (error == null) {
                Toast.makeText( MainActivity.this, "No internet connection.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void navigateToUserList(@Nullable ImageResponse response) {
        Intent myIntent = new Intent(MainActivity.this, UserList.class);
        myIntent.putExtra(LOGIN_EXTRA_DATA_ID, loginEditText.getText().toString().trim());
        if (response == null) {
            myIntent.putExtra(AVATAR_FROM_RES_EXTRA_DATA_ID, R.drawable.avatar_icon);
        } else {
            myIntent.putExtra(AVATAR_FROM_PATH_EXTRA_DATA_ID, pathToImage);
            myIntent.putExtra(AVATAR_FROM_HTTP_LINK_EXTRA_DATA_ID, response.data.link);
        }
        MainActivity.this.startActivity(myIntent);
    }

}

