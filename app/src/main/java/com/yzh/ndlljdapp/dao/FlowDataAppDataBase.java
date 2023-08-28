package com.yzh.ndlljdapp.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yzh.ndlljdapp.entity.User;

/**
 * 指明是需要从那个class文件中创建数据库，并必须指明版本号
 */
@Database(entities = {User.class}, version = 1,exportSchema = false)
public abstract class FlowDataAppDataBase extends RoomDatabase {

    private static FlowDataAppDataBase INSTANCE;//声明单例对象，减小数据库开销
    private static final String DATABASE_NAME= "flowDataApp.db";

    public static FlowDataAppDataBase getINSTANCE(Context context) {//通过单例对象获取数据库
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, FlowDataAppDataBase.class, DATABASE_NAME)
                    //.addMigrations(MIGRATION_1_2)更新数据库
                    .build();
        }
        return INSTANCE;
    }
    public abstract UserDao getUserDao();//创建抽象的dao方法以便获取dao对象
}
