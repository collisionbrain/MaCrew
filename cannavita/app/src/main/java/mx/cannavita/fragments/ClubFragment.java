package mx.cannavita.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.opencv.android.Utils;

import java.util.ArrayList;
import java.util.List;

import mx.cannavita.ClubActivity;
import mx.cannavita.R;
import mx.cannavita.RegisterActivity;
import mx.cannavita.db.DbHelper;
import mx.cannavita.model.Objects;
import mx.cannavita.utils.DataUtils;
import mx.cannavita.utils.OnSwipeListener;

import static mx.cannavita.utils.Constants.HTML_TEXT_PRIVACY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClubFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClubFragment extends Fragment {


    public Context context;
    private View view;
    private ImageView imageView,btnCheck;
    private GestureDetector gdt;
    private static final int MIN_SWIPPING_DISTANCE = 50;
    private static final int THRESHOLD_VELOCITY = 50;
    private FirebaseFirestore db;
    private DbHelper ldb;
    private Bitmap bmp;
    private List<Objects> listObj;
    private TextView txtCounter;
    private Button btnPlus,btnMinus;
     private  int e=0;
    private int n=0;
    private int total=0;
    private Objects current_obj;
    public List<Objects> listaPedido=new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    public ClubFragment() {
        // Required empty public constructor
    }

     public static ClubFragment newInstance(String param1, String param2) {
        ClubFragment fragment = new ClubFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        ldb=new DbHelper(getActivity());

        readDocuments();
        listObj=ldb.listaObjetos();
        n=listObj.size();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        context = getContext();
        this.view = inflater.inflate(R.layout.fragment_club, container, false);
        imageView =this.view.findViewById(R.id.imvProduct);
        btnPlus=this.view.findViewById(R.id.opt_plus);
        btnMinus=this.view.findViewById(R.id.opt_minus);
        txtCounter=this.view.findViewById(R.id.opt_counter);
       // btnMap=this.view.findViewById(R.id.opt_map);
        btnCheck=this.view.findViewById(R.id.opt_final);
        swipeRefreshLayout=this.view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        imageView.setOnTouchListener(new OnSwipeListener(context));
        gdt = new GestureDetector(new GestureListener());
        imageView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            } });

      /*  btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ClubActivity)context).startMap();
            }
        });

       */

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // int tt=listaPedido.size();
                ((ClubActivity)context).startMap(listaPedido);
               // ((ClubActivity)context).showTotalDialog(listaPedido);

            }

        });
        btnPlus.setOnClickListener(controlListener);
        btnMinus.setOnClickListener(controlListener);
        setImage(e);
        return this.view;

    }


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
    public int setTotal(){
        return listaPedido.size();
    }
    public void setImage(int element){
        if(element<0 || element>n-1){
            element=0;
            e=0;
        }


        if(listObj.size()>0){
            current_obj=listObj.get(element);
            bmp=DataUtils.base64ToBitmap(current_obj.Cadena);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    imageView.setImageBitmap(bmp);

                }
            });
        }


    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if (e1.getX() - e2.getX() > MIN_SWIPPING_DISTANCE && Math.abs(velocityX) > THRESHOLD_VELOCITY)
            {
              //  Toast.makeText(context, "You have swipped left side", Toast.LENGTH_SHORT).show();
                /* Code that you want to do on swiping left side*/
                e++;
                setImage(e);

                return false;
            }
            else if (e2.getX() - e1.getX() > MIN_SWIPPING_DISTANCE && Math.abs(velocityX) > THRESHOLD_VELOCITY)
            {
                //Toast.makeText(context, "You have swipped right side", Toast.LENGTH_SHORT).show();
                /* Code that you want to do on swiping right side*/
                e--;
                setImage(e);
                return false;
            }
            return false;
        }
    }

    private void readDocuments(){
        db.collection("objetos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("XXXX", document.getId() + " => " + document.getData().get("valor").toString());
                                Log.d("YYY", "  => "+ document.getData().get("posicion").toString());

                                if(ldb.checkDataBase()){
                                    ldb.insertObjects(document.getId(),
                                            document.getData().get("valor").toString(),
                                            document.getData().get("posicion").toString());
                                     listObj=ldb.listaObjetos();
                                }
                            }

                        } else {
                            Log.w("XXXX", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    SwipeRefreshLayout.OnRefreshListener refreshListener=new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            readDocuments();
        }
    } ;
    View.OnClickListener controlListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if(v.getId()==R.id.opt_plus){
               // current_obj.IdObject;
                listaPedido.add(current_obj);
                txtCounter.setText(String.valueOf(setTotal()));

            }

            if(v.getId()==R.id.opt_minus){
                listaPedido.remove(current_obj);
                txtCounter.setText(String.valueOf(setTotal()));

            }

        }
    };

}
