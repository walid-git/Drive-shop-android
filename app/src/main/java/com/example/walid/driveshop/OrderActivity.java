package com.example.walid.driveshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Adapters.OrderAdapter;
import Shared.Order;
import Shared.SubOrder;

public class OrderActivity extends AppCompatActivity {

    public static String TAG = "waliiiid";
    RecyclerView recyclerView;
    OrderAdapter adapter;
    ArrayList<SubOrder> cart;
    Button sendButton;
    TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        cart = (ArrayList<SubOrder>) getIntent().getSerializableExtra(ProductsActivity.PRODUCTS_EXTRA_KEY);
        recyclerView = findViewById(R.id.recycler);
        totalPriceTextView = findViewById(R.id.total);
        adapter = new OrderAdapter(cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        calculateTotal();
        sendButton = findViewById(R.id.order);
        sendButton.setOnClickListener((l) -> {

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to order these products ?")
                    .setPositiveButton("confirm", (d, w) -> {
                        new OrdersTask().execute(new Order(225, getSharedPreferences(LoginActivity.PREFFERENCES, MODE_PRIVATE).getLong(LoginActivity.ID_KEY, -1), cart));
                    })
                    .setNegativeButton("cancel", (d, w) -> {
                        d.cancel();
                    })
                    .show();


        });
        adapter.setOnItemClickListener((p -> {
            for (int i = 0; i < cart.size(); i++) {
                Intent intent = new Intent();
                intent.putExtra(ProductsActivity.PRODUCTS_EXTRA_KEY, cart);
                setResult(0, intent);
                if (cart.get(i).getProduct().getId() == p.getId())
                    cart.remove(i);
                adapter.notifyDataSetChanged();
                if (cart.isEmpty()) {
                    sendButton.setEnabled(false);
                }
            }
            calculateTotal();
        }));
        findViewById(R.id.back).setOnClickListener((i) -> this.onBackPressed());

    }

    private void calculateTotal() {
        int total = 0;
        for (SubOrder subOrder : cart) {
            total += subOrder.getQuantity() * subOrder.getProduct().getPrice();
        }
        totalPriceTextView.setText("" + total + " DA");
    }

    public class OrdersTask extends AsyncTask<Order, Integer, Integer> {
        @Override
        protected Integer doInBackground(Order... order) {
            try {
                Socket socket = new Socket(ProductsActivity.SERVER_ADDRESS, 5564);//walidserver.ddns.net
                Log.d(TAG, "doInBackground: Connected");
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                outputStream.writeObject(order[0]);
                outputStream.flush();
                System.out.println("Orders Task : order sent");
                boolean accepted = inputStream.readBoolean();
                inputStream.close();
                outputStream.close();
                socket.close();
                if (accepted)
                    return 0;
                else
                    return 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                startActivity(new Intent(OrderActivity.this, MyOrdersActivity.class));
                finish();
            } else if (integer == 1) {

                new AlertDialog.Builder(OrderActivity.this).setMessage("Some of the products you ordered are no more available, please refresh the products list and try again")
                .setPositiveButton("ok",(dialog, which) -> {startActivity(new Intent(OrderActivity.this, ProductsActivity.class));
                    finish();})
                .setCancelable(false)
                .show();

            }
        }
    }


}
