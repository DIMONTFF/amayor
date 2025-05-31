package com.example.amayor.FirstActivity.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private OnAddToCartListener addToCartListener;

    public interface OnAddToCartListener {
        void onAddToCart(Producto producto);
    }

    public ProductoAdapter(List<Producto> productos, OnAddToCartListener listener) {
        this.productos = productos;
        this.addToCartListener = listener;
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreProducto;
        public TextView tvPrecio;
        public ImageView ivProductoImagen;
        public Button btnAddToCart;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivProductoImagen = itemView.findViewById(R.id.ivProductoImagen);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvNombreProducto.setText(producto.getNombre());
        holder.tvPrecio.setText(String.format("$%.2f", producto.getPrecio()));

        // Set product image
        if (producto.getImagen() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(producto.getImagen(), 0, producto.getImagen().length);
            holder.ivProductoImagen.setImageBitmap(bitmap);
        } else {
            holder.ivProductoImagen.setImageResource(R.drawable.placeholder); // Ensure you have a placeholder drawable
        }

        // Set click listener for Add button
        holder.btnAddToCart.setOnClickListener(v -> {
            if (addToCartListener != null) {
                addToCartListener.onAddToCart(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }
}
