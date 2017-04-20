package com.teamhub.utd.hub;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class RemoveDeviceRequest extends StringRequest {
    private static final String REMOVE_DEVICE_REQUEST_URL = "http://hueless-discharge.000webhostapp.com/RemoveDevice.php";
    private Map<String, String> params;

    public RemoveDeviceRequest(int device_id, Response.Listener<String> listener) {
        super(Method.POST, REMOVE_DEVICE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("device_id", String.valueOf(device_id));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}