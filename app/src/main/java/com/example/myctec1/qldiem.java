package com.example.myctec1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
            String masv = c.getString(0);
            String tensv = c.getString(1);
            maSvList.add(masv);
            dsSv.add(masv + " - " + tensv);
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
                "SELECT mamon, tenmon, namhoc FROM monhoc WHERE makhoa = ?",
                new String[]{makhoa});

        while (c.moveToNext()) {
            String mamon = c.getString(0);
            String tenmon = c.getString(1);
            String namhoc = c.getString(2);
            maMonList.add(mamon);
            dsMon.add(mamon + " - " + tenmon + "-"+ namhoc);
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
// tính điểm trung bình
private void tinhDiemTrungBinh() {
    String d1 = edt_diem1.getText().toString().trim();
    String d2 = edt_diem2.getText().toString().trim();
    String d3 = edt_diem3.getText().toString().trim();

    if (!d1.isEmpty() && !d2.isEmpty() && !d3.isEmpty()) {
        try {
            double diem1 = Double.parseDouble(d1);
            double diem2 = Double.parseDouble(d2);
            double diem3 = Double.parseDouble(d3);

            double dtb = (diem1 + diem2 + (diem3 * 2)) / 4;

            tv_dtb.setText(String.format("%.2f", dtb));
        } catch (NumberFormatException e) {
            tv_dtb.setText("");
        }
    } else {
        tv_dtb.setText("");
    }
}
// load điểm sinh viên
private void loadDiemSinhVien(String masv, String namhoc) {

    dsDiem.clear();

    Cursor c = mydata.rawQuery(
            "SELECT mamon, diem_1, diem_2, diem_3, diem_tb " +
                    "FROM diem WHERE masv = ? AND namhoc = ?",
            new String[]{masv, namhoc});

    while (c.moveToNext()) {

        String mamon = c.getString(0);
        String d1 = c.getString(1);
        String d2 = c.getString(2);
        String d3 = c.getString(3);
        String dtb = c.getString(4);

        dsDiem.add(
                mamon + " - " +
                        d1 + " - " +
                        d2 + " - " +
                        d3 + " - " +
                        dtb
        );
    }

    c.close();

    adapterDiem = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            dsDiem
    );

    lv2.setAdapter(adapterDiem);
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
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tinhDiemTrungBinh();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        edt_diem1.addTextChangedListener(watcher);
        edt_diem2.addTextChangedListener(watcher);
        edt_diem3.addTextChangedListener(watcher);

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
        // cbb sinh viên load điểm sinh viên đó
        cbb_dsv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String masv = maSvList.get(position);

                if (cbb_dnam.getSelectedItem() != null) {
                    String namhoc = cbb_dnam.getSelectedItem().toString();
                    loadDiemSinhVien(masv, namhoc);
                }
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
        btn_dmoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_diem1.setText("");
                edt_diem2.setText("");
                edt_diem3.setText("");
                tv_dtb.setText("0");
                Toast.makeText(qldiem.this, "Đã làm sạch thông tin có thể nhập mới", Toast.LENGTH_SHORT).show();
            }
        });


        // nút lưu
        btn_dluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // gán giá trị cho biến
                // Lấy mã từ Spinner (dùng list mã đã lưu)

                String masv = maSvList.get(cbb_dsv.getSelectedItemPosition());
                String mamon = maMonList.get(cbb_dmon.getSelectedItemPosition());
                String namhoc = cbb_dnam.getSelectedItem().toString();

                // Lấy điểm từ EditText
                String d1 = edt_diem1.getText().toString().trim();
                String d2 = edt_diem2.getText().toString().trim();
                String d3 = edt_diem3.getText().toString().trim();

                // Lấy điểm trung bình từ TextView
                String dtb = tv_dtb.getText().toString().trim();

                // Kiểm tra rỗng
                if (d1.isEmpty() || d2.isEmpty() || d3.isEmpty()) {
                    Toast.makeText(qldiem.this,
                            "Vui lòng nhập đầy đủ điểm!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    double diem1 = Double.parseDouble(d1);
                    double diem2 = Double.parseDouble(d2);
                    double diem3 = Double.parseDouble(d3);

                    double dtbValue = (diem1 + diem2 + (diem3 * 2)) / 4;

                    ContentValues values = new ContentValues();
                    values.put("masv", masv);
                    values.put("mamon", mamon);
                    values.put("namhoc", namhoc);
                    values.put("diem_1", diem1);
                    values.put("diem_2", diem2);
                    values.put("diem_3", diem3);
                    values.put("diem_tb", dtbValue);

                    long result = mydata.insert("diem", null, values);

                    if (result == -1) {
                        Toast.makeText(qldiem.this,
                                "Điểm đã tồn tại!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        loadDiemSinhVien(masv, namhoc);
                        Toast.makeText(qldiem.this,
                                "Lưu thành công!",
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {

                    Toast.makeText(qldiem.this,
                            "Điểm phải là số hợp lệ!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        // nhấn lên list view hiển thị thêm thông tin
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String dong = dsDiem.get(position);

                // tách dữ liệu
                String[] data = dong.split(" - ");

                String mamon = data[0];
                String d1 = data[1];
                String d2 = data[2];
                String d3 = data[3];
                String dtb = data[4];

                // đổ lên EditText
                edt_diem1.setText(d1);
                edt_diem2.setText(d2);
                edt_diem3.setText(d3);
                tv_dtb.setText(dtb);

                // set Spinner môn
                int indexMon = maMonList.indexOf(mamon);
                if (indexMon != -1) {
                    cbb_dmon.setSelection(indexMon);
                }

                Toast.makeText(qldiem.this,
                        "Đã chọn để chỉnh sửa",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // cập nhật điểm
        btn_dsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Lấy mã từ Spinner
                String masv = maSvList.get(cbb_dsv.getSelectedItemPosition());
                String mamon = maMonList.get(cbb_dmon.getSelectedItemPosition());
                String namhoc = cbb_dnam.getSelectedItem().toString();

                // Lấy điểm từ EditText
                String d1 = edt_diem1.getText().toString().trim();
                String d2 = edt_diem2.getText().toString().trim();
                String d3 = edt_diem3.getText().toString().trim();

                if (d1.isEmpty() || d2.isEmpty() || d3.isEmpty()) {
                    Toast.makeText(qldiem.this,
                            "Vui lòng nhập đầy đủ điểm!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    double diem1 = Double.parseDouble(d1);
                    double diem2 = Double.parseDouble(d2);
                    double diem3 = Double.parseDouble(d3);

                    double dtbValue = (diem1 + diem2 + (diem3 * 2)) / 4;

                    ContentValues values = new ContentValues();
                    values.put("diem_1", diem1);
                    values.put("diem_2", diem2);
                    values.put("diem_3", diem3);
                    values.put("diem_tb", dtbValue);

                    int result = mydata.update(
                            "diem",
                            values,
                            "masv = ? AND mamon = ? AND namhoc = ?",
                            new String[]{masv, mamon, namhoc}
                    );

                    if (result > 0) {

                        loadDiemSinhVien(masv, namhoc);

                        Toast.makeText(qldiem.this,
                                "Cập nhật thành công!",
                                Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(qldiem.this,
                                "Không tìm thấy dữ liệu để cập nhật!",
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {

                    Toast.makeText(qldiem.this,
                            "Điểm phải là số hợp lệ!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}