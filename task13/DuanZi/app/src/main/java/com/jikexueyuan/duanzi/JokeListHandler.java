package com.jikexueyuan.duanzi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dej on 2016/10/29.
 */
public class JokeListHandler {

    public static List<Joke> jokeList = new ArrayList<>();

    public static void updateJokeList() {
        if (!jokeList.isEmpty()) {
            jokeList.clear();
        }

    }
}
