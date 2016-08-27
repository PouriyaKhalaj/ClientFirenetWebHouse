package com.ifirenet.clientfirenetwebhouse.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ifirenet.clientfirenetwebhouse.Adapters.DetailTicketRecycleAdapter;
import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.DetailTicket;
import com.ifirenet.clientfirenetwebhouse.Utils.Keys;
import com.ifirenet.clientfirenetwebhouse.Utils.PublicClass;
import com.ifirenet.clientfirenetwebhouse.Utils.Urls;
import com.ifirenet.clientfirenetwebhouse.Utils.User;
import com.ifirenet.clientfirenetwebhouse.Utils.UserInfo;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailTicketFragment.OnDetailTicketListener} interface
 * to handle interaction events.
 */
public class DetailTicketFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String ARG_NodeId = "NodeId";

    private String nodeId;
    private UserInfo userInfo;

    private OnDetailTicketListener mListener;
    private View view;
    private ProgressDialog progressDialog;
    private PublicClass publicClass;
    private Spinner sp_customer_result, sp_support_attach, sp_support_status;
    List<User> attachList;

    public DetailTicketFragment() {
    }

    public static DetailTicketFragment newInstance(String param1, String param2) {
        DetailTicketFragment fragment = new DetailTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NodeId, param1);
        args.putString(Keys.ARG_USER_INFO, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nodeId = getArguments().getString(ARG_NodeId);
            String u = getArguments().getString(Keys.ARG_USER_INFO);
            userInfo = new Gson()
                    .fromJson(u, UserInfo.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_tickets, container, false);
        init();
        return view;
    }
    private void init(){
        publicClass = new PublicClass(getActivity());
        if (userInfo.user.role.equals(Keys.ROLE_CUSTOMER))
            initCustomer();
        else initSupport();

        ArrayList<DetailTicket> detailTicketList = getDetailTickets();
    }
    private void initCustomer(){
        //LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_support_detail_ticket_update);
        //layout.setVisibility(View.GONE);

        CardView cardView = (CardView) view.findViewById(R.id.card_view_support_detail_ticket_update);
        cardView.setVisibility(View.GONE);

        sp_customer_result = (Spinner) view.findViewById(R.id.spinner_customer_detail_ticket_result);
       // ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.status_arrays, R.layout.custom_spinner_list);
       // ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.status_arrays, R.layout.custom_spinner_list);
        //adapter.setDropDownViewResource(R.layout.customer_spinner);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // sp_support_status.setAdapter(adapter);
        sp_customer_result.setOnItemSelectedListener(this);
    }

    private void initSupport(){
        boolean isConnect = publicClass.isConnection();
        if (!isConnect){
            publicClass.showToast("از وصل بودن اینترنت مطمئن شوید");
            return;
        }
        //LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_customer_detail_ticket_update);
        //layout.setVisibility(View.GONE);

        CardView cardView = (CardView) view.findViewById(R.id.card_view_customer_detail_ticket_update);
        cardView.setVisibility(View.GONE);


        CardView card_view_submit_status = (CardView) view.findViewById(R.id.card_view_support_detail_ticket_update_submit);

        initSpinner();
        card_view_submit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_support_status.getSelectedItemPosition() == 0){
                    publicClass.showToast("وضعیت را انتخاب نمایید");
                    return;
                }
                progressDialog = ProgressDialog.show(getActivity(), null,
                        "در حال دریافت اطلاعات، لطفا صبر نمایید...", false, false);

                int pos = sp_support_attach.getSelectedItemPosition();
                int statusPos = sp_support_status.getSelectedItemPosition();
                String fullUrl = Urls.baseURL + "ClientPortalService.svc/UpdateTicket/"
                        + userInfo.login.getUsername()  + "/"
                        + userInfo.login.getPassword() + "/"
                        + statusPos
                        + "/" + attachList.get(pos).id
                        + "/" + nodeId + "/"  + userInfo.user.id;
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
                                        JSONObject object = new JSONObject(result.getResult());
                                        if (object.has("text"))
                                            if (object.getBoolean("text")){
                                                publicClass.showToast("با موفقیت ثبت شد.");
                                                getDetailTickets();
                                            }

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        publicClass.showToast("خطا در دریافت اطلاعات! "+ e1.getMessage());
                                    }
                                } else publicClass.showToast(result.getHeaders().message());
                            }
                        });
            }
        });
        sp_support_attach.setOnItemSelectedListener(this);
        sp_support_status.setOnItemSelectedListener(this);
    }
    private void initSpinner(){
        boolean isConnect = publicClass.isConnection();
        if (!isConnect){
            publicClass.showToast("از وصل بودن اینترنت مطمئن شوید");
            return;
        }
        sp_support_attach = (Spinner) view.findViewById(R.id.spinner_support_detail_ticket_attach);
        sp_support_status = (Spinner) view.findViewById(R.id.spinner_support_detail_ticket_status);

        final List<String> attachUsers = new ArrayList<>();

        String fullUrl = Urls.baseURL + "ClientPortalService.svc/GetUsersOfRole/" + Keys.ROLE_SUPPORT;
        Ion.with(getActivity())
                .load(fullUrl)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if (e != null){
                            publicClass.showToast("خطا در دریافت اطلاعات!");
                            return;
                        }
                        if (result.getHeaders().code() == 200) {
                            attachList = new ArrayList<>();
                            try {
                                JSONArray array = new JSONArray(result.getResult());
                                for (int i = 0; i < array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);
                                    Gson gson = new Gson();
                                    User user = gson.fromJson(object.toString(), User.class);
                                    attachList.add(user);
                                }
                                Collections.sort(attachList, new Comparator<User>() {
                                    @Override
                                    public int compare(User u1, User u2) {
                                        return u2.firstName.compareTo(u1.firstName);
                                    }
                                });
                                for (int i = 0; i < attachList.size(); i++)
                                    attachUsers.add(attachList.get(i).firstName + " " + attachList.get(i).lastName);

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_item, attachUsers);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp_support_attach.setAdapter(dataAdapter);

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else publicClass.showToast(result.getHeaders().message());
                    }
                });



    }

    private ArrayList<DetailTicket> getDetailTickets(){
        boolean isConnect = publicClass.isConnection();
        if (!isConnect){
            publicClass.showToast("از وصل بودن اینترنت مطمئن شوید");
            return null;
        }
        final ArrayList<DetailTicket> detailTicketList = new ArrayList<>();

        progressDialog = ProgressDialog.show(getActivity(), null,
                "در حال دریافت اطلاعات، لطفا صبر نمایید...", false, false);

        String fullUrl = Urls.baseURL + "ClientPortalService.svc/GetTicketThreads/" + userInfo.login.getUsername()  + "/" + userInfo.login.getPassword() + "/" + nodeId;
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
                                JSONArray array = new JSONArray(result.getResult());
                                for (int i = 0; i < array.length(); ++i) {
                                    JSONObject object = array.getJSONObject(i);

                                    Gson gson = new GsonBuilder().create();
                                    DetailTicket detailTicket = gson.fromJson(object.toString(), DetailTicket.class);
                                    detailTicketList.add(detailTicket);
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
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_detail_ticket);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new DetailTicketRecycleAdapter(getActivity(), detailTicketList));

        final FloatingActionButton  fab = (FloatingActionButton) view.findViewById(R.id.fab_follow_up);
        fab.hide();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recyclerView.scrollToPosition(10);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    public void showCreateTicketDialog(){
        boolean isConnect = publicClass.isConnection();
        if (!isConnect){
            publicClass.showToast("از وصل بودن اینترنت مطمئن شوید");
            return;
        }
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_popup_create_ticket);
        final EditText input_title = (EditText) dialog.findViewById(R.id.input_create_ticket_alert_dialog_title);
        final EditText input_text = (EditText) dialog.findViewById(R.id.input_create_ticket_alert_dialog_text);
        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_create_ticket_alert_dialog_title);
        txt_title.setText("ایجاد پاسخ جدید");
        LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.ll_create_ticket_alert_dialog_priority);
        layout.setVisibility(View.GONE);
        FrameLayout fl_accept_submit = (FrameLayout) dialog.findViewById(R.id.fl_create_ticket_dialog_accept_submit);
        FrameLayout fl_unAccept_submit = (FrameLayout) dialog.findViewById(R.id.fl__create_ticket_dialog_un_accept_submit);
        fl_accept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = input_title.getText().toString();
                String text = input_text.getText().toString();
                if (!TextUtils.isEmpty(input_title.getText()) && !TextUtils.isEmpty(input_text.getText()))
                {
                    progressDialog = ProgressDialog.show(getActivity(), null,
                            "در حال دریافت اطلاعات، لطفا صبر نمایید...", false, false);
                    String baseUrl = Urls.baseURL + "ClientPortalService.svc/"+  "CreateThread/" + userInfo.login.getUsername()  + "/" + userInfo.login.getPassword() + "/";
                    String requestURL = String.format( baseUrl + "%s/%s/%s/%s", Uri.encode(title), Uri.encode(text), Uri.encode(String.valueOf(nodeId)), Uri.encode(String.valueOf(userInfo.user.id)));

                    Ion.with(getActivity())
                            .load(requestURL)
                            .asString()
                            .withResponse()
                            .setCallback(new FutureCallback<Response<String>>() {
                                @Override
                                public void onCompleted(Exception e, Response<String> result) {
                                    progressDialog.dismiss();
                                    if (e != null){
                                        publicClass.showToast("خطا در دریافت اطلاعات! "+ e.getMessage());
                                        return;
                                    }
                                    if (result.getHeaders().code() == 200) {
                                        try {
                                            JSONObject object = new JSONObject(result.getResult());
                                            if (object.has("text"))
                                                if (object.getBoolean("text")){
                                                    publicClass.showToast("با موفقیت ارسال شد");
                                                    getDetailTickets();
                                                }

                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                            publicClass.showToast("خطا در دریافت اطلاعات! "+ e1.getMessage());
                                        }
                                    } else publicClass.showToast("خطا در دریافت اطلاعات! ");
                                }
                            });
                    dialog.dismiss();
                }
            }
        });
        fl_unAccept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void showAlertDialog(String title, String text, String yesSubmitText, String noSubmitText){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_popup_public);
        final TextView txt_title = (TextView) dialog.findViewById(R.id.txt_public_dialog_title);
        final TextView txt_text = (TextView) dialog.findViewById(R.id.txt_public_dialog_text);
        final TextView txt_yes_submit = (TextView) dialog.findViewById(R.id.txt_public_dialog_yes_submit_text);
        final TextView txt_no_submit = (TextView) dialog.findViewById(R.id.txt_public_dialog_no_submit_text);
        FrameLayout fl_accept_submit = (FrameLayout) dialog.findViewById(R.id.fl_public_dialog_accept_submit);
        FrameLayout fl_unAccept_submit = (FrameLayout) dialog.findViewById(R.id.fl_public_dialog_un_accept_submit);

        txt_title.setText(title);
        txt_text.setText(text);
        txt_yes_submit.setText(yesSubmitText);
        txt_no_submit.setText(noSubmitText);

        fl_accept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateTicketExternalStatus();
            }
        });
        fl_unAccept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateTicketExternalStatus(){
        boolean isConnect = publicClass.isConnection();
        if (!isConnect){
            publicClass.showToast("از وصل بودن اینترنت مطمئن شوید");
            return;
        }
        progressDialog = ProgressDialog.show(getActivity(), null,
                "در حال دریافت اطلاعات، لطفا صبر نمایید...", false, false);

        String fullUrl = Urls.baseURL + "ClientPortalService.svc/UpdateTicketExternalStatus/"+ userInfo.login.getUsername()  + "/" + userInfo.login.getPassword() + "/" + sp_customer_result.getSelectedItemPosition() + "/" + nodeId + "/" + userInfo.user.id;
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
                            publicClass.showToast("نتیجه " + sp_customer_result.getSelectedItem() + " با موفقیت ثبت شد.");
                        } else publicClass.showToast(result.getHeaders().message());
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_ticket_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_addTicket:
                showCreateTicketDialog();
                return true;
        }
        return false;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_customer_detail_ticket_result:
                if (position != 0) {
                    showAlertDialog("اعلام نتیجه بررسی", parent.getItemAtPosition(position) + " را قبول دارید؟", "بله، ارسال شود", "خیر");
                }
                break;

            case R.id.spinner_support_detail_ticket_attach:
                break;
            case R.id.spinner_support_detail_ticket_status:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnDetailTicketListener {
        // TODO: Update argument type and name
        void onDetailTicket();
    }
}