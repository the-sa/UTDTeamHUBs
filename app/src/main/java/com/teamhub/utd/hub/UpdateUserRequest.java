package com.teamhub.utd.hub;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Garrett on 4/21/2017.
 */

public class UpdateUserRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/UpdateUser.php";
    private Map<String, String> params;

    public UpdateUserRequest(int user_id,String username, String password, int role, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
        params.put("username", username);
        params.put("password", password);
        params.put("role", String.valueOf(role));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

