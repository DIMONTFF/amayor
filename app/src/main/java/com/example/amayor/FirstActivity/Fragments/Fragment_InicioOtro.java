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

public class Fragment_InicioOtro extends Fragment {

    private static final String ARG_TIENDA_ID = "tienda_id";

    private int mTiendaId;

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
            mTiendaId = getArguments().getInt(ARG_TIENDA_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_otro, container, false);

        // Initialize TextViews
        TextView textoBienvenidoOtro = view.findViewById(R.id.textoBienvenidoOtro);
        TextView textoGestionarOtro = view.findViewById(R.id.textoGestionarOtro);

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
            Toast.makeText(requireContext(), "ID de tienda no válido", Toast.LENGTH_SHORT).show();
        }
        textoBienvenidoOtro.setText("¡Bienvenido " + nombreTienda + "!");

        // Initialize buttons
        ImageView botonIrCitas = view.findViewById(R.id.botonIrCitas);
        ImageView ivAccountDetails = view.findViewById(R.id.ivAccountDetails);

        // Navigate to ListaCitas
        botonIrCitas.setOnClickListener(v -> {
            if (mTiendaId != -1) {
                Fragment_ListaCitas fragment = Fragment_ListaCitas.newInstance(mTiendaId);
                navigateToFragment(fragment, "Lista de citas");
            } else {
                Toast.makeText(requireContext(), "ID de tienda no válido", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to DetallesCuentaTienda
        ivAccountDetails.setOnClickListener(v -> {
            Fragment_DetallesCuentaTienda fragment = new Fragment_DetallesCuentaTienda();
            navigateToFragment(fragment, "Detalles de la cuenta");
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