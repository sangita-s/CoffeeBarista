package com.lyeng.coffeebarista;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeHolder> {
    private List<Coffee> listCoffee = new ArrayList<>();
    private OnItemCoffeeClickListener listener;

    public interface OnItemCoffeeClickListener {
        void onCoffeeItemClick(Coffee coffee);
    }

    public void setOnItemCoffeeClickListener(OnItemCoffeeClickListener listener) {
        this.listener = listener;
    }

    public Coffee getCoffeeAt(int pos) {
        return listCoffee.get(pos);
    }

    public void setCoffeelist(List<Coffee> listCoffee) {
        this.listCoffee = listCoffee;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoffeeHolder onCreateViewHolder(@NonNull ViewGroup pViewGroup, int pI) {
        View itemView = LayoutInflater.from(pViewGroup.getContext())
                .inflate(R.layout.coffee_item, pViewGroup, false);
        return new CoffeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeHolder pCoffeeHolder, final int pI) {
        Coffee selectedCoffee = listCoffee.get(pI);

        pCoffeeHolder.tv_coffeeName.setText(selectedCoffee.getCoffeeName());
        pCoffeeHolder.tv_coffeeOrigin.setText(selectedCoffee.getCoffeeOrigin());

        String steepTimeString = selectedCoffee.getSteepTime();
        if (selectedCoffee.getSteepTime().equals("1")) steepTimeString += " minute";
        else steepTimeString += " minutes";
        pCoffeeHolder.tv_steepTIme.setText(steepTimeString);

    }

    @Override
    public int getItemCount() {
        return listCoffee.size();
    }


    public class CoffeeHolder extends RecyclerView.ViewHolder {
        private TextView tv_coffeeName;
        private TextView tv_coffeeOrigin;
        private TextView tv_steepTIme;
        private ImageView del;

        public CoffeeHolder(final View itemView) {
            super(itemView);
            tv_coffeeName = itemView.findViewById(R.id.tv_CoffeeName);
            tv_coffeeOrigin = itemView.findViewById(R.id.tvCoffeeOrigin);
            tv_steepTIme = itemView.findViewById(R.id.tv_coffeeSteepTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION)
                        listener.onCoffeeItemClick(listCoffee.get(pos));
                }
            });
        }
    }

}
