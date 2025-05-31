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

import com.example.amayor.FirstActivity.Adapters.CitasAdapter;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Pedido;
import com.example.amayor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ListaCitas extends Fragment {

    private static final String ARG_TIENDA_ID = "tienda_id";

    private RecyclerView recyclerViewCitas;
    private CitasAdapter citasAdapter;
    private List<Pedido> listaCitas;
    private DataBaseHelper dbHelper;
    private int mTiendaId;

    public Fragment_ListaCitas() {
        // Required empty public constructor
    }

    public static Fragment_ListaCitas newInstance(int tiendaId) {
        Fragment_ListaCitas fragment = new Fragment_ListaCitas();
        Bundle args = new Bundle();
        args.putInt(ARG_TIENDA_ID, tiendaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTiendaId = getArguments().getInt(ARG_TIENDA_ID);
        }
        dbHelper = new DataBaseHelper(getContext());
        listaCitas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_citas, container, false);

        // Initialize RecyclerView
        recyclerViewCitas = view.findViewById(R.id.recyclerCitas);
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(getContext()));

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
        listaCitas.clear();

        // Obtener citas para la tienda
        if (mTiendaId != -1) {
            listaCitas.addAll(dbHelper.getCitasByTienda(mTiendaId));
        }

        // Configurar el adaptador
        citasAdapter = new CitasAdapter(listaCitas, getActivity());
        recyclerViewCitas.setAdapter(citasAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list to avoid duplicates
        listaCitas.clear();
        if (mTiendaId != -1) {
            listaCitas.addAll(dbHelper.getCitasByTienda(mTiendaId));
        }
        if (citasAdapter != null) {
            citasAdapter.notifyDataSetChanged();
        }
    }
}