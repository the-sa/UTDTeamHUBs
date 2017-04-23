package com.teamhub.utd.hub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * {@link AdminUserList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminUserList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminUserList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayAdapter<User> usersArrayAdapter;
    // make a list of user
    ArrayList<User> users = new ArrayList<User>();
    ListView listView;

    private OnFragmentInteractionListener mListener;

    public AdminUserList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminUserList.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminUserList newInstance(String param1, String param2) {
        AdminUserList fragment = new AdminUserList();
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
        usersArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_user_list, container, false);
        // button to do something
        FloatingActionButton button = (FloatingActionButton)rootView.findViewById(R.id.addUserButton);
        FloatingActionButton button2 = (FloatingActionButton)rootView.findViewById(R.id.userListRefresh);
        // list view to do something
        listView = (ListView)rootView.findViewById(R.id.userList);

        // populate list
        populateList();

        //button listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                startActivity(i);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), EditUserActivity.class);
                User user = (User) adapterView.getItemAtPosition(i);
                intent.putExtra("User", user);
                //Log.e("ClickedDevice", String.valueOf(user.id));
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users.clear();
                populateList();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    // to add to the list
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
                            User user = new User();
                            user.setId(array.getJSONObject(i).getInt("id"));
                            user.setUsername(array.getJSONObject(i).getString("username"));
                            user.setPassword(array.getJSONObject(i).getString("password"));
                            user.setRole(array.getJSONObject(i).getInt("role"));
                            users.add(user);
                            Log.e("UserList:", user.toString());
                        }

                        // array adapter
                        //usersArrayAdapter = new ArrayAdapter<User>(getActivity(), android.R.layout.simple_list_item_1, users);
                        usersArrayAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // call request
        PopulateUsersRequest loginRequest = new PopulateUsersRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loginRequest);

        usersArrayAdapter = new ArrayAdapter<User>(getActivity(), android.R.layout.simple_list_item_1, users);
        listView.setAdapter(usersArrayAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
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
