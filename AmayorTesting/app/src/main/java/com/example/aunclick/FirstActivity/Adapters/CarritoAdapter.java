package com.example.amayor.FirstActivity.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;

import java.util.Map;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private Map<Producto, Integer> carrito;
    private OnQuantityChangeListener quantityChangeListener;

    public CarritoAdapter(Map<Producto, Integer> carrito) {
        this.carrito = carrito;
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreProducto;
        public TextView tvCantidad;
        public TextView tvSubtotal;
        public Button btnIncrease;
        public Button btnDecrease;

        public CarritoViewHolder(View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Producto producto = getProductoAtPosition(position);
        int cantidad = carrito.get(producto);

        holder.tvNombreProducto.setText(producto.getNombre());
        holder.tvCantidad.setText("Cantidad: " + cantidad);
        double subtotal = producto.getPrecio() * cantidad;
        holder.tvSubtotal.setText(String.format("Subtotal: $%.2f", subtotal));

        holder.btnIncrease.setOnClickListener(v -> {
            carrito.put(producto, cantidad + 1);
            notifyItemChanged(position);
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged();
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (cantidad > 1) {
                carrito.put(producto, cantidad - 1);
                notifyItemChanged(position);
            } else {
                carrito.remove(producto);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, carrito.size());
            }
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carrito.size();
    }

    private Producto getProductoAtPosition(int position) {
        return carrito.keySet().toArray(new Producto[0])[position];
    }
}