package com.jhonj.pgrs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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