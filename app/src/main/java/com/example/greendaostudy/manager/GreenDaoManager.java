package com.example.greendaostudy.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.greendaostudy.entity.User;
import com.example.greendaostudy.greendao.DaoMaster;
import com.example.greendaostudy.greendao.DaoSession;
import com.example.greendaostudy.greendao.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GreenDaoManager {


    private Context mContext;

    //创建数据库名
    private String DB_NAME;

    //多线程中要被共享的使用volatile关键字修饰GreenDaoUtil;
    private volatile static GreenDaoManager mGDInstance;

    //实际保存的数据库对象
    private static DaoMaster mDaoMaster;

    //创建数据库的工具
    private static DaoMaster.DevOpenHelper mHelper;

    //数据库的增删改查操作类
    private static DaoSession mDaoSession;

    /**
     * 获取单例模式下的数据库操作对象
     * */
    private GreenDaoManager(){}

    public static GreenDaoManager getInstance(){
        synchronized (GreenDaoManager.class){
            if(mGDInstance==null){
                mGDInstance = new GreenDaoManager();
            }
        }
        return mGDInstance;
    }

    public void init(Context context,String db_name){
        this.mContext=context;
        this.DB_NAME=db_name;
    }

    /**
     * 判断数据库是否存在，如若不存在进行创建
     * */
    public DaoMaster getDaoMaster(){
        if (mDaoMaster==null){
            mHelper=new DaoMaster.DevOpenHelper(mContext,DB_NAME);
            SQLiteDatabase db=mHelper.getWritableDatabase();
            mDaoMaster=new DaoMaster(db);
        }
        return mDaoMaster;
    }

    /**
     * 数据库增删改查方法
     */
    public DaoSession getDaoSession(){
            if(mDaoSession==null){
                if(mDaoMaster==null){
                    mDaoMaster=getDaoMaster();
                }
                mDaoSession=mDaoMaster.newSession();
            }
            return mDaoSession;
    }

    /**
     * 关闭所有操作
     * */
    public void closeConnection(){

        if(mHelper!=null){
            mHelper.close();
            mHelper=null;
        }

        if(mDaoSession!=null){
            mDaoSession.clear();
            mDaoSession=null;
        }

    }

    ////////////
    //增加
    ///////////
    public void insert(User user){
        mDaoSession.getUserDao().insert(user);
    }

    //////////
    //查询
    /////////
    public List<User> selectAll(){
        List<User> userList=mDaoSession.getUserDao().queryBuilder().list();
        List<User> userList2=mDaoSession.getUserDao().queryBuilder().listLazy();
        return userList;
    }

    public User select(String name){
        UserDao userDao=getDaoSession().getUserDao();
        QueryBuilder<User> qb=userDao.queryBuilder();
        return  qb.where(UserDao.Properties.Name.eq(name)).unique();
    }

    public List<User> search(int age){
        UserDao userDao=getDaoSession().getUserDao();
        List<User> userList=userDao.queryBuilder().where(UserDao.Properties.Age.eq(age)).build().list();
        return userList;
    }

    ///////////
    //删除
    //////////

    public void delete(long id){
        UserDao userDao=getDaoSession().getUserDao();
        userDao.deleteByKey(id);
        //userDao.delete(user);
        //userDao.deleteAll();
    }

    public void deleteAll(){
        UserDao userDao=getDaoSession().getUserDao();
        userDao.deleteAll();
    }

    //////////
    //更新
    /////////
    public void correct(long id,String name){
        UserDao userDao=getDaoSession().getUserDao();
        userDao.update(new User(id,name,22));
    }

    ////////////////
    //修改或替换
    ///////////////
    public void insertOrReplace(long id,int age){
        UserDao userDao=getDaoSession().getUserDao();
        userDao.insertOrReplace(new User(id,"张三",age));
    }


}
