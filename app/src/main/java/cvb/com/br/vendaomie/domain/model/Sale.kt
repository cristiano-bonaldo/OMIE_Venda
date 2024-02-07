package cvb.com.br.vendaomie.domain.model

data class Sale(

    val id: Long,
    val date: Long,
    val client: String,

    //--
    
    var listItemSale: List<ItemSale> = emptyList(),
    var total: Double = 0.00
)
