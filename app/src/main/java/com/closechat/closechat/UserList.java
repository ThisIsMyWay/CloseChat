package com.closechat.closechat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserList extends Activity {


    String[] NAMES = {"user1", "koziolekmatolek", "agent BOLEK"};
    String[] DESCRIPTION = {"jakiś nob", "kolo z Pacanowa", "a ten był prezydentem"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        ListView userList=(ListView)findViewById(R.id.ListView);

        UsserItem user=new UsserItem();
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
            view = getLayoutInflater().inflate(R.layout.user_item,null);

            TextView textView_name=(TextView)view.findViewById(R.id.textView_name);
            TextView textView_description=(TextView)view.findViewById(R.id.textView_description);

            textView_name.setText(NAMES[i]);
            textView_description.setText(DESCRIPTION[i]);
            return view;
        }
    }
}
