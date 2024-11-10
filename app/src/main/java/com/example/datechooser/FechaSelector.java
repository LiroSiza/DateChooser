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

    private Spinner spinnerMes;  // Spinner para seleccionar el mes
    private Spinner spinnerAnio; // Spinner para seleccionar el año
    private TextView textViewFecha;  // Referencia al TextView que muestra la fecha seleccionada
    private OnFechaChangeListener fechaChangeListener;  // Listener para notificar cambios de fecha

    // Constructor que inicializa la vista del control personalizado
    public FechaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializarVista(context);  // Llamar al método para inflar la vista
    }

    private void inicializarVista(Context context) {
        // Inflar el layout del control personalizado
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_fecha_selector, this, true);  // Inflar el XML

        // Asignar las referencias a los elementos del layout
        spinnerMes = findViewById(R.id.spinnerMes);
        spinnerAnio = findViewById(R.id.spinnerAnio);
        textViewFecha = findViewById(R.id.textViewFecha);  // Inicializar TextView para mostrar la fecha

        // Configurar los spinners para mes y año
        configurarSpinnerMesAnio(context);

        // Establecer listeners para los spinners
        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                notificarCambio();  // Notificar cuando se cambia el mes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona ningún ítem
            }
        });

        spinnerAnio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                notificarCambio();  // Notificar cuando se cambia el año
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona ningún ítem
            }
        });
    }

    // Configura los spinners para mes y año
    private void configurarSpinnerMesAnio(Context context) {
        // Configurar el spinner para seleccionar el mes
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        // Adaptador para el spinner de meses
        ArrayAdapter<String> adapterMeses = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, meses) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                // Cambiar el fondo de la lista desplegable a blanco
                view.setBackgroundResource(R.drawable.spinner_background);
                return view;
            }
        };
        adapterMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMes.setAdapter(adapterMeses);  // Asignar el adaptador al spinner de mes

        // Configurar el spinner para seleccionar el año
        List<String> anios = new ArrayList<>();
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);  // Obtener el año actual
        for (int i = anioActual; i >= 1900; i--) {
            anios.add(String.valueOf(i));  // Agregar años de 1900 hasta el actual
        }

        // Adaptador para el spinner de años
        ArrayAdapter<String> adapterAnios = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, anios) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                // Cambiar el fondo de la lista desplegable a blanco
                view.setBackgroundResource(R.drawable.spinner_background);
                return view;
            }
        };
        adapterAnios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnio.setAdapter(adapterAnios);  // Asignar el adaptador al spinner de año
    }

    // Notifica un cambio en la fecha (mes o año)
    private void notificarCambio() {
        if (fechaChangeListener != null) {
            int mesSeleccionado = spinnerMes.getSelectedItemPosition();  // Obtener el mes seleccionado
            int anioSeleccionado = Integer.parseInt(spinnerAnio.getSelectedItem().toString());  // Obtener el año seleccionado
            fechaChangeListener.onFechaChanged(mesSeleccionado, anioSeleccionado);  // Notificar el cambio al listener

            // Actualizar el TextView con la fecha seleccionada
            String fechaSeleccionada = getMesNombre(mesSeleccionado) + " " + anioSeleccionado;
            textViewFecha.setText(fechaSeleccionada);  // Mostrar la fecha en el TextView
        }
    }

    // Devuelve el nombre del mes a partir del índice
    private String getMesNombre(int mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes];  // Retorna el nombre del mes
    }

    // Métodos para cambiar el mes y el año programáticamente
    public void setMes(int mes) {
        if (mes >= 0 && mes <= 11) {
            spinnerMes.setSelection(mes);  // Cambiar la selección del spinner de mes
        }
    }

    public void setAnio(int anio) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerAnio.getAdapter();
        int posicion = adapter.getPosition(String.valueOf(anio));  // Obtener la posición del año
        if (posicion >= 0) {
            spinnerAnio.setSelection(posicion);  // Cambiar la selección del spinner de año
        }
    }

    // Obtener el mes seleccionado
    public int getMes() {
        return spinnerMes.getSelectedItemPosition();  // Retorna la posición seleccionada del spinner de mes
    }

    // Obtener el año seleccionado
    public int getAnio() {
        return Integer.parseInt(spinnerAnio.getSelectedItem().toString());  // Retorna el año seleccionado
    }

    // Asignar el listener para cambios de fecha
    public void setOnFechaChangeListener(OnFechaChangeListener listener) {
        this.fechaChangeListener = listener;  // Asigna el listener para notificar cambios
    }
}
