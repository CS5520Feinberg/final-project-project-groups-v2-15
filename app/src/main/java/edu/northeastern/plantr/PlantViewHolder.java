package edu.northeastern.plantr;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PlantViewHolder extends RecyclerView.ViewHolder{
    public final TextView plantTV;
    public final TextView plantSpeciesTV;

    public PlantViewHolder(@NonNull View itemView, final PlantClickListener listener){
        super(itemView);
        this.plantTV = itemView.findViewById(R.id.plantNameView);
        this.plantSpeciesTV = itemView.findViewById(R.id.plantSpeciesView);
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(listener != null){
                    int position = getLayoutPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}
