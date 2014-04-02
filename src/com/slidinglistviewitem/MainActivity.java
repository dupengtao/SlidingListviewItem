package com.slidinglistviewitem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.slidinglistviewitem.HorizontalScrollViewItem.ScrollViewListener;
/**
 * 
 * @author dupengtao88@gmail.com
 *
 * 2014-4-2
 */
public class MainActivity extends Activity {

    private MainActivity mContext;
    private ScrollViewListener scrollViewListener;
    private List<HorizontalScrollViewItem> list = new ArrayList<HorizontalScrollViewItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ScrollAdapter());
        lv.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                for (HorizontalScrollViewItem item : list) {
                    if (item.isMove()) {
                        item.scrollToRight();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
            }
        });
        scrollViewListener = new ScrollViewListener() {
            @Override
            public void onScrollChanged(HorizontalScrollViewItem scrollView, int x, int y, int oldx, int oldy) {
                for (HorizontalScrollViewItem item : list) {

                    if (item.isMove()) {
                        if (item != scrollView) {
                            item.scrollToRight();
                        }
                    }
                }
            }
        };
    }

    private View getDemoView1(){
        LinearLayout .LayoutParams params = new LinearLayout .LayoutParams(LinearLayout .LayoutParams.MATCH_PARENT,LinearLayout .LayoutParams.MATCH_PARENT);
        TextView  fristView = new TextView(mContext);
        fristView.setLayoutParams(params);
        fristView.setText("hello du !");
        fristView.setGravity(Gravity.CENTER);
        return fristView;
    }
    
    private View getDemoView2() {
        LinearLayout .LayoutParams params = new LinearLayout .LayoutParams(LinearLayout .LayoutParams.MATCH_PARENT,LinearLayout .LayoutParams.MATCH_PARENT);
        LinearLayout secondView = new LinearLayout(mContext);
        secondView.setLayoutParams(params);
        secondView.setGravity(Gravity.CENTER);
        LinearLayout .LayoutParams params2 = new LinearLayout .LayoutParams(LinearLayout .LayoutParams.WRAP_CONTENT,LinearLayout .LayoutParams.WRAP_CONTENT);
        Button btn1 = new Button(mContext);
        btn1.setLayoutParams(params2);
        btn1.setText("Delete");
        secondView.addView(btn1);
        Button btn2 = new Button(mContext);
        btn2.setLayoutParams(params2);
        btn2.setText("updata");
        secondView.addView(btn2);
        Button btn3 = new Button(mContext);
        btn3.setLayoutParams(params2);
        btn3.setText("frivate");
        secondView.addView(btn3);
        return secondView;
    }

    static class ViewHolder {
        HorizontalScrollViewItem scrollViewItem;
    }

    class ScrollAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                HorizontalScrollViewItem scrollViewItem = new HorizontalScrollViewItem(mContext,getDemoView1(),getDemoView2());
                scrollViewItem.setScrollViewListener(scrollViewListener);
                list.add(scrollViewItem);
                convertView = scrollViewItem;
                holder.scrollViewItem = (HorizontalScrollViewItem) convertView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
