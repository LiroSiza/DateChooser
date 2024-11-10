package com.example.datechooser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private CustomView customView;  // Vista personalizada para el dibujo del calendario

    public FechaSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);  // Asegurar que la vista se organiza verticalmente
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

        // Crear el CustomView y agregarlo al LinearLayout
        customView = new CustomView(context);
        this.addView(customView);  // Agregar el CustomView a este LinearLayout

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

    private void notificarCambio() {
        if (fechaChangeListener != null) {
            int mesSeleccionado = spinnerMes.getSelectedItemPosition();  // Obtener el mes seleccionado
            int anioSeleccionado = Integer.parseInt(spinnerAnio.getSelectedItem().toString());  // Obtener el año seleccionado
            fechaChangeListener.onFechaChanged(mesSeleccionado, anioSeleccionado);  // Notificar el cambio al listener

            // Actualizar el TextView con la fecha seleccionada
            String fechaSeleccionada = getMesNombre(mesSeleccionado) + " " + anioSeleccionado;
            textViewFecha.setText(fechaSeleccionada);  // Mostrar la fecha en el TextView

            customView.invalidate();  // Esto forzará a llamar a onDraw() del CustomView para redibujar el calendario
        }
    }

    // Método para obtener el nombre del mes a partir del índice
    private String getMesNombre(int mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes];  // Retorna el nombre del mes
    }

    // Asignar el listener para cambios de fecha
    public void setOnFechaChangeListener(OnFechaChangeListener listener) {
        this.fechaChangeListener = listener;  // Asigna el listener para notificar cambios
    }

    // Clase interna para manejar el dibujo del calendario
    private class CustomView extends View {
        public CustomView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // Crear un objeto Paint para definir cómo se va a dibujar
            Paint paint = new Paint();
            paint.setColor(Color.RED);  // Establecer el color del texto
            paint.setTextSize(50);  // Tamaño de fuente grande para asegurar que se vea

            // Dibujar un texto en las coordenadas (100, 100)
            canvas.drawText("Fecha Seleccionada:", 100, 100, paint);

            // Añadir más elementos gráficos como líneas, círculos, etc.
            paint.setColor(Color.BLUE);  // Cambiar color para dibujar otras cosas
            canvas.drawCircle(300, 300, 100, paint);  // Dibujar un círculo azul en la posición (300, 300)
        }
    }
}
