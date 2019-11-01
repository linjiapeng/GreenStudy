package com.example.greendaostudy.activity;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.greendaostudy.R;
import com.example.greendaostudy.entity.User;
import com.example.greendaostudy.greendao.DaoMaster;
import com.example.greendaostudy.greendao.DaoSession;
import com.example.greendaostudy.greendao.UserDao;
import com.example.greendaostudy.manager.GreenDaoManager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String DB_NAME = "app.db";
    public GreenDaoManager mGDManager;
    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGreenDao();
        ACID();
        GreenDaoManager.getInstance().closeConnection();
    }

    /**
     * DaoMaster: 该类保存数据库对象，内部的OpenHelper 和 DevOpenHelper 是SQLiteDatabase 的实现
     * DaoSession:提供了对数据库的增删改查的方法
     * Dao:实体类
     * */
    private void initGreenDao(){
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,DB_NAME);  //获取一个数据库帮助类对象
//        SQLiteDatabase db=helper.getWritableDatabase();  //打开或创建数据库,并返回一个数据库操作对象
//        DaoMaster daoMaster=new DaoMaster(db);   //保存数据库对象
//        mDaoSession = daoMaster.newSession();  //获取数据库操作对象
        GreenDaoManager.getInstance().init(this,DB_NAME);
        mDaoSession=GreenDaoManager.getInstance().getDaoSession();
    }

    private void  ACID(){
//        mDaoSession.getUserDao().insert(new User(null,"张三",23));
//        mDaoSession.getUserDao().insert(new User(null,"李四",25));
//        mDaoSession.getUserDao().insert(new User(null,"王五",23));

//        List<User> userList=mDaoSession.getUserDao().loadAll();
//        for(int i=0;i<userList.size();i++){
//            Log.d("greendao",userList.get(i).getId()+"--"+userList.get(i).getName()+"--"+userList.get(i).getAge());
//        }

        User user=GreenDaoManager.getInstance().select("张三");
        Log.d("gd", String.valueOf(user.getId())+"---"+user.getName()+"----"+user.getAge());

        List<User> userList=GreenDaoManager.getInstance().search(23);
        for(int i=0;i<userList.size();i++){
            Log.d("gd",userList.get(i).getId()+"--"+userList.get(i).getName()+"--"+userList.get(i).getAge());
        }

    }


}
