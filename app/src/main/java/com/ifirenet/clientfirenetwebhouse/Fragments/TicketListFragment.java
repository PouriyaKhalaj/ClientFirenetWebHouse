package com.ifirenet.clientfirenetwebhouse.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ifirenet.clientfirenetwebhouse.Adapters.TicketRecyclerAdapter;
import com.ifirenet.clientfirenetwebhouse.Links.Tickets;
import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.Client.ClientTicketFilter;
import com.ifirenet.clientfirenetwebhouse.Utils.Client.CreateTicket;
import com.ifirenet.clientfirenetwebhouse.Utils.Keys;
import com.ifirenet.clientfirenetwebhouse.Utils.Client.ClientTicket;
import com.ifirenet.clientfirenetwebhouse.Utils.PublicClass;
import com.ifirenet.clientfirenetwebhouse.Utils.SharedPreference;
import com.ifirenet.clientfirenetwebhouse.Utils.Support.SupportTicket;
import com.ifirenet.clientfirenetwebhouse.Utils.Support.SupportTicketFilter;
import com.ifirenet.clientfirenetwebhouse.Utils.Urls;
import com.ifirenet.clientfirenetwebhouse.Utils.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketListFragment.OnQuestionFragmentListener} interface
 * to handle interaction events.
 * Use the {@link TicketListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketListFragment extends Fragment implements TicketRecyclerAdapter.OnQuestionsRecyclerAdapterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    View view;
    private RecyclerView recyclerView;
    FloatingActionButton fab;
    ProgressDialog progressDialog;
    ArrayList<Object> allTicketList = new ArrayList<>();
    Object objectFilter;
    User user;
    PublicClass publicClass;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnQuestionFragmentListener mListener;

    public TicketListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TicketListFragment newInstance() {
        TicketListFragment fragment = new TicketListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question_list, container, false);
        objectFilter = null;
        publicClass = new PublicClass(getActivity());
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_question_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        createItemList();
    }

    private void createItemList() {
        final SharedPreference preference = new SharedPreference(getActivity());
        String user_json = preference.Get(Keys.PREF_APP, Keys.User);
        if (user_json.equals(Keys.NULL))
            return;
        progressDialog = ProgressDialog.show(getActivity(), null,
                "در حال دریافت اطلاعات، لطفا صبر نمایید...", false, false);
        Gson gson = new Gson();
        user = gson.fromJson(user_json, User.class);
        Tickets tickets = new Tickets(user.id, -1, -1);
        String fullUrl;
        if (user.role.equals("TicketingManager"))
            fullUrl = tickets.getSupportTicketUrl();
        else
            fullUrl = tickets.getClientTicketUrl();

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
                            if (user.role.equals("TicketingManager"))
                                setSupportTickets(result.getResult());
                            else if (user.role.equals("Customer"))
                                setClientTickets(result.getResult());
                        }
                        else publicClass.showToast(result.getHeaders().message());
                    }
                });
    }

    private void setClientTickets(String json_str){
        allTicketList = new ArrayList<>();
        //String s = result.getResult();
        try {
            JSONArray array = new JSONArray(json_str);
            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);

                Gson gson = new GsonBuilder().create();
                allTicketList.add(gson.fromJson(object.toString(), ClientTicket.class));
            }
            progressDialog.dismiss();
            displayData(allTicketList);
        } catch (JSONException e1) {
            progressDialog.dismiss();
            e1.printStackTrace();
        }
    }
    private void setSupportTickets(String json_str){
        allTicketList = new ArrayList<>();
        //String s = result.getResult();
        try {
            JSONArray array = new JSONArray(json_str);
            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);

                Gson gson = new GsonBuilder().create();
                allTicketList.add(gson.fromJson(object.toString(), SupportTicket.class));
            }
            progressDialog.dismiss();
            displayData(allTicketList);
        } catch (JSONException e1) {
            progressDialog.dismiss();
            e1.printStackTrace();
        }
    }

    private void displayData(ArrayList<Object> objects){

        recyclerView.setAdapter(new TicketRecyclerAdapter(getActivity().getApplicationContext(), objects, new TicketListFragment()));

        fab = (FloatingActionButton) view.findViewById(R.id.fab_follow_up);
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

    private void showFilterDialog(final MenuItem item) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_popup_client_ticket_filter);

        final EditText input_trackingCode = (EditText) dialog.findViewById(R.id.input_alert_dialog_tracking_code);
        if (objectFilter != null){
            if (objectFilter instanceof ClientTicketFilter){
                ClientTicketFilter filter = (ClientTicketFilter) objectFilter;
                input_trackingCode.setText(filter.getTrackingCode());
            } else if (objectFilter instanceof SupportTicketFilter){
                SupportTicketFilter filter = (SupportTicketFilter) objectFilter;
                input_trackingCode.setText(String.valueOf(filter.getTrackingCode()));
            }
        }
        FrameLayout fl_accept_submit = (FrameLayout) dialog.findViewById(R.id.fl_dialog_accept_submit);
        FrameLayout fl_unAccept_submit = (FrameLayout) dialog.findViewById(R.id.fl_dialog_un_accept_submit);

        fl_accept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(input_trackingCode.getText())) {
                    ArrayList<Object> objectList = new ArrayList<Object>();
                    for (int i = 0; i < allTicketList.size(); i++) {
                        if (allTicketList.get(i) instanceof ClientTicket) {
                            ClientTicket clientTicket = (ClientTicket) allTicketList.get(i);
                            int code = Integer.parseInt(input_trackingCode.getText().toString());
                            if (clientTicket.trackingCode == code) {
                                objectList.add(clientTicket);
                            }
                            ClientTicketFilter filter = new ClientTicketFilter();
                            filter.setTrackingCode(code);
                            objectFilter = filter;
                        }
                        else if (allTicketList.get(i) instanceof SupportTicket) {
                            SupportTicket supportTicket = (SupportTicket) allTicketList.get(i);
                            int code = Integer.parseInt(input_trackingCode.getText().toString());
                            if (supportTicket.trackingCode == code) {
                                objectList.add(supportTicket);
                            }
                            SupportTicketFilter filter = new SupportTicketFilter();
                            filter.setTrackingCode(code);
                            objectFilter = filter;
                        }
                    }
                    if (item != null) {
                        tintMenuIcon(getActivity(), item, R.color.green);
                    }
                    displayData(objectList);
                   // recyclerView.getAdapter().notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    publicClass.showToast("جهت جستجو اطلاعات خواسته شده را پر نمایید");
                }
            }
        });
        fl_unAccept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (item != null) {
                    tintMenuIcon(getActivity(), item, android.R.color.white);
                }
                objectFilter = null;
                displayData(allTicketList);
            }
        });

        dialog.show();
    }

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }

    public void showCreateTicketDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_popup_create_ticket);
        final EditText input_title = (EditText) dialog.findViewById(R.id.input_create_ticket_alert_dialog_title);
        final EditText input_text = (EditText) dialog.findViewById(R.id.input_create_ticket_alert_dialog_text);
        final EditText input_priority = (EditText) dialog.findViewById(R.id.input_create_ticket_alert_dialog_priority);
        FrameLayout fl_accept_submit = (FrameLayout) dialog.findViewById(R.id.fl_create_ticket_dialog_accept_submit);
        FrameLayout fl_unAccept_submit = (FrameLayout) dialog.findViewById(R.id.fl__create_ticket_dialog_un_accept_submit);
        fl_accept_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = input_title.getText().toString();
                String text = input_text.getText().toString();
                int priority = 1;
                if (!TextUtils.isEmpty(input_title.getText()) && !TextUtils.isEmpty(input_text.getText()) && !TextUtils.isEmpty(input_priority.getText()))
                {
                    CreateTicket ticket = new CreateTicket(title, text, 1, user.id);
                    String fullUrl = Urls.baseUrl + "ClientPortalService.svc/CreateTicket/" + title + "/" + text + "/" + priority + "/" + user.id;
                    Ion.with(getActivity())
                            .load(fullUrl)
                            .asString()
                            .withResponse()
                            .setCallback(new FutureCallback<Response<String>>() {
                                @Override
                                public void onCompleted(Exception e, Response<String> result) {
                                    if (e != null){
                                        progressDialog.dismiss();
                                        publicClass.showToast("خطا در دریافت اطلاعات!");
                                        return;
                                    }
                                    if (result.getHeaders().code() == 200) {
                                        if (user.role.equals("Client"))
                                        {

                                        }
                                    }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ticket_list_fragment, menu);

        MenuItem menuItemAdd = menu.findItem(R.id.action_addTicket);
        MenuItem menuFilter = menu.findItem(R.id.action_search);

        /*if (menuItemAdd != null) {
            tintMenuIcon(getActivity(), menuItemAdd, android.R.color.holo_green_light);
        }
        if (menuFilter != null) {
            tintMenuIcon(getActivity(), menuFilter, android.R.color.holo_orange_light);
        }*/

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                showFilterDialog(item);
                return true;
            case R.id.action_addTicket:
                publicClass.showToast("Click on add icon");
                showCreateTicketDialog();
                return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionFragmentListener) {
            mListener = (OnQuestionFragmentListener) context;
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
    public void onItemClick(Object object) {
        if (object instanceof ClientTicket){
            ClientTicket clientTicket = (ClientTicket) object;
            mListener.onQuestionDetail(clientTicket.nodeID);
        } else if(object instanceof SupportTicket){
            SupportTicket supportTicket = (SupportTicket) object;
            mListener.onQuestionDetail(supportTicket.nodeID);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnQuestionFragmentListener {
        // TODO: Update argument type and name
        void onQuestionDetail(int nodeId);
    }
}
