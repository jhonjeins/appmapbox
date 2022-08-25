package com.jhonj.pgrs.Fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jhonj.pgrs.R;
import com.jhonj.pgrs.ReporteActivity;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReporteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReporteFragment extends Fragment implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapLongClickListener {
    private static final String TAG = "DirectionsActivity";

    View mview;
    MapView mapView;
    private LocationComponent locationComponent;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private static final String MARKER_IMAGE_ID = "MARKER_IMAGE_ID";

    public ReporteFragment() {
        // Required empty public constructor
    }


    public static ReporteFragment newInstance(String param1, String param2) {
        ReporteFragment fragment = new ReporteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));

        mview = inflater.inflate(R.layout.fragment_reporte, container, false);
        return mview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map_reporte);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        ReporteFragment.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        mapboxMap.addOnMapLongClickListener(ReporteFragment.this);

                        LocationComponentOptions locationComponentOptions =
                                LocationComponentOptions.builder(getContext())
                                        .pulseEnabled(true)
                                        .pulseColor(Color.GREEN)
                                        .pulseAlpha(.4f)
                                        .pulseInterpolator(new BounceInterpolator())
                                        .build();

                        LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions
                                .builder(getContext(), style)
                                .locationComponentOptions(locationComponentOptions)
                                .build();

                        locationComponent = mapboxMap.getLocationComponent();
                        locationComponent.activateLocationComponent(locationComponentActivationOptions);

                        double lat = mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude();
                        double lng = mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude();

                        CameraPosition old = mapboxMap.getCameraPosition();
                        CameraPosition pos = new CameraPosition.Builder()
                                .target(new LatLng(lat,lng))
                                .zoom(old.zoom)
                                .tilt(old.tilt)
                                .build();

                        //mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos),1000);
                    }
                });


    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }


    @Override
    public boolean onMapLongClick(@NonNull LatLng point) {
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        Intent intent = new Intent(getContext(), ReporteActivity.class);
        intent.putExtra("latitud", originPoint.latitude());
        intent.putExtra("latitud", originPoint.longitude());
        startActivity(intent);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();

        }
    }
}