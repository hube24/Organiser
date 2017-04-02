package com.example.hubert.organiser;

import android.content.Intent;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private TextView searchTextView;
    private Button searchBtn;
    private Button saveBtn;
    private String currentCoords = null;
    private String addressname = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchTextView = (TextView) findViewById(R.id.Map_searchField);

        searchBtn = (Button) findViewById(R.id.Map_SearchButton);
        searchBtn.setOnClickListener(ClickHandler);

        saveBtn = (Button) findViewById(R.id.Map_saveLocalisationButton);
        saveBtn.setOnClickListener(ClickHandler);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng currloc = new LatLng(50.0626248, 19.9290715);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currloc));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }

    private void searchForAddress()
    {
        String searchTxt = searchTextView.getText().toString();
        List<android.location.Address> addressList = null;
        if(searchTxt!=null && !searchTxt.equals(""))
        {
            Geocoder gc = new Geocoder(this);
            try {
                addressList = gc.getFromLocationName(searchTxt, 1);
            }catch (IOException e){
                e.printStackTrace();
            }
            android.location.Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());

            mMap.clear();
            addressname = address.getAddressLine(0);
            mMap.addMarker(new MarkerOptions().position(latLng).title(addressname));

            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            currentCoords = latLng.toString();

        }else {
            Toast.makeText(this,"Please, enter location first",Toast.LENGTH_SHORT).show();
        }

    }

    private void saveLocalisation()
    {
        Toast.makeText(this,"localisation saved",Toast.LENGTH_SHORT).show();
        if(currentCoords!=null) {
            Intent intent = new Intent();
            intent.putExtra("coords", currentCoords);
            intent.putExtra("address", addressname);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Toast.makeText(this,"Please, enter location first",Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener ClickHandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.Map_SearchButton:
                        searchForAddress();
                    break;
                case R.id.Map_saveLocalisationButton:
                        saveLocalisation();
                    break;
            }
        }
    };
}
