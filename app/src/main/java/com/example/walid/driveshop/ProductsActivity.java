package com.example.walid.driveshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import Adapters.ProductsAdapter;
import Util.Product;
import Util.SubOrder;

public class ProductsActivity extends AppCompatActivity {

    public static final String SERVER_ADDRESS = "192.168.43.63" +
            "";//"walidserver.ddns.net";
    public static String PRODUCTS_EXTRA_KEY = "products";
    public static String TAG = "waliiiid";
    RecyclerView recyclerView;
    ProductsAdapter adapter;
    ArrayList<Product> products;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<SubOrder> cart;
    Button myCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Log.d(TAG, "onCreate: ");
        cart = new ArrayList<SubOrder>();
        products = new ArrayList<Product>();
        recyclerView = findViewById(R.id.recycler);
        adapter = new ProductsAdapter(products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new ProductsTask().execute();
        });
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(Product p) {
                showAddDialog(p);
            }
        });
        myCartButton = findViewById(R.id.order);
        myCartButton.setOnClickListener((l) -> {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra(PRODUCTS_EXTRA_KEY, cart);
            startActivityForResult(intent,0);
        });
        new ProductsTask().execute();
        /*
        ArrayList<SubOrder>  tmpCart = (ArrayList<SubOrder>) getIntent().getSerializableExtra(ProductsActivity.PRODUCTS_EXTRA_KEY);
        if (tmpCart == null) {
            new ProductsTask().execute();
        } else {
            cart = tmpCart;
            if (cart.isEmpty())
                myCartButton.setEnabled(false);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<SubOrder>  tmpCart = (ArrayList<SubOrder>) data.getSerializableExtra(ProductsActivity.PRODUCTS_EXTRA_KEY);
            if (tmpCart != null) {
                cart = tmpCart;
                if (cart.isEmpty())
                    myCartButton.setEnabled(false);
            }
        }
    }

    private void showAddDialog(Product p) {
        AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_main_product_add);
        dialog.setCancelable(false);
        dialog.show();
        EditText quantity = dialog.findViewById(R.id.quantity);
        dialog.findViewById(R.id.plus).setOnClickListener((i) -> {
            String str = quantity.getText().toString();
            quantity.setText((str.contentEquals("") ? "1" : "" + (Integer.parseInt(str) + 1)));
        });
        dialog.findViewById(R.id.minus).setOnClickListener((i) -> {
            String str = quantity.getText().toString();
            quantity.setText((str.contentEquals("") ? "1" : "" + (Integer.parseInt(str) - 1 < 1 ? 1 : Integer.parseInt(str) - 1)));
        });
        dialog.findViewById(R.id.add).setOnClickListener((i) -> {
            String str = quantity.getText().toString();
            if (str.contentEquals("") || Integer.parseInt(str) < 1)
                Toast.makeText(dialog.getContext(), "Insert a correct value", Toast.LENGTH_SHORT).show();
            else {
                addToCart(p, Integer.parseInt(str));
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener((i) -> {
            dialog.cancel();
        });
    }

    private void addToCart(Product p, int q) {
        if (cart.isEmpty()) {
            myCartButton.setEnabled(true);
        }
        for (SubOrder subOrder : cart) {
            if (subOrder.getProduct().getId()==p.getId()) {
              subOrder.setQuantity(subOrder.getQuantity()+q);
              return;
            }
        }
        cart.add(new SubOrder(p, q));
    }



    public class ProductsTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                Socket socket = new Socket(SERVER_ADDRESS, 5563);//
                Log.d(TAG, "doInBackground: Connected");
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Log.d(TAG, "doInBackground: waiting for input ");
                //while ((inputStream.available()<1)) ;
                Log.d(TAG, "doInBackground: input available " + inputStream.available());
                products.clear();
                Product p;

                try {
                    while ((p = (Product) (inputStream.readObject())) != null) {
                        products.add(p);
                        Log.d(TAG, "" + (p + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Network problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            adapter.setProducts(products);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}

