package cvb.com.br.vendaomie.presentation.page_list_item

import cvb.com.br.vendaomie.domain.model.ItemSale

sealed class ListItemEvent {

    data class LoadSale(val saleId: Long) : ListItemEvent()

    data object DeleteSale : ListItemEvent()

    data class LoadData(val saleId: Long) : ListItemEvent()

    data class SaveItem(val itemSale: ItemSale) : ListItemEvent()

    data class DeleteItem(val itemSale: ItemSale) : ListItemEvent()
}

