package com.example.bookstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.BookDetailActivity;
import com.example.bookstore.BookListByCategoryActivity;
import com.example.bookstore.Object.Book;
import com.example.bookstore.Object.Category;
import com.example.bookstore.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BookCategoryDataAdapter extends RecyclerView.Adapter<BookCategoryDataAdapter.DataViewHolder>{
    //private HashSet<String> bookCategories;
    private ArrayList<Category> bookCategories;
    private Context context;

    public BookCategoryDataAdapter(Context context, ArrayList<Category> bookCategories){
        this.context = context;
        this.bookCategories = bookCategories;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_category_item;
        public  DataViewHolder(View itemView){
            super(itemView);
            tv_category_item = (TextView) itemView.findViewById(R.id.tv_category_item);
        }

    }
    @NonNull
    @Override
    public BookCategoryDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        BookCategoryDataAdapter.DataViewHolder dataViewHolder = new BookCategoryDataAdapter.DataViewHolder(itemView);
        return  dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookCategoryDataAdapter.DataViewHolder holder, int position) {

        Category bookCategory = bookCategories.get(position);
        holder.tv_category_item.setText(bookCategory.getCategory_name());
        //String book_id = books.get(position).getBook_id();
        //holder.tv_book_name.setText(book_id);
        holder.tv_category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.tv_category_item.getContext(), BookListByCategoryActivity.class);
                intent.putExtra("category_id",bookCategory.getCategory_id());
                intent.putExtra("category_name",bookCategory.getCategory_name());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.tv_category_item.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookCategories == null ? 0 : bookCategories.size();
    }
}
