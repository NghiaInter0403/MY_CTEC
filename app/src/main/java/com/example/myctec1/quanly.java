package com.example.myctec1;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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
    //--------------------------------- ngăn cách hehe
    }
}