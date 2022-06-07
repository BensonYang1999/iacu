package com.google.mediapipe.examples.facemesh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymptomFragment extends Fragment {

    SimpleAdapter simpleAdapter;
    GlobalVariable gv = GlobalVariable.getInstance();
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrayName = new ArrayList<String>();


    public SymptomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    String[] name = {"眼球震顫","新生兒窒息急性鼻竇炎","假性近視","新生兒痙攣","外斜視","流眼淚","視神經萎縮","淚囊炎","上牙痛","眼瞼緣炎","眼瞼下垂","眼部疲勞","中心漿液性視網膜病變","假性延髓麻痺","針刺麻醉陣痛","齒齦潰瘍","眶上神經痛","失明","重型顱腦損傷","充血性心臟衰竭","子癇","鼻竇炎","鼻塞","鼻息肉","煤氣中毒","嗅覺麻痺","術前焦慮","眼睛紅腫","眼眶脹痛","眼球後神經炎","眼花","眼內或周圍疼痛","病毒性腦炎","面腫","面神經炎","流涕","急性結膜炎","青光眼","抽筋","妥瑞癥","小兒熱痙攣","上肢麻痺","三叉神經痛(第三支痛)","三叉神經痛(第二支痛)","顳顎關節癥候群","熱痙攣","破傷風","反射性交感神經營養不良","口臭","三叉神經痛","頭暈","口瘡","四肢麻痺","壞疽性口炎","膽道蛔蟲癥","頸性眩暈","鼻炎","暈厥","黃褐斑","乾眼癥","面神經麻痺","苯中毒","流涎","阻塞型睡眠呼吸中止癥","近視","昏迷","低血壓","色盲","牙痛","中風後打嗝","三叉神經痛(第一支痛)","術後疼痛","偏頭痛","急性腰扭傷","急性下背痛","耳聾","中風後失眠","過敏性鼻炎","腹瀉","歇斯底里癥","麥粒腫","酒渣鼻","胸悶","氣喘","水腫","心因性嘔吐癥","癲癇","頭痛","鼻血","精神官能癥","視神經炎","眼球疾患","帶狀皰疹","高血壓","眩暈","消化不良","帕金森氏癥","夜盲","周邊神經病變","角膜結膜炎","抑鬱癥","耳鳴","失眠","中風"};
//    String[][] acu_array = { {"睛明", "球後"},{"水溝"},{"迎香", "印堂", "上迎香"},{"睛明", "承泣"},{"水溝"},{"睛明"},{"陽白", "瞳子髎"},{"太陽", "睛明", "球後", "瞳子髎"},{"太陽", "睛明"},{"太陽"},{"攢竹", "睛明", "瞳子髎"},{"攢竹", "魚腰", "絲竹空"},{"攢竹", "太陽", "絲竹空", "睛明"},{"攢竹", "睛明", "球後"},{"承漿", "地倉"},{"承漿", "水溝"},{"俠承漿", "承漿"},{"攢竹", "魚腰", "陽白", "絲竹空"},{"承漿", "顴髎", "四白", "水溝", "睛明", "承泣"},{"水溝"},{"水溝"},{"水溝"},{"攢竹", "迎香", "上迎香"},{"迎香", "印堂", "上迎香"},{"迎香", "上迎香"},{"水溝"},{"迎香"},{"印堂"},{"攢竹", "太陽", "絲竹空", "睛明", "印堂", "承泣"},{"太陽"},{"太陽", "睛明", "球後", "承泣"},{"陽白"},{"陽白", "印堂"},{"水溝", "印堂"},{"俠承漿", "承漿", "水溝"},{"承漿", "魚腰", "四白", "地倉"},{"迎香", "印堂"},{"太陽", "陽白", "睛明", "瞳子髎"},{"攢竹", "太陽", "印堂"},{"印堂"},{"太陽", "印堂"},{"水溝"},{"攢竹", "陽白", "四白", "地倉", "絲竹空"},{"俠承漿", "承漿", "地倉"},{"顴髎", "太陽", "四白", "巨髎", "地倉"},{"顴髎", "攢竹", "魚腰", "太陽", "絲竹空", "印堂"},{"水溝", "印堂"},{"地倉", "水溝"},{"四白", "迎香", "瞳子髎"},{"水溝"},{"俠承漿", "承漿", "顴髎"},{"太陽", "印堂"},{"俠承漿", "承漿"},{"水溝"},{"地倉"},{"迎香", "水溝"},{"絲竹空"},{"迎香", "印堂", "上迎香"},{"水溝"},{"承漿", "顴髎", "太陽", "陽白", "四白", "地倉", "迎香", "印堂", "承泣"},{"攢竹", "太陽", "陽白", "四白", "絲竹空", "迎香", "睛明", "印堂", "瞳子髎", "承泣"},{"俠承漿", "承漿", "顴髎", "太陽", "陽白", "四白", "巨髎", "地倉", "迎香", "水溝"},{"太陽", "地倉"},{"地倉", "水溝"},{"迎香"},{"攢竹", "魚腰", "太陽", "四白", "絲竹空", "睛明", "球後", "承泣"},{"水溝"},{"印堂"},{"攢竹", "四白", "絲竹空", "睛明", "球後", "瞳子髎"},{"俠承漿", "承漿", "顴髎", "巨髎", "絲竹空", "水溝", "禾髎"},{"攢竹", "水溝"},{"攢竹", "魚腰", "太陽", "陽白"},{"睛明", "瞳子髎", "承泣"},{"太陽", "絲竹空", "印堂"},{"太陽", "陽白", "印堂"},{"水溝", "印堂"},{"水溝"},{"禾髎"},{"太陽", "印堂"},{"迎香", "印堂", "上迎香"},{"陽白"},{"水溝"},{"攢竹"},{"迎香", "印堂"},{"承漿"},{"地倉", "絲竹空"},{"水溝"},{"水溝"},{"承漿", "攢竹", "水溝", "印堂"},{"攢竹", "太陽", "印堂"},{"迎香", "水溝", "禾髎", "上迎香"},{"攢竹", "水溝"},{"太陽", "睛明", "球後", "承泣"},{"太陽", "睛明", "魚尾"},{"太陽"},{"太陽", "水溝", "印堂"},{"印堂"},{"印堂"},{"水溝", "印堂"},{"攢竹", "四白", "絲竹空", "睛明", "承泣"},{"陽白", "四白", "地倉", "球後"},{"太陽", "睛明"},{"太陽", "水溝", "印堂"},{"攢竹", "迎香", "印堂"},{"承漿", "攢竹", "太陽", "水溝", "印堂"},{"攢竹", "水溝", "禾髎"}};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_symptom, container, false);
        ListView listView = rootView.findViewById(R.id.listview_sym);
        JSONArray jsonArray = gv.getDisJson();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                HashMap<String, String> m_li = new HashMap<String, String >();
                m_li.put("疾病", object.getString("疾病"));
                m_li.put("資料", "對應穴道：" + object.getString("對應穴道") );
//                m_li.put("別名", "別名 " + object.getString("別名"));
                arrayList.add(m_li);
                arrayName.add(object.getString("疾病"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] keys =  {"疾病", "資料"};
        int[] ids = {android.R.id.text1, android.R.id.text2};
        simpleAdapter = new SimpleAdapter(getActivity(), arrayList, android.R.layout.simple_list_item_2, keys, ids) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = view.findViewById(android.R.id.text1);
                text1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

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
//                ListView listViewTemp = (ListView) adapterView;
//                HashMap<String, String> h_temp = new HashMap<String, String>((Map<String, String>) listViewTemp.getItemAtPosition(i));
//                Toast.makeText(getActivity(), h_temp.get("疾病"), Toast.LENGTH_SHORT).show();
//                gv.setDisease(new String[]{h_temp.get("疾病")});
//                gv.setDisIdx(new int[]{arrayName.indexOf(h_temp.get("疾病"))});
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), CameraActivity.class);
//                startActivity(intent);
                ListView listViewTemp = (ListView) adapterView;
                HashMap<String, String> h_temp = new HashMap<String, String>((Map<String, String>) listViewTemp.getItemAtPosition(i));
                try {
                    // acupoint list
                    List<String> acu_list = new ArrayList<>();
//                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject object = get_obj_by_disease_name(jsonArray,h_temp.get("疾病"));
                    JSONArray acu_json = object.getJSONArray("對應穴道");
                    for (int j = 0; j < acu_json.length(); j++) {
                        acu_list.add(acu_json.getString(j));
                    }
                    GlobalVariable gv = GlobalVariable.getInstance();
                    gv.setAcupoint((String[]) acu_list.toArray(new String[acu_list.size()]));
                    int[] idx = new int[acu_list.size()];
                    for (int j = 0; j < acu_list.size(); j++) {
//                        idx[j] = arrayName.indexOf(acu_list.get(j));
                        idx[j] = j;
                    }
                    gv.setAcuIdx(idx);
                    Toast.makeText(getActivity(), Arrays.toString(acu_list.toArray()), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), CameraActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return rootView;

//        arrayAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_list_item_1, name);
//        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                List<String> acu_list = Arrays.asList(acu_array[i]);
//
//                Toast.makeText(getActivity(), Arrays.toString(acu_list.toArray()), Toast.LENGTH_SHORT).show();
////                Toast.makeText(getApplicationContext(), Arrays.toString(acu_list.toArray()), Toast.LENGTH_SHORT).show();
//                GlobalVariable gv = GlobalVariable.getInstance();
//                gv.setAcupoint((String[]) acu_list.toArray(new String[acu_list.size()]));
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), CameraActivity.class);
//                startActivity(intent);
//            }
//        });
//        return rootView;
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

    public JSONObject get_obj_by_disease_name(JSONArray jsonArray,String disease_name){
        try {
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = jsonArray.getJSONObject(j);
                if (object.getString(("疾病")) == disease_name){
                    return object;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}