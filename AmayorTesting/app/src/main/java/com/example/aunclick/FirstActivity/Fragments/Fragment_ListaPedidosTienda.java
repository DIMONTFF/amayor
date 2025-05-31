package com.example.amayor.FirstActivity.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Toast;

import com.example.amayor.FirstActivity.Adapters.PedidosTiendaAdapter;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Pedido;
import com.example.amayor.Objetos.Estado;
import com.example.amayor.R;
import java.util.List;

public class Fragment_ListaPedidosTienda extends Fragment implements PedidosTiendaAdapter.OnEstadoCambiadoListener {

    private static final String ARG_TIENDA_ID = "tienda_id";

    private int mTiendaId;
    private RecyclerView recyclerView;
    private PedidosTiendaAdapter adapter;
    private DataBaseHelper dbHelper;
    private List<Pedido> pedidos;

    public Fragment_ListaPedidosTienda() {
        // Required empty public constructor
    }

    public static Fragment_ListaPedidosTienda newInstance(int tiendaId) {
        Fragment_ListaPedidosTienda fragment = new Fragment_ListaPedidosTienda();
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
            Log.d("ListaPedidosTienda", "Tienda ID recibido: " + mTiendaId);
        }
        dbHelper = new DataBaseHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_pedidos_tienda, container, false);

        recyclerView = view.findViewById(R.id.recyclerPedidosTienda);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Cargar pedidos desde la base de datos
        pedidos = dbHelper.getPedidosByTiendaAndEstado(mTiendaId);
        Log.d("ListaPedidosTienda", "Número de pedidos cargados: " + pedidos.size());
        for (Pedido pedido : pedidos) {
            Log.d("ListaPedidosTienda", "Pedido: ID=" + pedido.getIdPedido() + ", Cliente=" + pedido.getNombreCliente() + ", Estado=" + pedido.getEstado());
        }
        adapter = new PedidosTiendaAdapter(pedidos, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onEstadoCambiado(Pedido pedido, Estado nuevoEstado) {
        // Actualizar el estado en la base de datos
        boolean actualizado = dbHelper.updatePedidoEstado(pedido.getIdPedido(), nuevoEstado);
        if (actualizado) {
            // Actualizar el objeto Pedido en la lista
            pedido.setEstado(nuevoEstado);
            // Notificar al adaptador que el item cambió
            int position = pedidos.indexOf(pedido);
            if (position != -1) {
                adapter.notifyItemChanged(position);
            }
            Toast.makeText(requireContext(), "Estado actualizado a " + formatEstado(nuevoEstado), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Error al actualizar el estado", Toast.LENGTH_SHORT).show();
        }
    }

    // Formatear el estado para mensajes de Toast
    private String formatEstado(Estado estado) {
        switch (estado) {
            case NOPREP:
                return "No preparado";
            case PREP:
                return "Preparado";
            case NOENTREG:
                return "No entregado";
            case ENTREG:
                return "Entregado";
            default:
                return estado.toString();
        }
    }
}