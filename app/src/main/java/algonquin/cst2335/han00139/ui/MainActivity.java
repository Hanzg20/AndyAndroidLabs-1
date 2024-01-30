package algonquin.cst2335.han00139.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.han00139.R;
import algonquin.cst2335.han00139.data.MainViewModel;
import algonquin.cst2335.han00139.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        // Initialize views using ViewBinding
        TextView mytext = variableBinding.textview;
        Button mybutton = variableBinding.mybutton;
        EditText myedit = variableBinding.myedittext;

        // Set up click listener for the button
        mybutton.setOnClickListener(view -> {
            // Get the text from EditText
            String editString = myedit.getText().toString();
            // Update TextView with the text from EditText
            mytext.setText("Your edit text has: " + editString);
        });

        model = new ViewModelProvider(this).get(MainViewModel.class);

        // ImageView
        ImageView imageView = findViewById(R.id.myimageview);
        imageView.setImageResource(R.drawable.logo_algonquin);

        // ImageButton
        ImageButton imageButton = findViewById(R.id.myimagebutton);
        imageButton.setImageResource(R.drawable.logo_algonquin);
        imageButton.setOnClickListener(view -> showDimensionsToast(view.getWidth(), view.getHeight()));

        // Initialize your CheckBox, Switch, and RadioButton using ViewBinding
        CheckBox checkBox = variableBinding.checkBox;
        Switch switchButton = variableBinding.switchButton;
        RadioButton radioButton = variableBinding.radioButton;

        // Set up observers for the MutableLiveData in MainViewModel
        model.getIsSelected().observe(this, isSelected -> {
            checkBox.setChecked(isSelected);
            switchButton.setChecked(isSelected);
            radioButton.setChecked(isSelected);

            // Show Toast for the state of compound buttons
            showToastForCompoundButtons(isSelected);
        });

        // Set up OnCheckedChangeListener for the CheckBox, Switch, and RadioButton
        checkBox.setOnCheckedChangeListener(checkedChangeListener);
        switchButton.setOnCheckedChangeListener(checkedChangeListener);
        radioButton.setOnCheckedChangeListener(checkedChangeListener);
    }

    private void showToastForCompoundButtons(boolean isSelected) {
        String message = "Compound buttons state: " + (isSelected ? "Checked" : "Unchecked");
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private final CompoundButton.OnCheckedChangeListener checkedChangeListener = (buttonView, isChecked) -> {
        model.getIsSelected().setValue(isChecked);
    };
    private void showDimensionsToast(int width, int height) {
        String message = "The width = " + width + " and height = " + height;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
