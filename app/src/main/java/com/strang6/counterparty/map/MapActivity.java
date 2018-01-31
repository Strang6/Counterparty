package com.strang6.counterparty.map;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.strang6.counterparty.ApiServices.GeocodingService;
import com.strang6.counterparty.Logger;
import com.strang6.counterparty.R;
import com.strang6.counterparty.database.CounterpartyDatabase;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapViewModel.LoadCoordinatesListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private MapViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        int id = getIntent().getExtras().getInt("id");
        Logger.d("MapActivity.onCreate; id = " + id);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MapViewModel.Factory factory = new MapViewModel
                .Factory(CounterpartyDatabase.getDatabase(getApplicationContext()), new GeocodingService());
        viewModel = ViewModelProviders.of(this, factory).get(MapViewModel.class);
        viewModel.setId(id);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Logger.d("MapActivity.onMapReady");
        if (googleMap != null) {
            map = googleMap;
            viewModel.setLoadCoordinatesListener(this);
        }
    }

    @Override
    public void onLoadCoordinates(LatLng latLng) {
        Logger.d("MapActivity.onLoadCoordinates");
        if (latLng != null) {
            map.addMarker(new MarkerOptions().position(latLng));
            CameraPosition position = new CameraPosition.Builder().target(latLng).zoom(17).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        } else {
            Toast.makeText(MapActivity.this, "Не найдены координаты контрагента", Toast.LENGTH_LONG).show();
        }
    }
}
