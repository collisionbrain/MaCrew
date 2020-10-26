package mx.cannavita;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import mx.cannavita.fragments.ClubFragment;
import mx.cannavita.fragments.CustomeTotalFragment;
import mx.cannavita.fragments.MapFragment;
import mx.cannavita.fragments.QRCodeFragment;
import mx.cannavita.fragments.ResultScanFragment;
import mx.cannavita.fragments.ScannFragment;
import mx.cannavita.model.Objects;


public class ClubActivity extends FragmentActivity implements ScannFragment.OnFragmentNextListener{


    Fragment fragmentScreenMap,fragmentScreenScann,fragmentScreenClub,fragmentScreenCode,fragmentCustomTotal,fragmentResultScan;
    Intent scanResutlIntent;
    String fragmentTagMap = MapFragment.class.getSimpleName();
    String fragmentTagScann = ScannFragment.class.getSimpleName();
    String fragmentTagCode = QRCodeFragment.class.getSimpleName();
    String fragmentTagClub= ClubFragment.class.getSimpleName();
    String fragmentTagDetailFinal= CustomeTotalFragment.class.getSimpleName();
    String fragmentTagResultScan= ResultScanFragment.class.getSimpleName();
    FragmentTransaction localFragmentTransaction;

    ImageButton imgClub,imgScan,imgCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        imgClub=findViewById(R.id.opt_one);
        imgCode=findViewById(R.id.opt_two);
        imgScan=findViewById(R.id.opt_thr);
        imgScan.setOnClickListener(optClickListener);
        imgClub.setOnClickListener(optClickListener);
        imgCode.setOnClickListener(optClickListener);
        scanResutlIntent=new Intent(this,ScanResultActivity.class);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        fragmentScreenClub  = ClubFragment.newInstance("","");
        fragmentScreenScann  = ScannFragment.newInstance();
        fragmentScreenCode  = QRCodeFragment.newInstance(width,height);
        fragmentCustomTotal= QRCodeFragment.newInstance(width,height);
        /*localFragmentTransaction
                .replace(R.id.fragmentContainerClub, fragmentScreenScann, fragmentTagScann)
                .addToBackStack(fragmentTagScann).commit();


            localFragmentTransaction = getSupportFragmentManager().beginTransaction();

*/



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    View.OnClickListener optClickListener=new View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            localFragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch(v.getId()){
                case R.id.opt_one:
                    localFragmentTransaction
                            .replace(R.id.fragmentContainerClub, fragmentScreenClub, fragmentTagClub)
                            .addToBackStack(fragmentTagClub).commit();

                    break;

                case R.id.opt_two:

                    localFragmentTransaction
                            .replace(R.id.fragmentContainerClub, fragmentScreenCode, fragmentTagCode)
                            .addToBackStack(fragmentTagCode).commit();


                    break;

                case R.id.opt_thr:
                    localFragmentTransaction
                            .replace(R.id.fragmentContainerClub, fragmentScreenScann, fragmentTagScann)
                            .addToBackStack(fragmentTagScann).commit();

                    break;

            }

        }
    };


    public void startMap(List<Objects> pedido){
        fragmentScreenMap=MapFragment.newInstance(pedido);
        localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction
                .replace(R.id.fragmentContainerClub, fragmentScreenMap, fragmentTagMap)
                .addToBackStack(fragmentTagMap).commit();

    }


    public void showTotalDialog(List<Objects> pedido){
        localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentCustomTotal= CustomeTotalFragment.newInstance( pedido);
        localFragmentTransaction
                .replace(R.id.fragmentContainerClub, fragmentCustomTotal, fragmentTagDetailFinal)
                .addToBackStack(fragmentTagDetailFinal).commit();

    }
    public void showResultScann(String code){
        scanResutlIntent.putExtra("code",code);
        startActivity(scanResutlIntent);

    }


    @Override
    public void onNextFragment(String code) {
        Log.i("CODE SCANED",code);
        showResultScann(code);
    }
}
