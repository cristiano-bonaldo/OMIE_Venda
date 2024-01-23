package cvb.com.br.vendaomie.data.db.dao.util

import cvb.com.br.vendaomie.data.db.entity.SaleEntity

object SaleEntityFactory {
    fun createSale(id: Long) = SaleEntity(
        id = id,
        date = System.currentTimeMillis(),
        client = "TESTE"
    )
}