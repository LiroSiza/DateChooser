package com.example.datechooser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class FechaSelector extends LinearLayout {

    private Spinner spinnerMes;  // Spinner para seleccionar el mes
    private NumberPicker numberPickerAnio; // NumberPicker para seleccionar el año
    private TextView textViewFecha;  // Referencia al TextView que muestra la fecha seleccionada
    private OnFechaChangeListener fechaChangeListener;  // Listener para notificar cambios de fecha

    private CalendarioView calendarioView; // Para dibujar el calendario
    private int diaCalendario;

    // Constructor que inicializa la vista del control personalizado
    public FechaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializarVista(context);
    }

    private void inicializarVista(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_fecha_selector, this, true);

        spinnerMes = findViewById(R.id.spinnerMes);
        numberPickerAnio = findViewById(R.id.numberPickerAnio);
        textViewFecha = findViewById(R.id.textViewFecha);
        calendarioView = findViewById(R.id.calendarioView);

        // Configurar los elementos del calendario y otros controles
        configurarSpinnerMes(context);
        configurarNumberPickerAnio();

        // Inicializar el TextView con el mes y año al inicio (sin día)
        String fechaInicial = getMesNombre(getMes()) + " del " + getAnio();
        textViewFecha.setText(fechaInicial);

        // Establecer el listener para el calendario
        calendarioView.setOnDiaSeleccionadoListener(dia -> {
            // Actualizar el TextView con el día seleccionado
            diaCalendario = dia;
            String fechaSeleccionada = dia + " de " + getMesNombre(getMes()) + " del " + getAnio();
            textViewFecha.setText(fechaSeleccionada);
        });

        // Listener para cambios en el spinner de mes
        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                notificarCambio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        calendarioView.actualizarCalendario(getMes(), getAnio());

        // Listener para cambios en el NumberPicker de año
        numberPickerAnio.setOnValueChangedListener((picker, oldVal, newVal) -> notificarCambio());
        // Eliminar la capacidad de modificar mediante teclado el texto
        numberPickerAnio.setWrapSelectorWheel(true);  // Esto permite que el número se pueda seleccionar deslizando
        numberPickerAnio.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);  // Bloquea el foco de los elementos internos

    }

    // Configura el spinner para seleccionar el mes
    private void configurarSpinnerMes(Context context) {
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
    }

    // Configura el NumberPicker para seleccionar el año
    private void configurarNumberPickerAnio() {
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        numberPickerAnio.setMinValue(1900);
        numberPickerAnio.setMaxValue(anioActual+50);
        numberPickerAnio.setValue(anioActual);
    }

    private void notificarCambio() {
        if (fechaChangeListener != null) {
            int mesSeleccionado = spinnerMes.getSelectedItemPosition();
            int anioSeleccionado = numberPickerAnio.getValue();
            fechaChangeListener.onFechaChanged(mesSeleccionado, anioSeleccionado);

            // Actualizar el TextView con la fecha seleccionada
            String fechaSeleccionada;
            if(diaCalendario == 0){  // Al iniciar app
                fechaSeleccionada = getMesNombre(getMes()) + " del " + getAnio();
            }else{
                fechaSeleccionada = diaCalendario + " de " + getMesNombre(getMes()) + " del " + getAnio();
            }
            textViewFecha.setText(fechaSeleccionada);

            // Llamar a actualizarCalendario para actualizar el calendario
            calendarioView.actualizarCalendario(getMes(), getAnio());
        }
    }


    // Método para obtener el nombre del mes a partir del índice
    private String getMesNombre(int mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes];
    }

    // Métodos para cambiar el mes y el año programáticamente
    public void setMes(int mes) {
        if (mes >= 0 && mes <= 11) {
            spinnerMes.setSelection(mes);
        }
    }

    public void setAnio(int anio) {
        if (anio >= 1900 && anio <= numberPickerAnio.getMaxValue()) {
            numberPickerAnio.setValue(anio);
        }
    }

    // Obtener el mes seleccionado
    public int getMes() {
        return spinnerMes.getSelectedItemPosition();
    }

    // Obtener el año seleccionado
    public int getAnio() {
        return numberPickerAnio.getValue();
    }

    // Asignar el listener para cambios de fecha
    public void setOnFechaChangeListener(OnFechaChangeListener listener) {
        this.fechaChangeListener = listener;
    }

    // Interfaz para notificar cambios de fecha
    public interface OnFechaChangeListener {
        void onFechaChanged(int mes, int anio);
    }
}
