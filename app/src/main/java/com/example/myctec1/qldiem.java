package com.example.myctec1;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
    SQLiteDatabase mydata;
    // hàm load
    // load khoa
    private ArrayList<String> maKhoaList = new ArrayList<>();

    private void loadKhoa() {
        dsKhoa.clear();
        maKhoaList.clear();

        Cursor c = mydata.rawQuery("SELECT makhoa, tenkhoa FROM khoa", null);

        while (c.moveToNext()) {
            maKhoaList.add(c.getString(0));
            dsKhoa.add(c.getString(1)); // hiển thị tên khoa
        }
        c.close();

        adapterKhoa = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dsKhoa);
        adapterKhoa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        cbb_dkhoa.setAdapter(adapterKhoa);
    }
    //load lop
    private ArrayList<String> maLopList = new ArrayList<>();

    private void loadLop(String makhoa) {
        dsLop.clear();
        maLopList.clear();

        Cursor c = mydata.rawQuery(
                "SELECT malop, tenlop FROM lop WHERE makhoa = ?",
                new String[]{makhoa});

        while (c.moveToNext()) {
            maLopList.add(c.getString(0));
            dsLop.add(c.getString(1));
        }
        c.close();

        adapterLop = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dsLop);
        adapterLop.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        cbb_dlop.setAdapter(adapterLop);
    }
// load sv
private ArrayList<String> maSvList = new ArrayList<>();

    private void loadSinhVien(String malop) {
        dsSv.clear();
        maSvList.clear();

        Cursor c = mydata.rawQuery(
                "SELECT masv, tensv FROM sinhvien WHERE malop = ?",
                new String[]{malop});

        while (c.moveToNext()) {
            maSvList.add(c.getString(0));
            dsSv.add(c.getString(1));
        }
        c.close();

        adapterSv = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dsSv);
        adapterSv.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        cbb_dsv.setAdapter(adapterSv);
    }
// load mon
private ArrayList<String> maMonList = new ArrayList<>();

    private void loadMon(String makhoa) {
        dsMon.clear();
        maMonList.clear();

        Cursor c = mydata.rawQuery(
                "SELECT mamon, tenmon FROM monhoc WHERE makhoa = ?",
                new String[]{makhoa});

        while (c.moveToNext()) {
            maMonList.add(c.getString(0));
            dsMon.add(c.getString(1));
        }
        c.close();

        adapterMon = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dsMon);
        adapterMon.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        cbb_dmon.setAdapter(adapterMon);
    }
// load nam
private void loadNam(String mamon) {
    dsNam.clear();

    Cursor c = mydata.rawQuery(
            "SELECT DISTINCT namhoc FROM monhoc WHERE mamon = ?",
            new String[]{mamon});

    while (c.moveToNext()) {
        dsNam.add(c.getString(0));
    }
    c.close();

    adapterNam = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, dsNam);
    adapterNam.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item);

    cbb_dnam.setAdapter(adapterNam);
}

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
        mydata=openOrCreateDatabase("qlsv1.db",MODE_PRIVATE,null);
        loadKhoa();

        cbb_dkhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String makhoa = maKhoaList.get(position);

                loadLop(makhoa);
                loadMon(makhoa);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        cbb_dlop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String malop = maLopList.get(position);
                loadSinhVien(malop);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        cbb_dmon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String mamon = maMonList.get(position);
                loadNam(mamon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
// set các nút
        // nút trở về
        btn_dtrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(qldiem.this);
                builder.setTitle("QUAY VỀ ?");
                builder.setMessage("Bạn có muốn quay về trang chủ không?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // thoát chương trình
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // quay lại chương trình
                    }
                });

                builder.show();
            }
        });
    }
}