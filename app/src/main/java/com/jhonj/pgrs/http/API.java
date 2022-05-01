package com.jhonj.pgrs.http;

import com.jhonj.pgrs.modelos.Reporte;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {

    @POST("https://proygrs.herokuapp.com/agregar_reporte.php")
    Call<ResponseReporte> enviarReporte(@Body Reporte param);
}
