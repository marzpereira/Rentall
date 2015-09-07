package com.mendonca.rentall;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.preference.PreferenceFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String TAG=MainActivity.class.getSimpleName();
    private static final int LISTING_ACTIVITY_CODE=111;
    private static final int PROFILE_ACTIVITY_CODE=222;


    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test parse module
        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        */

        //Populate list on create
        mNavItems.add(new NavItem("Home","Main Page",R.drawable.ic_home_black_48dp));
        mNavItems.add(new NavItem("Rent Out an Item", "Put an item on rent",R.drawable.ic_camera_alt_black_36dp));
        mNavItems.add(new NavItem("Look for an item", "Check local items available for rent", R.drawable.ic_search_black_48dp));
        mNavItems.add(new NavItem("About", "About Us", R.drawable.ic_settings_black_48dp));
        mNavItems.add(new NavItem("Profile", "Profile", R.drawable.ic_settings_black_48dp));


        //Drawer Layout
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);


        //Populate the drawer
        mDrawerPane=(RelativeLayout)findViewById(R.id.drawerPane);
        mDrawerList=(ListView)findViewById(R.id.navList);
        DrawerListAdapter adapter=new DrawerListAdapter(this,mNavItems);
        mDrawerList.setAdapter(adapter);


        //On click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectItemFromDrawer(position);
            }
        });



        // Action icon for drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Drawer open and close
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();

            }
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "on drawer closed" + getTitle());
                invalidateOptionsMenu();
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);



        //Set profile name according to user who is logged in
        ParseUser currentUser= ParseUser.getCurrentUser();


        TextView userdisplay=(TextView)findViewById(R.id.userName);
        userdisplay.setText(currentUser.getUsername());

        TextView emaildisplay=(TextView)findViewById(R.id.email);
        emaildisplay.setText(currentUser.getEmail());


        //Create new home fragment
        Fragment fragment;
        fragment=new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment,"HOME").commit();
        setTitle("Home");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==LISTING_ACTIVITY_CODE || requestCode==PROFILE_ACTIVITY_CODE){
            if(resultCode== Activity.RESULT_OK){
                //Navigate to home
                Fragment fragment;

                FragmentManager fragmentManager = getFragmentManager();
                fragment=fragmentManager.findFragmentByTag("HOME");
                fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
                setTitle("Home");
            }
            else if(resultCode==Activity.RESULT_CANCELED){
                Fragment fragment;

                FragmentManager fragmentManager = getFragmentManager();
                fragment=fragmentManager.findFragmentByTag("HOME");
                fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
                setTitle("Home");

            }

        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }



    //Create new fragment based on position

    private void selectItemFromDrawer(int position){



        Fragment fragment;
        Intent intent;
        FragmentManager fragmentManager = getFragmentManager();
        switch (position){
            case 0:
                fragment=fragmentManager.findFragmentByTag("HOME");
                fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
                break;
            case 1:
                fragment=new RentFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
                break;
            case 2:
                intent=new Intent(MainActivity.this,ListingActivity.class);
                startActivityForResult(intent, LISTING_ACTIVITY_CODE);

                break;
            case 3:
                fragment=new PreferencesFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
                break;
            case 4:
                /*intent=new Intent(MainActivity.this,ProfileActivity.class);
                startActivityForResult(intent, PROFILE_ACTIVITY_CODE);*/
                fragment=new ProfileFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
                break;
        }





        mDrawerList.setItemChecked(position,true);
        setTitle(mNavItems.get(position).mTitle);

        //Close Drawer
        mDrawerLayout.closeDrawer(mDrawerPane);

    }


    //Called when invalidate menu option is called in drawer open close listener

    public boolean onPrepareOptionsMenu(Menu menu){
        //if drawer is open hide action items related to content view
        boolean drawerOpen=mDrawerLayout.isDrawerOpen(mDrawerPane);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    //Inner class for nav item
    class NavItem{
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon){
            mTitle=title;
            mSubtitle=subtitle;
            mIcon=icon;
        }

    }

    //Base adapter impl
    class DrawerListAdapter extends BaseAdapter{
        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems){
            mContext=context;
            mNavItems=navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item,null);
            }
            else{
                view=convertView;
            }
            TextView titleView=(TextView)view.findViewById(R.id.title);
            TextView subtitleView=(TextView)view.findViewById(R.id.subtitle);
            ImageView iconView=(ImageView)view.findViewById(R.id.icon);

            titleView.setText(mNavItems.get(position).mTitle);
            subtitleView.setText(mNavItems.get(position).mSubtitle);
            iconView.setImageResource(mNavItems.get(position).mIcon);


            return view;
        }
    }



}
