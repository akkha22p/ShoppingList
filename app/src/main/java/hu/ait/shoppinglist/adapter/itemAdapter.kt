package hu.ait.shoppinglist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.ScrollingActivity
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.Item
import hu.ait.shoppinglist.touch.itemTouchHelperCallback
import kotlinx.android.synthetic.main.item_row.view.*

import java.util.*

class itemAdapter : RecyclerView.Adapter<itemAdapter.ViewHolder>, itemTouchHelperCallback {


    var items = mutableListOf<Item>()

    private val context: Context


    constructor(context: Context, listItem: List<Item>) : super() {
        this.context = context
        items.addAll(listItem)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemRowView = LayoutInflater.from(context).inflate(
            R.layout.item_row, viewGroup, false
        )

        return ViewHolder(itemRowView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val item  = items.get(viewHolder.adapterPosition)

        viewHolder.tvName.text = item.createName
        viewHolder.tvCategory.text = item.createCate
        viewHolder.tvPrice.text = item.createPrice
        viewHolder.tvDetail.text = item.createDesc
        viewHolder.cbDone.isChecked = item.done

        if(viewHolder.tvCategory.text == "Drink"){
            viewHolder.icon.setImageResource(R.drawable.drink)
        }
        else if(viewHolder.tvCategory.text == "Food"){
            viewHolder.icon.setImageResource(R.drawable.food)
        }
        else if(viewHolder.tvCategory.text == "Cosmetic"){
            viewHolder.icon.setImageResource(R.drawable.cosmetic)
        }
        else {
            viewHolder.icon.setImageResource(R.drawable.elec)
        }

        viewHolder.btnDelete.setOnClickListener {
            deleteItem(viewHolder.adapterPosition)
        }

        viewHolder.cbDone.setOnClickListener {
            item.done = viewHolder.cbDone.isChecked
            updateItem(item)
        }

        viewHolder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditTodoDialog(item,
                viewHolder.adapterPosition)
        }

        viewHolder.btnDetail.setOnClickListener {
            if(viewHolder.btnDetail.text == "Show Detail") {
                viewHolder.loDetail.visibility = View.VISIBLE
                viewHolder.btnDetail.text = "HIDE"
            }
            else if(viewHolder.btnDetail.text == "HIDE"){
                viewHolder.loDetail.visibility = View.GONE
                viewHolder.btnDetail.text = "Show Detail"
            }
        }
    }

    fun updateItem(item: Item) {
        Thread{
            AppDatabase.getInstance(context).itemDao().updateItem(item)
        }.start()
    }

    fun updateItem(item: Item, editIndex: Int) {
        items.set(editIndex, item)
        notifyItemChanged(editIndex)
    }


    fun addItem(item: Item) {
        items.add(0, item)
        //notifyDataSetChanged()
        notifyItemInserted(0)
    }

    fun deleteItem(deletePosition: Int) {
        Thread{
            AppDatabase.getInstance(context).itemDao().deleteItem(items.get(deletePosition))

            (context as ScrollingActivity).runOnUiThread {
                items.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
            }
        }.start()
    }

    fun removeAll() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvName = itemView.tvName
        var tvCategory = itemView.tvCategory
        var tvPrice = itemView.tvPrice
        var loDetail = itemView.loDetail
        var tvDetail = itemView.tvDetail
        var cbDone = itemView.cbDone
        var btnDelete = itemView.btnDelete
        var btnEdit = itemView.btnEdit
        var btnDetail = itemView.btnDetail
        var icon = itemView.myIcon
    }

}