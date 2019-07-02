package com.example.walid.driveshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Adapters.ProductsAdapter;
import Shared.Product;
import Shared.SubOrder;

import static com.example.walid.driveshop.LoginActivity.PREFFERENCES;

public class ProductsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SERVER_ADDRESS = "walidserver.ddns.net";//"walidserver.ddns.net"; "192.168.43.63";
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.d(TAG, "onCreate: ");
        cart = new ArrayList<SubOrder>();
        products = new ArrayList<Product>();
        recyclerView = findViewById(R.id.recycler);
        adapter = new ProductsAdapter(products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new ProductsActivity.ProductsTask().execute();
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
        new ProductsActivity.ProductsTask().execute();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myCart) {
            if (!cart.isEmpty()) {
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra(PRODUCTS_EXTRA_KEY, cart);
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(this, "Cart is empty !", Toast.LENGTH_SHORT).show();
            } 
            
        } else if (id == R.id.myOrders) {
            startActivity(new Intent(this,MyOrdersActivity.class));
        } else if (id == R.id.logout) {
            SharedPreferences preferences = getSharedPreferences(PREFFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit().remove(LoginActivity.ID_KEY);
            editor.commit();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void showAddDialog(Product p) {
        AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_main_product_add);
        dialog.setCancelable(false);
        dialog.show();
        EditText quantity = dialog.findViewById(R.id.quantity);
        dialog.findViewById(R.id.plus).setOnClickListener((i) -> {
            String str = quantity.getText().toString();
            quantity.setText( (str.contentEquals("") ? "1" :
                    (str.contentEquals(p.getAvailable_qty()+"")? str : ""+ (Integer.parseInt(str) + 1))));
        });
        dialog.findViewById(R.id.minus).setOnClickListener((i) -> {
            String str = quantity.getText().toString();
            quantity.setText((str.contentEquals("") ? "1" : "" + (Integer.parseInt(str) - 1 < 1 ? 1 : Integer.parseInt(str) - 1)));
        });
        dialog.findViewById(R.id.add).setOnClickListener((i) -> {
            String str = quantity.getText().toString();
            if (str.contentEquals("") || Integer.parseInt(str) < 1)
                Toast.makeText(dialog.getContext(), "Insert a correct value", Toast.LENGTH_SHORT).show();
            else if (Integer.parseInt(str) > p.getAvailable_qty()) {
                Toast.makeText(this, "Max available quantity for this product is "+p.getAvailable_qty(), Toast.LENGTH_SHORT).show();
                quantity.setText(p.getAvailable_qty()+"");
            }
            else {
                addToCart(p, Integer.parseInt(str));
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.cancel).setOnClickListener((i) -> {
            dialog.cancel();
        });
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
                Log.d(TAG, "Network problem: "+e.getMessage());
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
