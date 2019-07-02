package com.example.walid.driveshop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Adapters.MyOrdersAdapter;
import Shared.Order;
import Shared.Product;

public class MyOrdersActivity extends AppCompatActivity {

    public static String PRODUCTS_EXTRA_KEY = "products";
    public static String TAG = "waliiiid";
    RecyclerView recyclerView;
    MyOrdersAdapter adapter;
    ArrayList<Product> products;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Order> orders;
    Long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Log.d(TAG, "onCreate: ");
        orders = new ArrayList<Order>();
        recyclerView = findViewById(R.id.recycler);
        adapter = new MyOrdersAdapter(orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        userID = getSharedPreferences(LoginActivity.PREFFERENCES, MODE_PRIVATE).getLong(LoginActivity.ID_KEY, -1);
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new MyOrdersTask().execute(userID);
        });

        /*myCartButton = findViewById(R.id.order);
        myCartButton.setOnClickListener((l) -> {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra(PRODUCTS_EXTRA_KEY, cart);
            startActivityForResult(intent,0);
        });*/
        new MyOrdersTask().execute(userID);


       /* ArrayList<SubOrder>  tmpCart = (ArrayList<SubOrder>) getIntent().getSerializableExtra(MyOrdersActivity.PRODUCTS_EXTRA_KEY);
        if (tmpCart == null) {
            new MyOrdersTask().execute();
        } else {
            cart = tmpCart;
            if (cart.isEmpty())
                myCartButton.setEnabled(false);
        }*/

    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<SubOrder>  tmpCart = (ArrayList<SubOrder>) data.getSerializableExtra(MyOrdersActivity.PRODUCTS_EXTRA_KEY);
            if (tmpCart != null) {
                cart = tmpCart;
                if (cart.isEmpty())
                    myCartButton.setEnabled(false);
            }
        }
    }*/

/*
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
*/

  /*  private void addToCart(Product p, int q) {
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
*/


    public class MyOrdersTask extends AsyncTask<Long, Integer, Integer> {

        @Override
        protected Integer doInBackground(Long... longs) {
            try {
                Socket socket = new Socket(ProductsActivity.SERVER_ADDRESS, 5566);//
                Log.d(TAG, "MyOrders doInBackground: Connected");
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                InputStream is = socket.getInputStream();
                ObjectInputStream inputStream = new ObjectInputStream(is);
                //while ((inputStream.available()<1)) ;
                Log.d(TAG, "MyOrders doInBackground: Sending data");
                outputStream.writeLong(longs[0]);
                outputStream.flush();
                Log.d(TAG, "MyOrders doInBackground: data sent");
                while ((is.available()<1));
                try {
                    Order o;
                    orders.clear();
                    while ((o = (Order) (inputStream.readObject())) != null) {
                        Log.d(TAG, "MyOrders  doInBackground: " + o.getId() + " " + o.getCustomerId() + " " + o.getOrderState());
                        orders.add(o);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                Log.d(TAG, "Network problem: " + e.getMessage());
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
            adapter.setOrders(orders);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }


}