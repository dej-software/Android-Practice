package com.jikexueyuan.jikecontacts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dej on 2016/9/21.
 */
public class MyContacts {

    // 用来存放读取到的联系人
    public static List<PhoneInfo> contactList = new ArrayList<>();

    /**
     * 更新联系人列表contactList
     * @param context
     */
    public static void getContacts(Context context) {
        // 管理联系人的电话的Uri
        Uri uri = Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        String phoneName;
        String phoneNumber;

        if (!contactList.isEmpty()) {
            contactList.clear();
        }

        while (cursor.moveToNext()) {
            // 得到联系人 名字、电话
            phoneName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
            phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));

            PhoneInfo phoneInfo = new PhoneInfo(phoneName, phoneNumber);
            contactList.add(phoneInfo);
        }
    }

    /**
     * 添加一个联系人
     * @param context
     * @param phoneInfo
     */
    public static void addContact(Context context, PhoneInfo phoneInfo) {
        ContentValues values = new ContentValues();
        // 获取系统返回的rawContactId
        Uri rawContactsUri  = context.getContentResolver().insert(RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactsUri);

        // 插入姓名
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId); // 添加ID
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE); // 添加内容类型
        values.put(StructuredName.DISPLAY_NAME, phoneInfo.getName()); // 添加名字到DISPLAY_NAME
        context.getContentResolver().insert(Data.CONTENT_URI, values);

        // 插入电话
        values.clear();
        values.put(Phone.RAW_CONTACT_ID, rawContactId); // 添加ID
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE); // 添加内容类型
        values.put(Phone.NUMBER, phoneInfo.getNumber());
        context.getContentResolver().insert(Data.CONTENT_URI, values);
    }
}
