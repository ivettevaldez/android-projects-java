package silviavaldez.mygitlabciapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSayHello = (Button) findViewById(R.id.main_button_say_hello);
        buttonSayHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textHello = (TextView) findViewById(R.id.main_text_hello);
                EditText editName = (EditText) findViewById(R.id.main_edit_name);
                textHello.setText(String.format("%s, %s!",
                        getString(R.string.main_hello), editName.getText().toString()));
            }
        });
    }

}
