package com.google.mediapipe.examples.facemesh;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

public class AcupointFragment extends Fragment {
    ArrayAdapter<String> arrayAdapter;
    GlobalVariable gv = GlobalVariable.getInstance();

    public AcupointFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    String[] name = {"絲竹空","印堂", "魚腰", "球後", "上迎香", "俠承漿", "睛明", "攢竹", "瞳子髎", "陽白", "承泣", "四白", "巨髎", "地倉", "顴髎", "水溝", "禾髎", "迎香", "承漿", "太陽", "孔最", "內觀", "梁秋", "銀門", "光明"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_acupoint, container, false);
        ListView listView = rootView.findViewById(R.id.listview_acu);
        arrayAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_list_item_1, name);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listViewTemp = (ListView) adapterView;
                Toast.makeText(getActivity(), listViewTemp.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                gv.setAcupoint(new String[]{listViewTemp.getItemAtPosition(i).toString()});
                Intent intent = new Intent();
                intent.setClass(getActivity(), CameraActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
}