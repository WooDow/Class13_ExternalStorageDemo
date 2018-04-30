package com.developer.scott.class13_externalstoragedemo;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;

/**
 * External Storage 外存
 * 1.儲存在外存的檔案，可與其他app共享使用，且使用者移除app時，不會刪除儲存在外存的檔案
 * 2.使用外存要先取得外存的使用權限，取得方式如下：
 *   在AndroidManifest.xml 檔中
 *   <manifest>
 *       <user-permission android:name = "android.permission.WRITE_EXTERNAL_STORAGE"
 *                        android:maxSdkVersion = "18" />
 *   </manifest>
 *
 * 3.因為外存(常指SDcard)有可能被使用者移除，所以每次使用前都要先驗證外存的狀態．
 *   String state = Environment.getExternalStorageState();
 *   if(state.equals(Environment.MEDIA_MOUNTED));
 * 4.讀寫在公開區的檔案，路徑取得方法如下：
 *   File path = Environment.getExternalStoragePublicDirectory(公開的資料夾); //資料夾不可為null
 * 5.讀寫在私有區的檔案，路徑取得方法如下：
 *   File path = Context.getExternalStorageFilesDie(資料夾);   //資料夾可為null
 *
 */

public class ExternalStorageActivity extends AppCompatActivity {

    private Spinner fileSpinner;
    private RadioButton rb_public, rb_private;
    private TextView tv_context, tv_showPath;
    private EditText ed_input, ed_fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);

        findviews();
    }

    private void findviews() {
        fileSpinner = findViewById(R.id.fileSpinner);
        rb_public = findViewById(R.id.rb_public);
        rb_private = findViewById(R.id.rb_private);
        tv_context = findViewById(R.id.tv_context);
        tv_showPath = findViewById(R.id.tv_showPath);
        ed_input = findViewById(R.id.ed_input);
    }

    public void onStore(View view) {
        View myView = getLayoutInflater().inflate(R.layout.dialog_view,null);
        ed_fileName = myView.findViewById(R.id.ed_fileName);
        new AlertDialog.Builder(ExternalStorageActivity.this)
                .setTitle("Set File Name")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFile();
                        setSpinner();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ExternalStorageActivity.this,"save file failed",Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .setView(myView)
                .create()
                .show();
    }

    private void setSpinner() {

    }

    private void setFile() {
        if(!isSDCardExists()){
            Toast.makeText(ExternalStorageActivity.this,"SD card is not exists!",Toast.LENGTH_SHORT).show();
            return;
        }
        File path = (rb_public.isChecked())?Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                :getExternalFilesDir(null);
        File file = new File(path,ed_fileName.getText().toString());

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write((ed_input.getText().toString()).getBytes());
            fos.close();
            Toast.makeText(ExternalStorageActivity.this,"save file successful",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //tv_showPath.setText(file.toString()); //會顯示目錄名稱加檔名
        tv_showPath.setText(path.toString());   //僅顯示路徑
    }

    private boolean isSDCardExists(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }


    public void onAppend(View view) {
    }

    public void onDelete(View view) {
    }
}
