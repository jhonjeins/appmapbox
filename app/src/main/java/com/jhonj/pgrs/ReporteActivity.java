package com.jhonj.pgrs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jhonj.pgrs.http.API;
import com.jhonj.pgrs.http.ApiServices;
import com.jhonj.pgrs.http.ResponseReporte;
import com.jhonj.pgrs.modelos.Reporte;
import com.jhonj.pgrs.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReporteActivity extends AppCompatActivity {
    Spinner spinnerCritico;
    Spinner spinnerResiduo;
    private String criticoSelecciondado;
    private String residuoSeleccionado;
    Button button_enviar_reporte;
    double longitud, latitud;
    private EditText editTextNombre, editTextCorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            latitud = bundle.getDouble("latitud");
            longitud =  bundle.getDouble("longitud");
        }
        editTextNombre = findViewById(R.id.et_nombre);
        editTextCorreo = findViewById(R.id.et_correo);
        button_enviar_reporte = findViewById(R.id.button_enviar_reporte);
        button_enviar_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = editTextNombre.getText().toString();
                String correo = editTextCorreo.getText().toString();
                String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                if (nombre.isEmpty()){
                    Toast.makeText(ReporteActivity.this, "Debe de ingresar el nombre", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (correo.isEmpty()){
                    Toast.makeText(ReporteActivity.this, "Debe de ingresar el correo", Toast.LENGTH_SHORT).show();
                    return;
                }
                Reporte reporte = new Reporte();
                reporte.setNombre(nombre);
                reporte.setCorreo(correo);
                reporte.setCritico(Integer.parseInt(criticoSelecciondado));
                reporte.setLatitud(latitud);
                reporte.setLongitud(longitud);
                reporte.setFecha(fecha);
                reporte.setResiduo(residuoSeleccionado);

                Call<ResponseReporte> enviarReporte = ApiServices.getClientRestrofit().create(API.class).enviarReporte(reporte);

                enviarReporte.enqueue(new Callback<ResponseReporte>() {
                    @Override
                    public void onResponse(Call<ResponseReporte> call, Response<ResponseReporte> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(ReporteActivity.this, "El reporte se envio correctamente", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseReporte> call, Throwable t) {
                        Toast.makeText(ReporteActivity.this, "No fue posible enviar el reporte", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        loadSpinnerRack();
        loadSpinnerResiduo();
    }

    private void loadSpinnerRack(){
        //Obtiene el array de las unidades de medida
        String[] array = getArrayString(R.array.critico);

        //Obtiene la lista de Strings
        List<String> arrayList = Utils.convertArrayStringListString(array);

        //Creamos el adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_critico, arrayList);
        spinnerCritico = findViewById(R.id.spinner_critico);
        spinnerCritico.setAdapter(adapter);
        spinnerCritico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                criticoSelecciondado = spinnerCritico.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadSpinnerResiduo(){
        //Obtiene el array de las unidades de medida
        String[] array = getArrayString(R.array.tiporesiduo);

        //Obtiene la lista de Strings
        List<String> arrayList = Utils.convertArrayStringListString(array);

        //Creamos el adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_residuo, arrayList);
        spinnerResiduo = findViewById(R.id.spinner_tiporesiduo);
        spinnerResiduo.setAdapter(adapter);
        spinnerResiduo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                residuoSeleccionado = spinnerResiduo.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected String[] getArrayString(final int id) {
        return this.getResources().getStringArray(id);
    }

}