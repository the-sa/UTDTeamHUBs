package com.teamhub.utd.hub;

/**
 * Created by Garrett on 4/20/2017.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AssignDeviceUserRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/AssignDeviceUser.php";
    private Map<String, String> params;

    public AssignDeviceUserRequest(int userID, int deviceID, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", String.valueOf(userID));
        params.put("device_id", String.valueOf(deviceID));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
