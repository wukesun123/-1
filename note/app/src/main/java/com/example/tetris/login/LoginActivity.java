package com.example.tetris.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.room.Room;

import com.example.tetris.MainActivity;
import com.example.tetris.R;
import com.example.tetris.RegisterActivity;
import com.example.tetris.dao.UserDao;
import com.example.tetris.db.AppDatabase;
import com.example.tetris.entity.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView text_title;
    private EditText edit_account, edit_password;
    private TextView text_msg, text_delete;
    private Button btn_login, btn_register;
    private ImageButton openpwd;
    private boolean flag = false;
    private String account, password;
//    private DBHelper dbHelper;
    public AppDatabase db;
    public UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar(); //获取ActionBar
        actionBar.hide(); //隐藏ActionBar

        init();
    }
    private void init() {
        text_title = (TextView) findViewById(R.id.text_title);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.yan);
        text_title.setTypeface(typeface);

        edit_account = (EditText) findViewById(R.id.edit_account);
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
//                .fallbackToDestructiveMigration()
                .build();
        userDao = db.userDao();

        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_password.getWindowToken(), 0);
                }
                return false;
            }
        });
        text_msg = (TextView) findViewById(R.id.text_msg);
        text_delete = (TextView) findViewById(R.id.text_delete);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        openpwd = (ImageButton) findViewById(R.id.btn_openpwd);
        text_msg.setOnClickListener(this);
        text_delete.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        openpwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (edit_account.getText().toString().trim().equals("") | edit_password.getText().
                        toString().trim().equals("")) {
                    Toast.makeText(this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_openpwd:
                if (flag == true) {//密码不可见
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    openpwd.setBackgroundResource(R.drawable.eye);
                } else { //密码可见
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    openpwd.setBackgroundResource(R.drawable.eye);
                }
                break;
            case R.id.text_msg:
                Intent i = new Intent(LoginActivity.this, ForgotInfoActivity.class);
                startActivity(i);
                break;
                //跳转到DeleteActivity
            case R.id.text_delete:
                Intent intent1 = new Intent(LoginActivity.this, DeleteActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 读取UserData.db中的用户信息
     * */
    protected void readUserInfo() {
        if (login(edit_account.getText().toString(), edit_password.getText().toString())) {
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("Username",edit_account.getText().toString());
            User user = userDao.findUser(edit_account.getText().toString(),edit_password.getText().toString());
            intent.putExtra("uid",user.uid);
            startActivity(intent);
        } else {
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 验证登录信息
     * */
    public boolean login(String username, String password) {
        User user = userDao.findUser(username,password);
        if (user!=null)
            return true;
        else
            return false;
    }
}