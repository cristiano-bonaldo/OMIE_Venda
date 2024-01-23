package cvb.com.br.vendaomie.data.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import cvb.com.br.vendaomie.data.db.AppDataBase
import cvb.com.br.vendaomie.data.db.dao.util.ItemSaleEntityFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ItemSaleDaoTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDataBase

    private lateinit var itemSaleDao: ItemSaleDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()

        itemSaleDao = db.itemSaleDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertItemPurchase() = runTest {
        val saleId = 1L
        val itemId = 1L

        val itemSaleEntity = ItemSaleEntityFactory.createItemSale(saleId, itemId)

        itemSaleDao.insert(itemSaleEntity)

        val itemSaleListFromDB = itemSaleDao.getListItemSaleBySale(saleId)

        assertThat(itemSaleListFromDB).isNotNull()
        assertThat(itemSaleListFromDB.size).isEqualTo(1)

        val itemSaleEntityFromDb = itemSaleListFromDB[0]

        assertThat(itemSaleEntityFromDb).isEqualTo(itemSaleEntity)

        assertThat(itemSaleEntityFromDb.saleId).isEqualTo(itemSaleEntity.saleId)
        assertThat(itemSaleEntityFromDb.id).isEqualTo(itemSaleEntity.id)
        assertThat(itemSaleEntityFromDb.product).isEqualTo(itemSaleEntity.product)
        assertThat(itemSaleEntityFromDb.unitValue).isEqualTo(itemSaleEntity.unitValue)
        assertThat(itemSaleEntityFromDb.quantity).isEqualTo(itemSaleEntity.quantity)
    }

    @Test
    fun updateItemSale() = runTest {
        val saleId = 1L
        val itemId = 1L

        val itemSaleEntity = ItemSaleEntityFactory.createItemSale(saleId, itemId)

        itemSaleDao.insert(itemSaleEntity)

        val itemSaleEntityUpdate = itemSaleEntity.copy(
            product = "PRODUTO-TESTE",
            unitValue = 10.55,
            quantity = 150
        )

        itemSaleDao.update(itemSaleEntityUpdate)

        val itemSaleListEntityFromDB = itemSaleDao.getListItemSaleBySale(saleId)

        assertThat(itemSaleListEntityFromDB).isNotNull()
        assertThat(itemSaleListEntityFromDB.size).isEqualTo(1)

        val itemSaleEntityFromDB = itemSaleListEntityFromDB[0]

        assertThat(itemSaleEntityFromDB).isEqualTo(itemSaleEntityUpdate)

        assertThat(itemSaleEntityFromDB.saleId).isEqualTo(itemSaleEntityUpdate.saleId)
        assertThat(itemSaleEntityFromDB.id).isEqualTo(itemSaleEntityUpdate.id)
        assertThat(itemSaleEntityFromDB.product).isEqualTo(itemSaleEntityUpdate.product)
        assertThat(itemSaleEntityFromDB.unitValue).isEqualTo(itemSaleEntityUpdate.unitValue)
        assertThat(itemSaleEntityFromDB.quantity).isEqualTo(itemSaleEntityUpdate.quantity)
    }

    @Test
    fun deleteItemSale() = runTest {
        val saleId = 1L
        val itemId = 1L

        val itemSaleEntity = ItemSaleEntityFactory.createItemSale(saleId, itemId)

        itemSaleDao.insert(itemSaleEntity)

        var itemSaleListFromDB = itemSaleDao.getListItemSaleBySale(saleId)

        assertThat(itemSaleListFromDB).isNotNull()
        assertThat(itemSaleListFromDB.size).isEqualTo(1)

        itemSaleDao.delete(itemSaleEntity)

        itemSaleListFromDB = itemSaleDao.getListItemSaleBySale(saleId)

        assertThat(itemSaleListFromDB).isNotNull()
        assertThat(itemSaleListFromDB.size).isEqualTo(0)
    }

    @Test
    fun deleteAllItemsBySale() = runTest {
        val saleId1 = 1L
        val itemSaleEntity1 = ItemSaleEntityFactory.createItemSale(saleId1, 1)
        val itemSaleEntity2 = ItemSaleEntityFactory.createItemSale(saleId1, 2)
        val itemSaleEntity3 = ItemSaleEntityFactory.createItemSale(saleId1, 3)

        val saleId2 = 2L
        val itemSaleEntity4 = ItemSaleEntityFactory.createItemSale(saleId2, 4)
        val itemSaleEntity5 = ItemSaleEntityFactory.createItemSale(saleId2, 5)

        val saleId3 = 3L
        val itemSaleEntity6 = ItemSaleEntityFactory.createItemSale(saleId3, 6)

        itemSaleDao.insert(itemSaleEntity1)
        itemSaleDao.insert(itemSaleEntity2)
        itemSaleDao.insert(itemSaleEntity3)
        itemSaleDao.insert(itemSaleEntity4)
        itemSaleDao.insert(itemSaleEntity5)
        itemSaleDao.insert(itemSaleEntity6)

        var itemSaleListFromDB = itemSaleDao.getListItemSaleBySale(saleId2)

        assertThat(itemSaleListFromDB).isNotNull()
        assertThat(itemSaleListFromDB.size).isEqualTo(2)

        itemSaleDao.deleteAllItemsBySale(saleId2)

        itemSaleListFromDB = itemSaleDao.getListItemSaleBySale(saleId2)

        assertThat(itemSaleListFromDB).isNotNull()
        assertThat(itemSaleListFromDB.size).isEqualTo(0)
    }

    @Test
    fun getListItemsSaleBySale() = runTest {
        val saleId1 = 1L
        val itemSaleEntity1 = ItemSaleEntityFactory.createItemSale(saleId1, 1)
        val itemSaleEntity2 = ItemSaleEntityFactory.createItemSale(saleId1, 2)
        val itemSaleEntity3 = ItemSaleEntityFactory.createItemSale(saleId1, 3)

        val saleId2 = 2L
        val itemSaleEntity4 = ItemSaleEntityFactory.createItemSale(saleId2, 4)
        val itemSaleEntity5 = ItemSaleEntityFactory.createItemSale(saleId2, 5)

        val saleId3 = 3L
        val itemSaleEntity6 = ItemSaleEntityFactory.createItemSale(saleId3, 6)

        itemSaleDao.insert(itemSaleEntity1)
        itemSaleDao.insert(itemSaleEntity2)
        itemSaleDao.insert(itemSaleEntity3)
        itemSaleDao.insert(itemSaleEntity4)
        itemSaleDao.insert(itemSaleEntity5)
        itemSaleDao.insert(itemSaleEntity6)

        var itemSaleListFromDB = itemSaleDao.getListItemSaleBySale(saleId1)
        assertThat(itemSaleListFromDB).isNotNull()
        assertThat(itemSaleListFromDB.size).isEqualTo(3)
        assertThat(itemSaleListFromDB).containsExactly(itemSaleEntity1, itemSaleEntity2, itemSaleEntity3)

        itemSaleListFromDB = itemSaleDao.getListItemSaleBySale(saleId2)
        assertThat(itemSaleListFromDB).isNotNull()
        assertThat(itemSaleListFromDB.size).isEqualTo(2)
        assertThat(itemSaleListFromDB).containsExactly(itemSaleEntity4, itemSaleEntity5)

        itemSaleListFromDB = itemSaleDao.getListItemSaleBySale(saleId3)
        assertThat(itemSaleListFromDB).isNotNull()
        assertThat(itemSaleListFromDB.size).isEqualTo(1)
        assertThat(itemSaleListFromDB).containsExactly(itemSaleEntity6)
    }
}

