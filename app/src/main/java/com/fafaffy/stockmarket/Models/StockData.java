package com.fafaffy.stockmarket.Models;

import java.util.Date;

/**
 * Created by alex on 2/1/18.
 * This is a model representing the data given to us by the stock data file
 */

public class StockData {
    public Date Date; //Date,Open,High,Low,Close,Volume,Adj Close
    public float Open;
    public float High;
    public float Low;
    public float Close;
    public float Volume;
    public float AdjClose;
}
