package cvb.com.br.vendaomie.data.repository.util

import cvb.com.br.vendaomie.domain.model.Sale


object SaleFactory {

    fun createSale(id: Long) = Sale(
        id = id,
        date = System.currentTimeMillis(),
        client = "TESTE"
    )
}