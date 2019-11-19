package com.example.lifeprice;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifeprice.data.BookFragmentAdapter;
import com.example.lifeprice.data.BookListFragment;
import com.example.lifeprice.data.BookSource;
import com.example.lifeprice.data.GameFragment;
import com.example.lifeprice.data.MapViewFragment;
import com.example.lifeprice.data.WebViewFragment;
import com.example.lifeprice.data.model.Book;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class BookListMainActivity extends AppCompatActivity {

    private static final int CONTEXT_MENU_ITEM_NEW = 1;
    private static final int CONTEXT_MENU_ITEM_UPDATE = 2;
    private static final int CONTEXT_MENU_ITEM_DELETE = 3;
    private static final int CONTEXT_MENU_ITEM_ABOUT = 4;
    private static final int REQUEST_CODE_NEW_BOOK = 201;
    private static final int REQUEST_CODE_UPDATE_BOOK = 202;

    private ArrayList<Book> theBooks;
    //private ListView listViewSuper;
    private BooksArrayAdapter theAdaper;

    private BookSource bookSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_price_main);

        bookSource=new BookSource(this);
        theBooks=bookSource.load();

        if(theBooks.size()==0)
            //InitData();
            theBooks.add(new Book("当前没有书", R.drawable.q));
        theAdaper=new BooksArrayAdapter(this,R.layout.list_item_book, theBooks);

        BookFragmentAdapter myPageAdapter = new BookFragmentAdapter(getSupportFragmentManager());

        ArrayList<Fragment> datas = new ArrayList<Fragment>();
        datas.add(new BookListFragment(theAdaper));
        datas.add(new WebViewFragment());
        datas.add(new MapViewFragment());
        datas.add(new GameFragment());
        myPageAdapter.setData(datas);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");
        titles.add("游戏");
        myPageAdapter.setTitles(titles);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        // 将适配器设置进ViewPager
        viewPager.setAdapter(myPageAdapter);
        // 将ViewPager与TabLayout相关联
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v==findViewById(R.id.list_view_books)){
            int itemPosition=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            menu.setHeaderTitle(theBooks.get(itemPosition).getName());
            menu.add(0,CONTEXT_MENU_ITEM_NEW,0,"新建");
            menu.add(0,CONTEXT_MENU_ITEM_UPDATE,0,"修改");
            menu.add(0,CONTEXT_MENU_ITEM_DELETE,0,"删除");
            menu.add(0,CONTEXT_MENU_ITEM_ABOUT,0,"关于...");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_NEW_BOOK:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("edit_position", 0);
                    String name = data.getStringExtra("book_name");

                    theBooks.add(position, new Book(name, R.drawable.a4));
                    theAdaper.notifyDataSetChanged();

                    Toast.makeText(this, "新建成功！", Toast.LENGTH_SHORT).show();
                }
            case REQUEST_CODE_UPDATE_BOOK:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("edit_position", 0);
                    String name = data.getStringExtra("book_name");

                    Book book = theBooks.get(position);
                    book.setName(name);
                    theAdaper.notifyDataSetChanged();

                    Toast.makeText(this, "更新成功！", Toast.LENGTH_SHORT).show();
                }

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case CONTEXT_MENU_ITEM_NEW:{
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Intent intent=new Intent(BookListMainActivity.this, EditBookActivity.class);
                intent.putExtra("edit_position",menuInfo.position);
                startActivityForResult(intent,REQUEST_CODE_NEW_BOOK);

                break;
            }
            case CONTEXT_MENU_ITEM_UPDATE:{
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Book book = theBooks.get(menuInfo.position);

                Intent intent=new Intent(BookListMainActivity.this, EditBookActivity.class);
                intent.putExtra("edit_position",menuInfo.position);
                intent.putExtra("edit_name", book.getName());
                startActivityForResult(intent,REQUEST_CODE_UPDATE_BOOK);
                break;
            }
            case CONTEXT_MENU_ITEM_DELETE:{
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int itemPosition=menuInfo.position;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("警告：");
                builder.setMessage("你确认删除这条数据吗?");
                builder.setIcon(R.drawable.q);
                //点击对话框以外的区域是否让对话框消失
                builder.setCancelable(true);
                //设置正面按钮
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        theBooks.remove(itemPosition);
                        theAdaper.notifyDataSetChanged();
                        Toast.makeText(BookListMainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });
                //设置反面按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                break;
            }
            case CONTEXT_MENU_ITEM_ABOUT:{
                Toast.makeText(this, "版权所有by hyx!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookSource.save();
    }

    private void InitData() {
        /*fileDataSource=new FileDataSource(this);
        theBooks =fileDataSource.load();
        if(theBooks.size()==0)  //加载不成功
            theBooks.add(new Book("目前没有书",R.drawable.book_no_name));
            */
        theBooks =new ArrayList<Book>();
        theBooks.add(new Book("计算机网络", R.drawable.a1));
        theBooks.add(new Book("信息安全数学基础",R.drawable.a2));
        theBooks.add(new Book("密码学",R.drawable.a3));


    }

    public class BooksArrayAdapter extends ArrayAdapter<Book>
    {
        private  int resourceId;
        public BooksArrayAdapter(@NonNull BookListMainActivity context, @LayoutRes int resource, @NonNull ArrayList<Book> objects) {
            super(context, resource, objects);
            resourceId=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId,null);

            ImageView img = (ImageView)item.findViewById(R.id.image_view_book);
            TextView name = (TextView)item.findViewById(R.id.text_view_name);


            Book book_item = this.getItem(position);
            img.setImageResource(book_item.getPictureId());
            name.setText("书名："+ book_item.getName());


            return item;
        }
    }
}
