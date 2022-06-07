package com.empty.volga.finnhub

import com.empty.volga.Items

interface Callback {
    fun ListOfStock(items: ArrayList<Items>)
    fun ListError()
    fun QuoteUpdate(num: Int, items: Items)
    fun getFirst(): Int
}