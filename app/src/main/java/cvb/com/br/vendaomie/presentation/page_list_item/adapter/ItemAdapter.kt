package cvb.com.br.vendaomie.presentation.page_list_item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cvb.com.br.vendaomie.databinding.ItemListItemBinding
import cvb.com.br.vendaomie.domain.model.ItemSale
import cvb.com.br.vendaomie.util.StringUtil
import javax.inject.Inject

class ItemAdapter @Inject constructor() : RecyclerView.Adapter<ItemAdapter.ItemSaleViewHolder>() {

    private var onEditEvent: ((itemItemSale: ItemSale) -> Unit)? = null
    private var onDeleteEvent: ((itemItemSale: ItemSale) -> Unit)? = null

    private var listItemSale: List<ItemSale> = listOf()

    fun submitList(listItemSale: List<ItemSale>) {
        this.listItemSale = listItemSale
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSaleViewHolder {
        val binding = ItemListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSaleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemSaleViewHolder, position: Int) {
        val itemItemSale = listItemSale[position]
        holder.bind(itemItemSale)
    }

    override fun getItemCount(): Int {
        return listItemSale.size
    }

    fun setOnDeleteEvent(deleteEvent: (itemItemSale: ItemSale) -> Unit) {
        this.onDeleteEvent = deleteEvent
    }

    fun setOnEditEvent(editEvent: (itemItemSale: ItemSale) -> Unit) {
        this.onEditEvent = editEvent
    }

    //--------------------------

    inner class ItemSaleViewHolder(private val binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemSale: ItemSale) {
            binding.apply {
                this.ivDelete.setOnClickListener {
                    onDeleteEvent?.invoke(itemSale)
                }

                this.ivEdit.setOnClickListener {
                    onEditEvent?.invoke(itemSale)
                }

                this.tvProduct.text = itemSale.product
                this.tvVlUnit.text = StringUtil.formatValue(itemSale.unitValue)
                this.tvQtd.text = itemSale.quantity.toString()

                val total = itemSale.unitValue * itemSale.quantity
                this.tvTotal.text = StringUtil.formatValue(total)
            }
        }
    }
}