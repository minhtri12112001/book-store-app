package com.example.bookstore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.Object.Book;

import java.util.ArrayList;
import java.util.List;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.DataViewHolder> {

    private ArrayList<Book> books;
    private Context context;

    public RecyclerDataAdapter(Context context, ArrayList<Book> books){
        this.context = context;
        this.books = books;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_book_name;
        private ImageView iv_book_image;
        public  DataViewHolder(View itemView){
            super(itemView);
            iv_book_image = (ImageView) itemView.findViewById(R.id.iv_book_image);
            tv_book_name = (TextView) itemView.findViewById(R.id.tv_book_name);
        }

    }
    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_view, parent, false);
        DataViewHolder dataViewHolder = new DataViewHolder(itemView);
        return  dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        Book book = books.get(position);
        String book_name = books.get(position).getBook_name();
        String book_id = books.get(position).getBook_id();
        holder.tv_book_name.setText(book_id);
        String img_url = book.getBook_image();
        Glide.with(context)
                .load(img_url)
                .into(holder.iv_book_image);
        holder.iv_book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.iv_book_image.getContext(),BookDetailActivity.class);
                intent.putExtra("book_name",book.getBook_name());
                intent.putExtra("book_cost",book.getCost());
                intent.putExtra("book_image",book.getBook_image());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.iv_book_image.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }


}