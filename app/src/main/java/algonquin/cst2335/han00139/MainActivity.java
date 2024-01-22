package algonquin.cst2335.han00139;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import algonquin.cst2335.han00139.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    private TextView mytext;
    private Button mybutton;
    private EditText myedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        // Initialize views using ViewBinding
        mytext = variableBinding.textview;
        mybutton = variableBinding.mybutton;
        myedit = variableBinding.myedittext;

        // Initialize EditText here or in a button click listener
         //myedit = findViewById(R.id.myedittext);

        // Example: Set up a click listener for a button
        //Button myButton = findViewById(R.id.mybutton);
        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text from EditText
                String editString = myedit.getText().toString();
                // Update TextView with the text from EditText
                mytext.setText("my edit text has: " + editString);
            }
        });
    }
}
