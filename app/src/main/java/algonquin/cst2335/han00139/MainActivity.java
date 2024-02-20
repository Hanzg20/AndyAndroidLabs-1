package algonquin.cst2335.han00139;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity class represents the main activity of the password checker app.
 */
public class MainActivity extends AppCompatActivity {

    /** Holds the text view at the center of the screen. */
    private TextView tv;

    /** Holds the edit text at the center of the screen. */
    private EditText et;

    /** Holds the button at the center of the screen. */
    private Button btn;

    /**
     * Initializes the activity layout and views .
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *     then this Bundle contains the  data it most recently supplied in {@link #onSaveInstanceState}.
     *     Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(view -> {
            String password = et.getText().toString();
            boolean isComplex = checkPasswordComplexity(password);

            if (isComplex) {
                tv.setText(R.string.password_meets_requirements);
            } else {
                tv.setText(R.string.password_does_not_meet_requirements);
                showToast();
            }
        });
    }

    /**
     * Checks if the provided password meets complexity requirements.
     *
     * @param password The password to be checked.
     * @return true if the password meets the requirements, false otherwise.
     */
    private boolean checkPasswordComplexity(String password) {
        boolean hasUpperCase = !password.equals(password.toLowerCase());
        boolean hasLowerCase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = containsSpecialCharacter(password);

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    /**
     * Displays a Toast message indicating that the password is not complex enough.
     */
    private void showToast() {
        Toast.makeText(this, R.string.password_not_complex_enough, Toast.LENGTH_SHORT).show();
    }

    /**
     * Checks if the given password contains at least one special character.
     *
     * @param password The password to be checked
     * @return true if the password contains a special character, false otherwise
     */
    private boolean containsSpecialCharacter(String password) {
        for (char c : password.toCharArray()) {
            if (isSpecialCharacter(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the provided character is a special character.
     *
     * @param c The character to be checked.
     * @return true if the character is a special character, false otherwise.
     */
    private boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
            case '\'':
            case '\"':
                return true;
            default:
                return false;
        }
    }
}
