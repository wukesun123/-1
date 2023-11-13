package com.example.tetris.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.room.Room;

import com.example.tetris.R;
import com.example.tetris.dao.UserDao;
import com.example.tetris.db.AppDatabase;
import com.example.tetris.entity.User;


public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView text_title;
    private Button btn_confirm, btn_cancel;
    private EditText edit_account, edit_password;

    public AppDatabase db;
    public UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        ActionBar actionBar = getSupportActionBar(); //获取ActionBar
        actionBar.hide(); //隐藏ActionBar
        init();
    }

    private void init() {
        text_title = (TextView) findViewById(R.id.text_title);
        //使用字体
        Typeface typeface = ResourcesCompat.getFont(this, R.font.zhao);
        text_title.setTypeface(typeface);

        edit_account = (EditText) findViewById(R.id.edit_account);
        //获取数据库对象,且对比Data.db中的username是否存在，存在则进行删除操作
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_account.clearFocus();
                }
                return false;
            }
        });

        db = Room.databaseBuilder( getApplicationContext(), AppDatabase.class, "mydb" )
                .allowMainThreadQueries()
                .build();
        userDao = db.userDao();

        edit_password = (EditText) findViewById(R.id.edit_password);
        //获取数据库对象,且对比Data.db中的password是否存在，存在则进行删除操作
//        dbHelper = new DBHelper(this, "Data.db", null, 1);
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password.clearFocus();
                }
                return false;
            }
        });

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
//        dbHelper = new DBHelper(this, "Data.db", null, 1);
    }

    @Override
    public void onClick(View v) {
        //获取输入的用户名和密码
        String account = edit_account.getText().toString();
        String password = edit_password.getText().toString();
        switch (v.getId()) {
            case R.id.btn_confirm:
                //获取数据库对象,且对比Data.db中的username和password是否存在，存在则进行删除操作id段
                User user = userDao.findUser(account,password);
                if (user!=null){
                    userDao.delete(user);
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                }
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                Cursor cursor = db.query("usertable", null, "username=? and password=?", new String[]{account, password}, null, null, null);
//                if (cursor.moveToFirst()) {
//                    db.delete("usertable", "username=? and password=?", new String[]{account, password});
//                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(this, LoginActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
//                }
                    break;
                case R.id.btn_cancel:    //清空输入框
                    edit_account.setText("");
                    edit_password.setText("");
                    finish();
        }
    }
}