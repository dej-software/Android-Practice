package com.jikexueyuan.jikecontacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnAdd;
    private ListView lvContacts;
    private PhoneAdapter phoneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        updateContacts();
    }

    // 更新联系人显示
    private void updateContacts() {
        MyContacts.getContacts(this);
//        for (int i = 0; i < MyContacts.contactList.size(); i++) {
//            PhoneInfo phoneInfo = MyContacts.contactList.get(i);
//            System.out.println("tese:" + phoneInfo.getName() + "<-->" + phoneInfo.getNumber());
//        }
        phoneAdapter = new PhoneAdapter(MyContacts.contactList, this);
        lvContacts.setAdapter(phoneAdapter);
    }

    /**
     * 初始化控件等信息
     */
    private void initView() {

        btnAdd = (Button) findViewById(R.id.btnAdd);
        lvContacts = (ListView) findViewById(R.id.lvContacts);

        btnAdd.setOnClickListener(this);
        lvContacts.setOnItemClickListener(this);
    }

    /**
     * MainActivity里按钮的点击处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                addContactDialog();
                break;
        }
    }

    /**
     * 添加联系人的处理
     */
    private void addContactDialog() {
        // 添加联系人的View及里面的EditText
        final View addContactView = getLayoutInflater().inflate(R.layout.activity_add_contact, null);
        final EditText etName = (EditText) addContactView.findViewById(R.id.etName);
        final EditText etPhone = (EditText) addContactView.findViewById(R.id.etPhone);

        // 设置AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        builder.setTitle(R.string.add_title);
        builder.setView(addContactView);
        builder.setPositiveButton(R.string.add_ok, null);
        builder.setNegativeButton(R.string.add_cancel, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // 重设PositiveButton监听 以实现输入空信息时不关闭对话框
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到输入的字符 并去掉首尾空格
                String name = etName.getText().toString().trim();
                String number = etPhone.getText().toString().trim();

                if (!name.equals("") && !number.equals("")) {
                    alertDialog.dismiss();
                    // 增加联系人
                    PhoneInfo phoneInfo = new PhoneInfo(name, number);
                    MyContacts.addContact(MainActivity.this, phoneInfo);
                    // 更新显示的列表
                    updateContacts();
                    System.out.println("Name:" + name + " Phone:" + number);
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, R.string.add_invalid, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    /**
     * ListView中的点击联系人Item处理
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // 得到被点击的联系人信息
        final TextView tvPhoneName = (TextView) view.findViewById(R.id.tvPhoneName);
        final TextView tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);

        System.out.println("被点击：" + tvPhoneName.getText().toString() + "-" + tvPhoneNumber.getText().toString());

        // AlertDialog 拨打电话、发短信的选择对话框
        final String[] items = new String[]{this.getString(R.string.call_phone), this.getString(R.string.send_sms)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.call_title);

        // 设置点击监听
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();

                switch (which) {
                    case 0:
                        // 到打电话界面
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + tvPhoneNumber.getText().toString()));
                        startActivity(intent);
                        break;
                    case 1:
                        // 到发短信界面
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + tvPhoneNumber.getText().toString()));
                        startActivity(intent);
                        break;
                }
            }
        });

        builder.setNegativeButton(R.string.call_cancel, null);
        builder.show();
    }
}