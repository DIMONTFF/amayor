package com.example.amayor.FirstActivity.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.FirstActivity.MainActivity;
import com.example.amayor.Objetos.Tienda;
import com.example.amayor.Objetos.TipoTienda;
import com.example.amayor.R;

public class Fragment_InicioSesionV1 extends Fragment {

    private EditText nombreUsuarioLogin, contraseniaLogin;
    private Spinner tipoUsuarioLogin;
    private Button botonIniciarSesion;
    private CheckBox mantenerSesionIniciada;
    private TextView textoNuevoAqui;
    private SharedPreferences prefs;

    public Fragment_InicioSesionV1() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicio_sesion_v1, container, false);

        // Initialize views
        nombreUsuarioLogin = v.findViewById(R.id.nombreUsuarioLogin);
        contraseniaLogin = v.findViewById(R.id.contraseniaLogin);
        tipoUsuarioLogin = v.findViewById(R.id.tipoUsuarioLogin);
        botonIniciarSesion = v.findViewById(R.id.botonIniciarSesion);
        mantenerSesionIniciada = v.findViewById(R.id.mantenerSesionInciada);
        textoNuevoAqui = v.findViewById(R.id.textoNuevoAqui);

        // Initialize SharedPreferences
        prefs = getContext().getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE);

        // Configure tipoUsuarioLogin Spinner
        String[] usuarios = getResources().getStringArray(R.array.usuarios);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.my_selected_item, usuarios);
        adapter.setDropDownViewResource(R.layout.my_dropdown_item);
        tipoUsuarioLogin.setAdapter(adapter);

        // Load saved session data
        String savedUsuario = prefs.getString("usuario", "");
        boolean savedMantenerSesion = prefs.getBoolean("mantenerSesion", false);
        if (!savedUsuario.isEmpty()) {
            nombreUsuarioLogin.setText(savedUsuario);
        }
        mantenerSesionIniciada.setChecked(savedMantenerSesion);

        // Navigate to Fragment_RegistroV1 on textoNuevoAqui click
        textoNuevoAqui.setOnClickListener(v1 -> {
            ((MainActivity) getActivity()).cargarFragmento(new Fragment_RegistroV1());
        });

        // Handle login button click
        botonIniciarSesion.setOnClickListener(v1 -> {
            String usuario = nombreUsuarioLogin.getText().toString().trim();
            String contrasenia = contraseniaLogin.getText().toString().trim();
            String tipoUsuario = tipoUsuarioLogin.getSelectedItem().toString();

            if (usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, complete usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            DataBaseHelper dbHelper = new DataBaseHelper(getContext());
            MainActivity mainActivity = (MainActivity) getActivity();

            // Check if usuario exists
            boolean usuarioExists = false;
            switch (tipoUsuario) {
                case "Cliente":
                    usuarioExists = dbHelper.usuarioExistsCliente(usuario);
                    break;
                case "Tienda":
                    usuarioExists = dbHelper.usuarioExistsTienda(usuario);
                    break;
                case "Repartidor":
                    usuarioExists = dbHelper.usuarioExistsRepartidor(usuario);
                    break;
                default:
                    Toast.makeText(getContext(), "Seleccione un tipo de usuario válido", Toast.LENGTH_SHORT).show();
                    return;
            }

            if (!usuarioExists) {
                Toast.makeText(getContext(), "Usuario no encontrado. Por favor, regístrate.", Toast.LENGTH_SHORT).show();
                mainActivity.cargarFragmento(new Fragment_RegistroV1());
                return;
            }

            // Validate credentials
            SharedPreferences.Editor editor = prefs.edit();
            if (mantenerSesionIniciada.isChecked()) {
                editor.putString("usuario", usuario);
                editor.putBoolean("mantenerSesion", true);
            } else {
                editor.clear();
            }
            editor.apply();

            switch (tipoUsuario) {
                case "Cliente":
                    int clienteId = dbHelper.validateCliente(usuario, contrasenia);
                    if (clienteId != -1) {
                        mainActivity.setLoggedInClienteId(clienteId);
                        mainActivity.cargarFragmento(new Fragment_InicioCliente());
                        Toast.makeText(getContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Tienda":
                    Tienda tienda = dbHelper.validateTienda(usuario, contrasenia);
                    if (tienda != null) {
                        mainActivity.setLoggedInTiendaId(tienda.getIdPersona());
                        // Load fragment based on TipoTienda
                        if (tienda.getTipoTienda() == TipoTienda.GENERAL || tienda.getTipoTienda() == TipoTienda.FARMACIA) {
                            mainActivity.cargarFragmento(Fragment_InicioTienda.newInstance(tienda.getIdPersona()));
                        } else if (tienda.getTipoTienda() == TipoTienda.OTROS) {
                            mainActivity.cargarFragmento(Fragment_InicioOtro.newInstance(tienda.getIdPersona()));
                        }
                        Toast.makeText(getContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Repartidor":
                    int repartidorId = dbHelper.validateRepartidor(usuario, contrasenia);
                    if (repartidorId != -1) {
                        mainActivity.setLoggedInRepartidorId(repartidorId);
                        mainActivity.cargarFragmento(new Fragment_InicioEmpleado());
                        Toast.makeText(getContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        });

        return v;
    }
}