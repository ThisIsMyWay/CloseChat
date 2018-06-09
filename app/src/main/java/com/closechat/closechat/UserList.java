package com.closechat.closechat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class UserList extends Activity {

    String[] IMAGES = {"https://image.ibb.co/kwbyNo/avatar.jpg", "https://image.ibb.co/kwbyNo/avatar.jpg", "https://image.ibb.co/kwbyNo/avatar.jpg"};
    String[] NAMES = {"user1", "koziolekmatolek", "agent BOLEK"};
    String[] DESCRIPTION = {"jakiś nob", "kolo z Pacanowa", "a ten był prezydentem"};
    private final Picasso picasso = Picasso.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        Intent intent = getIntent();

        TextView textView_login = (TextView) findViewById(R.id.textView_login);
        textView_login.setText(intent.getStringExtra("login"));

        String pathToImg = intent.getStringExtra("avatarFromFile");
        ImageView imageView_avatar = (ImageView) findViewById(R.id.imageView_user_login);
        if (pathToImg != null) {
            File imgFile = new File(pathToImg);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView_avatar.setImageBitmap(myBitmap);
            }
        } else {
            imageView_avatar.setImageResource(intent.getIntExtra("avatarFromRes", 0));
        }

        ListView userList = (ListView) findViewById(R.id.ListView);

        UsserItem user = new UsserItem();
        userList.setAdapter(user);

        addActionToViews();
    }

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

    class UsserItem extends BaseAdapter {

        @Override
        public int getCount() {
            return NAMES.length;
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

            picasso.load(IMAGES[i]).into(imageView_avatar, new Callback() {
                @Override
                public void onSuccess() {
                    Log.i("LIST", "Successfully downloaded avatar: " + IMAGES[i]);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("LIST", "Problem", e);
                }
            });
            textView_name.setText(NAMES[i]);
            textView_description.setText(DESCRIPTION[i]);
            return view;
        }
    }
}
