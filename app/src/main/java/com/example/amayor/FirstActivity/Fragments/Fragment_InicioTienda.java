package com.example.amayor.FirstActivity.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.R;

public class Fragment_InicioTienda extends Fragment {

    private static String ARG_TIENDA_ID = "tienda_id";

    private int mTiendaId;

    public static Fragment_InicioTienda newInstance(int tiendaId) {
        Fragment_InicioTienda fragment = new Fragment_InicioTienda();
        Bundle args = new Bundle();
        args.putInt(ARG_TIENDA_ID, tiendaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_tienda, container, false);

        // Get tiendaId from arguments
        if (getArguments() != null) {
            mTiendaId = getArguments().getInt(ARG_TIENDA_ID, -1);
        }

        // Initialize TextViews
        TextView textoBienvenidoTienda = view.findViewById(R.id.textoBienvenidoTienda);
        TextView textoGestionarTienda = view.findViewById(R.id.textoGestionarTienda);

        // Fetch store name from database or SharedPreferences
        String nombreTienda = "Tienda";
        if (mTiendaId != -1) {
            DataBaseHelper dbHelper = new DataBaseHelper(requireContext());
            ContentValues tiendaDetails = dbHelper.getTiendaDetails(mTiendaId);
            nombreTienda = tiendaDetails.getAsString("nombre_tienda") != null
                    ? tiendaDetails.getAsString("nombre_tienda") : nombreTienda;
        } else {
            SharedPreferences prefs = requireContext().getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE);
            nombreTienda = prefs.getString("nombre_tienda", nombreTienda);
        }
        textoBienvenidoTienda.setText("¡Bienvenido " + nombreTienda + "!");

        // Initialize buttons
        ImageView botonIrProductosStock = view.findViewById(R.id.botonIrProductosStock);
        ImageView botonIrListaPedidos = view.findViewById(R.id.botonIrListaPedidos);
        ImageView ivAccountDetails = view.findViewById(R.id.ivAccountDetails);

        // Navigate to ProductosTienda
        botonIrProductosStock.setOnClickListener(v -> {
            if (mTiendaId != -1) {
                navigateToFragment(Fragment_ProductosTienda.newInstance(mTiendaId), "Gestionar productos");
            } else {
                Toast.makeText(requireContext(), "ID de tienda no válido", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to ListaPedidosTienda
        botonIrListaPedidos.setOnClickListener(v -> {
            if (mTiendaId != -1) {
                navigateToFragment(Fragment_ListaPedidosTienda.newInstance(mTiendaId), "Ver pedidos");
            } else {
                Toast.makeText(requireContext(), "ID de tienda no válido", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to DetallesCuentaTienda
        ivAccountDetails.setOnClickListener(v -> {
            navigateToFragment(new Fragment_DetallesCuentaTienda(), "Detalles de la cuenta");
        });

        return view;
    }

    private void navigateToFragment(Fragment fragment, String actionName) {
        FragmentManager fm = getParentFragmentManager();
        if (fm != null && isAdded()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerView, fragment);
            ft.addToBackStack(null);
            ft.commit();
            Toast.makeText(requireContext(), "Navegando a " + actionName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Error al navegar", Toast.LENGTH_SHORT).show();
        }
    }
}