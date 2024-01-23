package cvb.com.br.vendaomie.domain.data_source

import cvb.com.br.vendaomie.domain.model.ItemSale


interface ItemSaleDataSource {

    suspend fun insert(itemSale: ItemSale)

    suspend fun delete(itemSale: ItemSale)

    suspend fun update(itemSale: ItemSale)

    suspend fun getListItemSaleBySale(saleId: Long): List<ItemSale>

    suspend fun deleteAllItemsBySale(saleId: Long)
}