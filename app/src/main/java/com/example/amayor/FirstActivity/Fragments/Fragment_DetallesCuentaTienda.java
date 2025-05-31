package com.example.amayor.FirstActivity.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.FirstActivity.MainActivity;
import com.example.amayor.R;

public class Fragment_DetallesCuentaTienda extends Fragment {

    private static final String TAG = "DetallesCuentaTienda";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_cuenta_tienda, container, false);

        // Initialize EditText fields
        EditText etNombreTienda = view.findViewById(R.id.etNombreTienda);
        EditText etTipoTienda = view.findViewById(R.id.etTipoTienda);
        EditText etTelefono = view.findViewById(R.id.etTelefono);

        // Initialize back and other buttons
        ImageView ivBack = view.findViewById(R.id.ivBack);
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        Button btnLlamarSoporte = view.findViewById(R.id.btnLLamarSoporte);

        // Get logged-in tienda ID
        MainActivity activity = (MainActivity) getActivity();
        int tiendaId = activity.getLoggedInTiendaId();
        Log.d(TAG, "onCreateView: tiendaId = " + tiendaId);

        // Fetch store details from database
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        ContentValues details = dbHelper.getTiendaDetails(tiendaId);
        Log.d(TAG, "onCreateView: tiendaDetails = " + (details != null ? details.toString() : "null"));

        // Set EditText fields with database values or defaults
        String nombreTienda = details != null && details.containsKey("nombre_tienda") && details.getAsString("nombre_tienda") != null
                ? details.getAsString("nombre_tienda") : "Tienda";
        String tipoTienda = details != null && details.containsKey("tipo_tienda") && details.getAsString("tipo_tienda") != null
                ? details.getAsString("tipo_tienda") : "GENERAL";
        String telefono = details != null && details.containsKey("telefono") && details.getAsString("telefono") != null
                ? details.getAsString("telefono") : "No disponible";

        if (details == null || !details.containsKey("telefono")) {
            Log.e(TAG, "onCreateView: telefono missing or null in database");
            Toast.makeText(requireContext(), "Teléfono no disponible en la base de datos", Toast.LENGTH_SHORT).show();
        }

        etNombreTienda.setText(nombreTienda);
        etTipoTienda.setText(tipoTienda);
        etTelefono.setText(telefono);

        // Logout button navigation
        btnCerrarSesion.setOnClickListener(v -> {
            // Clear SharedPreferences
            SharedPreferences prefs = requireContext().getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Reset repartidor ID
            if (activity != null) {
                activity.setLoggedInRepartidorId(-1);
            }

            // Navigate to login fragment
            Fragment_InicioSesionV1 fragment = new Fragment_InicioSesionV1();
            FragmentManager fm = getParentFragmentManager();
            if (fm != null && isAdded()) {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainerView, fragment);
                ft.commit();
                Toast.makeText(requireContext(), "Navegando a Login", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error al navegar", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate back
        ivBack.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.popBackStack();
            Toast.makeText(requireContext(), "Navegando a Inicio Tienda", Toast.LENGTH_SHORT).show();
        });

        // Support button to dial phone number
        btnLlamarSoporte.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:653828228")); // Replace with actual support number
            try {
                startActivity(dialIntent);
                Toast.makeText(requireContext(), "Abriendo marcador", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "Error al abrir marcador: " + e.getMessage());
                Toast.makeText(requireContext(), "Error al abrir marcador", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}