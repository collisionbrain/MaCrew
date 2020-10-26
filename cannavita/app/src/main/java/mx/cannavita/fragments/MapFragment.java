package mx.cannavita.fragments;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import mx.cannavita.ClubActivity;
import mx.cannavita.R;
import mx.cannavita.model.Objects;
import mx.cannavita.utils.AppLocation;
import mx.cannavita.utils.ItemAdapter;

public class MapFragment extends Fragment implements LocationListener {

    private MapView mMapView;
    private ListView listView;
    private double longitude;
    private double latitude;
    private Context context;
    private IMapController mapController;
    private LocationManager locationManager;
    private Location location;
     private Marker startMarker;
    private GeoPoint pointCenter;
    private boolean previus=false;
    private TextView total;
    private boolean devlivery=false;
    private static List<Objects> mlist;
    private  GeoPoint gPt;
    @SuppressWarnings("deprecation")


    public MapFragment() {
        // Required empty public constructor
    }
    public static MapFragment newInstance(List<Objects> _list) {
        MapFragment fragment = new MapFragment();
        mlist=_list;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map, null);
        mMapView = v.findViewById(R.id.mapview1);
        listView=v.findViewById(R.id.list);
        total=v.findViewById(R.id.final_count);
        context=getActivity();
        Configuration.getInstance().setUserAgentValue(getActivity().getPackageName());
        ArrayList<String> list=computeList(mlist);
        ItemAdapter adapter = new ItemAdapter(context,R.layout.list_item,list);
        ArrayAdapter arrayAdapter=new ArrayAdapter(context,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        startMarker = new Marker(mMapView);
        mMapView.setUseDataConnection(true);
        mapController = mMapView.getController();
        mapController.setZoom(2);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setTileProvider(new MapTileProviderBasic(getContext()));

        if(mlist.size()<1)
            total.setText("$0.00");


            location = AppLocation.getLocation(context);
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();

            } else {
                String bestProvider = AppLocation.getbestProvider();
                //AppLocation.getLocationManager().requestLocationUpdates(bestProvider, 1000, 0, this);
            }

/*

        if(!devlivery){
            mMapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {


                    return true;
                }
            });
        }

        mMapView.setBuiltInZoomControls(false);
        mMapView.setMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                 return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {

                return false;
            }
        }, 200));

        mMapView.setMultiTouchControls(true);
*/
        return v;

    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);


    }

    @Override
    public void onPause(){
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
       // latitude = 19.427171;
       // longitude=  -99.166741;
        setLocationInMap(latitude, longitude);


        if (mMapView != null) {

            mMapView.onResume();
        }

        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    return true;

                }
                return false;
            }
        } );
    }
    @Override
    public void onLocationChanged(Location location) {
        if(this.location!=null){
            if( !this.location.equals(location)){
                this.location=location;
            }
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void setLocationInMap(double latitude,double longitude){
        GeoPoint point = new GeoPoint(latitude, longitude);
        mapController = mMapView.getController();
        mapController.setZoom(19);
        mapController.setCenter(point);
        mapController.animateTo(point);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //mMapView.getOverlays().add(startMarker);
        startMarker.setIcon(getResources().getDrawable(R.drawable.marker));
     //   startMarker.setTitle("Aqui nos vemos");
       // startMarker.showInfoWindow();
    }


    private ArrayList<String> computeList(List<Objects> _lista){
        ArrayList<String> lista=new ArrayList<String>();
        for (Objects object:_lista
             ) {
            String cadena=object.IdObject;
            String posicion=object.Posicion;
            lista.add(cadena);

        }
        return  lista;

    }

    private void loadMarker(){
        pointCenter=new GeoPoint(
                mMapView.getMapCenter().getLatitude(),
                mMapView.getMapCenter().getLongitude());

        startMarker.setPosition(pointCenter);
        startMarker.setIcon(getResources().getDrawable(R.drawable.marker));
        startMarker.setTitle("Aqui nos vemos");
        startMarker.showInfoWindow();

          }


}
