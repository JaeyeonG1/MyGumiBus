package com.poha.mygumibus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.poha.mygumibus.R;
import com.poha.mygumibus.adapter.MainFragmentAdapter;
import com.poha.mygumibus.fragment.LocationFragment;
import com.poha.mygumibus.fragment.MainFragment;
import com.poha.mygumibus.fragment.SearchFragment;
import com.poha.mygumibus.util.NonSwipeViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Fragment> fragList;
    ArrayList<String> fragNameList;

    Toolbar toolbar;

    DrawerLayout drawerLayout;
    View drawerView;

    MainFragmentAdapter fragmentAdapter;
    TabLayout tab;
    NonSwipeViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar 를 Activity 의 AppBar 로 설정
        toolbar = findViewById(R.id.toolbar_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // 홈 버튼 활성화
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_24dp); // 아이콘 대체
        getSupportActionBar().setTitle("20151264 최재영");

        // 어댑터에 추가할 항목 설정
        fragList = new ArrayList<>();
        fragNameList = new ArrayList<>();
        setFragments();

        // Viewpager 를 FragmentManager 로 사용하기 위해 설정
        fragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragList, fragNameList);
        viewPager = findViewById(R.id.viewPager_holder);
        viewPager.setAdapter(fragmentAdapter);

        // TabLayout 과 Viewpager 연동
        tab = findViewById(R.id.tabLayout_holder);
        tab.setupWithViewPager(viewPager);
    }

    // Fragment 매칭
    private void setFragments(){
        fragList.add(new SearchFragment());
        fragList.add(new LocationFragment());
        fragNameList.add("Search");
        fragNameList.add("Location");
    }

    // 뒤로가기 버튼 눌렸을 때 동작
    @Override
    public void onBackPressed() {
        // Drawer 열려있으면 닫기
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
