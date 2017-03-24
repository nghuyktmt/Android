package com.example.huynguyen.note_customlistview;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDatabase myDB=null;
    public static String DATABASE_NAME = "MyFirstDB.db";
    public static String TABLE_NAME = "MyFirstTable";

    EditText edtTitle;
    ImageButton btnAdd;

    ListView lvNote;
    ArrayList<Note>dsNote;
    NoteAdapter adapterNote;

    public static  boolean afterdel= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDB();
        createTable();

        addControls();
        loadAllTitle();
        addEvents();
    }

    //Xử lý các sự kiện
    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title,content,time;
                title = edtTitle.getText().toString();
                content = "";
                time = "";
                if(title.equalsIgnoreCase("")) {
                    title = "NoTitle " + String.valueOf(dsNote.size());
                    Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                }
                InsertNote(title,content,time);
                edtTitle.setText("");
            }
        });

    }

    //Ánh xạ
    public void addControls() {
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);

        lvNote = (ListView) findViewById(R.id.lvNote);
        dsNote = new ArrayList<>();
        adapterNote = new NoteAdapter(MainActivity.this,
                R.layout.item_note,
                dsNote);
        lvNote.setAdapter(adapterNote);
    }

    //Main ACtivity được khởi chạy hoặc từ 1 ACtivity khác quay về thì sẽ chạy hàm này
    @Override
    protected void onResume() {
        super.onResume();
        loadAllTitle();
        Toast.makeText(this,"onResume: LoadAllTitle",Toast.LENGTH_SHORT).show();
    }

    // Activity mới đã đóng, ta dùng phương thức onActivityResult() để đọc giá trị trả về
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String oldtitle_r="", title_r = "", detail_r="", time_r="";
        //Kiểm tra có đúng requestCode và resultCode hay không
        if (resultCode == RESULT_OK && requestCode == 13) {
            if (data.hasExtra("OldTitleR"))
                oldtitle_r = data.getExtras().getString("OldTitleR");
            if (data.hasExtra("TitleNameR"))
                title_r = data.getExtras().getString("TitleNameR");
            if (data.hasExtra("DetailR"))
                detail_r = data.getExtras().getString("DetailR");
            if (data.hasExtra("TimeR"))
                time_r = data.getExtras().getString("TimeR");

            updateTable(oldtitle_r, title_r, detail_r,time_r);
            loadAllTitle();
            adapterNote.notifyDataSetChanged();
            Toast.makeText(this, oldtitle_r + " + " + title_r + " + " + detail_r, Toast.LENGTH_SHORT).show();
        }
    }

    //Tạo DataBase
    public void createDB () {
        myDB = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
    }

    //Xử lý tạo bảng
    public void createTable()
    {
        String sql="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +" (";
        //String sql="CREATE TABLE IF NOT EXISTS MyFirstTable (";
        sql+=" Title TEXT primary key, ";
        sql += "Detail TEXT, ";
        sql += "Time TEXT);";
        myDB.execSQL(sql);
    }

    //Xử lý load lại dữ liệu listView
    public void loadAllTitle()
    {
        dsNote.clear();
        Cursor csr = myDB.query(TABLE_NAME, null, null, null, null, null, null);
        csr.moveToFirst();

        String datatitle="";
        String datacontent="";
        String datatime="";
        while(csr.isAfterLast()==false)
        {
            datatitle = csr.getString(0);
            datacontent = csr.getString(1);
            datatime = csr.getString(2);
            Note note = new Note();
            note.setTitle(datatitle);
            note.setContent(datacontent);
            note.setTime(datatime);
            dsNote.add(note);

            csr.moveToNext();
        }
        csr.close();
        adapterNote.notifyDataSetChanged();
    }

    //Xử lý lấy dữ liệu Note - hàm này chưa dùng tới
    /*public String getContent(String ttl) {
        Cursor csr = myDB.rawQuery("SELECT * FROM "
                + TABLE_NAME +" WHERE title = \"" + ttl + "\"", null);

        if (csr.moveToFirst() == false)
        {
            return null;
        }
        String data = csr.getString(1);
        csr.close();
        return data;
    }*/

    //Xử lý thêm Note vào DataBase
    public void InsertNote(String title, String content, String time)
    {
        ContentValues values=new ContentValues();
        values.put("Title", title);
        values.put("Detail", content);
        values.put("Time", time);


        String msg="";
        if(myDB.insert(TABLE_NAME, null, values)==-1){
            //msg="Failed to insert record";
            msg="please insert another title name.";
        }
        else{
            msg="insert title name is successful";
            //adapter.add(editText.getText().toString());
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            note.setTime(time);
            dsNote.add(note);
            adapterNote.notifyDataSetChanged();
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //Xử lý chỉnh sửa Note và update vào dataBase
    public void updateTable(String oldTitle, String title, String detail, String time)
    {
        ContentValues values = new ContentValues();
        if(!oldTitle.equalsIgnoreCase(title))
            values.put("Title",title);

        values.put("Detail",detail);
        values.put("Time",time);

        String[] arg = new String[] {oldTitle};
        String msg="";
        if (myDB.update(TABLE_NAME, values, "Title = ?", arg) == 0){
            msg="Failed to update record";
        }
        else{
            msg= title + " : " + detail;
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //Xử lý xóa Note
    public  void deleteNote(String title)
    {
        String msg = "";
        if(myDB.delete(TABLE_NAME, " Title = \"" + title + "\"", null ) == 0)
        {
            msg="Delete is failed";
        }
        else
        {
            msg="Delete is successful";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
