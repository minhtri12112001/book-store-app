package com.example.bookstore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> implements Filterable {

    private List<Book> mListBooks;
    private List<Book> mListBooksOld;

    public BookAdapter(List<Book> mListBooks) {
        this.mListBooks = mListBooks;
        this.mListBooksOld = mListBooks;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = mListBooks.get(position);
        if (book == null){
            return;
        }

        holder.imgBook.setImageResource(book.getImage());
        holder.tvName.setText(book.getName());
    }

    @Override
    public int getItemCount() {
        if (mListBooks != null) {
            return mListBooks.size();
        }
        return 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgBook;
        private TextView tvName;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook = itemView.findViewById(R.id.img_book);
            tvName = itemView.findViewById(R.id.tv_name);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    mListBooks = mListBooksOld;
                }
                else {
                    List<Book> list = new ArrayList<>();
                    for (Book book : mListBooksOld){
                        if (book.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(book);
                        }
                    }
                    mListBooks = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListBooks;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListBooks = (List<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
