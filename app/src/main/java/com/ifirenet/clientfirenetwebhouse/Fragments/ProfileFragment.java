package com.ifirenet.clientfirenetwebhouse.Fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.Keys;
import com.ifirenet.clientfirenetwebhouse.Utils.SharedPreference;
import com.ifirenet.clientfirenetwebhouse.Utils.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.onProfileFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private onProfileFragmentListener mListener;
    View view;
    SharedPreference preference;
    EditText input_firstName, input_lastName, input_email, input_phone, input_cellPhone, input_company;
    CardView card_submit;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return view;
    }

    private void init(){
        preference = new SharedPreference(getActivity());
        input_firstName = (EditText) view.findViewById(R.id.input_profile_firstName);
        input_lastName = (EditText) view.findViewById(R.id.input_profile_lastName);
        input_email = (EditText) view.findViewById(R.id.input_profile_email);
        input_phone = (EditText) view.findViewById(R.id.input_profile_phoneNumber);
        input_cellPhone = (EditText) view.findViewById(R.id.input_profile_cellPhoneNumber);
        input_company = (EditText) view.findViewById(R.id.input_profile_company);
        card_submit = (CardView) view.findViewById(R.id.card_view_profile_submit);

        CardView card_first_name = (CardView) view.findViewById(R.id.card_view_profile_first_name);
        CardView card_last_name = (CardView) view.findViewById(R.id.card_view_profile_last_name);
        CardView card_email = (CardView) view.findViewById(R.id.card_view_profile_email);
        CardView card_phone = (CardView) view.findViewById(R.id.card_view_profile_phone);
        CardView card_cellPhone = (CardView) view.findViewById(R.id.card_view_profile_phone_number);
        CardView card_company = (CardView) view.findViewById(R.id.card_view_profile_company);

        String user_json = preference.Get(Keys.PREF_APP, Keys.User);
        if (user_json.equals(Keys.NULL)) {
            return;
        }
        Gson gson = new Gson();
        User user = gson.fromJson(user_json , User.class);

        input_firstName.setText(user.firstName);
        input_lastName.setText(user.lastName);
        input_email.setText(user.email);
        input_phone.setText(user.phone);
        input_cellPhone.setText(user.cellPhone);
        input_company.setText(user.company);

        if (TextUtils.isEmpty(user.firstName))
            card_first_name.setVisibility(View.GONE);
        if (TextUtils.isEmpty(user.lastName))
            card_last_name.setVisibility(View.GONE);
        if (TextUtils.isEmpty(user.email))
            card_email.setVisibility(View.GONE);
        if (TextUtils.isEmpty(user.phone))
            card_phone.setVisibility(View.GONE);
        if (TextUtils.isEmpty(user.cellPhone))
            card_cellPhone.setVisibility(View.GONE);
        if (TextUtils.isEmpty(user.company))
            card_company.setVisibility(View.GONE);

        card_submit.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onProfileFragment();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onProfileFragmentListener) {
            mListener = (onProfileFragmentListener) context;
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
        switch (v.getId()){
            case R.id.card_view_profile_submit:
                break;
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
    public interface onProfileFragmentListener {
        // TODO: Update argument type and name
        void onProfileFragment();
    }
}
