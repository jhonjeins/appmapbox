package com.jhonj.pgrs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.jhonj.pgrs.Fragments.EnableLlocationFragment;

import java.util.Objects;

public class InicioSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);


    }

    public void irIniciar (View view){
        Intent i = new Intent( this, IniciarSesionActivity.class);
        startActivity(i);
    }

    public void irRegistrarse (View view){
        Intent i = new Intent(this, RegistrarseActivity.class);
        startActivity(i);
    }


}