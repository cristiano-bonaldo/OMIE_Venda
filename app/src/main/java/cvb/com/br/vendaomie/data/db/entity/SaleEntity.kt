package cvb.com.br.vendaomie.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale")
data class SaleEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val date: Long = System.currentTimeMillis(),

    val client: String
)

