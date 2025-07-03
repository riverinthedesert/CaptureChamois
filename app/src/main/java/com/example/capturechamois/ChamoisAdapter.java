package com.example.capturechamois;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChamoisAdapter extends RecyclerView.Adapter<ChamoisViewHolder> {
    // FOR DATA
    private List<Chamois> chamoiS;

    // CONSTRUCTOR
    public ChamoisAdapter(List<Chamois> chamoisList) {
        this.chamoiS = chamoisList;
    }

    @Override
    public ChamoisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.avtivity_captures_chamois,parent, false);

        return new ChamoisViewHolder(view);
    }

    // UPDATE VIEW HOLDER WITH A GITHUBUSER
    @Override
    public void onBindViewHolder(ChamoisViewHolder viewHolder, int position) {
        viewHolder.updateWithChamois(this.chamoiS.get(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.chamoiS.size();
    }
}
