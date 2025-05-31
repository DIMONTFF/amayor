package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amayor.FirstActivity.Adapters.TiendaOtrosAdapter;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Tienda;
import com.example.amayor.Objetos.TipoTienda;
import com.example.amayor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ListaTiendaOtros extends Fragment {

    private RecyclerView recyclerViewTiendas;
    private TiendaOtrosAdapter tiendaOtrosAdapter;
    private List<Tienda> listaTiendasOtros;
    private DataBaseHelper dbHelper;

    public Fragment_ListaTiendaOtros() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DataBaseHelper(getContext());
        listaTiendasOtros = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_tienda_otros, container, false);

        // Initialize RecyclerView
        recyclerViewTiendas = view.findViewById(R.id.recyclerViewTiendasOtros);
        recyclerViewTiendas.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Back Button
        ImageView ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
                Toast.makeText(requireContext(), "Volviendo a la pantalla anterior", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "No hay fragmentos anteriores", Toast.LENGTH_SHORT).show();
            }
        });

        // Clear the list to avoid duplicates
        listaTiendasOtros.clear();

        // Obtener todas las tiendas y filtrar por tipo OTROS
        List<Tienda> todasTiendas = dbHelper.getAllTiendas();
        for (Tienda tienda : todasTiendas) {
            if (tienda.getTipoTienda() == TipoTienda.OTROS) {
                listaTiendasOtros.add(tienda);
            }
        }

        // Configurar el adaptador
        tiendaOtrosAdapter = new TiendaOtrosAdapter(listaTiendasOtros, getActivity());
        recyclerViewTiendas.setAdapter(tiendaOtrosAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list to ensure no duplicates
        listaTiendasOtros.clear();
        List<Tienda> todasTiendas = dbHelper.getAllTiendas();
        for (Tienda tienda : todasTiendas) {
            if (tienda.getTipoTienda() == TipoTienda.OTROS) {
                listaTiendasOtros.add(tienda);
            }
        }
        if (tiendaOtrosAdapter != null) {
            tiendaOtrosAdapter.notifyDataSetChanged();
        }
    }
}