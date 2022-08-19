package com.simona.pastebin3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPaste extends RecyclerView.Adapter<AdapterPaste.ViewHolderPaste> {

    Context context;
    ArrayList<Paste> postsArray;
    ManagePastesInterface managePastesInterface;

    public AdapterPaste(Context context, ArrayList<Paste> postsArray,
                        ManagePastesInterface managePastesInterface) {
        this.context = context;
        this.postsArray = postsArray;
        this.managePastesInterface = managePastesInterface;
    }

    class ViewHolderPaste extends RecyclerView.ViewHolder{

        TextView titleTV, contentTV, userTV, postDateTV, numberOfViewsTV, expirationDateTV;
        ManagePastesInterface mpi;

        public ViewHolderPaste(@NonNull View itemView, ManagePastesInterface mpi) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTV);
            contentTV = itemView.findViewById(R.id.contentTV);
            userTV = itemView.findViewById(R.id.userTV);
            postDateTV = itemView.findViewById(R.id.postDateTV);
            numberOfViewsTV = itemView.findViewById(R.id.viewsTV);
            expirationDateTV = itemView.findViewById(R.id.expirationDateTV);
            this.mpi = mpi;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedId = getAdapterPosition();
                    if (clickedId != RecyclerView.NO_POSITION){
                        mpi.clickToViewPaste(getAdapterPosition());
                    } else {
                        return;
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int clickedId = getAdapterPosition();
                    if (clickedId != RecyclerView.NO_POSITION){
                        mpi.longClickToUpdatePaste(getAdapterPosition());
                    } else {
                        return false;
                    }
                    return true;
                }
            });

        }
    }


    @NonNull
    @Override
    public ViewHolderPaste onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ViewHolderPaste(v, managePastesInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPaste holder, int position) {
        Paste currentPost = postsArray.get(position);
        holder.titleTV.setText(currentPost.getPostTitle());
        holder.contentTV.setText(currentPost.getPostContent());

        holder.userTV.setText(currentPost.getUserName());
        holder.postDateTV.setText(currentPost.getPostDate());
        holder.expirationDateTV.setText(currentPost.getPostExpirationDate());
        holder.numberOfViewsTV.setText(String.valueOf(currentPost.getNumberOfViews()));
    }

    @Override
    public int getItemCount() {
        if (postsArray.size() != 0){
            return postsArray.size();
        }
        return 0;
    }


}
