/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.recyclerview;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Implements a basic RecyclerView that displays a list of generated words.
 * - Clicking an item marks it as clicked.
 * - Clicking the fab button adds a new word to the list.
 *
 *
 *
 *Check this out
 * https://www.journaldev.com/12372/android-recyclerview-example
 */
public class MainActivity extends AppCompatActivity {

    private final LinkedList<VolumeInfo> mWordList = new LinkedList<>();

    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int wordListSize = mWordList.size();
                // Add a new word to the wordList.
                //mWordList.addLast("+ Word " + wordListSize);
                // Notify the adapter, that the data has changed.
                mRecyclerView.getAdapter().notifyItemInserted(wordListSize);
                // Scroll to the bottom.
                mRecyclerView.smoothScrollToPosition(wordListSize);
            }
        });

        // Put initial data into the word list.
       /* for (int i = 0; i < 20; i++) {
            mWordList.addLast("Word " + i);
        }*/

        // Create recycler view.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        hakan sisir=new hakan(this,mWordList,mRecyclerView,mAdapter);
        sisir.execute();

    }

    /**
     * Inflates the menu, and adds items to the action bar if it is present.
     *
     * @param menu Menu to inflate.
     * @return Returns true if the menu inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles app bar item clicks.
     *
     * @param item Item clicked.
     * @return True if one of the defined items was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // This comment suppresses the Android Studio warning about simplifying
        // the return statements.
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
     public class hakan extends AsyncTask<Void, Void, LinkedList<VolumeInfo>>
     {   LinkedList<VolumeInfo> datalist ;
         Context con;
         RecyclerView mRecyclerViewrecycleisaret;
       WordListAdapter adapterisaret;

         public hakan(Context c, LinkedList<VolumeInfo> datalist, RecyclerView mRecyclerViewrecycleisaret, WordListAdapter adapterisaret) {
             con=c;
             this.datalist = datalist;
             this.mRecyclerViewrecycleisaret = mRecyclerViewrecycleisaret;
             this.adapterisaret = adapterisaret;
         }

         @Override
         protected LinkedList<VolumeInfo> doInBackground(Void... voids) {

            String returned= NetworkUtils.getBookInfo("otello");
             processreturned(returned);//since data list fieldof the main activity




             return datalist;
         }//doin backgrround

         private void processreturned(String returned) {
             JSONObject jsonObject = null;
             try {
                 jsonObject = new JSONObject(returned);
                 JSONArray itemsArray = jsonObject.getJSONArray("items");
                 for (int i=0;i<itemsArray.length();i++)
                 {
                     JSONObject book = itemsArray.getJSONObject(i);
                     JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                     // catch if either field is empty and move on.
                     try {
                        String title = volumeInfo.getString("title");
                        String authors = "";
                        for (int j=0;j<volumeInfo.getJSONArray("authors").length();j++) {
                           authors+= volumeInfo.getJSONArray("authors").toString();
                        }
                        String description=volumeInfo.getString("description");
                        int pagecount=volumeInfo.getInt("pageCount");
                        VolumeInfo inf=new VolumeInfo();
                        inf.setTitle(title);

                        LinkedList <String> auths=new LinkedList<String>();
                         for (int t=0;t<volumeInfo.getJSONArray("authors").length();t++)
                         {auths.add(volumeInfo.getJSONArray("authors").get(t).toString());

                         }
                         inf.setAuthors(auths);




                        datalist.add(inf);


                     } catch (Exception e){
                         e.printStackTrace();
                     }


                 }//for
             } catch (JSONException e) {
                 e.printStackTrace();
             }
             // Get the JSONArray of book items.





         }

         protected void onPostExecute(LinkedList<VolumeInfo> result) {




             mAdapter = new WordListAdapter(con, mWordList);
             // Connect the adapter with the recycler view.
             mRecyclerView.setAdapter(mAdapter);
             // Give the recycler view a default layout manager.
             mRecyclerView.setLayoutManager(new LinearLayoutManager(con));



         }
     }//async


}//main
