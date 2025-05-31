package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.amayor.R;

public class Fragment_InicioTienda extends Fragment {

    private static final String ARG_TIENDA_ID = "tienda_id";

    private int mTiendaId;

    public Fragment_InicioTienda() {
        // Required empty public constructor
    }

    public static Fragment_InicioTienda newInstance(int tiendaId) {
        Fragment_InicioTienda fragment = new Fragment_InicioTienda();
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
        View view = inflater.inflate(R.layout.fragment_inicio_tienda, container, false);

        // Configurar el botón para ir a productos
        ImageView botonIrProductosStock = view.findViewById(R.id.botonIrProductosStock);
        botonIrProductosStock.setOnClickListener(v -> {
            Fragment_ProductosTienda fragment = Fragment_ProductosTienda.newInstance(mTiendaId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Configurar el botón para ir a lista de pedidos
        ImageView botonIrListaPedidos = view.findViewById(R.id.botonIrListaPedidos);
        botonIrListaPedidos.setOnClickListener(v -> {
            Fragment_ListaPedidosTienda fragment = Fragment_ListaPedidosTienda.newInstance(mTiendaId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}