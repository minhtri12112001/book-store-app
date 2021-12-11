package com.example.bookstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.BookDetailActivity;
import com.example.bookstore.CartActivity;
import com.example.bookstore.MainActivity;
import com.example.bookstore.Object.Book;
import com.example.bookstore.Object.CartItem;
import com.example.bookstore.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartItemDataAdapter extends RecyclerView.Adapter<CartItemDataAdapter.DataViewHolder>{
    private ArrayList<CartItem> cartItems;
    private Context context;

    public CartItemDataAdapter(Context context, ArrayList<CartItem> cartItems){
        this.context = context;
        this.cartItems = cartItems;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_cart_item_book_name, tv_cart_item_total_cost;
        private ImageView iv_cart_item_image;
        private EditText et_cart_item_total_number;
        private ImageView btn_minus, btn_plus, btn_delete;
        public  DataViewHolder(View itemView){
            super(itemView);

            iv_cart_item_image = (ImageView) itemView.findViewById(R.id.iv_cart_item_image);
            tv_cart_item_book_name = (TextView) itemView.findViewById(R.id.tv_cart_item_book_name);
            tv_cart_item_total_cost = (TextView) itemView.findViewById(R.id.tv_cart_item_total_cost);
            et_cart_item_total_number = (EditText) itemView.findViewById(R.id.et_cart_item_total_number);
            btn_minus = (ImageView) itemView.findViewById(R.id.btn_minus);
            btn_plus = (ImageView)  itemView.findViewById(R.id.btn_plus);
            btn_delete = (ImageView) itemView.findViewById(R.id.btn_delete);
        }
    }
    @NonNull
    @Override
    public CartItemDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        CartItemDataAdapter.DataViewHolder dataViewHolder = new CartItemDataAdapter.DataViewHolder(itemView);
        return  dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemDataAdapter.DataViewHolder holder, int position) {

        CartItem cartItem = cartItems.get(position);
        String book_name = cartItem.getBook_name();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long cost = cartItem.getCost();
        Integer total_number = cartItem.getTotal_number();
        if (cartItem.getTotal_number() >= 10){
            cartItem.setTotal_number(9);
            total_number = cartItem.getTotal_number();
        }
        holder.tv_cart_item_book_name.setText(book_name);
        holder.tv_cart_item_total_cost.setText(decimalFormat.format(cost) + " đ");
        holder.et_cart_item_total_number.setText(total_number.toString());
        String img_url = cartItem.getBook_image();
        Log.d("total_number",String.valueOf(total_number));
        Glide.with(context)
                .load(img_url)
                .into(holder.iv_cart_item_image);

        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = cartItem.getTotal_number();
                value = value - 1;
                if(value < 1){
                    value = 1;
                }
                cartItem.setTotal_number(value);
                Log.d("total_number",String.valueOf(cartItem.getTotal_number()));
                holder.et_cart_item_total_number.setText(String.valueOf(value));
                CartActivity.sumTotalCost();
            }
        });
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer value = cartItem.getTotal_number();
                value = value + 1;
                if (value >= 10){
                    value = 9;
                }
                cartItem.setTotal_number(value);
                Log.d("total_number",String.valueOf(cartItem.getTotal_number()));
                holder.et_cart_item_total_number.setText(String.valueOf(value));
                CartActivity.sumTotalCost();
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cartItems.remove(cartItem);
                CartActivity.cartItemDataAdapter.notifyDataSetChanged();
                CartActivity.checkData();
                CartActivity.sumTotalCost();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems == null ? 0 : cartItems.size();
    }
}
