package com.jhonj.pgrs.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jhonj.pgrs.R;
import com.jhonj.pgrs.ReporteActivity;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class ReporteFragment extends Fragment implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    View mview;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private final String ID = "reporte";
    private final String URL_GET_DATA = "https://proygrs.herokuapp.com/reportes_get.php";
    private static final String PROPERTY_SELECTED = "selected";
    private static final String PROPERTY_NAME = "gid";
    private static final String PROPERTY_REPORTE = "nombre";
    private static final String PROPERTY_CRITICO = "critico";
    private GeoJsonSource geoJsonSource;
    FeatureCollection featureCollection;
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";
    private static final String MARKER_LAYER_ID = "MARKER_LAYER_ID";
    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String MARKER_IMAGE_ID = "MARKER_IMAGE_ID";
    //ruteo
    private static final String TAG = "DirectionsActivity";
    private ImageButton img_report_reportes;
    public ReporteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        mview = inflater.inflate(R.layout.fragment_reporte, container, false);
        img_report_reportes = mview.findViewById(R.id.img_report_reportes);
        img_report_reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),  "Hola", Toast.LENGTH_SHORT).show();
            }
        });

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
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        addDestinationIconSymbolLayer(style);
                        new ReporteFragment.LoadGeoJsonDataTask(ReporteFragment.this).execute();
                        mapboxMap.addOnMapClickListener(ReporteFragment.this);
                        mapboxMap.addOnMapLongClickListener(ReporteFragment.this);

                    }
                }
        );
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }


    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(getActivity(), loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
        loadedMapStyle.addImage(MARKER_IMAGE_ID,
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));

    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    @Override

    public boolean onMapLongClick(@NonNull LatLng point) {

        Intent intent = new Intent(getContext(), ReporteActivity.class);
        intent.putExtra("latitud", point.getLatitude());
        intent.putExtra("longitud", point.getLongitude());
        startActivity(intent);
        return true;
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {

        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(getActivity(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }

    public void setUpData(final FeatureCollection collection) {
        featureCollection = collection;
        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                setupSource(style);
                setUpImage(style);
                setUpMarkerLayer(style);
                setUpInfoWindowLayer(style);
            });
        }
    }


    private void setupSource(@NonNull Style loadedStyle) {
        geoJsonSource = new GeoJsonSource(GEOJSON_SOURCE_ID, featureCollection);
        loadedStyle.addSource(geoJsonSource);
    }

    private void setUpImage(@NonNull Style loadedStyle) {
        loadedStyle.addImage(MARKER_IMAGE_ID, BitmapFactory.decodeResource(
                this.getResources(), R.drawable.critico));
    }

    private void refreshSource() {
        if (geoJsonSource != null && featureCollection != null) {
            geoJsonSource.setGeoJson(featureCollection);
        }
    }

    private void setUpMarkerLayer(@NonNull Style loadedStyle) {
        loadedStyle.addLayer(new SymbolLayer(MARKER_LAYER_ID, GEOJSON_SOURCE_ID)
                .withProperties(
                        iconImage(MARKER_IMAGE_ID),
                        iconAllowOverlap(true),
                        iconOffset(new Float[]{0f, -8f})
                ));
    }

    private void setUpInfoWindowLayer(@NonNull Style loadedStyle) {
        loadedStyle.addLayer(new SymbolLayer(CALLOUT_LAYER_ID, GEOJSON_SOURCE_ID)
                .withProperties(
                        iconImage("{gid}"),
                        iconAnchor(ICON_ANCHOR_BOTTOM),
                        iconAllowOverlap(true),
                        iconOffset(new Float[]{-2f, -28f}))
                .withFilter(eq((get(PROPERTY_SELECTED)), literal(true))));
    }

    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, MARKER_LAYER_ID);
        if (!features.isEmpty()) {
            String name = features.get(0).getStringProperty(PROPERTY_NAME);
            List<Feature> featureList = featureCollection.features();
            if (featureList != null) {
                for (int i = 0; i < featureList.size(); i++) {
                    if (featureList.get(i).getStringProperty(PROPERTY_NAME).equals(name)) {
                        if (featureSelectStatus(i)) {
                            setFeatureSelectState(featureList.get(i), false);
                        } else {
                            setSelected(i);
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void setSelected(int index) {
        if (featureCollection.features() != null) {
            Feature feature = featureCollection.features().get(index);
            setFeatureSelectState(feature, true);
            refreshSource();
        }
    }

    private void setFeatureSelectState(Feature feature, boolean selectedState) {
        if (feature.properties() != null) {
            feature.properties().addProperty(PROPERTY_SELECTED, selectedState);
            refreshSource();
        }
    }

    private boolean featureSelectStatus(int index) {
        if (featureCollection == null) {
            return false;
        }
        return featureCollection.features().get(index).getBooleanProperty(PROPERTY_SELECTED);
    }

    public void setImageGenResults(HashMap<String, Bitmap> imageMap) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                style.addImages(imageMap);
            });
        }
    }

    private static class LoadGeoJsonDataTask extends AsyncTask<Void, Void, FeatureCollection> {

        private final WeakReference<ReporteFragment> activityRef;

        LoadGeoJsonDataTask(ReporteFragment activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... params) {
            ReporteFragment activity = activityRef.get();
            if (activity == null) {
                return null;
            }
            String geoJson = getJSON("https://proygrs.herokuapp.com/reportes_get.php");
            return FeatureCollection.fromJson(geoJson);
        }


        @Override
        protected void onPostExecute(FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            ReporteFragment activity = activityRef.get();
            if (featureCollection == null || activity == null) {
                return;
            }


            for (Feature singleFeature : featureCollection.features()) {
                singleFeature.addBooleanProperty(PROPERTY_SELECTED, false);
            }

            activity.setUpData(featureCollection);
            new ReporteFragment.GenerateViewIconTask(activity).execute(featureCollection);

            Toast.makeText(activity.getContext(),
                    "Clic sostenido para crear un nuevo reporte o Seleccione un marcador para obtener información",
                    Toast.LENGTH_LONG).show();
        }

        public static String getJSON(String url) {
            HttpsURLConnection con = null;
            try {
                URL u = new URL(url);
                con = (HttpsURLConnection) u.openConnection();

                con.connect();


                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                //Log.d("json",sb.toString());
                return sb.toString();


            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.disconnect();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return null;
        }

    }

    private static class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {

        private final HashMap<String, View> viewMap = new HashMap<>();
        private final WeakReference<ReporteFragment> activityRef;
        private final boolean refreshSource;

        GenerateViewIconTask(ReporteFragment activity, boolean refreshSource) {
            this.activityRef = new WeakReference<>(activity);
            this.refreshSource = refreshSource;
        }

        GenerateViewIconTask(ReporteFragment activity) {
            this(activity, false);
        }


        @SuppressWarnings("WrongThread")
        @Override
        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
            ReporteFragment activity = activityRef.get();
            if (activity != null) {
                HashMap<String, Bitmap> imagesMap = new HashMap<>();
                LayoutInflater inflater = LayoutInflater.from(activity.getContext());

                FeatureCollection featureCollection = params[0];

                for (Feature feature : featureCollection.features()) {

                    BubbleLayout bubbleLayout = (BubbleLayout)
                            inflater.inflate(R.layout.symbol_layer_info_window_layout_callout, null);

                    String str = feature.getStringProperty(PROPERTY_NAME);
                    StringBuilder desc = new StringBuilder();
                    for (int i = 0; i < str.length(); i++) {
                        if (i > 0 && (i % 40 == 0)) {
                            desc.append("\n");
                        }

                        desc.append(str.charAt(i));
                    }
                    str = desc.toString();
                    String name = feature.getStringProperty(PROPERTY_NAME);
                    TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
                    titleTextView.setText(name);

                    //if(feature.properties().has("horario"))
                    String style = feature.getStringProperty(PROPERTY_REPORTE);
                    style += "\n" + "Valor de critico: " + feature.getStringProperty(PROPERTY_CRITICO);
                    TextView descriptionTextView = bubbleLayout.findViewById(R.id.info_window_description);
                    descriptionTextView.setText(
                            String.format(activity.getString(R.string.reportes), style));


                    int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    bubbleLayout.measure(measureSpec, measureSpec);

                    float measuredWidth = bubbleLayout.getMeasuredWidth();

                    bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

                    Bitmap bitmap = ReporteFragment.SymbolGenerator.generate(bubbleLayout);
                    imagesMap.put(name, bitmap);
                    viewMap.put(name, bubbleLayout);
                }

                return imagesMap;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
            super.onPostExecute(bitmapHashMap);
            ReporteFragment activity = activityRef.get();
            if (activity != null && bitmapHashMap != null) {
                activity.setImageGenResults(bitmapHashMap);
                if (refreshSource) {
                    activity.refreshSource();
                }
            }
        }
    }

    private static class SymbolGenerator {
        static Bitmap generate(@NonNull View view) {
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            view.layout(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}