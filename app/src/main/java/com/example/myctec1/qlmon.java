package com.example.myctec1;

import android.content.ContentValues;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
        // nút quay về
        btn_quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(qlmon.this);
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
        // nút lưu
        btn_luu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gán giá trị cho biến
                String mamon = edt_mamon.getText().toString().trim();
                String tenmon = edt_tenmon.getText().toString().trim();
                String tinchi = edt_tin.getText().toString().trim();
                String nam = edt_nam.getText().toString().trim();
                int vitriKhoa = cbb_monkhoa.getSelectedItemPosition();
                String khoa = maKhoaList.get(vitriKhoa);
                if (mamon.isEmpty() || tenmon.isEmpty() || tinchi.isEmpty() || nam.isEmpty() || khoa.isEmpty()) {
                    Toast.makeText(qlmon.this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = mydata;

                    Cursor cursor = db.rawQuery(
                            "SELECT mamon FROM monhoc WHERE mamon = ?",
                            new String[]{mamon}
                    );

                    if (cursor.getCount() > 0) {
                        edt_mamon.setError("Mã sinh viên đã tồn tại");
                        edt_mamon.requestFocus();
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("mamon", mamon);
                        values.put("tenmon", tenmon);
                        values.put("sotinchi",tinchi);
                        values.put("namhoc",nam);
                        values.put("makhoa",khoa);
                        db.insert("monhoc", null, values);

                        Toast.makeText(qlmon.this,
                                "Lưu thành công", Toast.LENGTH_SHORT).show();

                        loadMon();
                    }
                    cursor.close();
                }
            }
        });
        // nhấn vô list view nó hiện thông tin lên ô nhập
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String dong = dsMon.get(position);

                // tách dữ liệu
                String[] data = dong.split(" - ");

                String mamon = data[0];
                String tenmon = data[1];
                String tinchi = data[2];
                String nam = data[3];
                String khoa = data[4];

                // đổ lên EditText
                edt_mamon.setText(mamon);
                edt_tenmon.setText(tenmon);
                edt_tin.setText(tinchi);
                edt_nam.setText(nam);
                // set Spinner khoa
                int indexKhoa = maKhoaList.indexOf(khoa);
                if (indexKhoa != -1) {
                    cbb_monkhoa.setSelection(indexKhoa);
                }
            }
        });
        // nút sửa
        btn_sua1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mamon = edt_mamon.getText().toString().trim();
                String tenmon = edt_tenmon.getText().toString().trim();
                String tinchi = edt_tin.getText().toString().trim();
                String nam = edt_nam.getText().toString().trim();
                int viTriKhoa = cbb_monkhoa.getSelectedItemPosition();
                if (viTriKhoa == -1) {
                    Toast.makeText(qlmon.this, "Chưa chọn khoa hoặc lớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                String makhoa = maKhoaList.get(viTriKhoa);
                // kiểm tra trống
                if (mamon.isEmpty() || tenmon.isEmpty() || tinchi.isEmpty() || nam.isEmpty()) {
                    Toast.makeText(qlmon.this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                    return;
                }
                SQLiteDatabase db = mydata;
                // kiểm tra tồn tại
                Cursor cursor = db.rawQuery(
                        "SELECT mamon FROM monhoc WHERE mamon = ?",
                        new String[]{mamon}
                );

                if (!cursor.moveToFirst()) {
                    Toast.makeText(qlmon.this, "Không tìm thấy môn để sửa", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor.close();

                // ===== UPDATE =====
                ContentValues values = new ContentValues();
                values.put("tensv", tenmon);
                values.put("sotinchi",tinchi);
                values.put("namhoc",nam);
                values.put("makhoa",makhoa);
                int row = db.update(
                        "monhoc",
                        values,
                        "mamon = ?",
                        new String[]{mamon}
                );

                if (row > 0) {
                    Toast.makeText(qlmon.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadMon();
                } else {
                    Toast.makeText(qlmon.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}