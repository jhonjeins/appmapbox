package com.jhonj.pgrs.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jhonj.pgrs.InicioSesion;
import com.jhonj.pgrs.R;
import com.jhonj.pgrs.splash_activity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnableLlocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnableLlocationFragment extends Fragment {

    private String PREF_NAME = "pref_name";
    View getView;
    Context context;
    Button enableLocation;
    SharedPreferences sharedPreferences;

    public EnableLlocationFragment() {
        // Required empty public constructor
    }

    public static EnableLlocationFragment newInstance(String param1, String param2) {
        EnableLlocationFragment fragment = new EnableLlocationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        getView = inflater.inflate(R.layout.fragment_enable_llocation, container, false);
        context = getContext();

        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        enableLocation = getView.findViewById(R.id.enable_location_btn);
        enableLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();

            }
        });


        return getView;
    }

    private void getLocationPermission() {

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                123);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetCurrentlocation();
                } else {

                    Toast.makeText(context, "Please Grant permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    public void GPSStatus() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}

        try {
            network_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ignored) {}

        if(!gps_enabled && !network_enabled) {
            Toast.makeText(context, "On Location in High Accuracy", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 2);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            GPSStatus();
        }
    }

    private void GetCurrentlocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
            return;
        }else {
            Intent intent = new Intent(getContext(), InicioSesion.class);
            startActivity(intent);
           getActivity().finish();
        }
    }




}