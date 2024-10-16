package ic.ac.polihasnur.ti.haqi.tikettrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SuccsessBuyTicketAct extends AppCompatActivity {
    Button btn_to_view_tiket,btn_to_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_succsess_buy_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_to_view_tiket = findViewById(R.id.btn_to_view_tiket);
        btn_to_view_tiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goprofile = new Intent(SuccsessBuyTicketAct.this,MyProfileAct.class);
                startActivity(goprofile);
            }
        });

        btn_to_dashboard = findViewById(R.id.btn_to_dashboard);
        btn_to_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(SuccsessBuyTicketAct.this,HomeAct.class);
                startActivity(gotohome);
            }
        });
    }
}