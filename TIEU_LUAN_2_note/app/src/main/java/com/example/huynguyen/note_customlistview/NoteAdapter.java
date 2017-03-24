package com.example.huynguyen.note_customlistview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import static com.example.huynguyen.note_customlistview.MainActivity.afterdel;

/**
 * Created by HUYNGUYEN on 11/9/2016.
 */

public class NoteAdapter extends ArrayAdapter<Note>  {

    //Đối số 1: Màn hình sử dụng Layout này
    Activity context;
    //Layout cho từng dòng muốn hiển thị
    int resource; //resource chính là item_note.xml
    //Danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    List<Note> objects;


    public NoteAdapter(Activity context, int resource, List<Note> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Lấy ra LayoutInflater, lớp dùng để build layout bình thường trở thành code java
        //load file xml vào hệ thống, build file xml thành code trong bộ nhớ
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);

        TextView txtTitle = (TextView) row.findViewById(R.id.txtTitle);
        TextView txtContent = (TextView) row.findViewById(R.id.txtContent);
        TextView txtTime = (TextView) row.findViewById(R.id.txtTime);

        ImageButton ibtnXoa = (ImageButton) row.findViewById(R.id.ibtnXoa);
        ImageButton ibtnXem = (ImageButton) row.findViewById(R.id.ibtnXem);
        ImageButton ibtnSua = (ImageButton) row.findViewById(R.id.ibtnSua);

        //Trả về Note hiện tại muốn vẽ
        final Note note = this.objects.get(position);

        txtTitle.setText(note.getTitle());
        txtContent.setText(note.getContent());
        txtTime.setText(note.getTime());

        //Khởi tạo MainActivity mainHelp để gọi các hàm bên MainActivity
        final MainActivity mainHelp = (MainActivity)context;

        ibtnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHelp.deleteNote(note.getTitle());
                mainHelp.loadAllTitle();
            }
        });

        ibtnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle(note.getTitle());
                if (note.getContent().equalsIgnoreCase("")){
                    b.setMessage("No Detail!!!");
                }
                else b.setMessage(note.getContent());
                b.create().show();
            }
        });
        ibtnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (afterdel == true)
                {
                    afterdel = false;
                    return;
                }
                //Dùng Intent để mở một Activity khác và có kiểm soát kết quả trả về.
                Intent intent = new Intent();
                ComponentName cpn = new ComponentName(getContext(),InsertTitleActivity.class);
                intent.setComponent(cpn);
                intent.putExtra("TitleName",note.getTitle());
                intent.putExtra("Detail",note.getContent());
                //Mở Activity với REQUEST_CODE_INPUT
                mainHelp.startActivityForResult(intent,13);
            }
        });

        return row;
    }
}
