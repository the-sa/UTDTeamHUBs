package com.teamhub.utd.hub;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Garrett on 4/20/17.
 */

public class RemoveUserRequest extends StringRequest {
    private static final String POPULATE_USERS_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/RemoveUser.php";
    //private Map< String, String> params;
    public RemoveUserRequest(Response.Listener<String> listener) {
        super(Method.POST, POPULATE_USERS_REQUEST_URL, listener, null);


    }
}