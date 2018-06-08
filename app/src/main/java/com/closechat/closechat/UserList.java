package com.closechat.closechat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserList extends Activity {

    int[] IMAGES = {R.drawable.avatar, R.drawable.avatar, R.drawable.avatar};
    String[] NAMES = {"user1", "koziolekmatolek", "agent BOLEK"};
    String[] DESCRIPTION = {"jakiś nob", "kolo z Pacanowa", "a ten był prezydentem"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);


        TextView textView_login = (TextView) findViewById(R.id.textView_login);
        textView_login.setText("grzegorz brzęczyszczykiewicz");

        ImageView imageView_avatar=(ImageView)findViewById(R.id.imageView_user_login);
        imageView_avatar.setImageResource(IMAGES[0]);

        ListView userList = (ListView) findViewById(R.id.ListView);

        UsserItem user = new UsserItem();
        userList.setAdapter(user);
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.user_item, null);

            ImageView imageView_avatar=(ImageView)view.findViewById(R.id.imageView_avatar);
            TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
            TextView textView_description = (TextView) view.findViewById(R.id.textView_description);

            imageView_avatar.setImageResource(IMAGES[i]);
            textView_name.setText(NAMES[i]);
            textView_description.setText(DESCRIPTION[i]);
            return view;
        }
    }
}
