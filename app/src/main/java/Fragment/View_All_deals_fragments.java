package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.My_Past_Order_adapter;
import Config.BaseURL;
import Model.My_Past_order_model;
import Model.View_All_Deal_Of_Day_model;
import ajwdy.app.AppController;
import ajwdy.app.MainActivity;
import ajwdy.app.MyOrderDetail;
import ajwdy.app.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonArrayRequest;
import util.RecyclerTouchListener;
import util.Session_management;

public class View_All_deals_fragments extends Fragment {

    //  private static String TAG = Fragment.My_Past_Order.class.getSimpleName();

    private RecyclerView rv_view_all;

    private List<View_All_Deal_Of_Day_model> view_all_deal_of_day_models = new ArrayList<>();

    public View_All_deals_fragments() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_deal, container, false);



        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

//                    Fragment fm = new Home_fragment();
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        rv_view_all = (RecyclerView) view.findViewById(R.id.rv_all_deals);
        rv_view_all.setLayoutManager(new LinearLayoutManager(getActivity()));

        // check internet connection
        if (ConnectivityReceiver.isConnected())

        {
        } else

        {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }


        return view;
    }




}
