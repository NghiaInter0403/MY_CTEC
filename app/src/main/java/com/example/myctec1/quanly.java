package com.example.myctec1;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class quanly extends AppCompatActivity {
// khai báo biến
    EditText edt_ma, edt_ten, edt_ngay, edt_sdt;
    ListView lv;
    Spinner cbb_khoa, cbb_lop;
    Button btn_moi, btn_luu, btn_sua, btn_xoa, btn_tc,btn_tim, btn_nhapd;
    SQLiteDatabase mydata;
    ArrayList<String> maLopList = new ArrayList<>();
    ArrayList<String> dsSinhVien = new ArrayList<>();
    ArrayAdapter<String> adapterSV;
    // haàm load lên list view
    private void loadSinhVien() {
        dsSinhVien.clear();

        Cursor c = mydata.rawQuery(
                "SELECT masv, tensv, malop, makhoa, ngaysinh, so_dt FROM sinhvien",
                null
        );

        while (c.moveToNext()) {
            String dong =
                    c.getString(0) + " - " +
                            c.getString(1) + " - " +
                            c.getString(2) + " - " +
                            c.getString(3) + " - " +
                            c.getString(4) + " - " +
                            c.getString(5);
            dsSinhVien.add(dong);
        }

        c.close();
        adapterSV.notifyDataSetChanged();
    }
    private void timSinhVienTheoMa(String masv) {

        dsSinhVien.clear();

        Cursor c = mydata.rawQuery(
                "SELECT masv, tensv, malop, makhoa, ngaysinh, so_dt FROM sinhvien WHERE masv = ?",
                new String[]{masv}
        );

        if (c.moveToFirst()) {
            do {
                String dong =
                        c.getString(0) + " - " +
                                c.getString(1) + " - " +
                                c.getString(2) + " - " +
                                c.getString(3) + " - " +
                                c.getString(4) + " - " +
                                c.getString(5);

                dsSinhVien.add(dong);
            } while (c.moveToNext());
        } else {
            Toast.makeText(this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
        }

        c.close();
        adapterSV.notifyDataSetChanged();
    }


//-===========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanly);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        //gan s biến cho giao diện
        edt_ma=findViewById(R.id.edt_ma);
        edt_ten=findViewById(R.id.edt_ten);
        edt_ngay = findViewById(R.id.edt_ngay);
        edt_sdt = findViewById(R.id.edt_sdt);
        // spiner
        cbb_khoa = findViewById(R.id.cbb_khoa);
        cbb_lop = findViewById(R.id.cbb_lop);
        // listview
        lv = findViewById(R.id.lv);
        // Button
        btn_moi = findViewById(R.id.btn_moi);
        btn_luu = findViewById(R.id.btn_luu);
        btn_sua = findViewById(R.id.btn_sua);
        btn_xoa = findViewById(R.id.btn_xoa);
        btn_tc = findViewById(R.id.btn_tc);
        btn_tim=findViewById(R.id.btn_tim);
        btn_nhapd = findViewById(R.id.btn_nhapd);
        // khởi tạo list view
        adapterSV = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dsSinhVien
        );
        lv.setAdapter(adapterSV);
        // gọi data
        mydata=openOrCreateDatabase("qlsv1.db",MODE_PRIVATE,null);
        loadSinhVien();
        // hàm chọn ngày cô liên gửi
        edt_ngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int D = calendar.get(Calendar.DAY_OF_MONTH);
                int M = calendar.get(Calendar.MONTH);
                int Y = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(quanly.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        if((Y-i)>18){
                            calendar.set(i,i1,i2);
                            edt_ngay.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                },Y,M,D);
                datePickerDialog.show();
            }
        });

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

        cbb_khoa.setAdapter(adapterKhoa);

        // chọn khoa thì load lớp
        cbb_khoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String makhoa = maKhoaList.get(position);
                loadLop(makhoa);
            }
            //load lớp theo mã khoa
            private void loadLop(String makhoa) {

                ArrayList<String> tenLopList = new ArrayList<>();
                maLopList.clear(); // QUAN TRỌNG

                Cursor c = mydata.rawQuery(
                        "SELECT malop, tenlop FROM lop WHERE makhoa = ?",
                        new String[]{makhoa}
                );

                while (c.moveToNext()) {
                    maLopList.add(c.getString(0));   // mã lớp
                    tenLopList.add(c.getString(1));  // tên lớp
                }
                c.close();

                ArrayAdapter<String> adapterLop =
                        new ArrayAdapter<>(quanly.this,
                                android.R.layout.simple_spinner_item, tenLopList);
                adapterLop.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                cbb_lop.setAdapter(adapterLop);
            }
            // ===== HÀM LOAD DỮ LIỆU LÊN LISTVIEW =====


//------------------------------------------------------------------------------

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // gán sự kiện vào các nút
        // gán vô nút trang chủ
        btn_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(quanly.this);
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
        // nút mới
        btn_moi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_ma.setText("");
                edt_ten.setText("");
                edt_ngay.setText("");
                edt_sdt.setText("");
                edt_ma.requestFocus();
                loadSinhVien();
                Toast.makeText(quanly.this, "Đã làm sạch thông tin bạn có thể nhập mới",Toast.LENGTH_SHORT).show();
            }
        });
        // nút lưu
        btn_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // gán text cho biến
                String masv = edt_ma.getText().toString().trim();
                String tensv = edt_ten.getText().toString().trim();
                String ngaysinh = edt_ngay.getText().toString().trim();
                String sdt = edt_sdt.getText().toString().trim();
                int viTriKhoa = cbb_khoa.getSelectedItemPosition();
                int viTriLop = cbb_lop.getSelectedItemPosition();

                String makhoa = maKhoaList.get(viTriKhoa);
                String malop = maLopList.get(viTriLop);

                // kiểm tra trống
                if(masv.isEmpty()||tensv.isEmpty()||ngaysinh.isEmpty()||sdt.isEmpty()){
                   Toast.makeText(quanly.this,"Vui lòng nhập đầy đủ",Toast.LENGTH_SHORT).show();
                }
                else {
                    SQLiteDatabase db = mydata;

                    Cursor cursor = db.rawQuery(
                            "SELECT masv FROM sinhvien WHERE masv = ?",
                            new String[]{masv}
                    );

                    if (cursor.getCount() > 0) {
                        edt_ma.setError("Mã sinh viên đã tồn tại");
                        edt_ma.requestFocus();
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("masv", masv);
                        values.put("tensv", tensv);
                        values.put("makhoa", makhoa);
                        values.put("malop", malop);
                        values.put("ngaysinh", ngaysinh);
                        values.put("so_dt", sdt);

                        db.insert("sinhvien", null, values);

                        Toast.makeText(quanly.this,
                                "Lưu thành công", Toast.LENGTH_SHORT).show();

                        loadSinhVien();
                    }

                    cursor.close();
                }

            }
        });
        // nhấn vô listview thì hiển thị thông tin đó lên các ô nhập liệu
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String dong = dsSinhVien.get(position);

                // tách dữ liệu
                String[] data = dong.split(" - ");

                String masv = data[0];
                String tensv = data[1];
                String malop = data[2];
                String makhoa = data[3];
                String ngaysinh = data[4];
                String sdt = data[5];

                // đổ lên EditText
                edt_ma.setText(masv);
                edt_ten.setText(tensv);
                edt_ngay.setText(ngaysinh);
                edt_sdt.setText(sdt);

                // set Spinner khoa
                int indexKhoa = maKhoaList.indexOf(makhoa);
                if (indexKhoa != -1) {
                    cbb_khoa.setSelection(indexKhoa);
                }

                // ⚠️ spinner lớp phải đợi load xong
                cbb_khoa.postDelayed(() -> {
                    int indexLop = maLopList.indexOf(malop);
                    if (indexLop != -1) {
                        cbb_lop.setSelection(indexLop);
                    }
                }, 200);
            }
        });
        // nút sửa
        btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String masv = edt_ma.getText().toString().trim();
                String tensv = edt_ten.getText().toString().trim();
                String ngaysinh = edt_ngay.getText().toString().trim();
                String sdt = edt_sdt.getText().toString().trim();

                int viTriKhoa = cbb_khoa.getSelectedItemPosition();
                int viTriLop = cbb_lop.getSelectedItemPosition();

                if (viTriKhoa == -1 || viTriLop == -1) {
                    Toast.makeText(quanly.this, "Chưa chọn khoa hoặc lớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                String makhoa = maKhoaList.get(viTriKhoa);
                String malop = maLopList.get(viTriLop);

                // kiểm tra trống
                if (masv.isEmpty() || tensv.isEmpty() || ngaysinh.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(quanly.this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = mydata;

                // kiểm tra tồn tại
                Cursor cursor = db.rawQuery(
                        "SELECT masv FROM sinhvien WHERE masv = ?",
                        new String[]{masv}
                );

                if (!cursor.moveToFirst()) {
                    Toast.makeText(quanly.this, "Không tìm thấy sinh viên để sửa", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor.close();

                // ===== UPDATE =====
                ContentValues values = new ContentValues();
                values.put("tensv", tensv);
                values.put("makhoa", makhoa);
                values.put("malop", malop);
                values.put("ngaysinh", ngaysinh);
                values.put("so_dt", sdt);

                int row = db.update(
                        "sinhvien",
                        values,
                        "masv = ?",
                        new String[]{masv}
                );

                if (row > 0) {
                    Toast.makeText(quanly.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadSinhVien();
                } else {
                    Toast.makeText(quanly.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
// nút xóa
        btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String masv = edt_ma.getText().toString().trim();

                if (masv.isEmpty()) {
                    Toast.makeText(quanly.this, "Vui lòng chọn sinh viên cần xóa", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(quanly.this);
                builder.setTitle("XÁC NHẬN XÓA");
                builder.setMessage("Bạn có chắc chắn muốn xóa sinh viên này không?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLiteDatabase db = mydata;

                        int row = db.delete(
                                "sinhvien",
                                "masv = ?",
                                new String[]{masv}
                        );

                        if (row > 0) {
                            Toast.makeText(quanly.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                            // làm sạch form
                            edt_ma.setText("");
                            edt_ten.setText("");
                            edt_ngay.setText("");
                            edt_sdt.setText("");

                            loadSinhVien();
                        } else {
                            Toast.makeText(quanly.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        //nút tìm

        btn_tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(quanly.this);
                builder.setTitle("Tìm sinh viên");

                final EditText edtTim = new EditText(quanly.this);
                edtTim.setHint("Nhập mã sinh viên");
                builder.setView(edtTim);

                builder.setPositiveButton("Tìm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String masv = edtTim.getText().toString().trim();

                        if (masv.isEmpty()) {
                            Toast.makeText(quanly.this, "Chưa nhập mã sinh viên", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        timSinhVienTheoMa(masv);
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        //--------------------------------- ngăn cách hehe
    }

}