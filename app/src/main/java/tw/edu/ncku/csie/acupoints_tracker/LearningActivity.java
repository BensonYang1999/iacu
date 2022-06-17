package tw.edu.ncku.csie.acupoints_tracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import org.json.JSONArray;

import tw.edu.ncku.csie.acupoints_tracker.databinding.ActivityLearningBinding;

public class LearningActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
    private ActivityLearningBinding binding;
    GlobalVariable gv = GlobalVariable.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLearningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() { // show every acupoints button
            @Override
            public void onClick(View view) {
                GlobalVariable gv = GlobalVariable.getInstance();
                gv.setAcupoint(new String[]{"All"});
                JSONArray jsonArray = gv.getAcuJson();
                int[] idx = new int[jsonArray.length()];
                for (int i=0; i<jsonArray.length(); i++) { idx[i] = i; }
                gv.setAcuIdx(idx);
                Intent intent = new Intent();
                intent.setClass(LearningActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.viewpager);

        String title[] = {"依穴道", "依疾病"};
        int pos = 0;

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        fragmentAdapter.addFragment(new AcupointFragment(), title[0]);
        fragmentAdapter.addFragment(new SymptomFragment(), title[1]);
        viewPager.setAdapter(fragmentAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(title[position])).attach();

    }
}