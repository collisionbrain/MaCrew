package mx.cannavita.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import mx.cannavita.RegisterActivity;
import mx.cannavita.R;

import static mx.cannavita.utils.Constants.HTML_TEXT_PRIVACY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrivacyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrivacyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivacyFragment extends Fragment {


    public WebView wvPrivacy;
    public RadioButton rbAceptar;
    public Context context;
    private View view;

    public PrivacyFragment() {
        // Required empty public constructor
    }

     public static PrivacyFragment newInstance(String param1, String param2) {
        PrivacyFragment fragment = new PrivacyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        context = getContext();
        this.view = inflater.inflate(R.layout.fragment_privacy, container, false);
        wvPrivacy =this.view.findViewById(R.id.wvPrivacy);
        rbAceptar=this.view.findViewById(R.id.rbAceptar);
        wvPrivacy.loadData(HTML_TEXT_PRIVACY, "text/html", "utf-8");
        rbAceptar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    ((RegisterActivity)context).setPrivacyFlag(isChecked);
            }
        });
return this.view;    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        }

    @Override
    public void onDetach() {
        super.onDetach();
      }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
