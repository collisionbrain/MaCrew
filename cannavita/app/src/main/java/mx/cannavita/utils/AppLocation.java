package mx.cannavita.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class AppLocation   {



    static boolean isGPSEnabled = false;
    static boolean isNetworkEnabled = false;
    static boolean canGetLocation = false;
    static Location location;
    protected static LocationManager locationManager;
    static Criteria criteria;
    static String bestProvider;

    public static String getbestProvider(){
        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
        return bestProvider;
    }

    public static LocationManager getLocationManager(){

        return locationManager;
    }
    public static Location getLocation(Context context){

        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                canGetLocation = true;
                if (isNetworkEnabled) {
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }else{

                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

}