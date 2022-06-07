package com.empty.volga.adapters


import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empty.volga.Items
import com.empty.volga.R
import com.empty.volga.databinding.StockItemBinding
import java.text.DecimalFormat


class ViewAdapter(var mList: ArrayList<Items>, val layoutManager: LinearLayoutManager):
    RecyclerView.Adapter<ViewAdapter.StockViewHolder>() {
    private val TAG = this.javaClass.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stock_item, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(mList.get(position))
        val first = getFirst()
        val last = getLast()
        val size = last - first
        Log.i(TAG, "onBindViewHolder: $first\t$last\t$size")
    }

    override fun getItemCount(): Int = mList.size

    fun getFirst(): Int = if(layoutManager.findFirstVisibleItemPosition() < 0) 0 else layoutManager.findFirstVisibleItemPosition()
    fun getLast(): Int = if(layoutManager.findLastVisibleItemPosition() < 0) 0 else layoutManager.findLastVisibleItemPosition()
    fun getVisibleArray(): ArrayList<Items>?{
        val outList = ArrayList<Items>()
        val first = getFirst()
        val last = getLast()
        for(i:Int in 0 until (last - first)){
            outList.add(i, mList[first + i])
        }
        return outList
    }

    inner class StockViewHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = StockItemBinding.bind(item)
        fun bind(stockItem: Items){
            val df = DecimalFormat("##.##")
            val df2 = DecimalFormat("##.###")

            binding.stockCompName.text = stockItem.description
            binding.stockTick.text = stockItem.tick
            binding.currentQuote.text = df.format(stockItem.getCurrQuote()) + " $"
            val diffprecent: Float = stockItem.getQuoteDiff() //разность в долларах

            if (diffprecent>0f) {
                binding.quoteDifPercent.setTextColor(Color.parseColor("#0aad3f")) // зеленый
                binding.quoteDif.setTextColor(Color.parseColor("#0aad3f"))
                binding.quoteDif.text = '+' + df2.format(stockItem.getQuoteDiff()) + "$"
                binding.quoteDifPercent.text = "(+" + df.format(diffprecent) + " %)"
            }
            if (diffprecent<0f){
                binding.quoteDifPercent.setTextColor(Color.parseColor("#B31920")) // красный
                binding.quoteDif.setTextColor(Color.parseColor("#B31920"))
                binding.quoteDif.text = df2.format(stockItem.getQuoteDiff()) + "$"
                binding.quoteDifPercent.text = "(" + df.format(diffprecent) + " %)"
            }
           // binding.quoteDif.text = df2.format(stockItem.getQuoteDiff()) + "$"
            //binding.quoteDifPercent.text = "(" + df.format(diffprecent) + " %)"

            binding.stockCompName.isSelected = true
        }



    }
}