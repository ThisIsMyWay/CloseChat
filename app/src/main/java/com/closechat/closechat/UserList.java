package com.closechat.closechat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UserList extends Activity {
    final List<Friend> friends = Collections.synchronizedList(  new LinkedList<Friend>());

    String[] IMAGES = {"https://image.ibb.co/kwbyNo/avatar.jpg", "https://image.ibb.co/kwbyNo/avatar.jpg", "https://image.ibb.co/kwbyNo/avatar.jpg"};
    String[] NAMES = {"user1", "koziolekmatolek", "agent BOLEK"};
    String[] DESCRIPTION = {"jakiś nob", "kolo z Pacanowa", "a ten był prezydentem"};
    FriendsNearby friendsNearby = new FriendsNearby();
    ListView userList = null;
    private final Picasso picasso = Picasso.get();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();

        TextView textView_login = (TextView) findViewById(R.id.textView_login);
        textView_login.setText(intent.getStringExtra(MainActivity.LOGIN_EXTRA_DATA_ID));

        String linkToImg = intent.getStringExtra(MainActivity.AVATAR_FROM_HTTP_LINK_EXTRA_DATA_ID);
        ImageView imageView_avatar = (ImageView) findViewById(R.id.imageView_user_login);
        if (linkToImg != null) {
            imageView_avatar.setImageDrawable(Drawable.createFromPath(intent.getStringExtra(MainActivity.AVATAR_FROM_PATH_EXTRA_DATA_ID)));
        } else {
            imageView_avatar.setImageResource(intent.getIntExtra( MainActivity.AVATAR_FROM_RES_EXTRA_DATA_ID, 0));
        }

        userList = (ListView) findViewById(R.id.ListView);


        friendsNearby.setup(textView_login.getText().toString(), linkToImg, UserList.this);

        // TODO periodically pull friends
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (;;) {
                    List<Friend> res = friendsNearby.discoverFriends();
                    if (friends.containsAll(res)) {
                        friends.clear();
                        friends.addAll(res);
                        // do stuff in a separate thread
                        uiCallback.sendEmptyMessage(0);
                    }

                    try {
                        Thread.sleep(2000);    // sleep for 3 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        UserItem user = new UserItem();
        userList.setAdapter(user);

        addActionToViews();
    }

    private Handler uiCallback = new Handler () {
        public void handleMessage (Message msg) {
           userList.invalidate();
        }
    };


    private void addActionToViews() {

        final Switch switchActive = findViewById(R.id.switch_active);
        switchActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchActive.isChecked()) {
                    switchActive.setText("Active");
                } else {
                    switchActive.setText("Inactive");
                }
            }
        });
    }

    class UserItem extends BaseAdapter {

        @Override
        public int getCount() {
            return friends.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.user_item, null);

            ImageView imageView_avatar = (ImageView) view.findViewById(R.id.imageView_avatar);
            TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
            TextView textView_description = (TextView) view.findViewById(R.id.textView_description);

            textView_name.setText(friends.get(i).getName());
            String avatarUrl = friends.get(i).getAvatarUrl();
            picasso.load(avatarUrl).into(imageView_avatar, new Callback() {
                @Override
                public void onSuccess() {
                    Log.i("LIST", "Successfully downloaded avatar: " + IMAGES[i]);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("LIST", "Problem", e);
                }
            });
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        friendsNearby.teardown(UserList.this);
    }
}
