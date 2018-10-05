package ajwdy.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import Config.SharedPref;
import ajwdy.app.NetworkConnectivity.NetworkConnection;
import ajwdy.app.NetworkConnectivity.NetworkError;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;

import static com.android.volley.VolleyLog.TAG;

public class PaymentGatWay extends AppCompatActivity {
    private Session_management sessionManagement;
    RelativeLayout confirm;
    private DatabaseHandler db_cart;
    private String getlocation_id = "";
    private String getstore_id = "";
    private String getvalue = "";
    private String get_wallet_ammount = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    String text;
    private String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instamojo_payment);
        sessionManagement = new Session_management(PaymentGatWay.this);
        String email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        String mobile = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        String name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        String total_rs = getIntent().getStringExtra("total");
        getlocation_id = getIntent().getStringExtra("getlocationid");
        getstore_id = getIntent().getStringExtra("getstoreid");
        gettime = getIntent().getStringExtra("gettime");
        getvalue = getIntent().getStringExtra("getpaymentmethod");
        getdate = getIntent().getStringExtra("getdate");
        get_wallet_ammount = getIntent().getStringExtra("wallet_ammount");
        //callInstamojoPay(email, mobile, total_rs, "official", name);
        sessionManagement = new Session_management(PaymentGatWay.this);
        db_cart = new DatabaseHandler(PaymentGatWay.this);

    }

   /* private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                Intent intent = new Intent(PaymentGatWay.this, ThanksOrder.class);
                startActivity(intent);
               
                finish();
                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                attemptOrder();
            }

            @Override
            public void onFailure(int code, String reason) {
                Intent intent = new Intent(PaymentGatWay.this, OrderFail.class);
                startActivity(intent);
                finish();
                //Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG).show();
            }
        };
    }


    private void attemptOrder() {
        ArrayList<HashMap<String, String>> items = db_cart.getCartAll();
        if (items.size() > 0) {
            JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("qty", map.get("qty"));
                    jObjP.put("unit_value", map.get("unit_value"));
                    jObjP.put("unit", map.get("unit"));
                    jObjP.put("price", map.get("price"));
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

            if (ConnectivityReceiver.isConnected()) {

                Log.e(TAG, "from:" + gettime + "\ndate:" + getdate +
                        "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:" + passArray.toString());

                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id, passArray);
            }
        }
    }

    private void makeAddOrderRequest(String date, String gettime, String userid, String location, String store_id, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        params.put("time", gettime);
        params.put("user_id", userid);
        params.put("location", location);
        params.put("store_id", store_id);
        params.put("payment_method", getvalue);
        params.put("data", passArray.toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        db_cart.clearCart();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(PaymentGatWay.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

*/
}
