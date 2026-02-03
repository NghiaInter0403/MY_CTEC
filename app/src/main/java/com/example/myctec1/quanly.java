package com.example.myctec1;

import android.app.DatePickerDialog;
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
    Button btn_moi, btn_luu, btn_sua, btn_xoa, btn_tc;
    SQLiteDatabase mydata;
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
        // gọi data
        mydata=openOrCreateDatabase("qlsv1.db",MODE_PRIVATE,null);
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

                Cursor c = mydata.rawQuery(
                        "SELECT tenlop FROM lop WHERE makhoa = ?",
                        new String[]{makhoa}
                );

                while (c.moveToNext()) {
                    tenLopList.add(c.getString(0));
                }
                c.close();

                ArrayAdapter<String> adapterLop =
                        new ArrayAdapter<>(quanly.this, android.R.layout.simple_spinner_item, tenLopList);
                adapterLop.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                cbb_lop.setAdapter(adapterLop);
            }

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
                Toast.makeText(quanly.this, "Đã làm sạch thông tin bạn có thể nhập mới",Toast.LENGTH_SHORT).show();
            }
        });
        // nút lưu
        btn_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gán text cho biến
                String ma = edt_ma.getText().toString().trim();
                String ten = edt_ten.getText().toString().trim();
                String ngay = edt_ngay.getText().toString().trim();
                String sdt = edt_sdt.getText().toString().trim();

                // kiểm tra trống
                if(ma.isEmpty()||ten.isEmpty()||ngay.isEmpty()||sdt.isEmpty()){
                   Toast.makeText(quanly.this,"Vui lòng nhập đầy đủ",Toast.LENGTH_SHORT).show();
                }
                else{
                    // kiểm tra trùng
                }
            }
        });
    //--------------------------------- ngăn cách hehe
    }
}