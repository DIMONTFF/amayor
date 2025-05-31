package com.example.amayor.FirstActivity.Adapters;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.FirstActivity.MainActivity;
import com.example.amayor.Objetos.Pedido;
import com.example.amayor.Objetos.Tienda;
import com.example.amayor.Objetos.Estado;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;

import java.util.LinkedHashMap;
import java.util.List;

public class TiendaOtrosAdapter extends RecyclerView.Adapter<TiendaOtrosAdapter.TiendaViewHolder> {

    private List<Tienda> tiendas;
    private FragmentActivity activity;
    private DataBaseHelper dbHelper;

    public TiendaOtrosAdapter(List<Tienda> tiendas, FragmentActivity activity) {
        this.tiendas = tiendas;
        this.activity = activity;
        this.dbHelper = new DataBaseHelper(activity);
    }

    public static class TiendaViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreTienda;
        public TextView tvTipoTienda;
        public TextView tvNumTelef;
        public Button btnCall;

        public TiendaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreTienda = itemView.findViewById(R.id.tvNombreTienda);
            tvTipoTienda = itemView.findViewById(R.id.tvTipoTienda);
            tvNumTelef = itemView.findViewById(R.id.tvNumTelef);
            btnCall = itemView.findViewById(R.id.btnCall);
        }
    }

    @NonNull
    @Override
    public TiendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tienda_otros, parent, false);
        return new TiendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TiendaViewHolder holder, int position) {
        Tienda tienda = tiendas.get(position);
        holder.tvNombreTienda.setText(tienda.getNombreTienda());
        holder.tvTipoTienda.setText(tienda.getTipoTienda().toString());
        holder.tvNumTelef.setText(tienda.getNumTelef());

        // Click listener para iniciar una llamada y crear un pedido
        holder.btnCall.setOnClickListener(v -> {
            // Iniciar la llamada
            String phoneNumber = tienda.getNumTelef();
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                activity.startActivity(dialIntent);
            }

            // Obtener idCliente desde MainActivity
            MainActivity mainActivity = (MainActivity) activity;
            int idCliente = mainActivity.getLoggedInClienteId();
            if (idCliente == -1) {
                Toast.makeText(activity, "No hay un cliente autenticado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener idTienda
            int idTienda = tienda.getIdPersona();
            if (idTienda == -1) {
                Toast.makeText(activity, "Error al obtener la tienda", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener detalles del cliente
            ContentValues clienteDetails = dbHelper.getClienteDetails(idCliente);
            String nombreCliente = clienteDetails.getAsString("usuario");
            String direccion = clienteDetails.getAsString("direccion");
            if (nombreCliente == null || direccion == null) {
                Toast.makeText(activity, "Error al obtener datos del cliente", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear un pedido de tipo "solicitud de cita"
            LinkedHashMap<Producto, Integer> descripcion = new LinkedHashMap<>();
            Pedido pedido = new Pedido(
                    0, // idPedido, será asignado por la base de datos
                    idCliente,
                    idTienda,
                    null, // idRepartidor
                    nombreCliente,
                    direccion,
                    0.0, // importe
                    descripcion,
                    Estado.NOPREP
            );

            long pedidoId = dbHelper.insertPedidoCita(pedido);
            if (pedidoId != -1) {
                Toast.makeText(activity, "Solicitud de cita creada exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Error al crear la solicitud de cita", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tiendas.size();
    }
}