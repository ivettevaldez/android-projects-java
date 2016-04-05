package com.hunabsys.customcomponentsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpData();
    }

    private void setUpData() {
        // Obtener arreglo de headers (strings) desde Resources para usarlo como variable.
        String[] headers = getResources().getStringArray(R.array.headers);

        // Ciclo para obtener y modificar las vistas agregadas mediante include
        // en nuestro layout principal.
        for (int i = 1; i < 6; i++) {
            // Encontrar la vista incluida mediante su identificador numerico (id),
            // el cual sera correspondiente al indice de nuestro ciclo.
            String strIncludeId = "include_item_" + i;
            int includeId = getResources().getIdentifier(strIncludeId, "id", getPackageName());
            View includeLayout = findViewById(includeId);

            // Si el layout fue encontrado, modificar su header con el valor correspondiente.
            if (includeLayout != null) {
                TextView textHeader = (TextView) includeLayout.findViewById(R.id.item_title);
                textHeader.setText(headers[i - 1]);
            }

            // Encontrar el arreglo de strings con el set de valores que corresponde a la vista.
            // En este caso, en el tipo de resource se especifica "array" para encontrar su ID.
            String strValuesArrayId = "set_" + i;
            int arrayId = getResources().getIdentifier(strValuesArrayId, "array", getPackageName());
            String[] strArray = getResources().getStringArray(arrayId);

            // Ciclo para obtener y modificar los textViews que representaran valores numericos.
            for (int ii = 0; ii < 4; ii++) {
                // Se suma uno a la posicion del indice para que coincida con el valor numerico del ID.
                int index = ii + 1;
                String strTextViewId = "item_value_" + index;
                int textViewId = getResources().getIdentifier(strTextViewId, "id", getPackageName());

                // Si el layout existe, se modifica el textView correspondiente a la posicion del indice.
                if (includeLayout != null) {
                    TextView textValue = (TextView) includeLayout.findViewById(textViewId);
                    textValue.setText(strArray[ii]);
                }
            }
        }
    }

}
