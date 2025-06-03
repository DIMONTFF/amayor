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

public class Fragment_DetallesCuentaCliente extends Fragment {

    private static final String TAG = "DetallesCuentaCliente";
    private String originalUsuario, originalTelefono, originalDireccion;
    private EditText etUsuario, etTelefono, etDireccion;
    private Button btnGuardarCambios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_cuenta_cliente, container, false);

        // Initialize EditText fields
        etUsuario = view.findViewById(R.id.etUsuario);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);

        // Make EditText fields editable
        etUsuario.setEnabled(true);
        etTelefono.setEnabled(true);
        etDireccion.setEnabled(true);

        // Initialize buttons
        ImageView ivBack = view.findViewById(R.id.ivBack);
        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        Button btnLlamarSoporte = view.findViewById(R.id.btnLLamarSoporte);
        btnGuardarCambios = view.findViewById(R.id.guardarCambiosRealizados);

        // Initially disable the save button
        btnGuardarCambios.setEnabled(false);

        // Get logged-in client ID
        MainActivity activity = (MainActivity) getActivity();
        int idCliente = activity.getLoggedInClienteId();
        Log.d(TAG, "onCreateView: idCliente = " + idCliente);

        // Fetch user details from database
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        ContentValues details = dbHelper.getClienteDetails(idCliente);
        Log.d(TAG, "onCreateView: clienteDetails = " + (details != null ? details.toString() : "null"));

        // Set EditText fields with database values or defaults
        originalUsuario = details != null && details.containsKey("usuario") && details.getAsString("usuario") != null
                ? details.getAsString("usuario") : "Usuario";
        originalTelefono = details != null && details.containsKey("telefono") && details.getAsString("telefono") != null
                ? details.getAsString("telefono") : "No disponible";
        originalDireccion = details != null && details.containsKey("direccion") && details.getAsString("direccion") != null
                ? details.getAsString("direccion") : "No disponible";

        if (details == null || !details.containsKey("telefono")) {
            Log.e(TAG, "onCreateView: telefono missing or null in database");
            Toast.makeText(requireContext(), "Teléfono no disponible en la base de datos", Toast.LENGTH_SHORT).show();
        }

        etUsuario.setText(originalUsuario);
        etTelefono.setText(originalTelefono);
        etDireccion.setText(originalDireccion);

        // Add TextWatchers to detect changes
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                boolean isChanged = !etUsuario.getText().toString().equals(originalUsuario) ||
                        !etTelefono.getText().toString().equals(originalTelefono) ||
                        !etDireccion.getText().toString().equals(originalDireccion);
                btnGuardarCambios.setEnabled(isChanged);
            }
        };

        etUsuario.addTextChangedListener(textWatcher);
        etTelefono.addTextChangedListener(textWatcher);
        etDireccion.addTextChangedListener(textWatcher);

        // Save changes button
        btnGuardarCambios.setOnClickListener(v -> {
            String newUsuario = etUsuario.getText().toString().trim();
            String newTelefono = etTelefono.getText().toString().trim();
            String newDireccion = etDireccion.getText().toString().trim();

            if (newUsuario.isEmpty() || newTelefono.isEmpty() || newDireccion.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the new username is already taken by another client
            if (!newUsuario.equals(originalUsuario) && dbHelper.usuarioExistsCliente(newUsuario)) {
                Toast.makeText(requireContext(), "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.updateCliente(idCliente, newUsuario, newTelefono, newDireccion);
            if (success) {
                Toast.makeText(requireContext(), "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
                originalUsuario = newUsuario;
                originalTelefono = newTelefono;
                originalDireccion = newDireccion;
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
            Toast.makeText(requireContext(), "Navegando a Inicio Cliente", Toast.LENGTH_SHORT).show();
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