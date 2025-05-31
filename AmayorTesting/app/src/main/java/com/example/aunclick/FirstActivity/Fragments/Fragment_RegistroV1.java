package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.FirstActivity.MainActivity;
import com.example.amayor.Objetos.TipoTienda;
import com.example.amayor.R;

public class Fragment_RegistroV1 extends Fragment {

    private EditText nombreUsuario, editTextPhone, contrasenia, nombreTienda;
    private Spinner tipoUsuario, tipoTienda;
    private TextView textoNombreTienda, textoTipoTienda;
    private Button botonRegistrar;

    public Fragment_RegistroV1() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registro_v1, container, false);

        // Initialize views
        nombreUsuario = v.findViewById(R.id.nombreUsuario);
        editTextPhone = v.findViewById(R.id.editTextPhone);
        contrasenia = v.findViewById(R.id.contrasenia);
        nombreTienda = v.findViewById(R.id.nombreTienda);
        tipoUsuario = v.findViewById(R.id.tipoUsuario);
        tipoTienda = v.findViewById(R.id.tipoTienda);
        textoNombreTienda = v.findViewById(R.id.textoNombreTienda);
        textoTipoTienda = v.findViewById(R.id.textoTipoTienda);
        botonRegistrar = v.findViewById(R.id.botonIniciarSesion);

        // Configure tipoUsuario Spinner
        String[] usuarios = getResources().getStringArray(R.array.usuarios);
        ArrayAdapter<String> usuarioAdapter = new ArrayAdapter<>(getContext(), R.layout.my_selected_item, usuarios);
        usuarioAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        tipoUsuario.setAdapter(usuarioAdapter);

        // Configure tipoTienda Spinner
        String[] tiendas = getResources().getStringArray(R.array.tiendas);
        ArrayAdapter<String> tiendaAdapter = new ArrayAdapter<>(getContext(), R.layout.my_selected_item, tiendas);
        tiendaAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        tipoTienda.setAdapter(tiendaAdapter);

        // Disable and hide tienda fields by default
        setTiendaFieldsEnabled(false);

        // Handle tipoUsuario selection
        tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                setTiendaFieldsEnabled(selected.equals("Tienda"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setTiendaFieldsEnabled(false);
            }
        });

        // Handle registration button click
        botonRegistrar.setOnClickListener(v1 -> {
            String usuario = nombreUsuario.getText().toString().trim();
            String telefono = editTextPhone.getText().toString().trim();
            String password = contrasenia.getText().toString().trim();
            String nombreTiendaText = nombreTienda.getText().toString().trim();
            String tipoUsuarioText = tipoUsuario.getSelectedItem().toString();
            String tipoTiendaText = tipoTienda.getSelectedItem() != null ? tipoTienda.getSelectedItem().toString() : "";

            // Validate inputs
            if (usuario.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, complete usuario, teléfono y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            DataBaseHelper dbHelper = new DataBaseHelper(getContext());

            // Check for duplicate usuario
            boolean usuarioExists = false;
            switch (tipoUsuarioText) {
                case "Cliente":
                    usuarioExists = dbHelper.usuarioExistsCliente(usuario);
                    break;
                case "Tienda":
                    usuarioExists = dbHelper.usuarioExistsTienda(usuario);
                    break;
                case "Repartidor":
                    usuarioExists = dbHelper.usuarioExistsRepartidor(usuario);
                    break;
            }
            if (usuarioExists) {
                Toast.makeText(getContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = -1;
            switch (tipoUsuarioText) {
                case "Cliente":
                    // For Cliente, use usuario as direccion (temporary, as form lacks direccion field)
                    result = dbHelper.insertCliente(usuario, telefono, password, usuario);
                    break;
                case "Tienda":
                    if (nombreTiendaText.isEmpty() || tipoTiendaText.isEmpty()) {
                        Toast.makeText(getContext(), "Nombre de tienda y tipo de tienda son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TipoTienda tipoTiendaEnum;
                    switch (tipoTiendaText) {
                        case "Mercado":
                            tipoTiendaEnum = TipoTienda.GENERAL;
                            break;
                        case "Farmacia":
                            tipoTiendaEnum = TipoTienda.FARMACIA;
                            break;
                        case "Otros":
                            tipoTiendaEnum = TipoTienda.OTROS;
                            break;
                        default:
                            Toast.makeText(getContext(), "Seleccione un tipo de tienda válido", Toast.LENGTH_SHORT).show();
                            return;
                    }
                    result = dbHelper.insertTienda(usuario, telefono, password, nombreTiendaText, tipoTiendaEnum);
                    break;
                case "Repartidor":
                    result = dbHelper.insertRepartidor(usuario, telefono, password);
                    break;
                default:
                    Toast.makeText(getContext(), "Seleccione un tipo de usuario válido", Toast.LENGTH_SHORT).show();
                    return;
            }

            if (result != -1) {
                Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).cargarFragmento(new Fragment_InicioSesionV1());
            } else {
                Toast.makeText(getContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void setTiendaFieldsEnabled(boolean enabled) {
        nombreTienda.setEnabled(enabled);
        nombreTienda.setVisibility(enabled ? View.VISIBLE : View.GONE);
        tipoTienda.setEnabled(enabled);
        tipoTienda.setVisibility(enabled ? View.VISIBLE : View.GONE);
        textoNombreTienda.setVisibility(enabled ? View.VISIBLE : View.GONE);
        textoTipoTienda.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }
}