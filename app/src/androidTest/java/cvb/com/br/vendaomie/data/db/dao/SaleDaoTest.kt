package cvb.com.br.vendaomie.data.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import cvb.com.br.vendaomie.data.db.AppDataBase
import cvb.com.br.vendaomie.data.db.dao.util.SaleEntityFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SaleDaoTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDataBase

    private lateinit var saleDao: SaleDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()

        saleDao = db.saleDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertSale() = runTest {
        val saleEntity = SaleEntityFactory.createSale(1)

        saleDao.insert(saleEntity)

        val list = saleDao.getListSale()

        val saleFromList = list[0]

        assertThat(saleFromList).isEqualTo(saleEntity)

        assertThat(saleFromList.id).isEqualTo(saleEntity.id)
        assertThat(saleFromList.date).isEqualTo(saleEntity.date)
        assertThat(saleFromList.client).isEqualTo(saleEntity.client)
    }

    @Test
    fun deleteSale() = runTest {
        val saleId = 1000L
        val saleEntity = SaleEntityFactory.createSale(saleId)

        saleDao.insert(saleEntity)

        var saleEntityFromDB = saleDao.getSaleById(saleId)

        assertThat(saleEntityFromDB).isNotNull()

        saleDao.delete(saleEntity)

        saleEntityFromDB = saleDao.getSaleById(saleId)

        assertThat(saleEntityFromDB).isNull()
    }

    @Test
    fun getListSale() = runTest {
        val saleEntity1 = SaleEntityFactory.createSale(1)
        val saleEntity2 = SaleEntityFactory.createSale(2)
        val saleEntity3 = SaleEntityFactory.createSale(3)

        val listTest = arrayListOf(saleEntity1, saleEntity2, saleEntity3)

        saleDao.insert(saleEntity1)
        saleDao.insert(saleEntity2)
        saleDao.insert(saleEntity3)

        val listFromDB = saleDao.getListSale()

        assertThat(listFromDB.size).isEqualTo(listTest.size)
        assertThat(listFromDB).containsExactly(saleEntity1, saleEntity2, saleEntity3)
    }

    @Test
    fun getListNoSale() = runTest {
        val listFromDB = saleDao.getListSale()

        assertThat(listFromDB).isNotNull()
        assertThat(listFromDB.size).isEqualTo(0)
    }

    @Test
    fun getSaleById() = runTest {
        val saleEntity1 = SaleEntityFactory.createSale(1)
        val saleEntity2 = SaleEntityFactory.createSale(2)
        val saleEntity3 = SaleEntityFactory.createSale(3)

        saleDao.insert(saleEntity1)
        saleDao.insert(saleEntity2)
        saleDao.insert(saleEntity3)

        val saleEntityFromDB = saleDao.getSaleById(2)

        assertThat(saleEntityFromDB).isNotNull()
        assertThat(saleEntityFromDB).isEqualTo(saleEntity2)
    }
}

