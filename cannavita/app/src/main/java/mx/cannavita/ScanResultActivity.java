package mx.cannavita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import mx.cannavita.utils.DataUtils;

public class ScanResultActivity extends YouTubeBaseActivity {
    String code;
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
    Bitmap bmp;
    ImageButton nextBtn,backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        code=getIntent().getStringExtra("code");
        context=this;
        db = FirebaseFirestore.getInstance();
        dataProduct=findViewById(R.id.imgPeoductInfo);
        videoView=findViewById(R.id.videPresentation);
        nextBtn=findViewById(R.id.btn_forw);
        backBtn=findViewById(R.id.btn_back);
        backBtn.setOnClickListener(buttonListener);
        nextBtn.setOnClickListener(buttonListener);
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
        getInfo(code);




    }


    private void actionDialog(String title,String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();

    }


    public void setView(int page){
        switch (page){
            case 1:

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoView.setVisibility(View.GONE);
                        Log.e("111111111"," pagina 1");
                        dataProduct.setImageBitmap(bmp);
                        dataProduct.setVisibility(View.VISIBLE);

                    }
                });
                break;
            case 2:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("2222222"," pagina 2");

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
                        bmp= DataUtils.base64ToBitmap(img_b64);

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

    View.OnClickListener buttonListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btn_back:

                    setView(1);


                    break;

                case R.id.btn_forw:
                    setView(2);
                    break;

            }

        }
    };
}