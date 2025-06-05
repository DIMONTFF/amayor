package com.example.amayor.FirstActivity.Fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.FirstActivity.Adapters.CarritoAdapter;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.FirstActivity.MainActivity;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Carrito extends DialogFragment {

    private static final String ARG_CARRITO = "carrito";
    private static final String TAG = "CarritoDialogFragment";
    private Map<Producto, Integer> carrito;
    private TextView tvPreparacion;
    private TextView tvTotal;
    private CarritoAdapter adapter;
    private OnCarritoUpdatedListener carritoUpdatedListener;

    public interface OnCarritoUpdatedListener {
        void onCarritoUpdated(Map<Producto, Integer> updatedCarrito);
    }

    public static Fragment_Carrito newInstance(HashMap<Producto, Integer> carrito) {
        Fragment_Carrito fragment = new Fragment_Carrito();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARRITO, carrito);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnCarritoUpdatedListener(OnCarritoUpdatedListener listener) {
        this.carritoUpdatedListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carrito = (HashMap<Producto, Integer>) getArguments().getSerializable(ARG_CARRITO);
        } else {
            carrito = new HashMap<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCarrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CarritoAdapter(carrito);
        recyclerView.setAdapter(adapter);

        tvPreparacion = view.findViewById(R.id.tvPreparacion);
        tvTotal = view.findViewById(R.id.tvTotal);

        updateTotalPrice();

        adapter.setOnQuantityChangeListener(() -> {
            updateTotalPrice();
            if (carritoUpdatedListener != null) {
                carritoUpdatedListener.onCarritoUpdated(carrito);
            }
        });

        Button btnRealizarTransaccion = view.findViewById(R.id.btnRealizarTransaccion);
        btnRealizarTransaccion.setOnClickListener(v -> {
            if (!carrito.isEmpty()) {
                DataBaseHelper dbHelper = new DataBaseHelper(getContext());
                MainActivity activity = (MainActivity) getActivity();
                int idCliente = activity.getLoggedInClienteId();
                if (idCliente == -1) {
                    Toast.makeText(getContext(), "No hay un cliente autenticado", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }

                int idTienda = -1;
                try {
                    idTienda = carrito.keySet().iterator().next().getIdTienda();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al obtener la tienda del carrito", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }

                ContentValues clienteDetails = dbHelper.getClienteDetails(idCliente);
                String nombreCliente = clienteDetails.getAsString("usuario");
                String direccion = clienteDetails.getAsString("direccion");
                if (nombreCliente == null || direccion == null) {
                    Toast.makeText(getContext(), "Error al obtener datos del cliente", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }

                String estado = "NOPREP";

                double total = calculateTotal();
                long pedidoId = dbHelper.insertPedido(idCliente, idTienda, nombreCliente, direccion, total, estado, carrito);
                if (pedidoId != -1) {
                    Log.d(TAG, "Pedido creado con ID: " + pedidoId);
                    carrito.clear();
                    Log.d(TAG, "Carrito size after clear: " + carrito.size());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Adapter item count: " + adapter.getItemCount());
                    updateTotalPrice();
                    if (carritoUpdatedListener != null) {
                        carritoUpdatedListener.onCarritoUpdated(carrito);
                    }
                    FragmentManager fm = getParentFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                        Toast.makeText(requireContext(), "Volviendo a la pantalla anterior", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "No hay fragmentos anteriores", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "Pedido creado exitosamente", Toast.LENGTH_SHORT).show();
                    // Delay dismiss to ensure UI updates
                    new Handler(Looper.getMainLooper()).postDelayed(this::dismiss, 500);
                } else {
                    Log.e(TAG, "Error al crear el Pedido");
                    Toast.makeText(getContext(), "Error al crear el pedido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateTotalPrice() {
        double subtotal = 0;
        for (Map.Entry<Producto, Integer> entry : carrito.entrySet()) {
            subtotal += entry.getKey().getPrecio() * entry.getValue();
        }
        double preparacion = 3.00;
        double total = subtotal + preparacion;

        tvPreparacion.setText(String.format("Preparación: $%.2f", preparacion));
        tvTotal.setText(String.format("Total: $%.2f", total));
    }

    private double calculateTotal() {
        double subtotal = 0;
        for (Map.Entry<Producto, Integer> entry : carrito.entrySet()) {
            subtotal += entry.getKey().getPrecio() * entry.getValue();
        }
        return subtotal + 3.00;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}