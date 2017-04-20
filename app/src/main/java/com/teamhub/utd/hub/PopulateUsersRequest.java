package com.teamhub.utd.hub;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class PopulateUsersRequest extends StringRequest {
    private static final String POPULATE_USERS_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/PopulateUsers.php";
   //private Map< String, String> params;
    public PopulateUsersRequest(Response.Listener<String> listener) {
        super(Method.POST, POPULATE_USERS_REQUEST_URL, listener, null);


    }

//    @Override
//    public Map<String, String> getParams() {
//        return params;
//    }

}