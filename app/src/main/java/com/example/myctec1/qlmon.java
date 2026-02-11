package com.example.myctec1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class qlmon extends AppCompatActivity {
EditText edt_mamon,edt_tenmon, edt_tin,edt_nam;
Spinner cbb_monkhoa;
Button btn_moi1, btn_luu1, btn_sua1, btn_xoa1, btn_quaylai;
ListView lv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qlmon);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // gán sự kiện
        edt_mamon = findViewById(R.id.edt_mamon);
        edt_tenmon = findViewById(R.id.edt_tenmon);
        edt_tin = findViewById(R.id.edt_tin);
        edt_nam = findViewById(R.id.edt_nam);
        cbb_monkhoa = findViewById(R.id.cbb_monkhoa);
        lv1 = findViewById(R.id.lv1);
        btn_moi1 = findViewById(R.id.btn_moi1);
        btn_luu1 = findViewById(R.id.btn_luu1);
        btn_sua1 = findViewById(R.id.btn_sua1);
        btn_xoa1 = findViewById(R.id.btn_xoa1);
        btn_quaylai = findViewById(R.id.btn_quaylai);
    }
}