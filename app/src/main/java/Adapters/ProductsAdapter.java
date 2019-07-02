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

import Shared.Product;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {


    private ArrayList<Product> products;

    public ProductsAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_recycler_view_item,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.productName.setText(products.get(i).getName());
        myViewHolder.description.setText(products.get(i).getDescription());
        myViewHolder.productPrice.setText(""+products.get(i).getPrice()+" DA");
        myViewHolder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(products.get(i).getImg(),0,products.get(i).getImg().length));
        if(this.onItemClickListener != null)
            myViewHolder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(products.get(i));
                }
            });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView description;
        TextView productPrice;
        AppCompatImageView imageView;
        Button addButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            productPrice = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.img);
            addButton = itemView.findViewById(R.id.add);
        }
    }
}

