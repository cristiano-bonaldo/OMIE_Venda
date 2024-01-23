package cvb.com.br.vendaomie.data.repository.util

import cvb.com.br.vendaomie.domain.model.ItemSale


object ItemSaleFactory {

    fun createItemSale(saleId: Long, id: Long) = ItemSale(
        saleId = saleId,
        id = id,
        product = "TESTE",
        unitValue = 1.5,
        quantity = 5
    )
}