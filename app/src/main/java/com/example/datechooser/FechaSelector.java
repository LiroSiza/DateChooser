package com.example.datechooser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FechaSelector extends LinearLayout {

    private Spinner spinnerMes;
    private Spinner spinnerAnio;
    private TextView textViewFecha;  // Referencia al TextView
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
        textViewFecha = findViewById(R.id.textViewFecha);  // Inicializar TextView

        configurarSpinnerMesAnio(context);

        // Agregar listeners para los Spinners
        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                notificarCambio();  // Actualizar la fecha cuando se cambia el mes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada
            }
        });

        spinnerAnio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                notificarCambio();  // Actualizar la fecha cuando se cambia el año
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada
            }
        });
    }

    private void configurarSpinnerMesAnio(Context context) {
        // Configurar Spinner para el Mes
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        ArrayAdapter<String> adapterMeses = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, meses) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                // Cambiar fondo de la lista desplegable a blanco
                view.setBackgroundResource(R.drawable.spinner_background);
                return view;
            }
        };
        adapterMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMes.setAdapter(adapterMeses);

        // Configurar Spinner para el Año
        List<String> anios = new ArrayList<>();
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = anioActual; i >= 1900; i--) {
            anios.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapterAnios = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, anios) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                // Cambiar fondo de la lista desplegable a blanco
                view.setBackgroundResource(R.drawable.spinner_background);
                return view;
            }
        };
        adapterAnios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnio.setAdapter(adapterAnios);
    }

    // Método para notificar un cambio en la fecha
    private void notificarCambio() {
        if (fechaChangeListener != null) {
            int mesSeleccionado = spinnerMes.getSelectedItemPosition();
            int anioSeleccionado = Integer.parseInt(spinnerAnio.getSelectedItem().toString());
            fechaChangeListener.onFechaChanged(mesSeleccionado, anioSeleccionado);

            // Actualizar el TextView con la fecha seleccionada
            String fechaSeleccionada = getMesNombre(mesSeleccionado) + " " + anioSeleccionado;
            textViewFecha.setText(fechaSeleccionada);  // Actualizar TextView
        }
    }

    // Método para obtener el nombre del mes
    private String getMesNombre(int mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes];
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

    // Método para asignar el listener
    public void setOnFechaChangeListener(OnFechaChangeListener listener) {
        this.fechaChangeListener = listener;
    }
}
