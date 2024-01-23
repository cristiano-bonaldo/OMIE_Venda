package cvb.com.br.vendaomie.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_sale")

data class ItemSaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name="sale_id")
    val saleId: Long = 0,

    val product: String,

    @ColumnInfo(name="unit_value")
    val unitValue: Double,

    val quantity: Int
)
