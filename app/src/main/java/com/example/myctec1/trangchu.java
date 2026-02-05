package com.example.myctec1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class trangchu extends AppCompatActivity {
// khai báo biến
    Button btn_ql,btn_tk,btn_thoat,btn_mon,btn_diem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trangchu2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // gắn id
        btn_ql=findViewById(R.id.btn_ql);
        btn_tk=findViewById(R.id.btn_tk);
        btn_thoat=findViewById(R.id.btn_thoat);
        btn_mon = findViewById(R.id.btn_mon);
        btn_diem = findViewById(R.id.btn_diem);
        // code chuyển hướng cho trang chủ
        btn_thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(trangchu.this);
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
        // nút ql môn
        btn_mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trangchu.this, qlmon.class);
                startActivity(intent);
            }
        });
        // nút khác
       btn_ql.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
            // chuyển qua giao diện quản lý
               Intent intent = new Intent(trangchu.this, quanly.class);
               startActivity(intent);
               // thử commit
           }
       });
    }
}