package com.example.capturechamois;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Captures extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Bitmap> mDatas;
    private HomeAdapter mAdapter;

    /**
     * @param savedInstanceState saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captures);

        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.fragment_main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());

    }

    /**
     * ajouter bitmap
     */
    protected void initData()
    {
        try {
            this.mDatas = new ArrayList<>();
            FileInputStream fis = openFileInput("image.data");
            Bitmap bm = BitmapFactory.decodeStream(fis);
            mDatas.add(bm);
        }
        catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    /**
     * adapteur
     */
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    Captures.this).inflate(R.layout.avtivity_captures_chamois, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.tv.setImageBitmap(mDatas.get(position));
            holder.tv2.setText("votre "+position+1+" capture");
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            ImageView tv;
            TextView tv2;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (ImageView) view.findViewById(R.id.fragment_main_item_title);
                tv2 = (TextView) view.findViewById(R.id.fragment_main_item_title_indice);
            }
        }
    }

}



