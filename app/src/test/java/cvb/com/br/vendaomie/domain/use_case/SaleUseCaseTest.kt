package cvb.com.br.vendaomie.domain.use_case

import com.google.common.truth.Truth.assertThat
import cvb.com.br.vendaomie.data.repository.util.SaleFactory
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.repository.SaleRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class SaleUseCaseTest {

    @Mock
    lateinit var saleRepository: SaleRepository

    @InjectMocks
    lateinit var saleUseCase: SaleUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun insert_Success() = runTest {
        val sale = SaleFactory.createSale(1)

        Mockito.`when`(saleRepository.insert(sale)).thenReturn(500)

        val saleId = saleUseCase.insertSale(sale)

        assertThat(saleId).isEqualTo(500)

        Mockito.verify(saleRepository, Mockito.times(1)).insert(sale)
        Mockito.verifyNoMoreInteractions(saleRepository)
    }

    @Test
    fun delete_Success() = runTest {
        val sale = SaleFactory.createSale(1)

        saleUseCase.deleteSale(sale)

        Mockito.verify(saleRepository, Mockito.times(1)).delete(sale)
        Mockito.verifyNoMoreInteractions(saleRepository)
    }

    @Test
    fun getListSale_WithData_Success() = runTest {
        val item1 = SaleFactory.createSale(1)
        val item2 = SaleFactory.createSale(2)
        val item3 = SaleFactory.createSale(3)

        val list = listOf(item1, item2, item3)

        Mockito.`when`(saleRepository.getListSale()).thenReturn(list)

        val listResult = saleUseCase.getListSale()

        Mockito.verify(saleRepository, Mockito.times(1)).getListSale()
        Mockito.verifyNoMoreInteractions(saleRepository)

        assertThat(listResult.size).isEqualTo(list.size)
        assertThat(listResult).contains(item1)
        assertThat(listResult).contains(item2)
        assertThat(listResult).contains(item3)
    }

    @Test
    fun getListSale_WithNoData_Success() = runTest {
        val list = listOf<Sale>()

        Mockito.`when`(saleRepository.getListSale()).thenReturn(list)

        val listResult = saleUseCase.getListSale()

        Mockito.verify(saleRepository, Mockito.times(1)).getListSale()
        Mockito.verifyNoMoreInteractions(saleRepository)

        assertThat(listResult).isEmpty()
    }

    @Test
    fun getSaleById_Success() = runTest {
        val sale = SaleFactory.createSale(1)

        Mockito.`when`(saleRepository.getSaleById(1)).thenReturn(sale)

        val saleFromMock = saleUseCase.getSaleById(1)

        Mockito.verify(saleRepository, Mockito.times(1)).getSaleById(1)
        Mockito.verifyNoMoreInteractions(saleRepository)

        assertThat(saleFromMock).isNotNull()
        assertThat(saleFromMock).isEqualTo(sale)
    }
}