package com.mgh.taskfirebase.FragmentImage;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.mgh.taskfirebase.LoginActivity;
import com.mgh.taskfirebase.R;
import com.mgh.taskfirebase.utils.PagerAdaptar;

public class ImageActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdaptar adaptar;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        InitializeViews();
        setUpWidgets();
    }

    private void setUpWidgets() {
        adaptar=new PagerAdaptar(getSupportFragmentManager());
        adaptar.addNewFragment(new uploadImage_Fragment());
        adaptar.addNewFragment(new showImages_Fragment());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adaptar);
        setUpTabsTopPager();
    }

    private void setUpTabsTopPager() {
        tabLayout.getTabAt(0).setText(R.string.upload);
        tabLayout.getTabAt(1).setText(R.string.show);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#A50202"));
    }


    private void InitializeViews() {

        viewPager = findViewById(R.id.viewpagerid);
        tabLayout = findViewById(R.id.tabs_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle Logout selection
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(ImageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
                   default:
                return super.onOptionsItemSelected(item);
        }
    }
}
