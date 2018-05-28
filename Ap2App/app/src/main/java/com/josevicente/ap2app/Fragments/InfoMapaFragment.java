package com.josevicente.ap2app.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.josevicente.ap2app.R;

import static android.app.Activity.RESULT_OK;


public class InfoMapaFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks {
    //Variables
    GoogleMap map;
    Location mLocation = new Location("");

    GoogleApiClient googleApiClient;
    Marker marker;

    LatLng latLng1;

    LocationRequest mLocationRequest;

    //Variables Google Places API
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    ImageButton placesBtn;



    public InfoMapaFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info_mapa, container, false);

        //Cargamos el Mapa en el fragementInfoID definido en fragmenet_info_mapa.xml
        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragmentInfoID);
        mapFragment.getMapAsync(this);


        //Instanciamos las variables
        placesBtn = v.findViewById(R.id.placesBtn);

        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

            double currentLatitud = mLocation.getLatitude();
            double currentLongitud = mLocation.getLongitude();

            //String type = "restaurants";
            StringBuilder stb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            //StringBuilder stb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters");
            stb.append("location="+currentLatitud+","+currentLongitud);
            stb.append("&radius=1000");
            //stb.append("&types"+type);
            stb.append("&sensor=true");
            stb.append("&key=AIzaSyDaC0sLSFl40JekIisridnIfP2MFuvO-vc");
            Toast.makeText(v.getContext(),"Mostrando...",Toast.LENGTH_LONG).show();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        //Listener pharmacuBtn
        placesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

                    double currentLatitud = mLocation.getLatitude();
                    double currentLongitud = mLocation.getLongitude();

                    //String type = "restaurants";
                    StringBuilder stb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    //StringBuilder stb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters");
                    stb.append("location="+currentLatitud+","+currentLongitud);
                    stb.append("&radius=1000");
                    //stb.append("&types"+type);
                    stb.append("&sensor=true");
                    stb.append("&key=AIzaSyDaC0sLSFl40JekIisridnIfP2MFuvO-vc");
                    Toast.makeText(v.getContext(),"Reiniciando Google Places...",Toast.LENGTH_LONG).show();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Instanciamos el Mapa
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL); //Tipo de mapa que se muestra por pantalla
        //Opciones de Google Maps
        map.getUiSettings().isZoomControlsEnabled();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().isMapToolbarEnabled();
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().isMyLocationButtonEnabled();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().isZoomGesturesEnabled();
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().isScrollGesturesEnabled();
        map.getUiSettings().setScrollGesturesEnabled(true);

        //Comprobación de Permisos
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        buildGoogleApiClient(); //Pasamos el método para crear un Cliente en Google Maps
        map.setMyLocationEnabled(true); //Habilitamos la posición actual
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (marker != null) {
            marker.remove(); //Actualiza el marcador
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Peticiones de Conexión
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(mLocation !=null){
            //Determinamos las Coordenadas por Defecto del dispositivo
            double currentLatitud = mLocation.getLatitude();
            double currentLongitud = mLocation.getLongitude();

            latLng1 = new LatLng(currentLatitud, currentLongitud);

            //Establecemos el Marcador de la localización del usuario
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng1).flat(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title("Estás Aquí");

            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Muestra un mensaje informativo cuando no puede establecer la conexión
        Snackbar.make(getView(),"Conexión Suspendida",Snackbar.LENGTH_LONG).show();
    }

    //Método para crear un Cliente de GoogleMaps
    protected synchronized void buildGoogleApiClient(){
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            googleApiClient.connect();
        }
    }

    //Muestra los lugares cercanos
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

}
