package com.josevicente.proyecto_finalpmm.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.josevicente.proyecto_finalpmm.R;


public class FotoFragment extends Fragment {
    //Variables para realizar fotos desde el dispositivo
    private ImageButton imagePic;
    private static final int CAM_REQUEST=1313;
    private Bitmap bitmap;


    private OnFragmentInteractionListener mListener;

    public FotoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foto, container, false);

        //Instanciamos las variables creadas en fragment_foto
        imagePic = view.findViewById(R.id.imageFoto);

        //Habilitamos el Listener del Botón
        imagePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Accedemos a la cámara del dispositivo
                Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentFoto,CAM_REQUEST);
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    //Generamos el método onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Nos permite la opción de hacer una foto al dispositivo y añadirla a la App
        if(requestCode == CAM_REQUEST  && resultCode == Activity.RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            imagePic.setImageBitmap(bitmap); //Le pasamos el imageButton imagePic
        }
    }
}
