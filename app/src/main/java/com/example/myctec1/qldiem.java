package com.example.myctec1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class qldiem extends AppCompatActivity {
TextView tv_dtb;
EditText edt_diem1,edt_diem2,edt_diem3;
Spinner cbb_dkhoa, cbb_dlop, cbb_dsv, cbb_dmon, cbb_dnam;
Button btn_dmoi, btn_dluu, btn_dsua, btn_dxoa, btn_dtrove;
ListView lv2;
    ArrayList<String> dsKhoa = new ArrayList<>();
    ArrayAdapter<String> adapterKhoa;
    ArrayList<String> dsLop = new ArrayList<>();
    ArrayAdapter<String> adapterLop;
    ArrayList<String> dsSv = new ArrayList<>();
    ArrayAdapter<String> adapterSv;
    ArrayList<String> dsMon = new ArrayList<>();
    ArrayAdapter<String> adapterMon;
    ArrayList<String> dsNam = new ArrayList<>();
    ArrayAdapter<String> adapterNam;
    ArrayList<String> dsDiem = new ArrayList<>();
    ArrayAdapter<String> adapterDiem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qldiem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // ánh xạ
        tv_dtb = findViewById(R.id.tv_dtb);
        edt_diem1 = findViewById(R.id.edt_diem1);
        edt_diem2 = findViewById(R.id.edt_diem2);
        edt_diem3 = findViewById(R.id.edt_diem3);
        cbb_dkhoa = findViewById(R.id.cbb_dkhoa);
        cbb_dlop = findViewById(R.id.cbb_dlop);
        cbb_dsv = findViewById(R.id.cbb_dsv);
        cbb_dmon = findViewById(R.id.cbb_dmon);
        cbb_dnam = findViewById(R.id.cbb_dnam);
        btn_dmoi = findViewById(R.id.btn_dmoi);
        btn_dluu = findViewById(R.id.btn_dluu);
        btn_dsua = findViewById(R.id.btn_dsua);
        btn_dxoa = findViewById(R.id.btn_dxoa);
        btn_dtrove = findViewById(R.id.btn_dtrove);
        lv2 = findViewById(R.id.lv2);
    }
}