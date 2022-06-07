package com.empty.volga.finnhub

import android.util.Log
import com.empty.volga.Items
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import java.lang.Exception

object Receiver {
    private val TAG = this.javaClass.name
    private var stockItems = ArrayList<Items>()
    private var bufferItem: Items? = null
    private var currItemNum = 0
    private var isGetting = false

    fun Start(callback: Callback){
        ApiClient.apiKey["token"] = "ca3ti7iad3ia58rfi620"
        val apiClient = DefaultApi()
        Thread {
            try {
                val symbolsList = apiClient.stockSymbols("US", "", "", "")
                val listSize = symbolsList.size
                val itemList = ArrayList<Items>()
                for (i: Int in 0 until listSize) {
                    itemList.add(i, Items(symbolsList[i]))
                }
                callback.ListOfStock(itemList)
            }
            catch (e: Exception){
                callback.ListError()
            }
        }.start()

        Thread {
            while(true){
                isGetting = true
                Log.i(TAG, "Start: request $currItemNum,\t${stockItems.size}")
                val first = callback.getFirst()
                if (stockItems.size > currItemNum)
                    try {
                        val quote = apiClient.quote(stockItems[currItemNum].tick!!)
                        Log.i(TAG, "Start: $quote, ${first + currItemNum}")
                        callback.QuoteUpdate(first + currItemNum, Items(stockItems[currItemNum], quote))
                    }
                    catch (e: Exception){
                        Log.e(TAG, "StartQuoteUpdate: ${e.message}")
                    }
                isGetting = false
                if (bufferItem != null){
                    stockItems.set(currItemNum, bufferItem!!)
                    bufferItem = null
                    currItemNum = 0
                }else if (currItemNum < stockItems.size)
                    currItemNum++
                else
                    currItemNum = 0
                Thread.sleep(1000)
            }
        }.start()
    }

    fun setList(list: ArrayList<Items>){
        try {
            Log.i(TAG, "setList: call!")
            if (!isGetting) {
                stockItems = list
                //currItemNum = 0
            } else {
                val size = if (stockItems.size > list.size) stockItems.size else list.size
                for (i: Int in 0..size)
                    if (i == currItemNum)
                        bufferItem = list[i]
                    else
                        stockItems.set(i, list[i])
            }
        }catch (e: Exception){
            Log.e(TAG, "setList: ${e.message}")
        }
    }

}