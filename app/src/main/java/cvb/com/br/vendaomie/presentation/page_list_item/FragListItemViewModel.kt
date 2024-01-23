package cvb.com.br.vendaomie.presentation.page_list_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import cvb.com.br.vendaomie.R
import cvb.com.br.vendaomie.di.DefaultDispatcher
import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.domain.use_case.ItemSaleUseCase
import cvb.com.br.vendaomie.domain.use_case.SaleUseCase
import cvb.com.br.vendaomie.presentation.UIStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragListItemViewModel @Inject constructor(
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val saleUseCase: SaleUseCase,
    private val itemSaleUseCase: ItemSaleUseCase
) : ViewModel() {

    private val mLoadSale = MutableLiveData<UIStatus<Sale>>()
    val loadSale: LiveData<UIStatus<Sale>>
        get() = mLoadSale

    private val mDeleteSale = MutableLiveData<UIStatus<Nothing>>()
    val deleteSale: LiveData<UIStatus<Nothing>>
        get() = mDeleteSale

    private val mLoadData = MutableLiveData<UIStatus<List<ItemSale>>>()
    val loadData: LiveData<UIStatus<List<ItemSale>>>
        get() = mLoadData

    private val mDeleteItem = MutableLiveData<UIStatus<List<ItemSale>>>()
    val deleteItem: LiveData<UIStatus<List<ItemSale>>>
        get() = mDeleteItem

    private val mInsertItem = MutableLiveData<UIStatus<List<ItemSale>>>()
    val insertItem: LiveData<UIStatus<List<ItemSale>>>
        get() = mInsertItem

    private val mTotalSale = MutableLiveData(0.00)
    val totalSale: LiveData<Double>
        get() = mTotalSale

    private val mIsLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = mIsLoading.distinctUntilChanged()

    private var currentSale: Sale? = null
    private var currentListItem: List<ItemSale> = emptyList()

    //--

    init {
        mIsLoading.addSource(mLoadSale) {
            updateMediatorIsLoading()
        }
        mIsLoading.addSource(mDeleteSale) {
            updateMediatorIsLoading()
        }
        mIsLoading.addSource(mLoadData) {
            updateMediatorIsLoading()
        }
        mIsLoading.addSource(mDeleteItem) {
            updateMediatorIsLoading()
        }
        mIsLoading.addSource(mInsertItem) {
            updateMediatorIsLoading()
        }
    }

    private fun updateMediatorIsLoading() {
        mIsLoading.value = isLoading()
    }

    private fun isLoading(): Boolean {
        return mLoadSale.value is UIStatus.Loading ||
               mDeleteSale.value is UIStatus.Loading ||
               mLoadData.value is UIStatus.Loading ||
               mDeleteItem.value is UIStatus.Loading ||
               mInsertItem.value is UIStatus.Loading
    }

    fun handleEvent(event: ListItemEvent) {
        when (event) {
            is ListItemEvent.LoadSale -> {
                loadSale(event.saleId)
            }

            is ListItemEvent.DeleteSale -> {
                deleteSale()
            }

            is ListItemEvent.LoadData -> {
                loadData(event.saleId)
            }

            is ListItemEvent.SaveItem -> {
                saveItem(event.itemSale)
            }

            is ListItemEvent.DeleteItem -> {
                deleteItem(event.itemSale)
            }
        }
    }

    private fun loadSale(saleId: Long) {
        viewModelScope.launch {
            try {
                mLoadSale.value = UIStatus.Loading

                currentSale = saleUseCase.getSaleById(saleId)

                mLoadSale.value = UIStatus.Success(currentSale)
            } catch (error: Throwable) {
                mLoadSale.value = UIStatus.Error(error)
            }
        }
    }

    private fun deleteSale() {
        viewModelScope.launch {
            try {
                mDeleteSale.value = UIStatus.Loading

                currentSale?.let { sale ->
                    itemSaleUseCase.deleteAllItemsBySale(sale.id)
                    saleUseCase.deleteSale(sale)
                }

                mDeleteSale.value = UIStatus.Success(null)
            } catch (error: Throwable) {
                mDeleteSale.value = UIStatus.Error(error)
            }
        }
    }

    private fun loadData(saleId: Long) {
        viewModelScope.launch {
            try {
                mLoadData.value = UIStatus.Loading

                val listItem = getListItemSale(saleId)

                mLoadData.value = UIStatus.Success(listItem)
            } catch (error: Throwable) {
                mLoadData.value = UIStatus.Error(error)
            }
        }
    }

    private suspend fun getListItemSale(saleId: Long): List<ItemSale> {
        currentListItem = (itemSaleUseCase.getListItemsSaleBySale(saleId))


        val total = currentListItem.sumOf { itemSale -> itemSale.unitValue * itemSale.quantity }
        mTotalSale.value = total

        return currentListItem
    }

    private fun saveItem(itemSale: ItemSale) {
        viewModelScope.launch {
            try {
                mInsertItem.value = UIStatus.Loading

                val saleId =
                    if (itemSale.id > 0) {
                        itemSaleUseCase.updateItem(itemSale)
                        itemSale.saleId
                    } else {
                        itemSaleUseCase.insertItem(itemSale)
                        itemSale.saleId
                    }

                val listItem = getListItemSale(saleId)

                mInsertItem.value = UIStatus.Success(listItem)
            } catch (error: Throwable) {
                mInsertItem.value = UIStatus.Error(error)
            }
        }
    }

    private fun deleteItem(itemSale: ItemSale) {
        viewModelScope.launch {
            try {
                mDeleteItem.value = UIStatus.Loading

                val saleId = itemSale.saleId

                itemSaleUseCase.deleteItem(itemSale)

                val listItem = getListItemSale(saleId)

                mDeleteItem.value = UIStatus.Success(listItem)
            } catch (error: Throwable) {
                mDeleteItem.value = UIStatus.Error(error)
            }
        }
    }

    fun validateItemSale(itemSale: ItemSale): Int {
        if (itemSale.product.trim().isBlank()) {
            return(R.string.page_item_dialog_error_prod)
        }

        if (itemSale.id <= 0) {
            val located = currentListItem.firstOrNull { item -> item.product == itemSale.product.trim() }
            located?.let {
                return (R.string.page_item_dialog_alert_prod)
            }
        }

        if (itemSale.unitValue <= 0) {
            return(R.string.page_item_dialog_error_vlunit)
        }

        val value = itemSale.unitValue.toString()
        if (value.contains(".")) {
            val idxDot = value.indexOf('.')
            val numDec = value.substring(idxDot + 1)
            if (numDec.length > 2) {
                return(R.string.page_item_dialog_error_vlunit_dec)
            }
        }

        if (itemSale.quantity <= 0) {
            return(R.string.page_item_dialog_error_qtd)
        }

        return -1
    }

    fun saleHasNoItems(): Boolean {
        return currentListItem.isEmpty()
    }
}