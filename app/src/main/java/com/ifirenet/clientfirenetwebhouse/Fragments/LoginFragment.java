package com.ifirenet.clientfirenetwebhouse.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ifirenet.clientfirenetwebhouse.Links.Login;
import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.Keys;
import com.ifirenet.clientfirenetwebhouse.Utils.PublicClass;
import com.ifirenet.clientfirenetwebhouse.Utils.SharedPreference;
import com.ifirenet.clientfirenetwebhouse.Utils.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginFragmentListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnLoginFragmentListener mListener;
    View view;
    EditText input_username, input_passdword;
    CardView submit_signIn;
    SharedPreference preference;
    PublicClass publicClass;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        return view;
    }
    private void init(){
        input_username = (EditText) view.findViewById(R.id.input_login_username);
        input_passdword = (EditText) view.findViewById(R.id.input_login_password);
        submit_signIn = (CardView) view.findViewById(R.id.card_view_login_submit_sign_in);
        input_username.requestFocus();
        preference = new SharedPreference(getActivity());
        publicClass = new PublicClass(getActivity());

        submit_signIn.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentListener) {
            mListener = (OnLoginFragmentListener) context;
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
    public void onClick(View v) {
        boolean isConnect = publicClass.isConnection();
        if (!isConnect){
            publicClass.showToast("از وصل بودن اینترنت مطمئن شوید");
            return;
        }
        String username = input_username.getText().toString();
        String password = input_passdword.getText().toString();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null,
                    "در حال دریافت اطلاعات، لطفا صبر نمایید...", false, false);

            Login login = new Login(username, password);
            Ion.with(getActivity())
                    .load(login.getLoginUrl())
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            progressDialog.dismiss();
                            User user = new User();
                            if (e != null){
                                publicClass.showToast("خطا در دریافت اطلاعات!" + e);
                                return;
                            }
                            String res = result.getResult();
                            if (!TextUtils.isEmpty(res)) {
                                JSONObject object = null;
                                try {
                                    object = new JSONObject(res);

                                    if (object.has("ID")) {
                                        Gson gson = new Gson();
                                        user = gson.fromJson(res, User.class);
                                        if (object.getInt("ID") == 0)
                                        {
                                            publicClass.showToast(user.message);
                                            input_passdword.setText(null);
                                            return;
                                        }
                                        publicClass.showToast(user.message);
                                        preference.Set(Keys.PREF_APP, Keys.User, res);
                                        mListener.onLoginFragment(true);
                                    } else {
                                        publicClass.showToast("ورود نا موفق!");
                                        input_passdword.setText(null);
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    progressDialog.dismiss();
                                    publicClass.showToast("خطا در دریافت اطلاعات!");
                                    input_passdword.setText(null);
                                }
                            }
                        }
                    });
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
    public interface OnLoginFragmentListener {
        // TODO: Update argument type and name
        void onLoginFragment(boolean isLogin);
    }
}
