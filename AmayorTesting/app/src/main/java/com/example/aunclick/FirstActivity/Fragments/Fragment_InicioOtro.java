package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.amayor.R;

public class Fragment_InicioOtro extends Fragment {

    private static final String ARG_TIENDA_ID = "tienda_id";

    private int mTiendaId;

    public Fragment_InicioOtro() {
        // Required empty public constructor
    }

    public static Fragment_InicioOtro newInstance(int tiendaId) {
        Fragment_InicioOtro fragment = new Fragment_InicioOtro();
        Bundle args = new Bundle();
        args.putInt(ARG_TIENDA_ID, tiendaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTiendaId = getArguments().getInt(ARG_TIENDA_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_otro, container, false);

        // Configurar el botón para ir a lista de citas
        ImageView botonIrCitas = view.findViewById(R.id.imageView5);
        botonIrCitas.setOnClickListener(v -> {
            Fragment_ListaCitas fragment = Fragment_ListaCitas.newInstance(mTiendaId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}