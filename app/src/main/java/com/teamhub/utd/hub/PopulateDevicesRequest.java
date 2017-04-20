package com.teamhub.utd.hub;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class PopulateDevicesRequest extends StringRequest {
    private static final String POPULATE_DEVICES_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/PopulateDevices.php";

    //private Map< String, String> params;
    public PopulateDevicesRequest(Response.Listener<String> listener) {
        super(Method.POST, POPULATE_DEVICES_REQUEST_URL, listener, null);

    }
}