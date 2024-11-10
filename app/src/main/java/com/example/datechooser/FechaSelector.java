package com.example.datechooser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FechaSelector extends LinearLayout {

    private Spinner spinnerMes;
    private Spinner spinnerAnio;
    private OnFechaChangeListener fechaChangeListener;

    public FechaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializarVista(context);
    }

    private void inicializarVista(Context context) {
        // Inflar el layout del control personalizado
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_fecha_selector, this, true);

        spinnerMes = findViewById(R.id.spinnerMes);
        spinnerAnio = findViewById(R.id.spinnerAnio);

        configurarSpinnerMes(context);
        configurarSpinnerAnio(context);
    }

    private void configurarSpinnerMes(Context context) {
        // Lista de meses
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        ArrayAdapter<String> adapterMeses = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, meses);
        adapterMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMes.setAdapter(adapterMeses);

        // Listener para detectar cambios en el mes
        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                notificarCambio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
    }

    private void configurarSpinnerAnio(Context context) {
        // Generar una lista de años (ejemplo: de 1900 al año actual)
        List<String> anios = new ArrayList<>();
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = anioActual; i >= 1900; i--) {
            anios.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapterAnios = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, anios);
        adapterAnios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerAnio.setAdapter(adapterAnios);

        // Listener para detectar cambios en el año
        spinnerAnio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                notificarCambio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
    }

    // Método para notificar un cambio en la fecha
    private void notificarCambio() {
        if (fechaChangeListener != null) {
            int mesSeleccionado = spinnerMes.getSelectedItemPosition();
            int anioSeleccionado = Integer.parseInt(spinnerAnio.getSelectedItem().toString());
            fechaChangeListener.onFechaChanged(mesSeleccionado, anioSeleccionado);
        }
    }

    // Métodos set para cambiar el mes y el año programáticamente
    public void setMes(int mes) {
        if (mes >= 0 && mes <= 11) {
            spinnerMes.setSelection(mes);
        }
    }

    public void setAnio(int anio) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerAnio.getAdapter();
        int posicion = adapter.getPosition(String.valueOf(anio));
        if (posicion >= 0) {
            spinnerAnio.setSelection(posicion);
        }
    }

    // Método para obtener el mes seleccionado
    public int getMes() {
        return spinnerMes.getSelectedItemPosition();
    }

    // Método para obtener el año seleccionado
    public int getAnio() {
        return Integer.parseInt(spinnerAnio.getSelectedItem().toString());
    }

    // Interfaz para el listener de cambios
    public interface OnFechaChangeListener {
        void onFechaChanged(int mes, int anio);
    }

    // Método para asignar el listener
    public void setOnFechaChangeListener(OnFechaChangeListener listener) {
        this.fechaChangeListener = listener;
    }
}
