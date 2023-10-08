package com.ackerman.foodappme.data.local.database.datasource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ackerman.foodappme.data.local.database.dao.MenuDao
import com.ackerman.foodappme.data.local.database.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

interface MenuDataSource {

    @Query("SELECT * FROM MENUS")
    fun getAllMenus(): Flow<List<MenuEntity>>

    @Query("SELECT * FROM MENUS WHERE id == :id")
    fun getMenuById(id: Int): Flow<MenuEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenus(menu: List<MenuEntity>)

    @Delete
    suspend fun deleteMenu(menu: MenuEntity): Int

    @Update
    suspend fun updateMenu(menu: MenuEntity): Int

}

class MenuDatabaseDataSource(private val dao: MenuDao) : MenuDataSource {
    override fun getAllMenus(): Flow<List<MenuEntity>> {
        return dao.getAllMenus()
    }

    override fun getMenuById(id: Int): Flow<MenuEntity> {
        return dao.getMenuById(id)
    }

    override suspend fun insertMenus(menu: List<MenuEntity>) {
        return dao.insertMenu(menu)
    }

    override suspend fun deleteMenu(menu: MenuEntity): Int {
        return dao.deleteMenu(menu)
    }

    override suspend fun updateMenu(menu: MenuEntity): Int {
        return dao.updateMenu(menu)
    }
}