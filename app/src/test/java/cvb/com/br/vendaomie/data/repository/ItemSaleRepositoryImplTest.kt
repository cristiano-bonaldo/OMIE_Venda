package cvb.com.br.vendaomie.data.repository

import com.google.common.truth.Truth.assertThat
import cvb.com.br.vendaomie.data.repository.util.ItemSaleFactory
import cvb.com.br.vendaomie.domain.data_source.ItemSaleDataSource
import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.domain.model.Sale
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ItemSaleRepositoryImplTest {

    @Mock
    lateinit var itemSaleDataSource: ItemSaleDataSource

    @InjectMocks
    lateinit var itemSaleRepositoryImpl: ItemSaleRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun insert_Success() = runTest {
        val itemSale = ItemSaleFactory.createItemSale(1, 1)

        itemSaleRepositoryImpl.insert(itemSale)

        Mockito.verify(itemSaleDataSource, Mockito.times(1)).insert(itemSale)
        Mockito.verifyNoMoreInteractions(itemSaleDataSource)
    }

    @Test
    fun delete_Success() = runTest {
        val itemSale = ItemSaleFactory.createItemSale(1, 1)

        itemSaleRepositoryImpl.delete(itemSale)

        Mockito.verify(itemSaleDataSource, Mockito.times(1)).delete(itemSale)
        Mockito.verifyNoMoreInteractions(itemSaleDataSource)
    }

    @Test
    fun deleteAllItemsBySale_Success() = runTest {
        itemSaleRepositoryImpl.deleteAllItemsBySale(1)

        Mockito.verify(itemSaleDataSource, Mockito.times(1)).deleteAllItemsBySale(1)
        Mockito.verifyNoMoreInteractions(itemSaleDataSource)
    }

    @Test
    fun update_Success() = runTest {
        val itemSale = ItemSaleFactory.createItemSale(1, 1)

        itemSaleRepositoryImpl.update(itemSale)

        Mockito.verify(itemSaleDataSource, Mockito.times(1)).update(itemSale)
        Mockito.verifyNoMoreInteractions(itemSaleDataSource)
    }

    @Test
    fun getListItemSaleBySale_WithData_Success() = runTest {
        val itemSale1 = ItemSaleFactory.createItemSale(1, 1)
        val itemSale2 = ItemSaleFactory.createItemSale(1, 2)
        val itemSale3 = ItemSaleFactory.createItemSale(1, 3)

        val list = listOf(itemSale1, itemSale2, itemSale3)

        Mockito.`when`(itemSaleDataSource.getListItemSaleBySale(1)).thenReturn(list)

        val listResult = itemSaleRepositoryImpl.getListItemSaleBySale(1)

        Mockito.verify(itemSaleDataSource, Mockito.times(1)).getListItemSaleBySale(1)
        Mockito.verifyNoMoreInteractions(itemSaleDataSource)

        assertThat(listResult.size).isEqualTo(list.size)
        assertThat(listResult).contains(itemSale1)
        assertThat(listResult).contains(itemSale2)
        assertThat(listResult).contains(itemSale3)
    }

    @Test
    fun getListItemSaleBySale_WithNoData_Success() = runTest {
        val list = listOf<ItemSale>()

        Mockito.`when`(itemSaleDataSource.getListItemSaleBySale(1)).thenReturn(list)

        val listResult = itemSaleRepositoryImpl.getListItemSaleBySale(1)

        Mockito.verify(itemSaleDataSource, Mockito.times(1)).getListItemSaleBySale(1)
        Mockito.verifyNoMoreInteractions(itemSaleDataSource)

        assertThat(listResult).isEmpty()
    }
}
