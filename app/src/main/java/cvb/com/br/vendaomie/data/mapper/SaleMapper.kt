package cvb.com.br.vendaomie.data.mapper

import cvb.com.br.vendaomie.data.db.entity.SaleEntity
import cvb.com.br.vendaomie.domain.model.Sale

fun SaleEntity.toSale() =
    Sale(
        this.id,
        this.date,
        this.client
    )

fun Sale.toSaleEntity() =
    SaleEntity(
        this.id,
        this.date,
        this.client
    )
