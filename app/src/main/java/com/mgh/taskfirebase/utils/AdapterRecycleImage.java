package com.mgh.taskfirebase.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mgh.taskfirebase.Model.ImageModel;
import com.mgh.taskfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterRecycleImage extends RecyclerView.Adapter<AdapterRecycleImage.holder> {
    Context mcontext;
    ArrayList<ImageModel> imageModelArrayList;

    public AdapterRecycleImage(Context mcontext, ArrayList<ImageModel> imageModelArrayList) {

        this.mcontext = mcontext;
        this.imageModelArrayList = imageModelArrayList;
    }


    @Override
    public holder onCreateViewHolder( ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(mcontext).inflate(R.layout.item_recycle_layout,viewGroup,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder( holder holder, int i) {

        ImageModel imageModel= imageModelArrayList.get(i);
        Picasso.with(mcontext)
                .load(Uri.parse(imageModel.getUriImage()))
                .into(holder.imageView);

        holder.textView.setText(imageModel.getNameImage());

    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }


    public class holder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public holder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tv_recycleItem);
            imageView=itemView.findViewById(R.id.img_recycleItem);

        }
    }
}
