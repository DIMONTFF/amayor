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

public class Fragment_DetallesCuentaCliente extends Fragment {

    private static final String TAG = "DetallesCuentaCliente";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_cuenta_cliente, container, false);

        // Initialize EditText fields
        EditText etUsuario = view.findViewById(R.id.etUsuario);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        EditText etDireccion = view.findViewById(R.id.etDireccion);

        // Initialize back and other buttons
        ImageView ivBack = view.findViewById(R.id.ivBack);
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        Button btnLlamarSoporte = view.findViewById(R.id.btnLLamarSoporte);

        // Get logged-in client ID
        MainActivity activity = (MainActivity) getActivity();
        int idCliente = activity.getLoggedInClienteId();
        Log.d(TAG, "onCreateView: idCliente = " + idCliente);

        // Fetch user details from database
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        ContentValues details = dbHelper.getClienteDetails(idCliente);
        Log.d(TAG, "onCreateView: clienteDetails = " + (details != null ? details.toString() : "null"));

        // Set EditText fields with database values or defaults
        String usuario = details != null && details.containsKey("usuario") && details.getAsString("usuario") != null
                ? details.getAsString("usuario") : "Usuario";
        String telefono = details != null && details.containsKey("telefono") && details.getAsString("telefono") != null
                ? details.getAsString("telefono") : "No disponible";
        String direccion = details != null && details.containsKey("direccion") && details.getAsString("direccion") != null
                ? details.getAsString("direccion") : "No disponible";

        if (details == null || !details.containsKey("telefono")) {
            Log.e(TAG, "onCreateView: telefono missing or null in database");
            Toast.makeText(requireContext(), "Teléfono no disponible en la base de datos", Toast.LENGTH_SHORT).show();
        }

        etUsuario.setText(usuario);
        etTelefono.setText(telefono);
        etDireccion.setText(direccion);

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
            Toast.makeText(requireContext(), "Navegando a Inicio Cliente", Toast.LENGTH_SHORT).show();
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