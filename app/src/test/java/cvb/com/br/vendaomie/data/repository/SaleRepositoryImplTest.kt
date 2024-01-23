package cvb.com.br.vendaomie.data.repository

import com.google.common.truth.Truth.assertThat
import cvb.com.br.vendaomie.data.repository.util.SaleFactory
import cvb.com.br.vendaomie.domain.data_source.SaleDataSource
import cvb.com.br.vendaomie.domain.model.Sale
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SaleRepositoryImplTest {

    @Mock
    lateinit var saleDataSource: SaleDataSource

    @InjectMocks
    lateinit var saleRepositoryImpl: SaleRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun insert_Success() = runTest {
        val sale = SaleFactory.createSale(1)

        Mockito.`when`(saleDataSource.insert(sale)).thenReturn(500)

        val saleId = saleRepositoryImpl.insert(sale)

        assertThat(saleId).isEqualTo(500)

        Mockito.verify(saleDataSource, Mockito.times(1)).insert(sale)
        Mockito.verifyNoMoreInteractions(saleDataSource)
    }

    @Test
    fun delete_Success() = runTest {
        val sale = SaleFactory.createSale(1)

        saleRepositoryImpl.delete(sale)

        Mockito.verify(saleDataSource, Mockito.times(1)).delete(sale)
        Mockito.verifyNoMoreInteractions(saleDataSource)
    }

    @Test
    fun getListSale_WithData_Success() = runTest {
        val sale1 = SaleFactory.createSale(1)
        val sale2 = SaleFactory.createSale(2)
        val sale3 = SaleFactory.createSale(3)

        val list = listOf(sale1, sale2, sale3)

        Mockito.`when`(saleDataSource.getListSale()).thenReturn(list)

        val listResult = saleRepositoryImpl.getListSale()

        Mockito.verify(saleDataSource, Mockito.times(1)).getListSale()
        Mockito.verifyNoMoreInteractions(saleDataSource)

        assertThat(listResult.size).isEqualTo(list.size)
        assertThat(listResult).contains(sale1)
        assertThat(listResult).contains(sale2)
        assertThat(listResult).contains(sale3)
    }

    @Test
    fun getListSale_WithNoData_Success() = runTest {
        val list = listOf<Sale>()

        Mockito.`when`(saleDataSource.getListSale()).thenReturn(list)

        val listResult = saleRepositoryImpl.getListSale()

        Mockito.verify(saleDataSource, Mockito.times(1)).getListSale()
        Mockito.verifyNoMoreInteractions(saleDataSource)

        assertThat(listResult).isEmpty()
    }

    @Test
    fun getSaleById_Success() = runTest {
        val sale = SaleFactory.createSale(1)

        Mockito.`when`(saleDataSource.getSaleById(1)).thenReturn(sale)

        val saleFromMock = saleRepositoryImpl.getSaleById(1)

        Mockito.verify(saleDataSource, Mockito.times(1)).getSaleById(1)
        Mockito.verifyNoMoreInteractions(saleDataSource)

        assertThat(saleFromMock).isNotNull()
        assertThat(saleFromMock).isEqualTo(sale)
    }
}
