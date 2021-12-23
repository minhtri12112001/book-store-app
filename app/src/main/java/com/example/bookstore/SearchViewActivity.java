//package com.example.bookstore;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.google.android.material.snackbar.Snackbar;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//
//import androidx.appcompat.widget.SearchView;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.app.SearchManager;
//
//import com.example.bookstore.databinding.ActivitySearchViewBinding;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SearchViewActivity extends AppCompatActivity {
//
//    private AppBarConfiguration appBarConfiguration;
//    private ActivitySearchViewBinding binding;
//    private RecyclerView rcvBooks;
//    private BookAdapter bookAdapter;
//    private SearchView searchView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_view);
//
//        rcvBooks = findViewById(R.id.rcv_books);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rcvBooks.setLayoutManager(linearLayoutManager);
//
//        bookAdapter = new BookAdapter(getListBooks());
//        rcvBooks.setAdapter(bookAdapter);
//
//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        rcvBooks.addItemDecoration(itemDecoration);
//
//        binding = ActivitySearchViewBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_search_view);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    private List<Book> getListBooks() {
//        List<Book> list = new ArrayList<>();
//        list.add(new Book(R.drawable.tuong_lai_cua_quyen_luc, "Tương lai của quyền lực"));
//        list.add(new Book(R.drawable.viet_su_giai_thoai, "Việt sử giai thoại"));
//        list.add(new Book(R.drawable.oc_sang_suot, "Óc sáng suốt"));
//        list.add(new Book(R.drawable.sapiens, "Sapiens"));
//        list.add(new Book(R.drawable.face_book, "Facebook"));
//        return list;
//    }
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.searchview_menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Search");
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                bookAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                bookAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (!searchView.isIconified()){
//            searchView.setIconified(true);
//            return;
//        }
//        super.onBackPressed();
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_search_view);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//}