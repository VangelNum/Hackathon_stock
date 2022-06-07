package com.empty.volga

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.empty.volga.adapters.ViewAdapter
import com.empty.volga.databinding.ActivityMainBinding
import com.empty.volga.finnhub.Callback
import com.empty.volga.finnhub.Receiver
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity(), Callback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewAdapter: ViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val activity: Activity = this
    private var stockItems = ArrayList<Items>()
    private var visibleLast: Int? = 0

    private val TAG = this.javaClass.name
    //private val receiver = FinnhubReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        linearLayoutManager = LinearLayoutManager(this)
        viewAdapter = ViewAdapter(stockItems, linearLayoutManager)
        binding.refreshBtn.setOnClickListener {
            binding.refreshBtn.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            Receiver.Start(this)
        }
        Receiver.Start(this)

        Thread {
            while (true){
                val last = viewAdapter.getLast()
                Log.i(TAG, "onCreate: $last")
                if (visibleLast != last){
                    Receiver.setList(viewAdapter.getVisibleArray()!!)
                    visibleLast = last
                }
                Thread.sleep(500)
            }
        }.start()
    }

    override fun ListOfStock(items: ArrayList<Items>) {
        this.runOnUiThread{
            this.stockItems = items
            binding.recycleView.setHasFixedSize(false)
            binding.recycleView.layoutManager = linearLayoutManager
            viewAdapter = ViewAdapter(items, linearLayoutManager)
            binding.recycleView.adapter = viewAdapter
            binding.progressLayout.visibility = View.GONE
        }
    }

    override fun ListError() {
        activity.runOnUiThread {
            Toast.makeText(applicationContext, "Error"/*e.message*/, Toast.LENGTH_LONG).show()
            binding.refreshBtn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun QuoteUpdate(num: Int, items: Items) {
        activity.runOnUiThread {
            try {
                if (items != stockItems[num]){
                    stockItems[num] = items
                    viewAdapter.notifyItemChanged(num)
                }
            }catch (e: Exception){
                Log.e(TAG, "QuoteUpdate: ${e.message}", )
            }
        }
    }

    override fun getFirst(): Int = viewAdapter.getFirst()
}