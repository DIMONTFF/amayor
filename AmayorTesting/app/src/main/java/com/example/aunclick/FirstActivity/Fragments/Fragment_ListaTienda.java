package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amayor.FirstActivity.Adapters.TiendaAdapter;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Tienda;
import com.example.amayor.Objetos.TipoTienda;
import com.example.amayor.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_ListaTienda extends Fragment {

    private RecyclerView recyclerView;
    private TiendaAdapter adapter;
    private List<Tienda> tiendas;
    private DataBaseHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DataBaseHelper(getContext());
        tiendas = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_tienda, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTiendas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Clear the list to avoid duplicates
        tiendas.clear();

        // Fetch and filter Tiendas from database (only FARMACIA and GENERAL)
        List<Tienda> todasTiendas = dbHelper.getAllTiendas();
        for (Tienda tienda : todasTiendas) {
            if (tienda.getTipoTienda() == TipoTienda.FARMACIA || tienda.getTipoTienda() == TipoTienda.GENERAL) {
                tiendas.add(tienda);
            }
        }

        // Set adapter
        adapter = new TiendaAdapter(tiendas, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list to ensure no duplicates
        tiendas.clear();
        List<Tienda> todasTiendas = dbHelper.getAllTiendas();
        for (Tienda tienda : todasTiendas) {
            if (tienda.getTipoTienda() == TipoTienda.FARMACIA || tienda.getTipoTienda() == TipoTienda.GENERAL) {
                tiendas.add(tienda);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}