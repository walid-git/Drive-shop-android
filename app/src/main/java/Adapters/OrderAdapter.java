package Adapters;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.walid.driveshop.ItemClickListener;
import com.example.walid.driveshop.R;

import java.util.ArrayList;

import Shared.SubOrder;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {


    private ArrayList<SubOrder> subOrders;

    public OrderAdapter(ArrayList<SubOrder> cart) {
        this.subOrders = cart;
    }

    public ArrayList<SubOrder> getSubOrders() {
        return subOrders;
    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

    public void setSubOrders(ArrayList<SubOrder> subOrders) {
        this.subOrders = subOrders;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_recycler_view_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.productName.setText(subOrders.get(i).getProduct().getName());
        myViewHolder.description.setText(subOrders.get(i).getProduct().getDescription());
        myViewHolder.quantity.setText("" + subOrders.get(i).getQuantity());
        myViewHolder.totalPrice.setText("" + subOrders.get(i).getQuantity()*subOrders.get(i).getProduct().getPrice() + " DA");
        myViewHolder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(subOrders.get(i).getProduct().getImg(), 0, subOrders.get(i).getProduct().getImg().length));
        if (this.onItemClickListener != null)
            myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(subOrders.get(i).getProduct());
                }
            });
    }

    @Override
    public int getItemCount() {
        return subOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView description;
        TextView quantity;
        TextView totalPrice;
        AppCompatImageView imageView;
        Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            quantity = itemView.findViewById(R.id.qty);
            totalPrice = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.img);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}

