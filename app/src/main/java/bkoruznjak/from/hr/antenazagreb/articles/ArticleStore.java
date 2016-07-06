package bkoruznjak.from.hr.antenazagreb.articles;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.List;

import bkoruznjak.from.hr.antenazagreb.constants.NetworkConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.ArticleModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bkoruznjak on 06/07/16.
 */
public class ArticleStore{

    public ArticleStore(){

    }

    public void fetchAllArticles(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(NetworkConstants.API_URI.concat(NetworkConstants.API_KEY))
                        .build();
                try{
                    Response response = client.newCall(request).execute();
                    List<ArticleModel> articleList = LoganSquare.parseList(response.body().byteStream(),ArticleModel.class);

                    for (ArticleModel article : articleList){
                        Log.d("BBB", ""+article.id);
                        Log.d("BBB", ""+article.published_at);
                        List<ArticleModel.Image> articleImagesList = article.images;
                        for (ArticleModel.Image articleImage : articleImagesList){
                            Log.d("BBB", articleImage.file_name);
                            Log.d("BBB", ""+articleImage.id);
                            Log.d("BBB", articleImage.url.toString());
                        }
                    }

                }catch(IOException IoEx){
                    Log.e("BBB", IoEx.toString());
                }
            }
        }).start();
    }

    public static void registerTypeConverters(){

    }
}
