package com.myapps.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.myapps.demo.fragment.ChatFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import yalantis.com.myapps.interfaces.Resourceble;
import yalantis.com.myapps.interfaces.ScreenShotable;
import yalantis.com.myapps.model.SlideMenuItem;

import com.myapps.demo.fragment.ContentFragment;
import com.myapps.demo.fragment.HomeFragment;
import com.myapps.demo.fragment.ImgFragment;
import com.myapps.demo.utils.SetDialog;
import com.myapps.demo.utils.SetDialogKey;
import yalantis.com.myapps.util.ViewAnimator;
import yalantis.com.sidemenu.sample.R;


public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    private int res = R.drawable.content_music;
    private LinearLayout linearLayout;
    ActionBar actionBar;
    ChatFragment chatFragment = new ChatFragment();
    ImgFragment imgFragment = new ImgFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init() {
        ContentFragment contentFragment = ContentFragment.newInstance(R.drawable.content_music);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = findViewById(R.id.left_drawer);

        setActionBar();
        createMenuList();


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
                drawerLayout.closeDrawers();
            }
        });
        viewAnimator = new ViewAnimator<>(this, list, contentFragment, drawerLayout, this);

    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.CHAT, R.drawable.chat_77x77);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.IMAGE, R.drawable.icn_3);
        list.add(menuItem3);


    }


    //设置ActionBarDrawerToggle
    private void setActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
                Log.d("onDrawerClosed", "onDrawerClosed: ");
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0) {
                    viewAnimator.showMenuContent();
                }
                Log.d("onDrawerSlide", "onDrawerSlide: ");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("onDrawerOpened", "onDrawerOpened: ");
            }

        };

        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //右侧菜单
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {

            case R.id.action_proxy:
                SetDialog setDialog = new SetDialog(this);
                setDialog.show();

                return true;

            case R.id.action_key:
                SetDialogKey setDialogKey = new SetDialogKey(this);
                setDialogKey.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //点击左侧菜单切换Fragment
    private ScreenShotable replaceFragment(String name, ScreenShotable screenShotable, int topPosition) {
        actionBar = getSupportActionBar();
        switch (name) {
            case ContentFragment.CHAT:
                setFragment((Fragment) screenShotable, chatFragment);
                actionBar.setTitle("ChatGpt");
                return chatFragment;

            case ContentFragment.IMAGE:
                setFragment((Fragment) screenShotable, imgFragment);
                actionBar.setTitle("ChatImg");
                return imgFragment;

            default:
                return null;
        }
    }


    //切换Fragment
    public void setFragment(Fragment fragment, Fragment fragment2) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment2.isAdded()) {// 先判断是否被add过
            fragmentTransaction.hide(fragment).add(R.id.frag_main, fragment2).commit();// 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(fragment).show(fragment2).commit();// 隐藏当前的fragment，显示下一个
        }
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case ContentFragment.CLOSE:

                return screenShotable;
            default:
                return replaceFragment(slideMenuItem.getName(), screenShotable, position);
        }
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

}
