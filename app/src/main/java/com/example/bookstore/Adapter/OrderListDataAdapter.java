package com.example.bookstore.Adapter;

import android.content.Context;
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
import com.example.bookstore.CartActivity;
import com.example.bookstore.MainActivity;
import com.example.bookstore.Object.CartItem;
import com.example.bookstore.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderListDataAdapter extends RecyclerView.Adapter<OrderListDataAdapter.DataViewHolder>{
    private ArrayList<CartItem> orderItems;
    private Context context;

    public OrderListDataAdapter(Context context, ArrayList<CartItem> orderItems){
        this.context = context;
        this.orderItems = orderItems;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_order_item_book_name, tv_order_item_cost, tv_order_item_total_number;
        private ImageView iv_order_item_image;
        public  DataViewHolder(View itemView){
            super(itemView);

            iv_order_item_image = (ImageView) itemView.findViewById(R.id.iv_order_item_image);
            tv_order_item_book_name = (TextView) itemView.findViewById(R.id.tv_order_item_book_name);
            tv_order_item_cost = (TextView) itemView.findViewById(R.id.tv_order_item_cost);
            tv_order_item_total_number = (TextView) itemView.findViewById(R.id.tv_order_item_total_number);
        }
    }
    @NonNull
    @Override
    public OrderListDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        OrderListDataAdapter.DataViewHolder dataViewHolder = new OrderListDataAdapter.DataViewHolder(itemView);
        return  dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListDataAdapter.DataViewHolder holder, int position) {

        CartItem orderItem = orderItems.get(position);
        String book_name = orderItem.getBook_name();
        long cost = orderItem.getCost();
        Integer total_number = orderItem.getTotal_number();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_order_item_book_name.setText(book_name);
        holder.tv_order_item_cost.setText(decimalFormat.format(cost) + " đ");
        holder.tv_order_item_total_number.setText("Số lượng: " + total_number.toString());
        String img_url = orderItem.getBook_image();
        //Log.d("total_number",String.valueOf(total_number));
        Glide.with(context)
                .load(img_url)
                .into(holder.iv_order_item_image);

    }

    @Override
    public int getItemCount() {
        return orderItems == null ? 0 : orderItems.size();
    }
}
