package com.example.bookstore.Adapter;

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
import com.example.bookstore.BookDetailActivity;
import com.example.bookstore.Object.Book;
import com.example.bookstore.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchBookDataAdapter extends RecyclerView.Adapter<SearchBookDataAdapter.DataViewHolder>{
    private ArrayList<Book> books;
    private Context context;

    public SearchBookDataAdapter(Context context, ArrayList<Book> books){
        this.context = context;
        this.books = books;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_book_name, tv_book_author, tv_book_cost;
        private ImageView iv_book_image;
        public  DataViewHolder(View itemView){
            super(itemView);
            iv_book_image = (ImageView) itemView.findViewById(R.id.iv_book_result_view_image);
            tv_book_name = (TextView) itemView.findViewById(R.id.tv_book_result_view_name);
            tv_book_cost = (TextView) itemView.findViewById(R.id.tv_book_result_view_cost);
        }

    }
    @NonNull
    @Override
    public SearchBookDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_result_view, parent, false);
        SearchBookDataAdapter.DataViewHolder dataViewHolder = new SearchBookDataAdapter.DataViewHolder(itemView);
        return  dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookDataAdapter.DataViewHolder holder, int position) {

        Book book = books.get(position);
        String book_name = books.get(position).getBook_name();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_book_name.setText(book_name);
        holder.tv_book_cost.setText(decimalFormat.format(book.getCost()) + " đ");
        String img_url = book.getBook_image();
        Glide.with(context)
                .load(img_url)
                .into(holder.iv_book_image);
        holder.iv_book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.iv_book_image.getContext(), BookDetailActivity.class);
                intent.putExtra("author",book.getAuthor());
                intent.putExtra("book_id",book.getBook_id());
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
