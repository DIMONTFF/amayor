package com.example.amayor.FirstActivity.Fragments;

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
import com.example.amayor.R;

public class Fragment_InicioCliente extends Fragment {

    public static Fragment_InicioCliente newInstance(String param1, String param2) {
        Fragment_InicioCliente fragment = new Fragment_InicioCliente();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

        // Initialize TextView for welcome message
        TextView textoBienvenidoCliente = view.findViewById(R.id.textoBienvenidoCliente);

        // Get username from SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = prefs.getString("usuario", "Usuario"); // Default to "Usuario" if not found

        // Set welcome message with username
        textoBienvenidoCliente.setText("¡Bienvenido " + nombreUsuario + "!");

        // Initialize buttons
        ImageView botonHacerCompra = view.findViewById(R.id.botonIrHacerCompra);
        ImageView botonOtros = view.findViewById(R.id.botonIrOtros);

        botonHacerCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_ListaTienda fragment_listaTienda = new Fragment_ListaTienda();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainerView, fragment_listaTienda);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        botonOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_ListaTiendaOtros fragment_listaTiendaOtros = new Fragment_ListaTiendaOtros();
                Bundle args = new Bundle();
                args.putString("tipoTienda", "OTROS");
                fragment_listaTiendaOtros.setArguments(args);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainerView, fragment_listaTiendaOtros);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }
}