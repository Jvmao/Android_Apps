package com.josevicente.proyecto_finalpmm.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.josevicente.proyecto_finalpmm.Modelo.Jugador;
import com.josevicente.proyecto_finalpmm.R;

public class MapaFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks {
    //Vairables Google Maps
    GoogleMap mapa;
    Location mLocation = new Location("");
    GoogleApiClient googleApiClient;
    Marker marker;
    LocationRequest mLocationRequest;

    public MapaFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mapa, container, false);

        //Cargamos el Mapa en el fragementMapa definido en fragmenet_mapa.xml
        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragmentMapa);
        mapFragment.getMapAsync(this);

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

    //Método Implementado desde onMapReadyCallback
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Instanciamos el Mapa
        mapa = googleMap;

        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL); //Tipo de mapa que se muestra por pantalla

        //Opciones de Google Maps
        mapa.getUiSettings().isZoomControlsEnabled();
        mapa.getUiSettings().isMapToolbarEnabled();
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.getUiSettings().setMapToolbarEnabled(true);

        //Permisos de Conexión
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient(); //le pasamos el método creado
        mapa.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (marker != null) {
            marker.remove(); //Actualiza el marcador
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //Permisos de Conexión
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(mLocation !=null){
            //Determinamos las Coordenadas por Defecto del dispositivo
            double currentLatitud = mLocation.getLatitude();
            double currentLongitud = mLocation.getLongitude();

            LatLng latLng = new LatLng(currentLatitud, currentLongitud);
        /*marker = mapa.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title("Estás Aquí"));
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));*/

            //Establecemos el Marcador de la localización del usuario
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).flat(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title("Estás Aquí");

            mapa.addMarker(markerOptions);
            mapa.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //Snackbar.make(getView(),"Coordenadas: "+currentLatitud+" "+currentLongitud,Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Snackbar.make(getView(),"Conexión Suspendida",Snackbar.LENGTH_LONG).show();
    }

    //Método para crear un Cliente de GoogleMaps
    protected synchronized void buildGoogleApiClient(){
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }
}
