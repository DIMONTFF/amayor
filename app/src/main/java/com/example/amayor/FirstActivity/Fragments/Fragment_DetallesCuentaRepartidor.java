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

public class Fragment_DetallesCuentaRepartidor extends Fragment {

    private static final String TAG = "DetallesCuentaRepartidor";

    public Fragment_DetallesCuentaRepartidor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_cuenta_repartidor, container, false);

        // Initialize UI elements
        EditText etNombreUsuario = view.findViewById(R.id.etNombreUsuario);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        ImageView ivBack = view.findViewById(R.id.ivBack);
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        Button btnLlamarSoporte = view.findViewById(R.id.btnLLamarSoporte);

        // Get logged-in repartidor ID
        MainActivity activity = (MainActivity) getActivity();
        int repartidorId = -1;
        if (activity != null) {
            repartidorId = activity.getLoggedInRepartidorId();
            Log.d(TAG, "onCreateView: repartidorId = " + repartidorId);
        } else {
            Log.e(TAG, "onCreateView: Activity is null");
            Toast.makeText(requireContext(), "Error al obtener actividad", Toast.LENGTH_SHORT).show();
        }

        // Fetch repartidor details from database
        DataBaseHelper dbHelper = new DataBaseHelper(requireContext());
        ContentValues details = dbHelper.getRepartidorDetails(repartidorId);
        Log.d(TAG, "onCreateView: repartidorDetails = " + (details != null ? details.toString() : "null"));

        // Set EditText fields with database values or defaults
        String nombreUsuario = details != null && details.containsKey("nombre_usuario") && details.getAsString("nombre_usuario") != null
                ? details.getAsString("nombre_usuario") : "Repartidor";
        String telefono = details != null && details.containsKey("telefono") && details.getAsString("telefono") != null
                ? details.getAsString("telefono") : "No disponible";

        if (details == null || !details.containsKey("telefono")) {
            Log.e(TAG, "onCreateView: telefono missing or null in database");
            Toast.makeText(requireContext(), "Teléfono no disponible en la base de datos", Toast.LENGTH_SHORT).show();
        }

        etNombreUsuario.setText(nombreUsuario);
        etTelefono.setText(telefono);

        // Back button navigation
        ivBack.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.popBackStack();
            Toast.makeText(requireContext(), "Navegando a Inicio Repartidor", Toast.LENGTH_SHORT).show();
        });

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