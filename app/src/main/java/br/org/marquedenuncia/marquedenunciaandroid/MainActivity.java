package br.org.marquedenuncia.marquedenunciaandroid;

import android.location.Location;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private SupportMapFragment mapFrag;
    private GoogleMap map;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleMapOptions options = new GoogleMapOptions();
        options.zOrderOnTop(true);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag1);
        map = mapFrag.getMap();

        configMap();
    }

    public void configMap(){
        map = mapFrag.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);

        LatLng latLng = new LatLng(-12.8809348,-38.4175853);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(11).bearing(0).tilt(90).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);

        //map.moveCamera(update);
        map.animateCamera(update, 3000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onCancel() {
            }
        });

        //Events
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (marker != null) {
                    marker.remove();
                }
            }
        });
        callConnectionGPS();
    }

    public void customAddMarker(LatLng latLng, String title, String snipper){
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title(title).snippet(snipper).draggable(true);

        Marker marker =  map.addMarker(options);
    }


    private synchronized void callConnectionGPS(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(l != null){

            customAddMarker(new LatLng(l.getLatitude(),l.getLongitude()), "My Location", "");
        }

    }


    //Implementaçao Default
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
}
