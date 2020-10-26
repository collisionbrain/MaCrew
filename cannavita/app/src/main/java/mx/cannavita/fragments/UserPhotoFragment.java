package mx.cannavita.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.cannavita.R;

public class UserPhotoFragment extends Fragment {

     public Context context;
    private View view;
    private CircleImageView  circleImageView;

    public UserPhotoFragment() {
        // Required empty public constructor
    }

     public static UserPhotoFragment newInstance(String param1, String param2) {
        UserPhotoFragment fragment = new UserPhotoFragment();
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
        this.view = inflater.inflate(R.layout.fragment_userphoto, container, false);
        circleImageView=this.view.findViewById(R.id.profile_image);
        return this.view;
    }

    public  void setBitmapProfile(Bitmap bitmap){
        circleImageView.setImageBitmap(bitmap);

    }

      @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        }

}
