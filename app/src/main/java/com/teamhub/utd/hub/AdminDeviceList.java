package com.teamhub.utd.hub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminDeviceList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminDeviceList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminDeviceList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayAdapter<Devices> devicesArrayAdapter;
    // array list to store device data
    final ArrayList<Devices> devices = new ArrayList<Devices>();
    ListView listView;

    private OnFragmentInteractionListener mListener;

    public AdminDeviceList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminDeviceList.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminDeviceList newInstance(String param1, String param2) {
        AdminDeviceList fragment = new AdminDeviceList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume () {
        super.onResume();
        devicesArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_admin_device_list, container, false);
        // button to do something
        FloatingActionButton button = (FloatingActionButton)rootView.findViewById(R.id.addButton);
        FloatingActionButton button2 = (FloatingActionButton)rootView.findViewById(R.id.deviceListRefresh);
        // list view to do something
        listView = (ListView)rootView.findViewById(R.id.deviceList);

        populateList();

        //button listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UnPairedActivity.class);
                startActivity(i);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devices.clear();
                populateList();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void populateList () {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                boolean success;
                try {
                    jsonResponse = new JSONObject(response);
                    //success = jsonResponse.getBoolean("result");

                    if (true) {
                        JSONArray array = jsonResponse.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            Devices device = new Devices();
                            device.setId(array.getJSONObject(i).getInt("id"));
                            device.setName(array.getJSONObject(i).getString("device_name"));
                            device.setMacaddress(array.getJSONObject(i).getString("mac_address"));
                            device.setBatteryLife(array.getJSONObject(i).getInt("battery_level"));
                            device.setUserID(array.getJSONObject(i).getInt("user_id"));
                            devices.add(device);
                        }

                        devicesArrayAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // call request
        PopulateDevicesRequest loginRequest = new PopulateDevicesRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loginRequest);

        // array adapter
        devicesArrayAdapter = new ArrayAdapter<Devices>(getActivity(), android.R.layout.simple_list_item_1, devices);
        listView.setAdapter(devicesArrayAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //@Override
    /*public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
