package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.amayor.R;

public class Fragment_InicioEmpleado extends Fragment {

    private static final String ARG_REPARTIDOR_ID = "repartidor_id";
    private int mRepartidorId;

    public Fragment_InicioEmpleado() {
        // Required empty public constructor
    }

    public static Fragment_InicioEmpleado newInstance(int repartidorId) {
        Fragment_InicioEmpleado fragment = new Fragment_InicioEmpleado();
        Bundle args = new Bundle();
        args.putInt(ARG_REPARTIDOR_ID, repartidorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRepartidorId = getArguments().getInt(ARG_REPARTIDOR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_empleado, container, false);

        ImageView botonIrPedidos = view.findViewById(R.id.imageView6);
        botonIrPedidos.setOnClickListener(v -> {
            Fragment_ListaPedidosRepartidor fragment = Fragment_ListaPedidosRepartidor.newInstance(mRepartidorId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}