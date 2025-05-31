package com.example.amayor.FirstActivity.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.FirstActivity.MainActivity;
import com.example.amayor.R;

public class Fragment_InicioEmpleado extends Fragment {

    private static final String TAG = "InicioEmpleado";

    public Fragment_InicioEmpleado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_empleado, container, false);

        // Initialize TextViews
        TextView textoBienvenidoRepartidor = view.findViewById(R.id.textoBienvenidoRepartidor);
        TextView textoHacer = view.findViewById(R.id.textoHacer);

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

        // Fetch username from database or SharedPreferences
        String nombreUsuario = "Repartidor";
        DataBaseHelper dbHelper = new DataBaseHelper(requireContext());
        ContentValues repartidorDetails = dbHelper.getRepartidorDetails(repartidorId);
        Log.d(TAG, "onCreateView: repartidorDetails = " + (repartidorDetails != null ? repartidorDetails.toString() : "null"));
        if (repartidorDetails.getAsString("nombre_usuario") != null) {
            nombreUsuario = repartidorDetails.getAsString("nombre_usuario");
        } else if (repartidorId == -1) {
            SharedPreferences prefs = requireContext().getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE);
            nombreUsuario = prefs.getString("usuario", nombreUsuario);
            Log.d(TAG, "onCreateView: Fallback to SharedPreferences, usuario = " + nombreUsuario);
        }
        textoBienvenidoRepartidor.setText("¡Bienvenido " + nombreUsuario + "!");

        // Initialize buttons
        ImageView botonIrPedidos = view.findViewById(R.id.botonIrPedidos);
        ImageView ivAccountDetails = view.findViewById(R.id.ivAccountDetails);

        // Navigate to ListaPedidosRepartidor
        botonIrPedidos.setOnClickListener(v -> {
            Fragment_ListaPedidosRepartidor fragment = new Fragment_ListaPedidosRepartidor(); // Adjust if newInstance required
            navigateToFragment(fragment, "Lista de pedidos");
        });

        // Navigate to DetallesCuentaRepartidor
        ivAccountDetails.setOnClickListener(v -> {
            Fragment_DetallesCuentaRepartidor fragment = new Fragment_DetallesCuentaRepartidor();
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