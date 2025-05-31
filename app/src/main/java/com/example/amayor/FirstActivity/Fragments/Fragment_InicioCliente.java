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
import com.example.amayor.FirstActivity.MainActivity;
import com.example.amayor.R;

public class Fragment_InicioCliente extends Fragment {

    public static Fragment_InicioCliente newInstance() {
        return new Fragment_InicioCliente();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

        // Initialize TextView for welcome message
        TextView textoBienvenidoCliente = view.findViewById(R.id.textoBienvenidoCliente);

        // Get username from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = prefs.getString("usuario", null);

        // Fallback to database if SharedPreferences lacks usuario
        if (nombreUsuario == null) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                int clienteId = activity.getLoggedInClienteId();
                if (clienteId != -1) {
                    DataBaseHelper dbHelper = new DataBaseHelper(requireContext());
                    ContentValues details = dbHelper.getClienteDetails(clienteId);
                    nombreUsuario = details.getAsString("usuario");
                }
            }
            if (nombreUsuario == null) {
                nombreUsuario = "Usuario";
                Toast.makeText(requireContext(), "Nombre de usuario no encontrado", Toast.LENGTH_SHORT).show();
            }
        }

        // Set welcome message with username
        textoBienvenidoCliente.setText("¡Bienvenido " + nombreUsuario + "!");

        // Initialize buttons
        ImageView botonHacerCompra = view.findViewById(R.id.botonIrHacerCompra);
        ImageView botonOtros = view.findViewById(R.id.botonIrOtros);
        ImageView ivAccountDetails = view.findViewById(R.id.ivAccountDetails);

        // Navigate to ListaTienda
        botonHacerCompra.setOnClickListener(v -> {
            Fragment_ListaTienda fragment = new Fragment_ListaTienda();
            navigateToFragment(fragment, "Hacer compra");
        });

        // Navigate to ListaTiendaOtros
        botonOtros.setOnClickListener(v -> {
            Fragment_ListaTiendaOtros fragment = new Fragment_ListaTiendaOtros();
            Bundle args = new Bundle();
            args.putString("tipoTienda", "OTROS");
            fragment.setArguments(args);
            navigateToFragment(fragment, "Otros");
        });

        // Navigate to DetallesCuenta
        ivAccountDetails.setOnClickListener(v -> {
            Fragment_DetallesCuentaCliente fragment = new Fragment_DetallesCuentaCliente();
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