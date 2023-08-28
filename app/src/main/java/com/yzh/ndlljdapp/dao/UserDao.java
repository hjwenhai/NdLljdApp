package com.yzh.ndlljdapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yzh.ndlljdapp.entity.User;

@Dao
public interface UserDao {
    /**
     * 根据名称查找用户
     * @param name
     * @return
     */
    @Query("select * from user where name=(:name)")
    User getUserByName(String name);

    /**
     * 插入一条用户onConflict = OnConflictStrategy.REPLACE表明若存在主键相同的情况则直接覆盖
     * 返回的long表示的是插入项新的id
     * @param user
     * @return
     */
    @Insert
    long insertUser(User user);

    /**
     * 更新数据，这意味着可以指定id然后传入新的User对象进行更新
     * 回的lint表示更新的行数
     * @param user
     * @return
     */
    @Update
    int updateUser(User user);

    /**
     * 删除数据，根据传入实体的主键进行数据的删除。
     * 返回int型数据，表明从数据库中删除的行数
     * @param user
     * @return
     */
    @Delete
    int deleteUser(User user);
}
