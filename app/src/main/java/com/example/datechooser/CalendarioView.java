package com.example.datechooser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

public class CalendarioView extends View {

    private int mes;
    private int anio;
    private Paint paintTexto;
    private Paint paintLinea;
    private Paint paintSeleccion;
    private int diasEnMes;
    private int primerDiaSemana;
    private int diaSeleccionado = -1;
    private String[] diasSemana = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
    private OnDiaSeleccionadoListener diaSeleccionadoListener;

    public CalendarioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializar();
    }

    private void inicializar() {
        paintTexto = new Paint();
        paintTexto.setColor(Color.BLACK);
        paintTexto.setTextSize(40);
        paintTexto.setAntiAlias(true);

        paintLinea = new Paint();
        paintLinea.setColor(Color.GRAY);
        paintLinea.setStrokeWidth(2);

        paintSeleccion = new Paint();
        paintSeleccion.setColor(Color.LTGRAY);

        mes = Calendar.getInstance().get(Calendar.MONTH);
        anio = Calendar.getInstance().get(Calendar.YEAR);
        calcularDatosMes();
    }

    // Modificar el método `calcularDatosMes()` si es necesario
    private void calcularDatosMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, anio);
        calendar.set(Calendar.MONTH, mes);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        diasEnMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        primerDiaSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Lunes es 0
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int numColumnas = 7;
        int numFilas = 7;
        int celdaWidth = width / numColumnas;
        int celdaHeight = height / numFilas;

        // Dibujar nombres de los días de la semana
        for (int i = 0; i < numColumnas; i++) {
            float x = i * celdaWidth + celdaWidth / 2f;
            float y = celdaHeight / 1.5f;
            float textoWidth = paintTexto.measureText(diasSemana[i]);
            canvas.drawText(diasSemana[i], x - textoWidth / 2f, y, paintTexto);
        }

        // Dibujar los números de los días y resaltar el día seleccionado
        int diaActual = 1;
        for (int fila = 1; fila < numFilas && diaActual <= diasEnMes; fila++) {
            for (int columna = 0; columna < numColumnas && diaActual <= diasEnMes; columna++) {
                if (fila == 1 && columna < primerDiaSemana) {
                    continue; // Espacio vacío antes del primer día del mes
                }

                float left = columna * celdaWidth;
                float top = fila * celdaHeight;
                float right = left + celdaWidth;
                float bottom = top + celdaHeight;

                // Dibujar fondo si el día está seleccionado
                if (diaActual == diaSeleccionado) {
                    canvas.drawRect(left, top, right, bottom, paintSeleccion);
                }

                // Dibujar el número del día
                float x = columna * celdaWidth + celdaWidth / 2f;
                float y = (fila + 1) * celdaHeight - celdaHeight / 3f;
                String diaTexto = String.valueOf(diaActual);
                float textoWidth = paintTexto.measureText(diaTexto);
                canvas.drawText(diaTexto, x - textoWidth / 2f, y, paintTexto);

                diaActual++;
            }
        }
    }

    // Método para actualizar el mes y año en el calendario
    public void actualizarCalendario(int mes, int anio) {
        this.mes = mes; // Actualizamos el mes
        this.anio = anio; // Actualizamos el año
        calcularDatosMes(); // Recalculamos los datos del mes

        invalidate(); // Redibujamos la vista con la nueva información
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int width = getWidth();
            int height = getHeight();
            int numColumnas = 7;
            int numFilas = 7;
            int celdaWidth = width / numColumnas;
            int celdaHeight = height / numFilas;

            float x = event.getX();
            float y = event.getY();

            // Determinar la columna y fila tocada
            int columna = (int) (x / celdaWidth);
            int fila = (int) (y / celdaHeight);

            // Ignorar toques fuera del área válida
            if (fila == 0 || fila >= numFilas) return false;

            // Calcular el día tocado
            int diaActual = 1;
            for (int f = 1; f < numFilas && diaActual <= diasEnMes; f++) {
                for (int c = 0; c < numColumnas && diaActual <= diasEnMes; c++) {
                    if (f == 1 && c < primerDiaSemana) {
                        continue;
                    }
                    if (f == fila && c == columna) {
                        diaSeleccionado = diaActual;
                        if (diaSeleccionadoListener != null) {
                            diaSeleccionadoListener.onDiaSeleccionado(diaSeleccionado);
                        }
                        invalidate(); // Redibujar el calendario
                        return true;
                    }
                    diaActual++;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnDiaSeleccionadoListener(OnDiaSeleccionadoListener listener) {
        this.diaSeleccionadoListener = listener;
    }

    public interface OnDiaSeleccionadoListener {
        void onDiaSeleccionado(int dia);
    }
}
