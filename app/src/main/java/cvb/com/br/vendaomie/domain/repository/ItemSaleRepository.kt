package cvb.com.br.vendaomie.domain.repository

import cvb.com.br.vendaomie.domain.model.ItemSale


interface ItemSaleRepository {
    
    suspend fun insert(itemSale: ItemSale)

    suspend fun delete(itemSale: ItemSale)

    suspend fun update(itemSale: ItemSale)

    suspend fun getListItemSaleBySale(saleId: Long): List<ItemSale>

    suspend fun deleteAllItemsBySale(saleId: Long)
}
