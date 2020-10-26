package mx.cannavita;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import mx.cannavita.fragments.PrivacyFragment;
import mx.cannavita.fragments.RegisterFragment;
import mx.cannavita.fragments.UserPhotoFragment;
import mx.cannavita.model.User;
import mx.cannavita.utils.DataUtils;
import mx.cannavita.utils.LocalPreference;


public class RegisterActivity extends AppCompatActivity {


    private static FirebaseAuth mAuth;
     private static  LocalPreference preferences;
    private  static String uid;
    private  static User user;

    String fragmentTagRegister = RegisterFragment.class.getSimpleName();
    String fragmentTagPhoto = UserPhotoFragment.class.getSimpleName();
    String fragmentTagPrivacy = PrivacyFragment.class.getSimpleName();
    int fcounter=0,PICK_SELFIE_REQUEST=10;
    AlertDialog.Builder builderError ;
    ProgressDialog progressDialog;

    FloatingActionButton btnAceptat,btnSelfie;
    Fragment fragmentRegister;
    Fragment fragmentPhoto;
    Fragment fragmentPrivacy;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String fragmentTag;
    FragmentTransaction localFragmentTransaction;
    boolean isRegister,dataComplete,isPrivacy;
    LocalPreference prefs;
    Context context;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs=new LocalPreference(this);
        user=new User();
        context=this;

        builderError= new AlertDialog.Builder(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("usuarios");

         preferences=new LocalPreference(context);
         isPrivacy= prefs.loadFlag("PRIVACY_ADVICE_ACEPTED");
        isRegister= prefs.loadFlag("USER_REGISTER_READY");
        btnAceptat =findViewById(R.id.btn_continuar);
        btnAceptat.setOnClickListener(clickNextListener);

        btnSelfie =findViewById(R.id.btn_camera);
        btnSelfie.setOnClickListener(clickSelfieListener);
        fragmentRegister = RegisterFragment.newInstance(null, null);
        fragmentPhoto = UserPhotoFragment.newInstance(null, null);
        fragmentPrivacy = PrivacyFragment.newInstance(null, null);
        localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.fragmentContainer, fragmentPrivacy, fragmentTag).addToBackStack(fragmentTag).commit();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         if (requestCode == PICK_SELFIE_REQUEST) {
             if (resultCode == RESULT_OK) {

                 user.IDB64= data.getData().toString();
                 Bitmap photo=DataUtils.base64ToBitmap(user.IDB64);
                 ((UserPhotoFragment) fragmentPhoto).setBitmapProfile(photo);
                  btnAceptat.setOnClickListener(clickFinishListener);

             }
        }
    }



    public View.OnClickListener clickSelfieListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent selfie = new Intent(v.getContext(), SelfieActivity.class);
            startActivityForResult(selfie,PICK_SELFIE_REQUEST);
        }
    };
    public View.OnClickListener clickFinishListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new registerTask().execute();

        }
    };

    public View.OnClickListener clickNextListener=new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       // isPrivacy= prefs.loadFlag("PRIVACY_ADVICE_ACEPTED");
       // dataComplete= isDataComplete();

        localFragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (fcounter) {

                 case 0:

                    isPrivacy= prefs.loadFlag("PRIVACY_ADVICE_ACEPTED");
                    if(isPrivacy){

                        localFragmentTransaction.replace(R.id.fragmentContainer, fragmentRegister, fragmentTagRegister).addToBackStack(fragmentTag).commit();
                        btnSelfie.hide();
                        fcounter++;


                    }else{

                        ErrorDialog("Aviso","Acepta nuestro acuerdo de privacidad");
                    }
                    break;
                case 1:
                    if(isDataComplete()){
                        if(isAdult()){


                            localFragmentTransaction.replace(R.id.fragmentContainer, fragmentPhoto, fragmentTagPhoto).addToBackStack(fragmentTag).commit();
                            btnSelfie.show();
                            fcounter++;
                        }

                    }else{

                        ErrorDialog("Aviso","No dejes campos vacios");
                    }

                    break;
            case 2:
                new registerTask().execute();


/* mAuth.createUserWithEmailAndPassword(user.Correo,user.FechaNacimiento)
                        .addOnCompleteListener((Executor) context, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    uid=task.getResult().getUser().getUid();
                                    preferences.saveData("ID_USER_KEY",uid);
                                    progressDialog.dismiss();
                                    saveUserData(uid,user);
                                 }else{
                                    task.getException().getCause();
                                }


                            }
                        });
*/

                break;

            }



        Log.i("XXXX","VALE " + fcounter);

     }
};


    public boolean isAdult(){
        String date= user.FechaNacimiento;
        String _date=date.substring(6,10);
        int year=Integer.valueOf(_date);
        if(year<2002)
            return true;
        else
            return false;


    }


    private boolean isDataComplete(){

            try{
                user=((RegisterFragment) fragmentRegister).getData();
                if(user.Nombre.length()>1 &&

                    user.Apellidos.length()>1 &&
                    user.FechaNacimiento.length()>3 &&
                    user.Correo.length()>0 &&
                    user.Telefono.length()>0){


                return true;
            }else{
                return false;
            }
        }catch (Exception ex){
            return false;
        }

    }
    private void ErrorDialog(String title,String message){
        builderError.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builderError.create();
        dialog.show();

    }
    private void actionDialog(String title,String message){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();

    }

    public void setPrivacyFlag(boolean value){
        prefs.saveFlag("PRIVACY_ADVICE_ACEPTED", value);


    }


    public void saveUserData( User user){
         Map<String, Object> usuario = new HashMap<>();
        usuario.put("Id", uid);
        usuario.put("Nombre", user.Nombre);
        usuario.put("Apellidos", user.Apellidos);
        usuario.put("FechaNacimiento", user.FechaNacimiento);
        usuario.put("Acepta", user.Acepta);
        usuario.put("Correo", user.Correo);
        usuario.put("Telefono", user.Telefono);
        usuario.put("Genero", user.Genero);
        usuario.put("IDB64", user.IDB64);
        DatabaseReference ref = database.getReference("registro");
        DatabaseReference usersRef = ref.child("clientes");
        usersRef.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intentStart=new Intent(context,ClubActivity.class);
                    startActivity(intentStart);
                    finish();

                }else{

                        Log.e("xxxxx", "Error adding document", task.getException());
                        actionDialog("ups", "algo salio mal");


                }

            }
        });



/*
        DatabaseReference ref = database.getReference("registro");
        DatabaseReference usersRef = ref.child("clientes");
        usersRef.child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();

            }
        });
        */

    }


    private class registerTask extends AsyncTask<Void, Void, Void> {
         @Override
        protected void onPreExecute() {
             actionDialog("Creando usuario","Estamos creando tu cuenta");

   }
        @Override
        protected Void doInBackground(Void... params) {
            try {

                mAuth.createUserWithEmailAndPassword(user.Correo,user.FechaNacimiento)


                        .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    uid=task.getResult().getUser().getUid();
                                    preferences.saveData("ID_USER_KEY",uid);
                                    new saveDataTask().execute();

                                }else{
                                    task.getException().getCause();
                                }


                            }
                        });


            }catch (Exception ex){
                ex.getStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void param) {

        }
    }
    private class saveDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                saveUserData(user);

            }catch (Exception ex){
                ex.getStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            progressDialog.dismiss();


        }
    }

}
