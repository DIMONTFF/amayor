package com.example.amayor.FirstActivity.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.Objetos.Pedido;
import com.example.amayor.R;

import java.util.List;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitaViewHolder> {

    private List<Pedido> citas;
    private FragmentActivity activity;
    private DataBaseHelper dbHelper;

    public CitasAdapter(List<Pedido> citas, FragmentActivity activity) {
        this.citas = citas;
        this.activity = activity;
        this.dbHelper = new DataBaseHelper(activity);
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cita, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Pedido cita = citas.get(position);
        holder.tvIdCita.setText("Cita #" + cita.getIdPedido());
        holder.tvNombreCliente.setText(cita.getNombreCliente() != null ? cita.getNombreCliente() : "Sin nombre");
        holder.tvDireccion.setText(cita.getDireccion() != null ? cita.getDireccion() : "Sin dirección");

        // Click listener para eliminar la cita
        holder.btnDeleteCita.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Pedido citaToDelete = citas.get(adapterPosition);
                boolean deleted = dbHelper.deletePedido(citaToDelete.getIdPedido());
                if (deleted) {
                    citas.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    Toast.makeText(activity, "La cita se ha completado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Error al completar la cita", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdCita, tvNombreCliente, tvDireccion;
        ImageButton btnDeleteCita;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdCita = itemView.findViewById(R.id.tvIdCita);
            tvNombreCliente = itemView.findViewById(R.id.tvNombreCliente);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            btnDeleteCita = itemView.findViewById(R.id.btnDeleteCita);
        }
    }
}
