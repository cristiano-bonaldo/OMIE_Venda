package cvb.com.br.vendaomie.data.mapper

import cvb.com.br.vendaomie.data.db.entity.ItemSaleEntity
import cvb.com.br.vendaomie.domain.model.ItemSale

fun ItemSaleEntity.toItemSale() =
    ItemSale(
        this.id,
        this.saleId,
        this.product,
        this.unitValue,
        this.quantity
    )

fun ItemSale.toItemSaleEntity() =
    ItemSaleEntity(
        this.id,
        this.saleId,
        this.product,
        this.unitValue,
        this.quantity
    )
