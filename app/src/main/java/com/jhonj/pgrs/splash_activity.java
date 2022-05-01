package com.jhonj.pgrs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import com.jhonj.pgrs.Fragments.EnableLlocationFragment;

import java.util.Objects;

public class splash_activity extends AppCompatActivity {
    private final int DURACION_SPLASH = 2000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activity);
        new Handler().postDelayed(new Runnable() {
            public void run() {

                    Intent intent = new Intent(splash_activity.this, InicioSesion.class);
                    startActivity(intent);
                    finish();

            };
        }, DURACION_SPLASH);
    }


    public void GPSStatus() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}

        try {
            network_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ignored) {}

        if(!gps_enabled && !network_enabled) {
            Toast.makeText(this, "On Location in High Accuracy", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            GPSStatus();
        }
    }

    private void enable_location() {
        EnableLlocationFragment enable_llocationFragment = new EnableLlocationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.from_right, R.anim.to_left, R.anim.from_left, R.anim.to_right);
        getSupportFragmentManager().popBackStackImmediate();
        transaction.replace(R.id.splash_init, enable_llocationFragment).addToBackStack(null).commit();

    }
}