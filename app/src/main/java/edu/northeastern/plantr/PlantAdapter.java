package edu.northeastern.plantr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantr.R;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantViewHolder>{
    private final List<Plant> plants;
    private PlantClickListener listener;
    private final Context context;

    public PlantAdapter(List<Plant> plants, Context context){
        this.plants = plants;
        this.context = context;
    }
    public void setOnCLickListener(PlantClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_plants, parent, false);
        return new PlantViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position){
        Plant item = plants.get(position);
        holder.plantTV.setText(item.getName());
        holder.plantSpeciesTV.setText(item.getPlantSpecies());
    }
    @Override
    public int getItemCount() {
        return plants.size();
    }
}
