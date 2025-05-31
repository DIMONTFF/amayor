package com.example.amayor.FirstActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.amayor.FirstActivity.Database.DataBaseHelper;
import com.example.amayor.FirstActivity.Fragments.Fragment_Carrito;
import com.example.amayor.FirstActivity.Fragments.Fragment_InicioCliente;
import com.example.amayor.FirstActivity.Fragments.Fragment_InicioEmpleado;
import com.example.amayor.FirstActivity.Fragments.Fragment_InicioSesionV1;
import com.example.amayor.FirstActivity.Fragments.Fragment_InicioTienda;
import com.example.amayor.Objetos.Producto;
import com.example.amayor.R;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Fragment_Carrito.OnCarritoUpdatedListener {

    private int loggedInClienteId = -1;
    private int loggedInTiendaId = -1;
    private int loggedInRepartidorId = -1;
    private HashMap<Producto, Integer> carrito = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Adjust padding to avoid content overlapping system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configure immersive fullscreen mode
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        // Initialize database with sample data
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        dbHelper.deleteAllData();
        dbHelper.resetAutoIncrement();
        dbHelper.insertSampleData();

        // Check for saved session
        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE);
            String userType = prefs.getString("userType", "");
            int userId = prefs.getInt("userId", -1);

            if (userId != -1 && !userType.isEmpty()) {
                // Auto-navigate to the appropriate Fragment
                switch (userType) {
                    case "Cliente":
                        loggedInClienteId = userId;
                        cargarFragmento(new Fragment_InicioCliente());
                        break;
                    case "Tienda":
                        loggedInTiendaId = userId;
                        cargarFragmento(new Fragment_InicioTienda());
                        break;
                    case "Repartidor":
                        loggedInRepartidorId = userId;
                        cargarFragmento(new Fragment_InicioEmpleado());
                        break;
                    default:
                        cargarFragmento(new Fragment_InicioSesionV1());
                }
            } else {
                // Load login Fragment if no session exists
                cargarFragmento(new Fragment_InicioSesionV1());
            }
        }
    }

    public void cargarFragmento(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showCarritoFragment() {
        Fragment_Carrito fragment = Fragment_Carrito.newInstance(carrito);
        fragment.setOnCarritoUpdatedListener(this);
        fragment.show(getSupportFragmentManager(), "CarritoDialog");
    }

    @Override
    public void onCarritoUpdated(Map<Producto, Integer> updatedCarrito) {
        // Update MainActivity's carrito with the updated one
        this.carrito = new HashMap<>(updatedCarrito);
    }

    public void setLoggedInClienteId(int id) {
        this.loggedInClienteId = id;
        this.loggedInTiendaId = -1;
        this.loggedInRepartidorId = -1;
    }

    public void setLoggedInTiendaId(int id) {
        this.loggedInTiendaId = id;
        this.loggedInClienteId = -1;
        this.loggedInRepartidorId = -1;
    }

    public void setLoggedInRepartidorId(int id) {
        this.loggedInRepartidorId = id;
        this.loggedInClienteId = -1;
        this.loggedInTiendaId = -1;
    }

    public int getLoggedInClienteId() {
        return loggedInClienteId;
    }

    public int getLoggedInTiendaId() {
        return loggedInTiendaId;
    }

    public int getLoggedInRepartidorId() {
        return loggedInRepartidorId;
    }

    public HashMap<Producto, Integer> getCarrito() {
        return carrito;
    }
}