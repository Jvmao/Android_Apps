package com.josevicente.ap2app.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.josevicente.ap2app.R;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

public class LocMapaFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks {
    //Variables
    GoogleMap map;
    Location mLocation1 = new Location("");
    Location mLocation2 = new Location("");

    GoogleApiClient googleApiClient;
    Marker marker;

    LatLng latLng1;
    LatLng latLng2;

    LocationRequest mLocationRequest;

    //Variables fragment_loc_mapa.xml
    private ImageButton imageBtnAlertLoc;

    //Variables Distancia
    NumberFormat numberFormat;
    double dist;
    String km;

    String localizacion;
    List<Address> addressList;
    Address address;

    //Añadimos las Variables para el Alert Dialog
    private AlertDialog.Builder alertDialogBilder;
    private AlertDialog dialog;
    private  LayoutInflater inflaterAlert;
    private EditText locInfo;
    private ImageButton imageBtnSearchLoc;
    private ImageButton imageBtnExitLoc;


    //Constructor
    public LocMapaFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_loc_mapa, container, false);

        //Cargamos el Mapa en el fragementLocationID definido en fragmenet_loc_mapa.xml
        MapFragment mapFragment = (MapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragmentLocationID);
        mapFragment.getMapAsync(this);

        imageBtnAlertLoc = view.findViewById(R.id.imageBtnAlertLoc);
        //imageBtnAlertLoc = getView().findViewById(R.id.imageBtnAlertLoc);

        //Alert Dialog creado en search_loc_map.xml
        imageBtnAlertLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Instanciamos el Alert Dialog
                //alertDialogBilder = new AlertDialog.Builder(view.getContext());
                alertDialogBilder = new AlertDialog.Builder(getView().getContext());
                //inflaterAlert = LayoutInflater.from(view.getContext());
                inflaterAlert = LayoutInflater.from(getView().getContext());
                final View viewAlert = inflaterAlert.inflate(R.layout.search_loc_map,null); //Le pasamos search_loc_map.xml

                //Instanciamos los elemento creados en search_loc_map.xml
                locInfo = viewAlert.findViewById(R.id.locInfo);
                imageBtnSearchLoc = viewAlert.findViewById(R.id.imageBtnSearchLoc);
                imageBtnExitLoc = viewAlert.findViewById(R.id.imageBtnExitLoc);

                //Botón para realizar la Búsqueda
                imageBtnSearchLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(locInfo !=null){
                            busqueda(); //Método que realiza la búsqueda
                            dialog.dismiss(); //Cierra el AlertDialog
                        }else{
                            Toast.makeText(getActivity(),"Introduce una Localización Válida",Toast.LENGTH_LONG).show();
                        }

                    }
                });

                //Botón que nos permite salir del AlertDialog
                imageBtnExitLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //Instanciamos el AlertDialog creado en update_lista.xml
                alertDialogBilder.setView(viewAlert);
                dialog = alertDialogBilder.create();
                dialog.show();
            }
        });
        return view;
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
        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

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

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        buildGoogleApiClient(); //Pasamos el método para crear un Cliente en Google Maps
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation1 = location;
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
        //Peticiones de Conexión
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //Establecemos permisos de Conexión
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocation1 = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(mLocation1 !=null){
            //Determinamos las Coordenadas por Defecto del dispositivo
            double currentLatitud = mLocation1.getLatitude();
            double currentLongitud = mLocation1.getLongitude();

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
                    .build();
            googleApiClient.connect();
        }
    }

    //Método para realizar búsuqedas en google maps
    public void busqueda(){
        localizacion = locInfo.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        try {
            addressList = geocoder.getFromLocationName(localizacion, 1);

            if(addressList !=null){
                for (int i = 0; i<addressList.size(); i++) {
                    address = addressList.get(0);
                    latLng2 = new LatLng(address.getLatitude(), address.getLongitude());
                    //map.addMarker(new MarkerOptions().position(latLng2).title(localizacion));
                    //map.animateCamera(CameraUpdateFactory.newLatLng(latLng2));

                    distancia(); //Añadimos el método que calcula la distancia entre dos marcadores

                    dist = Math.round(SphericalUtil.computeDistanceBetween(latLng1,latLng2)); //Enlaza los dos marcadores con una línea
                    km = numberFormat.format(dist/1000)+" Km";
                    map.addMarker(new MarkerOptions().position(latLng2).title(localizacion+" a "+km));
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng2));
                }
            }else{
                Toast.makeText(getActivity(),"No existe la localización",Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"Error de Localización",Toast.LENGTH_LONG).show();
        }
    }

    //Método para establecer la distancia entre dos puntos
    public void distancia(){
        if(mLocation1 !=null && mLocation2 !=null){
            numberFormat = NumberFormat.getNumberInstance();
            dist = Math.round(SphericalUtil.computeDistanceBetween(latLng1,latLng2)); //Enlaza los dos marcadores con una línea
            km = "Distancia: "+numberFormat.format(dist/1000)+" Km";

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(latLng1).add(latLng2).color(Color.GRAY); //Muestra la línea entre los marcadores
            map.addPolyline(polylineOptions);

            if(dist >= 1000){
                numberFormat.setMaximumFractionDigits(2);
                //Muestra la distancia en KM entre los dos marcadores
                Toast.makeText(getActivity(),km,Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(),"No es posible calcular la distancia",Toast.LENGTH_LONG).show();
        }
    }
}
