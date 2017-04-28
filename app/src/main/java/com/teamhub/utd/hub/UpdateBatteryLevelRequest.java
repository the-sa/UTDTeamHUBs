package com.teamhub.utd.hub;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Garrett on 4/21/2017.
 */

public class UpdateBatteryLevelRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/UpdateBatteryLevel.php";
    private Map<String, String> params;

    public UpdateBatteryLevelRequest(String mac_address,int battery_level, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("mac_address", mac_address);
        params.put("battery_level", String.valueOf(battery_level));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}