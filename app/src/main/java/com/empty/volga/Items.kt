package com.empty.volga

import io.finnhub.api.models.Quote
import io.finnhub.api.models.StockSymbol

data class Items(
    var description: String?,
    var tick: String?,
    var currentQuote: Float?,
    var quoteDifference: Float?,
    var quoteDifferencePercent: Float?
) {
    constructor(stockSymbol: StockSymbol?, quote: Quote?) : this(
        stockSymbol?.description,
        stockSymbol?.displaySymbol,
        quote?.c,
        quote?.d,
        quote?.dp
    )

    constructor(stockSymbol: StockSymbol?) : this(stockSymbol, null)
    constructor(
        items: Items?,
        quote: Quote?
    ) : this(items?.description, items?.tick, quote?.c, quote?.d, quote?.dp)

    fun getCurrQuote(): Float {
        if (currentQuote != null)
            return currentQuote!!
        return 0f
    }

    fun getQuoteDiff(): Float {
        if (quoteDifference != null)
            return quoteDifference!!
        return 0f
    }

    fun getQuoteDiffPercent(): Float {
        if (quoteDifferencePercent != null)
            return quoteDifferencePercent!!
        return 0f
    }
}
