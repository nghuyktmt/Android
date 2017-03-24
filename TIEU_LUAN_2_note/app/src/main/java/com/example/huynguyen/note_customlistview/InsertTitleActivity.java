package com.example.huynguyen.note_customlistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.huynguyen.note_customlistview.MainActivity.TABLE_NAME;

public class InsertTitleActivity extends AppCompatActivity {

    EditText edtTitle, edtDetail;
    String title, detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_title);

        //Ánh xạ
        edtTitle = (EditText)findViewById(R.id.edtTitle);
        edtDetail = (EditText)findViewById(R.id.edtDetail);

        //dùng đối tượng Bundle để đóng gói dữ liệu để truyền tải qua các Activity khác.
        //Ở bên nhận Bundle thì dùng các phương thức getXXX tương ứng để lấy dữ liệu theo key bên gửi.
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        title = bundle.getString("TitleName");
        detail = bundle.getString("Detail");
        edtTitle.setText(title);
        edtDetail.setText(detail);

    }


    //Hàm onClick khi nhấn nút ImageButton Xóa
    public void delete(View view) {
        String msg = "";
        if(MainActivity.myDB.delete(TABLE_NAME," Title = \"" + title + "\"", null)==0){
            msg="Delete is failed";
        }
        else
        {
            msg="Delete "+title+" is successful";
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    //Hàm onclick khi nhấn nút ImageButton Save
    public void save(View view) {

        Date date = new Date();
        String strDate = "hh:mm:ss a dd/MM/yyyy";
        SimpleDateFormat sdfdate = new SimpleDateFormat(strDate);
        String Time = sdfdate.format(date);

        //Khởi tạo Intent
        Intent data = new Intent();
        //Đính kèm giá trị gửi với key tương ứng
        data.putExtra("OldTitleR",title);
        data.putExtra("TitleNameR",edtTitle.getText().toString());
        data.putExtra("DetailR",edtDetail.getText().toString());
        data.putExtra("TimeR",Time);
        //Trả về resultCode là RESULT_OK
        setResult(RESULT_OK,data);
        //Đóng Activity
        finish();
    }
}
