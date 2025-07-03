package com.example.capturechamois;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChamoisViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fragment_main_item_title)
    TextView textView;

    public ChamoisViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithChamois(Chamois chamois){
        this.textView.setText(chamois.getColorType());
    }
}
