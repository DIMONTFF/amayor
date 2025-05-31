package com.example.amayor.FirstActivity.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Estado;
import com.example.amayor.Objetos.Pedido;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;
import java.util.List;
import java.util.Map;

public class PedidosRepartidorAdapter extends RecyclerView.Adapter<PedidosRepartidorAdapter.PedidoViewHolder> {

    private List<DataBaseHelper.PedidoConTienda> pedidos;
    private FragmentActivity activity;

    public PedidosRepartidorAdapter(List<DataBaseHelper.PedidoConTienda> pedidos, FragmentActivity activity) {
        this.pedidos = pedidos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido_repartidor, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        DataBaseHelper.PedidoConTienda pedidoConTienda = pedidos.get(position);
        Pedido pedido = pedidoConTienda.getPedido();
        holder.tvIdPedido.setText("Pedido #" + pedido.getIdPedido());
        holder.tvNombreCliente.setText(pedido.getNombreCliente() != null ? pedido.getNombreCliente() : "Sin nombre");
        holder.tvDireccion.setText(pedido.getDireccion() != null ? pedido.getDireccion() : "Sin dirección");
        holder.tvNombreTienda.setText(pedidoConTienda.getNombreTienda() != null ? pedidoConTienda.getNombreTienda() : "Sin tienda");

        // Format products list
        StringBuilder productosStr = new StringBuilder();
        for (Map.Entry<Producto, Integer> entry : pedido.getDescripcion().entrySet()) {
            if (productosStr.length() > 0) {
                productosStr.append(", ");
            }
            productosStr.append(entry.getKey().getNombre()).append(" x").append(entry.getValue());
        }
        holder.tvProductos.setText(productosStr.length() > 0 ? productosStr.toString() : "Sin productos");

        // Format estado
        String estadoStr;
        switch (pedido.getEstado()) {
            case NOPREP:
                estadoStr = "No Preparado";
                break;
            case PREP:
                estadoStr = "No Entregado";
                break;
            case NOENTREG:
                estadoStr = "No Entregado";
                break;
            case ENTREG:
                estadoStr = "Entregado";
                break;
            default:
                estadoStr = "Desconocido";
        }
        holder.tvEstado.setText(estadoStr);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdPedido, tvNombreCliente, tvDireccion, tvNombreTienda, tvProductos, tvEstado;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdPedido = itemView.findViewById(R.id.tvIdPedido);
            tvNombreCliente = itemView.findViewById(R.id.tvNombreCliente);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvNombreTienda = itemView.findViewById(R.id.tvNombreTienda);
            tvProductos = itemView.findViewById(R.id.tvProductos);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
