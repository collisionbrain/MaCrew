package mx.cannavita;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import mx.cannavita.permissions.PermissionHandler;
import mx.cannavita.permissions.Permissions;
import mx.cannavita.utils.LocalPreference;


public class SplashActivity extends AppCompatActivity {


     LocalPreference prefs;
     Context context;
     Intent intentStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs=new LocalPreference(this);
        context=this;
        if(isIdExistence()){
            intentStart=new Intent(context,ClubActivity.class);

        }else{
            intentStart=new Intent(context, RegisterActivity.class);

        }
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE };
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                startActivity(intentStart);
                finish();
            }
        });

    }


    public boolean isIdExistence(){
        try {
            String id = prefs.loadData("ID_USER_KEY");
            if(id.length()>5)
                return true ;
            else
                return false;
        }
        catch (Exception ex){

            return false;
        }


        }






}
