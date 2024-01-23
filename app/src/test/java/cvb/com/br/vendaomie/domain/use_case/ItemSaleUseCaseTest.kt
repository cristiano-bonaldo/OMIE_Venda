package cvb.com.br.vendaomie.domain.use_case

import com.google.common.truth.Truth.assertThat
import cvb.com.br.vendaomie.data.repository.util.ItemSaleFactory
import cvb.com.br.vendaomie.domain.data_source.ItemSaleDataSource
import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.domain.repository.ItemSaleRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ItemSaleUseCaseTest {

    @Mock
    lateinit var itemSaleRepository: ItemSaleRepository

    @InjectMocks
    lateinit var itemSaleUseCase: ItemSaleUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun insert_Success() = runTest {
        val itemSale = ItemSaleFactory.createItemSale(1, 1)

        itemSaleUseCase.insertItem(itemSale)

        Mockito.verify(itemSaleRepository, Mockito.times(1)).insert(itemSale)
        Mockito.verifyNoMoreInteractions(itemSaleRepository)
    }

    @Test
    fun delete_Success() = runTest {
        val itemSale = ItemSaleFactory.createItemSale(1, 1)

        itemSaleUseCase.deleteItem(itemSale)

        Mockito.verify(itemSaleRepository, Mockito.times(1)).delete(itemSale)
        Mockito.verifyNoMoreInteractions(itemSaleRepository)
    }

    @Test
    fun deleteAllItemsBySale_Success() = runTest {
        itemSaleUseCase.deleteAllItemsBySale(1)

        Mockito.verify(itemSaleRepository, Mockito.times(1)).deleteAllItemsBySale(1)
        Mockito.verifyNoMoreInteractions(itemSaleRepository)
    }

    @Test
    fun update_Success() = runTest {
        val itemSale = ItemSaleFactory.createItemSale(1, 1)

        itemSaleUseCase.updateItem(itemSale)

        Mockito.verify(itemSaleRepository, Mockito.times(1)).update(itemSale)
        Mockito.verifyNoMoreInteractions(itemSaleRepository)
    }

    @Test
    fun getListItemSaleBySale_WithData_Success() = runTest {
        val itemSale1 = ItemSaleFactory.createItemSale(1, 1)
        val itemSale2 = ItemSaleFactory.createItemSale(1, 2)
        val itemSale3 = ItemSaleFactory.createItemSale(1, 3)

        val list = listOf(itemSale1, itemSale2, itemSale3)

        Mockito.`when`(itemSaleRepository.getListItemSaleBySale(1)).thenReturn(list)

        val listResult = itemSaleUseCase.getListItemsSaleBySale(1)

        Mockito.verify(itemSaleRepository, Mockito.times(1)).getListItemSaleBySale(1)
        Mockito.verifyNoMoreInteractions(itemSaleRepository)

        assertThat(listResult.size).isEqualTo(list.size)
        assertThat(listResult).contains(itemSale1)
        assertThat(listResult).contains(itemSale2)
        assertThat(listResult).contains(itemSale3)
    }

    @Test
    fun getListItemSaleBySale_WithNoData_Success() = runTest {
        val list = listOf<ItemSale>()

        Mockito.`when`(itemSaleRepository.getListItemSaleBySale(1)).thenReturn(list)

        val listResult = itemSaleUseCase.getListItemsSaleBySale(1)

        Mockito.verify(itemSaleRepository, Mockito.times(1)).getListItemSaleBySale(1)
        Mockito.verifyNoMoreInteractions(itemSaleRepository)

        assertThat(listResult).isEmpty()
    }
}
