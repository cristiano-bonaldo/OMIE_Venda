package cvb.com.br.vendaomie.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cvb.com.br.vendaomie.data.db.entity.SaleEntity

@Dao
interface SaleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(saleEntity: SaleEntity): Long

    @Delete
    suspend fun delete(saleEntity: SaleEntity)

    @Query("SELECT * FROM sale ORDER BY id DESC")
    suspend fun getListSale(): List<SaleEntity>

    @Query("SELECT * FROM sale WHERE id = :saleId")
    suspend fun getSaleById(saleId: Long): SaleEntity
}