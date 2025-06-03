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
import com.example.amayor.Objetos.TipoTienda;
import com.example.amayor.R;

public class Fragment_DetallesCuentaTienda extends Fragment {

    private static final String TAG = "DetallesCuentaTienda";
    private String originalNombreTienda, originalTipoTienda, originalTelefono, originalUsuario;
    private EditText etNombreTienda, etTipoTienda, etTelefono;
    private Button btnGuardarCambios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_cuenta_tienda, container, false);

        // Initialize EditText fields
        etNombreTienda = view.findViewById(R.id.etNombreTienda);
        etTipoTienda = view.findViewById(R.id.etTipoTienda);
        etTelefono = view.findViewById(R.id.etTelefono);

        // Make EditText fields editable
        etNombreTienda.setEnabled(true);
        etTipoTienda.setEnabled(true);
        etTelefono.setEnabled(true);

        // Initialize buttons
        ImageView ivBack = view.findViewById(R.id.ivBack);
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        Button btnLlamarSoporte = view.findViewById(R.id.btnLLamarSoporte);
        btnGuardarCambios = view.findViewById(R.id.guardarCambiosRealizados);

        // Initially disable the save button
        btnGuardarCambios.setEnabled(false);

        // Get logged-in tienda ID
        MainActivity activity = (MainActivity) getActivity();
        int tiendaId = activity.getLoggedInTiendaId();
        Log.d(TAG, "onCreateView: tiendaId = " + tiendaId);

        // Fetch store details from database
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        ContentValues details = dbHelper.getTiendaDetails(tiendaId);
        Log.d(TAG, "onCreateView: tiendaDetails = " + (details != null ? details.toString() : "null"));

        // Set EditText fields with database values or defaults
        originalUsuario = details != null && details.containsKey("usuario") && details.getAsString("usuario") != null
                ? details.getAsString("usuario") : "Tienda";
        originalNombreTienda = details != null && details.containsKey("nombre_tienda") && details.getAsString("nombre_tienda") != null
                ? details.getAsString("nombre_tienda") : "Tienda";
        originalTipoTienda = details != null && details.containsKey("tipo_tienda") && details.getAsString("tipo_tienda") != null
                ? details.getAsString("tipo_tienda") : "GENERAL";
        originalTelefono = details != null && details.containsKey("telefono") && details.getAsString("telefono") != null
                ? details.getAsString("telefono") : "No disponible";

        if (details == null || !details.containsKey("telefono")) {
            Log.e(TAG, "onCreateView: telefono missing or null in database");
            Toast.makeText(requireContext(), "Teléfono no disponible en la base de datos", Toast.LENGTH_SHORT).show();
        }

        etNombreTienda.setText(originalNombreTienda);
        etTipoTienda.setText(originalTipoTienda);
        etTelefono.setText(originalTelefono);

        // Add TextWatchers to detect changes
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                boolean isChanged = !etNombreTienda.getText().toString().equals(originalNombreTienda) ||
                        !etTipoTienda.getText().toString().equals(originalTipoTienda) ||
                        !etTelefono.getText().toString().equals(originalTelefono);
                btnGuardarCambios.setEnabled(isChanged);
            }
        };

        etNombreTienda.addTextChangedListener(textWatcher);
        etTipoTienda.addTextChangedListener(textWatcher);
        etTelefono.addTextChangedListener(textWatcher);

        // Save changes button
        btnGuardarCambios.setOnClickListener(v -> {
            String newNombreTienda = etNombreTienda.getText().toString().trim();
            String newTipoTienda = etTipoTienda.getText().toString().trim().toUpperCase();
            String newTelefono = etTelefono.getText().toString().trim();
            String newUsuario = originalUsuario; // Assuming username is not editable in UI

            if (newNombreTienda.isEmpty() || newTipoTienda.isEmpty() || newTelefono.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate tipo_tienda
            try {
                TipoTienda.valueOf(newTipoTienda);
            } catch (IllegalArgumentException e) {
                Toast.makeText(requireContext(), "Tipo de tienda inválido. Use: GENERAL, FARMACIA, OTROS", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update database
            boolean success = dbHelper.updateTienda(tiendaId, newUsuario, newTelefono, newNombreTienda, newTipoTienda);
            if (success) {
                Toast.makeText(requireContext(), "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
                originalNombreTienda = newNombreTienda;
                originalTipoTienda = newTipoTienda;
                originalTelefono = newTelefono;
                btnGuardarCambios.setEnabled(false);
            } else {
                Toast.makeText(requireContext(), "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
            }
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

        // Navigate back
        ivBack.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            fm.popBackStack();
            Toast.makeText(requireContext(), "Navegando a Inicio Tienda", Toast.LENGTH_SHORT).show();
        });

        // Support button to call phone number
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