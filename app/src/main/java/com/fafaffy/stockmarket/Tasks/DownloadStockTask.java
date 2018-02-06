package com.fafaffy.stockmarket.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.fafaffy.stockmarket.Models.StockData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Brian on 2/1/18.
 * This is a task which downloads the user-specified stock text file, and returns a list of stock data
 * The list is to be used to be displayed on a list
 */

public class DownloadStockTask extends AsyncTask<String,Void,ArrayList<StockData>> {
    protected boolean errorDownloading = false;

    @Override
    protected ArrayList<StockData> doInBackground(String... strings) {
        // Define list of stock data that we'll return
        ArrayList<StockData> listOfStockData = new ArrayList<StockData>();


        // Try/catch due to the nature of http network connections
        try {
            // Create an input stream of the desired text
            String urlString = "http://utdallas.edu/~jxc064000/2017Spring/" + strings[0] + ".txt";
            URL url = new URL(urlString);
            HttpURLConnection urlC = (HttpURLConnection) url.openConnection();


            // Set the 45 second timeout interval
            urlC.setConnectTimeout(45000);
            urlC.setReadTimeout(45000);
            urlC.connect();

            // Open the stream
            InputStream in = urlC.getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in));
            String s;
            // Skip first line of CSV (which is all just headers)
            streamReader.readLine();

            // Parse the line of stock data
            while (streamReader.ready()) {
                s = streamReader.readLine();
                // Split data
                // Example data:
                // Date,Open,High,Low,Close,Volume,Adj Close
                // 2017-01-20,36.759998,37.029999,36.580002,36.939999,23536700,36.939999

                String[] sSplit = s.split(",");
                StockData newDataEntry = new StockData();

                // Parse the date
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                newDataEntry.Date = df.parse(sSplit[0]);

                // Parse the open price
                newDataEntry.Open = Float.parseFloat(sSplit[1]);
                // Parse the High price
                newDataEntry.High = Float.parseFloat(sSplit[2]);
                // Parse the Low price
                newDataEntry.Low = Float.parseFloat(sSplit[3]);
                // Parse the Close price
                newDataEntry.Close = Float.parseFloat(sSplit[4]);
                // Parse the Volume price
                newDataEntry.Volume = Float.parseFloat(sSplit[5]);
                // Parse the Adjust Close price
                newDataEntry.AdjClose = Float.parseFloat(sSplit[6]);

                listOfStockData.add(newDataEntry);
            }
        } catch (SocketTimeoutException e) {
            errorDownloading = true;
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        // Download text file
        // Parse it
        // Put it into list
        // Return parsed list
        return listOfStockData;
    }

    // We'll be overriding this in the main program, so this is set empty
    @Override
    protected void onPostExecute(ArrayList<StockData> data) {
        super.onPostExecute(data);
        // my code
    }

}
