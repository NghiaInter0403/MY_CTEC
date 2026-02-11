package com.example.myctec1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class qlmon extends AppCompatActivity {
EditText edt_mamon,edt_tenmon, edt_tin,edt_nam;
Spinner cbb_monkhoa;
Button btn_moi1, btn_luu1, btn_sua1, btn_xoa1, btn_quaylai;
ListView lv1;
    ArrayList<String> dsMon = new ArrayList<>();
    ArrayAdapter<String> adapterMon;

SQLiteDatabase mydata;
    private void loadMon() {
        dsMon.clear();

        Cursor c = mydata.rawQuery(
                "SELECT mamon,tenmon,sotinchi,namhoc,makhoa FROM monhoc",
                null
        );

        while (c.moveToNext()) {
            String dong =
                    c.getString(0) + " - " +
                            c.getString(1) + " - " +
                            c.getString(2) + " - " +
                            c.getString(3) + " - " +
                            c.getString(4);
            dsMon.add(dong);
        }

        c.close();
        adapterMon.notifyDataSetChanged();
    }
    // -----------

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
        adapterMon = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dsMon
        );
        lv1.setAdapter(adapterMon);
        mydata=openOrCreateDatabase("qlsv1.db",MODE_PRIVATE,null);
        // spiner chọn khoa
        // gán cho spiner mà quen gọi cbb hehe
        // load dữ liệu cho cbb khoa

        ArrayList<String> tenKhoaList = new ArrayList<>();
        ArrayList<String> maKhoaList = new ArrayList<>();

        Cursor c = mydata.rawQuery("SELECT makhoa, tenkhoa FROM khoa", null);
        while (c.moveToNext()) {
            maKhoaList.add(c.getString(0));
            tenKhoaList.add(c.getString(1));
        }
        c.close();

        ArrayAdapter<String> adapterKhoa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenKhoaList);

        adapterKhoa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        cbb_monkhoa.setAdapter(adapterKhoa);

// nút mới
        btn_moi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_mamon.setText("");
                edt_tenmon.setText("");
                edt_tin.setText("");
                edt_nam.setText("");
                edt_mamon.requestFocus();
                loadMon();
                Toast.makeText(qlmon.this, "Đã làm sạch thông tin bạn có thể nhập mới",Toast.LENGTH_SHORT).show();
            }
        });
        // nút lưu
    }
}