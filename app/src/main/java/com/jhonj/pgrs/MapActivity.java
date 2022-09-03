package com.jhonj.pgrs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.jhonj.pgrs.Fragments.BodegasFragment;
import com.jhonj.pgrs.Fragments.CestasFragment;
import com.jhonj.pgrs.Fragments.ClandestinoFragment;
import com.jhonj.pgrs.Fragments.EmpresasFragment;
import com.jhonj.pgrs.Fragments.GestoresFragment;
import com.jhonj.pgrs.Fragments.IndebidoFragment;
import com.jhonj.pgrs.Fragments.MicrorutaFragment;
import com.jhonj.pgrs.Fragments.PrincipalFragment;
import com.jhonj.pgrs.Fragments.ReporteFragment;
import com.jhonj.pgrs.Fragments.ZonaFragment;

public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);



        // establezco el evento onClick al navigationView
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargar el fragment principal//
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new PrincipalFragment());
        fragmentTransaction.commit();

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.inicio){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new PrincipalFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId()==R.id.gestores){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new GestoresFragment());
            fragmentTransaction.commit();
        }
        if (item.getItemId() == R.id.reporte){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ReporteFragment());
            fragmentTransaction.commit();
        }

        if(item.getItemId()==R.id.empresas){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new EmpresasFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId()==R.id.bodegas){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new BodegasFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId()==R.id.indebido){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new IndebidoFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId()==R.id.clandestino){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ClandestinoFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId()==R.id.cestas){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new CestasFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId()==R.id.zona_estudio){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ZonaFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId()==R.id.microruta){
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new MicrorutaFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId()==R.id.descarga){
            String PDF_URL= "https://drive.google.com/file/d/1qJUnPLH7LpKunnMNaVqrKET_aQrRXMWk/view?usp=sharing";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PDF_URL)));
        }
        return false;
    }
}
