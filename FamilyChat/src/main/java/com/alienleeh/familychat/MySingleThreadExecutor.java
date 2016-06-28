package com.alienleeh.familychat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by AlienLeeH on 2016/6/26.
 */
public class MySingleThreadExecutor {
    private static MySingleThreadExecutor instance;
    private Executor executor;

    private MySingleThreadExecutor() {
        executor = Executors.newSingleThreadExecutor();
    }

    public synchronized static MySingleThreadExecutor getInstance(){
        if (instance == null){
            instance = new MySingleThreadExecutor();
        }
        return instance;
    }

    public void execute(Runnable runnable){
        if (executor != null){
            executor.execute(runnable);
        }
    }
}
