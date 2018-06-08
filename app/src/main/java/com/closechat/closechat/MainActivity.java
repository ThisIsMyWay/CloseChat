package com.closechat.closechat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.closechat.closechat.utils.Utils;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import xyz.dev_juyoung.cropicker.CroPicker;
import xyz.dev_juyoung.cropicker.models.Media;


public class MainActivity extends Activity {


    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 9999;
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
                        myIntent.putExtra("avatarFromFile", pathToImage);
                    }
                    MainActivity.this.startActivity(myIntent);
                } else {
                    Toast.makeText( MainActivity.this, "Write your login, please.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private byte[] retrieveImage(ImageButton imageButton) {
        Bitmap bitmap =  Utils.drawableToBitmap(imageButton.getDrawable());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public void chooseAvatar(View view) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            retrieveAvatar();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveAvatar();
                } else {
                    Toast.makeText( MainActivity.this, "I haven't got permission to your photos :(", Toast.LENGTH_LONG).show();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    String pathToImage = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CroPicker.REQUEST_ALBUM) {
            ArrayList<Media> results = data.getParcelableArrayListExtra(CroPicker.EXTRA_RESULT_IMAGES);
            if (results.size() == 1){
                pathToImage = results.get(0).getImagePath();
                chooseAvatarBtn.setImageDrawable(Drawable.createFromPath(pathToImage));
            }
        }
    }

    private void retrieveAvatar() {
        CroPicker.Options options = new CroPicker.Options();
        options.setLimitedCount(1);
        options.setMessageViewType(CroPicker.REQUEST_ALBUM);


        CroPicker
                .init(MainActivity.this)
                .withOptions(options) //Optional
                .start();
    }
}
