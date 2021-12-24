package com.example.bookstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.BookDetailActivity;
import com.example.bookstore.Object.Book;
import com.example.bookstore.Object.Order;
import com.example.bookstore.OrderDetailActivity;
import com.example.bookstore.R;

import java.util.ArrayList;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.DataViewHolder>{
    private ArrayList<Order> orders;
    private Context context;

    public UserOrderAdapter(Context context, ArrayList<Order> orders){
        this.context = context;
        this.orders = orders;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_order_number, tv_order_address, tv_order_status;
        private LinearLayout itemLayout;
        public  DataViewHolder(View itemView){
            super(itemView);
            tv_order_number = (TextView) itemView.findViewById(R.id.order_number);
            tv_order_address = (TextView) itemView.findViewById(R.id.order_address);
            tv_order_status = (TextView) itemView.findViewById(R.id.order_status);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.order_item_layout);
        }

    }
    @NonNull
    @Override
    public UserOrderAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order, parent, false);
        UserOrderAdapter.DataViewHolder dataViewHolder = new UserOrderAdapter.DataViewHolder(itemView);
        return  dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderAdapter.DataViewHolder holder, int position) {

        Order order = orders.get(position);
        holder.tv_order_number.setText(order.getOrder_number());
        holder.tv_order_address.setText(order.getAddress());
        if (order.isPaid() == false){
            holder.tv_order_status.setText("Chưa thanh toán");
        }
        else {
            holder.tv_order_status.setText("Đã thanh toán");
        }
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.tv_order_number.getContext(), OrderDetailActivity.class);
                intent.putExtra("order_id", order.getOrder_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

}
