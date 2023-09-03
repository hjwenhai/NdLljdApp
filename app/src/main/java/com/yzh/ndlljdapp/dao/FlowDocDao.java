package com.yzh.ndlljdapp.dao;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yzh.ndlljdapp.entity.FlowDoc;
import com.yzh.ndlljdapp.entity.User;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface FlowDocDao {
    @Query(
            "SELECT * FROM flowDoc " +
                    "WHERE (:devNo IS NULL OR devNo= :devNo)" +
                    "  AND (:date IS NULL OR date = :date)" +
                    "  AND (:customer IS NULL OR customer =:customer)" +
                    "  AND (:manufactor IS NULL OR manufactor =:manufactor)" +
                    "  AND (:fileName IS NULL OR fileName = :fileName)"
    )
    LiveData<List<FlowDoc>> getFlowDocList(@Nullable String devNo, @Nullable Long date, @Nullable String customer,
                                           @Nullable String manufactor, @Nullable String fileName);


    /**
     * 插入一条用户onConflict = OnConflictStrategy.REPLACE表明若存在主键相同的情况则直接覆盖
     * 返回的long表示的是插入项新的id
     * @param flowDoc
     * @return
     */
    @Insert
    long insertFlowDoc(FlowDoc flowDoc);

    /**
     * 更新数据，这意味着可以指定id然后传入新的User对象进行更新
     * 回的lint表示更新的行数
     * @param flowDoc
     * @return
     */
    @Update
    int updateFlowDoc(FlowDoc flowDoc);

    /**
     * 删除数据，根据传入实体的主键进行数据的删除。
     * 返回int型数据，表明从数据库中删除的行数
     * @param flowDoc
     * @return
     */
    @Delete
    int deleteFlowDoc(FlowDoc flowDoc);
}
