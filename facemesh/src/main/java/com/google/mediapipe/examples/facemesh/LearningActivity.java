package com.google.mediapipe.examples.facemesh;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.mediapipe.examples.facemesh.databinding.ActivityLearningBinding;

import java.util.ArrayList;
import java.util.List;

public class LearningActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
    private ActivityLearningBinding binding;


//    ListView listView;
//    String[] name = {"絲竹空","印堂/曲眉", "魚腰", "球後", "上迎香", "夾承漿", "睛明", "攢竹", "瞳子膠/目髎", "陽白", "承泣", "四白", "巨膠", "地倉", "顴髎", "水溝", "禾髎", "迎香", "承漿", "孔最", "內觀", "梁秋", "銀門", "光明"};

//    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLearningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//
        setSupportActionBar(binding.toolbar);
//        setContentView(R.layout.activity_learning);

//        listView = findViewById(R.id.listview);
//        arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, name);
//        listView.setAdapter(arrayAdapter);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_learning);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable gv = GlobalVariable.getInstance();
                gv.setAcupoint("test");
                Intent intent = new Intent();
                intent.setClass(LearningActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.viewpager);

        String title[] = {"依穴道", "依症狀"};
        int pos = 0;

        AcupointAdapter adapter = new AcupointAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new AcupointFragment(), title[pos]);
//        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(title[position])).attach();


//        tabLayout.addTab(tabLayout.newTab().setText("依穴道"));
//        tabLayout.addTab(tabLayout.newTab().setText("依症狀"));

    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_learning);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu, menu);
//
//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Type here to search");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//
//                arrayAdapter.getFilter().filter(s);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
}