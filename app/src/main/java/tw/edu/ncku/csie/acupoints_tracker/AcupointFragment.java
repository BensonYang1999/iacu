package tw.edu.ncku.csie.acupoints_tracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AcupointFragment extends Fragment {
    SimpleAdapter simpleAdapter;
    GlobalVariable gv = GlobalVariable.getInstance();
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrayName = new ArrayList<String>();

    public AcupointFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_acupoint, container, false);
        ListView listView = rootView.findViewById(R.id.listview_acu);
        JSONArray jsonArray = gv.getAcuJson();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                HashMap<String, String> m_li = new HashMap<String, String >();
                m_li.put("穴道", object.getString("穴道"));
                m_li.put("資料", "代碼 " + object.getString("代碼") + "\n" + "別名 " + object.getString("別名"));
                arrayList.add(m_li);
                arrayName.add(object.getString("穴道"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] keys =  {"穴道", "資料"};
        int[] ids = {android.R.id.text1, android.R.id.text2};
        simpleAdapter = new SimpleAdapter(getActivity(), arrayList, android.R.layout.simple_list_item_2, keys, ids) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = view.findViewById(android.R.id.text1);
                text1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
//                TextPaint tp = text1.getPaint();
//                tp.setFakeBoldText(true);

                TextView text2 = view.findViewById(android.R.id.text2);
                text2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                text2.setTextColor(Color.rgb(136,136,136));


                return view;
            }
        };

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listViewTemp = (ListView) adapterView;
                HashMap<String, String> h_temp = new HashMap<String, String>((Map<String, String>) listViewTemp.getItemAtPosition(i));
                Toast.makeText(getActivity(), h_temp.get("穴道"), Toast.LENGTH_SHORT).show();
                gv.setAcupoint(new String[]{h_temp.get("穴道")});
                gv.setAcuIdx(new int[]{arrayName.indexOf(h_temp.get("穴道"))});
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
                simpleAdapter.getFilter().filter(s);
                return false;
            }
        });
    }


}