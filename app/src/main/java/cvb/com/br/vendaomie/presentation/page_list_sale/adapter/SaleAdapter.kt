package cvb.com.br.vendaomie.presentation.page_list_sale.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cvb.com.br.vendaomie.databinding.ItemListSaleBinding
import cvb.com.br.vendaomie.databinding.ItemListSaleHeadBinding
import cvb.com.br.vendaomie.domain.model.Sale
import cvb.com.br.vendaomie.util.DateTimeUtil
import cvb.com.br.vendaomie.util.StringUtil
import javax.inject.Inject

class SaleAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onShareEvent: ((sale: Sale) -> Unit)? = null
    private var onEditEvent: ((sale: Sale) -> Unit)? = null
    private var onDeleteEvent: ((sale: Sale) -> Unit)? = null

    private var listItem: List<ListItem> = listOf()

    fun submitList(listSale: List<ListItem>) {
        this.listItem = listSale
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (listItem[position]) {
            is ListItem.HeadItem -> 0
            is ListItem.InfoItem -> 1
        }
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            0 -> {
                val bHead = ItemListSaleHeadBinding.inflate(LayoutInflater.from(parent.context), parent,false)
                HeadViewHolder(bHead)
            }

            1 -> {
                val bInfo = ItemListSaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                InfoViewHolder(bInfo)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = listItem[position]) {
            is ListItem.HeadItem -> (holder as HeadViewHolder).bind(item.data)
            is ListItem.InfoItem -> (holder as InfoViewHolder).bind(item.sale)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    fun setOnDeleteEvent(deleteEvent: (sale: Sale) -> Unit) {
        this.onDeleteEvent = deleteEvent
    }

    fun setOnEditEvent(editEvent: (sale: Sale) -> Unit) {
        this.onEditEvent = editEvent
    }

    fun setOnShareEvent(shareEvent: (sale: Sale) -> Unit) {
        this.onShareEvent = shareEvent
    }

    //--------------------------

    inner class InfoViewHolder(private val binding: ItemListSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sale: Sale) {
            binding.apply {
                this.ivShare.setOnClickListener {
                    onShareEvent?.invoke(sale)
                }

                this.ivDelete.setOnClickListener {
                    onDeleteEvent?.invoke(sale)
                }

                this.ivEdit.setOnClickListener {
                    onEditEvent?.invoke(sale)
                }

                this.tvClient.text = sale.client
                this.tvId.text = sale.id.toString()
                this.tvDate.text = DateTimeUtil.convertTimeMillisToString(sale.date)
                this.tvTotal.text = StringUtil.formatValue(sale.total)
            }
        }
    }

    inner class HeadViewHolder(private val binding: ItemListSaleHeadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            binding.apply {
                this.tvIdLabel.text = data
            }
        }
    }
}