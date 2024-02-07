package cvb.com.br.vendaomie.data.db

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cvb.com.br.vendaomie.data.db.dao.ItemSaleDao
import cvb.com.br.vendaomie.data.db.dao.SaleDao
import cvb.com.br.vendaomie.data.db.entity.ItemSaleEntity
import cvb.com.br.vendaomie.data.db.entity.SaleEntity
import cvb.com.br.vendaomie.util.Constants

@Database(
    entities = [
        SaleEntity::class,
        ItemSaleEntity::class
    ],
    //version = 1,
    version = 2,
    exportSchema = true
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun saleDao(): SaleDao
    abstract fun itemSaleDao(): ItemSaleDao

    companion object {

        const val DATABASE_NAME = "OMIE_Venda.DB"

        val databaseCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.i(Constants.TAG, "Database - onCreate")
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                Log.i(Constants.TAG, "Database - onDestructiveMigration")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.i(Constants.TAG, "Database - onOpen")
            }
        }
    }
}