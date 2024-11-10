# Date Chooser - Calendario Personalizado para Android

**FechaSelector** es un control personalizado de calendario para aplicaciones Android, que permite a los usuarios seleccionar fechas de forma visual e interactiva.

## Características

- Visualización de calendario mensual con días de la semana.
- Selección de fechas mediante toques.
- Cambio de mes y año mediante `Spinner` y `NumberPicker`.
- Rango de fechas limitado hasta 50 años mas desde el año actual.

## Instalación

1. Copia las clases `FechaSelector` y `CalendarioView` en tu proyecto.
2. Añade el control en tu layout XML:
   ```xml
   <com.example.datechooser.FechaSelector
       android:id="@+id/fechaSelector"
       android:layout_width="match_parent"
       android:layout_height="wrap_content" />
4. Configura el control en tu actividad:
   ```java
    FechaSelector fechaSelector = findViewById(R.id.fechaSelector);
    fechaSelector.setMes(mes);
    fechaSelector.setAnio(anio);
5. Toma los 2 layouts necesarios para el control.
## Funcionalidad
- Selección de Fecha: Los usuarios pueden seleccionar un día tocando sobre el calendario.
- Cambio de Mes y Año: Usa Spinner y NumberPicker para cambiar el mes y el año.
- Personalización: Los colores y tamaños del calendario se pueden ajustar.
