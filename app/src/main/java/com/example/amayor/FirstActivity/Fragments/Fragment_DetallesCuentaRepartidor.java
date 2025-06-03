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
import android.text.Editable;
import android.text.TextWatcher;
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
    private String originalNombreUsuario, originalTelefono;
    private EditText etNombreUsuario, etTelefono;
    private Button btnGuardarCambios;

    public Fragment_DetallesCuentaRepartidor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_cuenta_repartidor, container, false);

        // Initialize UI elements
        etNombreUsuario = view.findViewById(R.id.etNombreUsuario);
        etTelefono = view.findViewById(R.id.etTelefono);
        ImageView ivBack = view.findViewById(R.id.ivBack);
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        Button btnLlamarSoporte = view.findViewById(R.id.btnLLamarSoporte);
        btnGuardarCambios = view.findViewById(R.id.guardarCambiosRealizados);

        // Make EditText fields editable
        etNombreUsuario.setEnabled(true);
        etTelefono.setEnabled(true);

        // Initially disable the save button
        btnGuardarCambios.setEnabled(false);

        // Get logged-in repartidor ID
        MainActivity activity = (MainActivity) getActivity();
        int repartidorId;
        if (activity != null) {
            repartidorId = activity.getLoggedInRepartidorId();
            Log.d(TAG, "onCreateView: repartidorId = " + repartidorId);
        } else {
            repartidorId = -1;
            Log.e(TAG, "onCreateView: Activity is null");
            Toast.makeText(requireContext(), "Error al obtener actividad", Toast.LENGTH_SHORT).show();
        }

        // Fetch repartidor details from database
        DataBaseHelper dbHelper = new DataBaseHelper(requireContext());
        ContentValues details = dbHelper.getRepartidorDetails(repartidorId);
        Log.d(TAG, "onCreateView: repartidorDetails = " + (details != null ? details.toString() : "null"));

        // Set EditText fields with database values or defaults
        originalNombreUsuario = details != null && details.containsKey("nombre_usuario") && details.getAsString("nombre_usuario") != null
                ? details.getAsString("nombre_usuario") : "Repartidor";
        originalTelefono = details != null && details.containsKey("telefono") && details.getAsString("telefono") != null
                ? details.getAsString("telefono") : "No disponible";

        if (details == null || !details.containsKey("telefono")) {
            Log.e(TAG, "onCreateView: telefono missing or null in database");
            Toast.makeText(requireContext(), "Teléfono no disponible en la base de datos", Toast.LENGTH_SHORT).show();
        }

        etNombreUsuario.setText(originalNombreUsuario);
        etTelefono.setText(originalTelefono);

        // Add TextWatchers to detect changes
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                boolean isChanged = !etNombreUsuario.getText().toString().equals(originalNombreUsuario) ||
                        !etTelefono.getText().toString().equals(originalTelefono);
                btnGuardarCambios.setEnabled(isChanged);
            }
        };

        etNombreUsuario.addTextChangedListener(textWatcher);
        etTelefono.addTextChangedListener(textWatcher);

        // Save changes button
        btnGuardarCambios.setOnClickListener(v -> {
            String newNombreUsuario = etNombreUsuario.getText().toString().trim();
            String newTelefono = etTelefono.getText().toString().trim();

            if (newNombreUsuario.isEmpty() || newTelefono.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the new username is already taken by another repartidor
            if (!newNombreUsuario.equals(originalNombreUsuario) && dbHelper.usuarioExistsRepartidor(newNombreUsuario)) {
                Toast.makeText(requireContext(), "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.updateRepartidor(repartidorId, newNombreUsuario, newTelefono);
            if (success) {
                Toast.makeText(requireContext(), "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
                originalNombreUsuario = newNombreUsuario;
                originalTelefono = newTelefono;
                btnGuardarCambios.setEnabled(false);
            } else {
                Toast.makeText(requireContext(), "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
            }
        });

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
            dialIntent.setData(Uri.parse("tel:653828228"));
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