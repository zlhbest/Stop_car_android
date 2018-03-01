package com.henshin.stop_car.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.henshin.stop_car.MainActivity;
import com.henshin.stop_car.R;
import com.henshin.stop_car.Tools.ArrayTools;
import com.henshin.stop_car.Tools.BaseTools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by henshin on 2018/2/9.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Message message = new Message();
    private static final int REQUEST_SIGNUP = 0;
    private  ProgressDialog progressDialog=null;
    private  String password;
    private  String username;
    private String date;
    @BindView(R.id.input_username)
    EditText _userText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    //用于自动登录的实现
    private SharedPreferences sp;
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            String obj = (String)msg.obj;
            if(obj.equals("1\r\n"))
            {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                saveUserName();
                                onLoginSuccess();
                               progressDialog.dismiss();
                            }
                        }, 1000);
            }
            else if(obj.equals("0\r\n"))
            {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                _loginButton.setEnabled(true);
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "网络故障", Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
    private void init()
    {
        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        _userText.setText(username);
        _passwordText.setText(password);
        sp = getSharedPreferences("setting",0);
    }
    public void login()
    {
        if (!validate())
        {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog= new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登录中...");
        progressDialog.show();

         username = _userText.getText().toString();
         password = _passwordText.getText().toString();
        connection();
        _loginButton.setEnabled(true);
    }

    private void connection()
    {
        try {
            password = BaseTools.getMd5Code(password);
            date = "username/" + username + "#password/" + password;
            final HashMap<String, Object> map = ArrayTools.splitArray(date);
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    builder.add(entry.getKey(), entry.getValue().toString());
                }
            }
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(getString(R.string.urla1001)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String reslut = "";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String reslut = response.body().string();
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        this._loginButton.setEnabled(true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _userText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty() || username.equals("")) {
            _userText.setError("请输入一个有效的用户名");
            valid = false;
        } else {
            _userText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("密码不正确");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    //保存PhoneNumber,用于自动登录
    private void saveUserName(){
        SharedPreferences.Editor editor =sp.edit();
        //保存用户名
        editor.putString("username", username);
        editor.putString("password",password);
        editor.apply();

    }

}