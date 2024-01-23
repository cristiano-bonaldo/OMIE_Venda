package cvb.com.br.vendaomie.presentation.page_list_sale

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import cvb.com.br.vendaomie.data.repository.util.SaleFactory
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.repository.ItemSaleRepository
import cvb.com.br.vendaomie.domain.use_case.ItemSaleUseCase
import cvb.com.br.vendaomie.domain.use_case.SaleUseCase
import cvb.com.br.vendaomie.presentation.UIStatus
import cvb.com.br.vendaomie.presentation.util.ItemSaleRepositoryFake
import cvb.com.br.vendaomie.presentation.util.SaleRepositoryFake
import cvb.com.br.vendaomie.util.MainDispatcherRule
import cvb.com.br.vendaomie.util.getOrAwaitValue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FragListSalelViewModelTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var saleRepository: SaleRepositoryFake
    private lateinit var saleUseCase: SaleUseCase

    private lateinit var itemSaleRepository: ItemSaleRepository
    private lateinit var itemSaleUseCase: ItemSaleUseCase

    private lateinit var viewModel: FragListSaleViewModel

    @Before
    fun setup() {
        saleRepository = SaleRepositoryFake()
        saleUseCase = SaleUseCase(saleRepository)

        itemSaleRepository = ItemSaleRepositoryFake()
        itemSaleUseCase = ItemSaleUseCase(itemSaleRepository)
    }

    @Test
    fun createSale_Status_Success() = runTest {
        viewModel = FragListSaleViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        viewModel.handleEvent(ListSaleEvent.CreateSaleRecord("CLIENTE-TESTE"))

        val status = viewModel.insertData.getOrAwaitValue()
        assertThat(status).isInstanceOf(UIStatus.Success::class.java)

        //---

        viewModel.handleEvent(ListSaleEvent.LoadData)
        val statusLoad = viewModel.loadData.getOrAwaitValue()
        assertThat(statusLoad).isInstanceOf(UIStatus.Success::class.java)

        val list = (statusLoad as UIStatus.Success).result
        val sale = list!![0]

        assertThat(sale).isNotNull()
        assertThat(sale.client).isEqualTo("CLIENTE-TESTE")
    }

    @Test
    fun createSale_Status_Error() = runTest {
        viewModel = FragListSaleViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        saleRepository.enableError()

        viewModel.handleEvent(ListSaleEvent.CreateSaleRecord("CLIENTE-TESTE"))

        val status = viewModel.insertData.getOrAwaitValue()
        assertThat(status).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun deleteSale_Status_Success() = runTest {
        viewModel = FragListSaleViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        viewModel.handleEvent(ListSaleEvent.CreateSaleRecord("CLIENTE-TESTE"))
        val statusInsert = viewModel.insertData.getOrAwaitValue()
        assertThat(statusInsert).isInstanceOf(UIStatus.Success::class.java)

        //---

        viewModel.handleEvent(ListSaleEvent.LoadData)
        var statusLoad = viewModel.loadData.getOrAwaitValue()
        assertThat(statusLoad).isInstanceOf(UIStatus.Success::class.java)

        var list = (statusLoad as UIStatus.Success).result
        val sale = list!![0]

        //---

        viewModel.handleEvent(ListSaleEvent.DeleteSaleRecord(sale))
        val statusDelete = viewModel.deleteData.getOrAwaitValue()
        assertThat(statusDelete).isInstanceOf(UIStatus.Success::class.java)

        //---

        viewModel.handleEvent(ListSaleEvent.LoadData)
        statusLoad = viewModel.loadData.getOrAwaitValue()
        assertThat(statusLoad).isInstanceOf(UIStatus.Success::class.java)

        list = (statusLoad as UIStatus.Success).result
        assertThat(list).isNotNull()
        assertThat(list).isEmpty()
    }

    @Test
    fun deleteSale_Status_Error() = runTest {
        viewModel = FragListSaleViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        saleRepository.enableError()

        val sale = SaleFactory.createSale(1)
        viewModel.handleEvent(ListSaleEvent.DeleteSaleRecord(sale))

        val status = viewModel.deleteData.getOrAwaitValue()
        assertThat(status).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun loadListSale_Status_Success() = runTest {
        viewModel = FragListSaleViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        val listStatus = mutableListOf<UIStatus<List<Sale>>>()

        viewModel.loadData.observeForever { status ->
            listStatus.add(status)
        }

        viewModel.handleEvent(ListSaleEvent.CreateSaleRecord("Cliente_1"))
        viewModel.handleEvent(ListSaleEvent.CreateSaleRecord("Cliente_2"))
        viewModel.handleEvent(ListSaleEvent.CreateSaleRecord("Cliente_3"))

        viewModel.handleEvent(ListSaleEvent.LoadData)

        assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(UIStatus.Success::class.java)

        val list = (listStatus[1] as UIStatus.Success).result

        assertThat(list).isNotNull()
        assertThat(list!!.size).isEqualTo(3)
        assertThat(list[0].client).isEqualTo("Cliente_1")
        assertThat(list[1].client).isEqualTo("Cliente_2")
        assertThat(list[2].client).isEqualTo("Cliente_3")
    }

    @Test
    fun loadListSale_Status_Error() = runTest {
        viewModel = FragListSaleViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        saleRepository.enableError()

        val listStatus = mutableListOf<UIStatus<List<Sale>>>()

        viewModel.loadData.observeForever { status ->
            listStatus.add(status)
        }

        viewModel.handleEvent(ListSaleEvent.LoadData)

        assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }
}
