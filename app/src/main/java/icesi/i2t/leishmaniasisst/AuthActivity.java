package icesi.i2t.leishmaniasisst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {

    EditText superpassword;
    Button submit_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        superpassword = findViewById(R.id.superpassword);
        submit_auth = findViewById(R.id.submit_auth);

        submit_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = superpassword.getText().toString().trim();
                if(pass.equals("cideim2019")){
                    Intent i = new Intent(AuthActivity.this, Planner.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(AuthActivity.this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
