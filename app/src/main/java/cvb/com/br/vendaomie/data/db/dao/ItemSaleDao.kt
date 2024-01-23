package cvb.com.br.vendaomie.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cvb.com.br.vendaomie.data.db.entity.ItemSaleEntity

@Dao
interface ItemSaleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itemSaleEntity: ItemSaleEntity)

    @Delete
    suspend fun delete(itemSaleEntity: ItemSaleEntity)

    @Update
    suspend fun update(itemSaleEntity: ItemSaleEntity)

    @Query("SELECT * FROM item_sale WHERE sale_id = :saleId ORDER BY product ASC")
    suspend fun getListItemSaleBySale(saleId: Long): List<ItemSaleEntity>

    @Query("DELETE FROM item_sale WHERE sale_id = :saleId")
    suspend fun deleteAllItemsBySale(saleId: Long)
}