package com.teamhub.utd.hub;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddDeviceRequest extends StringRequest {
    private static final String ADD_DEVICE_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/AddDevice.php";
    private Map<String, String> params;

    public AddDeviceRequest(String device, String macAddress, int user_id, Response.Listener<String> listener) {
        super(Method.POST, ADD_DEVICE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("device", device);
        params.put("macAddress", macAddress);
        params.put("user_id", String.valueOf(user_id));
    }
//Comment
    //Hi this is a comment to push
    //This is request for adding devices
    @Override
    public Map<String, String> getParams() {
        return params;
    }


}