package cvb.com.br.vendaomie.presentation.page_list_item

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import cvb.com.br.vendaomie.data.repository.util.ItemSaleFactory
import cvb.com.br.vendaomie.domain.model.ItemSale
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

class FragListItemViewModelTest {

    // Run on the Main Thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var saleRepository: SaleRepositoryFake
    private lateinit var saleUseCase: SaleUseCase

    private lateinit var itemSaleRepository: ItemSaleRepositoryFake
    private lateinit var itemSaleUseCase: ItemSaleUseCase

    private lateinit var viewModel: FragListItemViewModel

    @Before
    fun setup() {
        saleRepository = SaleRepositoryFake()
        saleUseCase = SaleUseCase(saleRepository)

        itemSaleRepository = ItemSaleRepositoryFake()
        itemSaleUseCase = ItemSaleUseCase(itemSaleRepository)
    }

    @Test
    fun createItemSale_Status_Success() = runTest {
        viewModel = FragListItemViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        val saleId = 1L
        val itemSale = ItemSaleFactory.createItemSale(saleId,1)
        viewModel.handleEvent(ListItemEvent.SaveItem(itemSale))

        val status = viewModel.insertItem.getOrAwaitValue()
        assertThat(status).isInstanceOf(UIStatus.Success::class.java)

        //---

        viewModel.handleEvent(ListItemEvent.LoadData(saleId))
        val statusLoad = viewModel.loadData.getOrAwaitValue()
        assertThat(statusLoad).isInstanceOf(UIStatus.Success::class.java)

        val list = (statusLoad as UIStatus.Success).result
        val itemSaleFromDB = list!![0]

        assertThat(itemSaleFromDB).isNotNull()
        assertThat(itemSaleFromDB).isEqualTo(itemSale)
    }

    @Test
    fun createItemSale_Status_Error() = runTest {
        viewModel = FragListItemViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        itemSaleRepository.enableError()

        val saleId = 1L
        val itemSale = ItemSaleFactory.createItemSale(saleId,1)
        viewModel.handleEvent(ListItemEvent.SaveItem(itemSale))

        val status = viewModel.insertItem.getOrAwaitValue()
        assertThat(status).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun deleteItemSale_Status_Success() = runTest {
        viewModel = FragListItemViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        val saleId = 1L
        val itemSale = ItemSaleFactory.createItemSale(saleId,1)
        viewModel.handleEvent(ListItemEvent.SaveItem(itemSale))
        val statusInsert = viewModel.insertItem.getOrAwaitValue()
        assertThat(statusInsert).isInstanceOf(UIStatus.Success::class.java)

        //---

        viewModel.handleEvent(ListItemEvent.LoadData(saleId))
        var statusLoad = viewModel.loadData.getOrAwaitValue()
        assertThat(statusLoad).isInstanceOf(UIStatus.Success::class.java)

        var list = (statusLoad as UIStatus.Success).result
        assertThat(list!!.size).isEqualTo(1)

        //---

        viewModel.handleEvent(ListItemEvent.DeleteItem(itemSale))
        val statusDelete = viewModel.deleteItem.getOrAwaitValue()
        assertThat(statusDelete).isInstanceOf(UIStatus.Success::class.java)

        //---

        viewModel.handleEvent(ListItemEvent.LoadData(saleId))
        statusLoad = viewModel.loadData.getOrAwaitValue()
        assertThat(statusLoad).isInstanceOf(UIStatus.Success::class.java)

        list = (statusLoad as UIStatus.Success).result
        assertThat(list).isNotNull()
        assertThat(list).isEmpty()
    }

    @Test
    fun deleteItemSale_Status_Error() = runTest {
        viewModel = FragListItemViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        itemSaleRepository.enableError()

        val saleId = 1L
        val itemSale = ItemSaleFactory.createItemSale(saleId,1)
        viewModel.handleEvent(ListItemEvent.DeleteItem(itemSale))

        val status = viewModel.deleteItem.getOrAwaitValue()
        assertThat(status).isInstanceOf(UIStatus.Error::class.java)
    }

    @Test
    fun loadListItemSale_Status_Success() = runTest {
        viewModel = FragListItemViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        val listStatus = mutableListOf<UIStatus<List<ItemSale>>>()

        viewModel.loadData.observeForever { status ->
            listStatus.add(status)
        }

        val saleId = 1L
        val itemSale1 = ItemSaleFactory.createItemSale(saleId,1)
        val itemSale2 = ItemSaleFactory.createItemSale(saleId,2)
        val itemSale3 = ItemSaleFactory.createItemSale(saleId,3)

        viewModel.handleEvent(ListItemEvent.SaveItem(itemSale1))
        viewModel.handleEvent(ListItemEvent.SaveItem(itemSale2))
        viewModel.handleEvent(ListItemEvent.SaveItem(itemSale3))

        viewModel.handleEvent(ListItemEvent.LoadData(saleId))

        assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(UIStatus.Success::class.java)

        val list = (listStatus[1] as UIStatus.Success).result

        assertThat(list).isNotNull()
        assertThat(list!!.size).isEqualTo(3)
        assertThat(list[0]).isEqualTo(itemSale1)
        assertThat(list[1]).isEqualTo(itemSale2)
        assertThat(list[2]).isEqualTo(itemSale3)
    }

    @Test
    fun loadListSale_Status_Error() = runTest {
        viewModel = FragListItemViewModel(
            StandardTestDispatcher(this.testScheduler),
            saleUseCase,
            itemSaleUseCase
        )

        itemSaleRepository.enableError()

        val listStatus = mutableListOf<UIStatus<List<ItemSale>>>()

        viewModel.loadData.observeForever { status ->
            listStatus.add(status)
        }

        val saleId = 1L
        viewModel.handleEvent(ListItemEvent.LoadData(saleId))

        assertThat(listStatus[0]).isInstanceOf(UIStatus.Loading::class.java)
        assertThat(listStatus[1]).isInstanceOf(UIStatus.Error::class.java)
    }
}
