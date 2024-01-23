package cvb.com.br.vendaomie.data.db.dao.util

import cvb.com.br.vendaomie.data.db.entity.ItemSaleEntity

object ItemSaleEntityFactory {
    fun createItemSale(saleId: Long, id: Long) = ItemSaleEntity(
        saleId = saleId,
        id = id,
        product = "TESTE",
        unitValue = 1.0,
        quantity = 5
    )
}