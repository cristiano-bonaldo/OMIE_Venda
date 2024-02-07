package cvb.com.br.vendaomie.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE item_sale_bk (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "sale_id INTEGER NOT NULL, " +
                "product TEXT NOT NULL, " +
                "unit_value REAL NOT NULL, " +
                "quantity REAL NOT NULL" +
                ")")
//
//
//        db.execSQL("CREATE TABLE IF NOT EXISTS `item_sale` " +
//                "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                "`sale_id` INTEGER NOT NULL, " +
//                "`product` TEXT NOT NULL, " +
//                "`unit_value` REAL NOT NULL, " +
//                "`quantity` REAL NOT NULL)");


        database.execSQL("INSERT INTO item_sale_bk(id, sale_id, product, unit_value, quantity) SELECT id, sale_id, product, unit_value, quantity FROM item_sale")





        database.execSQL("DROP TABLE item_sale")

        database.execSQL("ALTER TABLE item_sale_bk RENAME TO item_sale")
    }
}