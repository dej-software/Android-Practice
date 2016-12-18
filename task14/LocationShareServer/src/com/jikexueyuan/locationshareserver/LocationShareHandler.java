package com.jikexueyuan.locationshareserver;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by dej on 2016/11/6.
 */
public class LocationShareHandler extends IoHandlerAdapter {

    // flag对应的JSON值说明：
    // 0 - 服务器发给客户端连接信息
    // 1 - 服务器发给客户端用户名信息
    // 1 - 用户发给服务器用户名信息
    // 2 - 用户共享的位置信息
    private static final int DATA_FLAG_CONNECT = 0;
    private static final int DATA_FLAG_USER = 1;
    private static final int DATA_FLAG_MAP = 2;

    private static final String JSON_FLAG = "flag";
    private static final String JSON_NAME = "name";
    private static final String JSON_MSG = "message";

//    private List<IoSession> userSessions = new ArrayList<>();
    private Map<String, IoSession> usersMap = new HashMap<>();

    private JSONObject makeMessage(int flag, String msg) {
        JSONObject root = new JSONObject();
        try {
            root.put(JSON_FLAG, flag);
            root.put(JSON_MSG, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);

        System.out.println(session.toString());
        session.write(makeMessage(DATA_FLAG_CONNECT, "@success").toString());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);

        System.out.println(session.toString());
        // 从map中去掉 并发送 {"flag":2,"name":"user1","latitude":-1,"longitude":-1}
        String name = "";
        Iterator iterator = usersMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (session.equals(entry.getValue())) {
                name = (String) entry.getKey();
                iterator.remove();
            }
        }
        // 此客户端用户存在 把断开连接的数据发送给客户端
        if (!"".equals(name)) {
            for (IoSession ioSession : usersMap.values()) {
                ioSession.write(String.format("{\"flag\":2,\"name\":\"%s\",\"latitude\":-1,\"longitude\":-1}", name));
            }
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        System.out.println("message:" + message);
        // 使用JSON格式解析数据
        JSONObject root = null;
        try {
            root = new JSONObject((String) message);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // 处理用户名情况
        int flag = root.getInt(JSON_FLAG);
        if (flag == DATA_FLAG_USER) {
            String name = root.getString(JSON_NAME);
            if (usersMap.containsKey(name)) {
                session.write(makeMessage(DATA_FLAG_USER, "@user_error").toString());
                return;
            } else {
                usersMap.put(name, session);
                session.write(makeMessage(DATA_FLAG_USER, "@user_success").toString());
            }
        }

        // 处理定位数据 发送给每一个客户端
        if (flag == DATA_FLAG_MAP) {
            for (IoSession ioSession : usersMap.values()) {
                ioSession.write(message);
            }
        }
    }
}
