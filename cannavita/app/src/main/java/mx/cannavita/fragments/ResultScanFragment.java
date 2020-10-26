package mx.cannavita.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import mx.cannavita.R;
import mx.cannavita.VideoConfig;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultScanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CODE_SCANNED = "00000";
    Context context;
    ProgressDialog progressDialog;
    ImageView dataProduct;
    YouTubePlayerView videoView;
    YouTubePlayer _youTubePlayer;
    private GestureDetector gdt;
    private FirebaseFirestore db;
    private static final int MIN_SWIPPING_DISTANCE = 50;
    private static final int THRESHOLD_VELOCITY = 50;
    int page=0;


    // TODO: Rename and change types of parameters
    private String codeScanned;


    public ResultScanFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ResultScanFragment newInstance(String code) {
        ResultScanFragment fragment = new ResultScanFragment();
        Bundle args = new Bundle();

        args.putString(CODE_SCANNED, code);
         fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            codeScanned = getArguments().getString(CODE_SCANNED);

         }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_result_scan, container, false);
        dataProduct=view.findViewById(R.id.imgPeoductInfo);
        videoView=view.findViewById(R.id.videPresentation);
        videoView.initialize(VideoConfig.API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    _youTubePlayer=youTubePlayer;
           // _youTubePlayer.loadVideo("xqtmuhYCdhI");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        gdt = new GestureDetector(new ResultScanFragment.GestureListener());
        videoView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            } });
        videoView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            } });

        getInfo(codeScanned);
        return view;
    }

    private void actionDialog(String title,String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if (e1.getX() - e2.getX() > MIN_SWIPPING_DISTANCE && Math.abs(velocityX) > THRESHOLD_VELOCITY)
            {


                page=1;
                setView(page);
                return false;
            }
            else if (e2.getX() - e1.getX() > MIN_SWIPPING_DISTANCE && Math.abs(velocityX) > THRESHOLD_VELOCITY)
            {
                page=2;

                                return false;
            }
            return false;
        }
    }

    public void setView(int page){
        switch (page){
            case 1:

                this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoView.setVisibility(View.GONE);
                        dataProduct.setVisibility(View.VISIBLE);

                    }
                });
                            break;
            case 2:
                this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoView.setVisibility(View.VISIBLE);
                        dataProduct.setVisibility(View.GONE);

                    }
                });

                break;



        }
    }
    private void getInfo(String code){

        actionDialog("Aviso","Descargando informacion");
        DocumentReference docRef = db.collection("inventario").document(code);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("SUCESS", "DocumentSnapshot data: " + document.getData());
                        progressDialog.dismiss();
                        String id_video=document.getData().get("url_video").toString();
                        String img_b64=document.getData().get("imgb64").toString();
                        _youTubePlayer.loadVideo(id_video);

                    } else {
                        Log.d("FAIL", "No such document");
                        progressDialog.dismiss();

                    }
                } else {
                    Log.d("ERROR", "get failed with ", task.getException());
                }
            }
        });


    }

}