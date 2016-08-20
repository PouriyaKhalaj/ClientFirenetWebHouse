package com.ifirenet.clientfirenetwebhouse.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ifirenet.clientfirenetwebhouse.Adapters.DetailTicketRecycleAdapter;
import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.DetailTicket;
import com.ifirenet.clientfirenetwebhouse.Utils.PublicClass;
import com.ifirenet.clientfirenetwebhouse.Utils.Urls;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailTicketFragment.OnDetailTicketListener} interface
 * to handle interaction events.
 */
public class DetailTicketFragment extends Fragment {

    public static final String ARG_NodeId = "NodeId";
    private static final String ARG_PARAM2 = "param2";

    private int nodeId;
    private String mParam2;

    private OnDetailTicketListener mListener;
    private View view;
    ProgressDialog progressDialog;
    PublicClass publicClass;

    public DetailTicketFragment() {
    }

    public static DetailTicketFragment newInstance(String param1, String param2) {
        DetailTicketFragment fragment = new DetailTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NodeId, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nodeId = getArguments().getInt(ARG_NodeId);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_ticket, container, false);
        init();
        return view;
    }
    private void init(){
        publicClass = new PublicClass(getActivity());
        ArrayList<DetailTicket> detailTicketList = getDetailTickets();
    }
    private ArrayList<DetailTicket> getDetailTickets(){
        final ArrayList<DetailTicket> detailTicketList = new ArrayList<>();

        progressDialog = ProgressDialog.show(getActivity(), null,
                "در حال دریافت اطلاعات، لطفا صبر نمایید...", false, false);

        String fullUrl = Urls.baseUrl + "ClientPortalService.svc/GetTicketThreads/" + nodeId;
        Ion.with(getActivity())
                .load(fullUrl)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        progressDialog.dismiss();
                        if (e != null){
                            publicClass.showToast("خطا در دریافت اطلاعات!");
                            return;
                        }
                        if (result.getHeaders().code() == 200) {
                            try {
                                JSONArray array = new JSONArray(result.toString());
                                for (int i = 0; i < array.length(); ++i) {
                                    JSONObject object = array.getJSONObject(i);

                                    Gson gson = new GsonBuilder().create();
                                    detailTicketList.add(gson.fromJson(object.toString(), DetailTicket.class));
                                }

                                displayData(detailTicketList);
                            } catch (JSONException e1) {
                                progressDialog.dismiss();
                                e1.printStackTrace();
                            }
                        } else publicClass.showToast(result.getHeaders().message());
                    }
                });
        return detailTicketList;
    }
    private void displayData(ArrayList<DetailTicket> detailTicketList){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_detail_ticket);
        recyclerView.setAdapter(new DetailTicketRecycleAdapter(getActivity(), detailTicketList));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailTicketListener) {
            mListener = (OnDetailTicketListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDetailTicketListener {
        // TODO: Update argument type and name
        void onDetailTicket();
    }
}
