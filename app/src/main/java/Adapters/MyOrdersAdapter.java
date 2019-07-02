package Adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.walid.driveshop.ItemClickListener;
import com.example.walid.driveshop.R;

import java.util.ArrayList;

import Shared.Order;
import Shared.Product;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {


    private ArrayList<Order> orders;

    public MyOrdersAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_orders_view_item,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
       myViewHolder.orderId.setText("Order nº "+orders.get(i).getId());
       myViewHolder.orderState.setText(orders.get(i).getOrderState()+"");
        switch (orders.get(i).getOrderState()) {
            case NEW:
                myViewHolder.orderState.setTextColor(0xFF005599);
                break;
            case READY:
                myViewHolder.orderState.setTextColor(0xFF00aa00);
                break;
            case PENDING:
                myViewHolder.orderState.setTextColor(0xFF9f0000);
                break;
            case IN_PROGRESS:
                myViewHolder.orderState.setTextColor(0xFFFFAA00);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView orderState;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderState = itemView.findViewById(R.id.state);

        }
    }
}

