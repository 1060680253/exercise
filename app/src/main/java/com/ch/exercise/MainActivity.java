package com.ch.exercise;

import android.bluetooth.BluetoothDevice;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.wuhaojie.bthelper.BtHelperClient;
import top.wuhaojie.bthelper.Filter;
import top.wuhaojie.bthelper.MessageItem;
import top.wuhaojie.bthelper.OnSearchDeviceListener;
import top.wuhaojie.bthelper.OnSendMessageListener;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BtHelperClient btHelperClient;
    private ListView mListView;
    private VideoListAdapter mAdapter;
    private ImageLoader mImageLoader;
    static class ViewHolder {
        public TextView title;
        public TextView summary;
        public ImageView image;
        public TextView postTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArticleList = new ArrayList<Article>();
        final String  href = "http://jcodecraeer.com/plus/list.php?tid=4";
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for releaseapp
                .build();//开始构建
        ImageLoader.getInstance().init(config);
        mImageLoader = ImageLoader.getInstance();
        mAdapter = new VideoListAdapter();
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(position > 0 && position <= mArticleList.size()){
//                   Intent intent  = new Intent(MainActivity.this, articleDetailActivity.class);
//                   intent.putExtra("href", mArticleList.get(position - 1).getLink());
//                   startActivity(intent);
                }
            }
        });
        mListView.setAdapter(mAdapter);
        loadNewsList(href, 1, true);
//        btHelperClient = BtHelperClient.from(MainActivity.this);
//
//        btHelperClient.requestEnableBt();
//
//        btHelperClient.setFilter(new Filter() {
//            @Override
//            public boolean isCorrect(String response) {
//                return response.trim().length() >= 5;
//            }
//        });
//
//        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                btHelperClient.searchDevices(new OnSearchDeviceListener() {
//                    @Override
//                    public void onStartDiscovery() {
//                        Log.d(TAG, "onStartDiscovery()");
//                    }
//
//                    @Override
//                    public void onNewDeviceFounded(BluetoothDevice device) {
//                        Log.d(TAG, "new device: " + device.getName() + " " + device.getAddress());
//                    }
//
//                    @Override
//                    public void onSearchCompleted(List<BluetoothDevice> bondedList, List<BluetoothDevice> newList) {
//                        Log.d(TAG, "SearchCompleted: bondedList" + bondedList.toString());
//                        Log.d(TAG, "SearchCompleted: newList" + newList.toString());
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//
//
//            }
//        });
//
//
//        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                MessageItem item = new MessageItem("Hello World");
//                btHelperClient.sendMessage("20:15:03:18:08:63", item, true, new OnSendMessageListener() {
//                    @Override
//                    public void onSuccess(int status, String response) {
//                        Toast.makeText(MainActivity.this, "收到设备回应信息: " + response, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onConnectionLost(Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//
//            }
//        });


    }

    class VideoListAdapter extends BaseAdapter {
        private int mLastAnimatedPosition;
        public int getCount() {
            return mArticleList.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.item_article_list, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.postTime = (TextView) convertView.findViewById(R.id.postTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Article article = mArticleList.get(position);
            viewHolder.title.setText(article.getTitle());
            viewHolder.summary.setText(article.getSummary());
            viewHolder.postTime.setText(article.getPostTime());
            if(!article.getImageUrl().equals("")){
                mImageLoader.displayImage(article.getImageUrl(), viewHolder.image);
            }else{
                viewHolder.image.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    private ArrayList<Article> mArticleList;
    public ArrayList<Article>  parseArticleList(String href, final int page){
        ArrayList<Article> articleList = new ArrayList<Article>();
        try {
            href = _MakeURL(href, new HashMap<String, Object>(){{
                put("PageNo", page);
            }});
            Log.i("url","url = " + href);
            Document doc = Jsoup.connect(href).timeout(10000).get();
            Element masthead = doc.select("ul.archive-list").first();
            Elements articleElements =  masthead.select("li.archive-item");
            for(int i = 0; i < articleElements.size(); i++) {
                Article article = new Article();
                Element articleElement = articleElements.get(i);
                Element titleElement = articleElement.select("h3 a").first();
                Element summaryElement = articleElement.select("div.archive-info").first();
                Element imgElement = null;
                if(articleElement.select("img").size() != 0){
                    imgElement = articleElement.select("img").first();
                }
                Element timeElement = articleElement.select(".glyphicon-class").first();
                String url = "http://www.jcodecraeer.com" + titleElement.attr("href");
                String title = titleElement.text();
                String summary = summaryElement.text();
                if(summary.length() > 70)
                    summary = summary.substring(0, 70);
                String imgsrc = "";
                if(imgElement != null){
                    imgsrc  ="http://www.jcodecraeer.com" + imgElement.attr("src");
                }

                String postTime = timeElement.text();
                article.setTitle(title);
                article.setSummary(summary);
                article.setImageUrl(imgsrc);
                article.setPostTime(postTime);
                article.setUrl(url);
                articleList.add(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articleList;
    }

    private void loadNewsList(final String href ,final int page, final boolean isRefresh) {
        final Handler handler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    ArrayList<Article> articleList = (ArrayList<Article>)msg.obj;
                    if (isRefresh) {
                        mArticleList.clear();	//下拉刷新之前先将数据清空
//                        mListView.onRefreshComplete (new Date().toLocaleString());
                    }
                    for (Article article : articleList) {
                        mArticleList.add(article);
                    }
                    mAdapter.notifyDataSetChanged();
                    if (articleList.size() < 10) {
//                        mListView.onLoadingMoreComplete(true);
                    } else if (articleList.size() == 10) {
//                        mListView.onLoadingMoreComplete(false);
                    }
                }
            }
        };

        new Thread() {
            public void run() {
                Message msg = new Message();
                ArrayList<Article> articleList = new ArrayList<Article>();
                try {
                    articleList = parseArticleList(href, page);
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.what = 1;
                msg.obj = articleList;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?")<0)
            url.append('?');
        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            //不做URLEncoder处理
            //url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }
        return url.toString().replace("?&", "?");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btHelperClient.close();
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

}

