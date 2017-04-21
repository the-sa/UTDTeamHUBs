package com.teamhub.utd.hub;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Garrett on 4/20/17.
 */

public class RemoveUserRequest extends StringRequest {
    private static final String POPULATE_USERS_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/RemoveUser.php";
    private Map< String, String> params;
    public RemoveUserRequest(int user_id, Response.Listener<String> listener) {
        super(Method.POST, POPULATE_USERS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}