package com.teamhub.utd.hub;

/**
 * Created by modte on 4/23/2017.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetDeviceUserRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/GetDeviceUser.php";
    private Map<String, String> params;

    public GetDeviceUserRequest(int user_id, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", String.valueOf(user_id));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}