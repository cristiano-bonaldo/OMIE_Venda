package cvb.com.br.vendaomie.presentation.page_list_sale.adapter

import cvb.com.br.vendaomie.domain.model.Sale

sealed class ListItem {
    data class HeadItem(val data: String) : ListItem()

    data class InfoItem(val sale: Sale) : ListItem()
}