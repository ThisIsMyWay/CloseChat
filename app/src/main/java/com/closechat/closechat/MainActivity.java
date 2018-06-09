package com.closechat.closechat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.closechat.closechat.utils.Utils;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import java.io.ByteArrayOutputStream;
import java.util.SortedSet;
import java.util.TreeSet;

public class MainActivity extends Activity {

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


        //TODO change name and url to real
        friendsNearby.setup("Ivan", "https://image.ibb.co/kwbyNo/avatar.jpg", MainActivity.this);

        // TODO periodically pull friends
        final SortedSet<Friend> friends = new TreeSet<>();
        Thread thread = new Thread() {
            @Override
            public void run() {
                SortedSet<Friend> res = friendsNearby.discoverFriends();
                friends.addAll(res);
            }
        };
        thread.start();
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        friendsNearby.teardown(MainActivity.this);
    }
}
