package cvb.com.br.vendaomie.presentation.page_list_sale

import cvb.com.br.vendaomie.domain.model.Sale

sealed class ListSaleEvent {

    data object LoadData: ListSaleEvent()

    data class CreateSaleRecord(val client: String): ListSaleEvent()

    data class DeleteSaleRecord(val sale: Sale): ListSaleEvent()
}

