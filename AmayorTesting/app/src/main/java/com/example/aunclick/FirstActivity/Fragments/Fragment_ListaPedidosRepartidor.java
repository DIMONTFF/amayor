package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.FirstActivity.Adapters.PedidosRepartidorAdapter;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.R;
import java.util.ArrayList;
import java.util.List;

public class Fragment_ListaPedidosRepartidor extends Fragment {

    private static final String ARG_REPARTIDOR_ID = "repartidor_id";

    private RecyclerView recyclerViewPedidos;
    private PedidosRepartidorAdapter pedidosAdapter;
    private List<DataBaseHelper.PedidoConTienda> listaPedidos;
    private DataBaseHelper dbHelper;
    private int mRepartidorId;

    public Fragment_ListaPedidosRepartidor() {
        // Required empty public constructor
    }

    public static Fragment_ListaPedidosRepartidor newInstance(int repartidorId) {
        Fragment_ListaPedidosRepartidor fragment = new Fragment_ListaPedidosRepartidor();
        Bundle args = new Bundle();
        args.putInt(ARG_REPARTIDOR_ID, repartidorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRepartidorId = getArguments().getInt(ARG_REPARTIDOR_ID);
        }
        dbHelper = new DataBaseHelper(getContext());
        listaPedidos = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_pedidos_repartidor, container, false);

        recyclerViewPedidos = view.findViewById(R.id.recyclerViewPedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getContext()));

        listaPedidos.clear();
        listaPedidos.addAll(dbHelper.getPreparedPedidosForRepartidor());

        pedidosAdapter = new PedidosRepartidorAdapter(listaPedidos, getActivity());
        recyclerViewPedidos.setAdapter(pedidosAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listaPedidos.clear();
        listaPedidos.addAll(dbHelper.getPreparedPedidosForRepartidor());
        if (pedidosAdapter != null) {
            pedidosAdapter.notifyDataSetChanged();
        }
    }
}