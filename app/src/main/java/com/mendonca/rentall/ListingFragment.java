package com.mendonca.rentall;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

/**
 * Created by Warren on 8/18/2015.
 */
public class ListingFragment extends android.app.Fragment  {

    private static final String RENT_LIST_TAG="RENT_LIST_TAG";
    //private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView mListView;

    private static RadioGroup radiogroup_list;
    private static RadioButton radioButton_newest;
    private static RadioButton radioButton_cheapest;
    private CustomAdapter customAdapter;

    private  ParseQueryAdapter<RentList> posts;

    public ListingFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        mainAdapter=new ParseQueryAdapter<ParseObject>(getActivity().getApplicationContext(), RentList.class);
        mainAdapter.setTextKey("title");
        mainAdapter.setImageKey("photo");
        */
        customAdapter=new CustomAdapter(getActivity());


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_listing,container,false);

        radiogroup_list=(RadioGroup)view.findViewById(R.id.radiogroup_list);
        radioButton_cheapest=(RadioButton)view.findViewById(R.id.radioButton_cheapest);
        radioButton_newest=(RadioButton)view.findViewById(R.id.radioButton_newest);

        //set default checked
        radiogroup_list.check(radioButton_newest.getId());

        radiogroup_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButton_newest.getId()) {
                    System.out.println("here 0");
                } else if (checkedId == radioButton_cheapest.getId()) {
                    System.out.println("here 1");
                }
            }
        });

        mListView = (ListView) view.findViewById(R.id.rent_list);
        mListView.setAdapter(customAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject obj=customAdapter.getItem(position);

                RentList rentList=(RentList)obj;

                Fragment fragment;
                fragment=ViewDetailsFragment.newInstance(rentList);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.listmaincontent,fragment).addToBackStack(RENT_LIST_TAG).commit();
            }
        });


        return view;
    }


}
