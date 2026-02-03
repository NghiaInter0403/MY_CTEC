package com.example.myctec1;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
// khai báo biến giao diện
    EditText edt_ten, edt_mk;
    Button btn_dn, btn_thoat;
    SQLiteDatabase mydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // viết code
        edt_ten=findViewById(R.id.edt_ten);
        edt_mk=findViewById(R.id.edt_mk);
        btn_dn=findViewById(R.id.btn_dn);
        btn_thoat=findViewById(R.id.btn_thoat);
        // tạo cơ sở dữ liệu
        mydata=openOrCreateDatabase("qlsv1.db",MODE_PRIVATE,null);
        // tạo table chứa dữ liệu
        // thông báo nếu đã tồn tại
        try{
            String sql = "CREATE TABLE giaovien(" +
                    "id_gv INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ten_gv TEXT," +
                    "ten_dn TEXT UNIQUE," +
                    "matkhau TEXT,"+

                    "email TEXT," +
                    "dia_chi TEXT," +
                    "so_dt TEXT" +
                    ")";
            mydata.execSQL(sql);
            // ===== BẢNG KHOA =====
            String khoa = "CREATE TABLE IF NOT EXISTS khoa (" +
                    "id_khoa INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "makhoa TEXT UNIQUE," +
                    "tenkhoa TEXT" +
                    ")";
            mydata.execSQL(khoa);

            // ===== BẢNG LỚP =====
            String lop = "CREATE TABLE IF NOT EXISTS lop (" +
                    "id_lop INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "malop TEXT UNIQUE," +
                    "tenlop TEXT," +
                    "makhoa TEXT," +
                    "siso INTEGER," +
                    "FOREIGN KEY (makhoa) REFERENCES khoa(makhoa)" +
                    ")";
            mydata.execSQL(lop);

            // ===== BẢNG SINH VIÊN =====
            String sinhvien = "CREATE TABLE IF NOT EXISTS sinhvien (" +
                    "id_sv INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "masv TEXT UNIQUE," +
                    "tensv TEXT," +
                    "malop TEXT," +
                    "makhoa"+
                    "ngaysinh TEXT," +
                    "so_dt TEXT," +
                    "FOREIGN KEY (malop) REFERENCES lop(malop)," +
                    "FOREIGN KEY (makhoa) REFERENCES khoa(makhoa)" +
                    ")";
            mydata.execSQL(sinhvien);
            // nhập thử 1 giáo viên
            String nhapgv = "INSERT INTO giaoVien VALUES (" +
                    "null," +
                    "'Phan Hiếu Nghĩa'," +
                    "'nghia'," +
                    "'123456789'," +
                    "'nghia@gmail.com'," +
                    "'Bạc Liêu'," +
                    "'0352755926'" +
                    ")";
            mydata.execSQL(nhapgv);
            // nhập thử khoa
            mydata.execSQL(
                    "INSERT INTO khoa (makhoa, tenkhoa) VALUES ('cntt', 'Công nghệ thông tin')"
            );
            mydata.execSQL(
                    "INSERT INTO khoa (makhoa, tenkhoa) VALUES ('tckt', 'Tài chính kế toán')"
            );
            mydata.execSQL(
                    "INSERT INTO khoa (makhoa, tenkhoa) VALUES ('cnts', 'Công nghệ thủy sản')"
            );
            // nhập thử lớp
            mydata.execSQL(
                    "INSERT INTO lop (malop, tenlop, makhoa, siso) VALUES ('hcntt23', 'CNTT CLC 23', 'cntt', 20)"
            );
            mydata.execSQL(
                    "INSERT INTO lop (malop, tenlop, makhoa, siso) VALUES ('hcntt24', 'CNTT CLC 24', 'cntt', 25)"
            );
            mydata.execSQL(
                    "INSERT INTO lop (malop, tenlop, makhoa, siso) VALUES ('ccntp23', 'CNTP 23', 'cnts', 50)"
            );

        }
        catch(Exception e){
           Log.e("Error","Bảng đã tồn tại" );
        }
        // Nút đăng nhập
        btn_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tendn = edt_ten.getText().toString().trim();
                String matkhau = edt_mk.getText().toString().trim();
                //kiểm tra trống
                if(tendn.isEmpty()||matkhau.isEmpty()){
                    Toast.makeText(MainActivity.this,"Vui lòng nhập đầy đủ",Toast.LENGTH_SHORT).show();
                }
                else{
                    String sql = "SELECT * FROM giaovien WHERE ten_dn = ? AND matkhau = ?";
                    Cursor cursor = mydata.rawQuery(sql, new String[]{tendn, matkhau});

                    if (cursor.moveToFirst()) {
                        // ĐÚNG → chuyển trang
                        Intent intent = new Intent(MainActivity.this, trangchu.class);
                        startActivity(intent);
                    } else {
                        // SAI → báo lỗi
                        Toast.makeText(MainActivity.this,
                                "Sai tên đăng nhập hoặc mật khẩu. Nhập lại!",
                                Toast.LENGTH_SHORT).show();
                        edt_mk.setText("");
                        edt_ten.requestFocus();
                    }

                    cursor.close();
                }
                }
        });
        // nút thoát
        btn_thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận thoát");
                builder.setMessage("Bạn có muốn thoát chương trình không?");

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