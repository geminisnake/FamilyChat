package com.alienleeh.familychat.utils.NetWork;

import android.os.Handler;

import com.alienleeh.familychat.NimTaskExecutor;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.LogUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * Created by AlienLeeH on 2016/7/4.
 */
public class NimHttpClient {
    private static final int MAX_CONNECTIONS = 10;
    private static final int WAIT_TIMEOUT = 5000;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;
    private static final int MAX_ROUTE_CONNECTIONS = 10;
    private static NimHttpClient instance;
    private boolean inited =false;
    private NimTaskExecutor executor;
    private ThreadSafeClientConnManager connManager;
    private DefaultHttpClient client;
    private Handler uiHandler;

    public static synchronized NimHttpClient getInstance() {
        if (instance == null)
            instance = new NimHttpClient();
        return instance;
    }

    public void init() {
        if (inited){
            return;
        }
        executor = new NimTaskExecutor("NIM_HTTP_TASK_EXECUTOR",new NimTaskExecutor.Config(1, 3, 10 * 1000, true));
        HttpParams httpParams = new BasicHttpParams();
        // 设置最大连接数
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_CONNECTIONS);
        // 设置获取连接的最大等待时间
        ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
        // 设置每个路由最大连接数
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS));
        // 设置连接超时时间
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
        // 设置读取超时时间
        HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());

        connManager = new ThreadSafeClientConnManager(httpParams, registry);
        client = new DefaultHttpClient(connManager, httpParams);

        uiHandler = new Handler(ApplicationUtils.getContext().getMainLooper());

        inited = true;
    }

    public void execute(String url, Map<String, String> headers, List<NameValuePair> bodyString, NimHttpCallback nimHttpCallback) {
        execute(url,headers,bodyString,true,nimHttpCallback);
    }

    private void execute(String url, Map<String, String> headers, List<NameValuePair> bodyString, boolean b, NimHttpCallback nimHttpCallback) {
        executor.execute(new NimHttpTask(url,headers,bodyString,true,nimHttpCallback));
    }

    public interface NimHttpCallback {
        void onResponse(String response,int code,String Exception);
    }

    private class NimHttpTask implements Runnable {
        private List<NameValuePair> jsonBody;
        private NimHttpCallback nimHttpCallback;
        private boolean post;
        private Map<String, String> headers;
        private String url;

        public NimHttpTask(String url, Map<String, String> headers, List<NameValuePair> bodyString, boolean post, NimHttpCallback nimHttpCallback) {
            this.url = url;
            this.headers = headers;
            this.jsonBody = bodyString;
            this.post = post;
            this.nimHttpCallback = nimHttpCallback;
        }

        @Override
        public void run() {
            String response = null;
            int errorCode = 0;
            try {
                response = post? post(url,headers,jsonBody) : get(url,headers);
            }catch (NimHttpException e){
                errorCode = e.getHttpCode();
            }finally {
                final String res = response;
                final int code = errorCode;
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (nimHttpCallback != null){
                            nimHttpCallback.onResponse(res,code,null);
                        }
                    }
                });
            }
        }
    }

    private String get(String url, Map<String, String> headers) {
        HttpResponse response;
        HttpGet request;
        try {
            request = new HttpGet(url);

            //add header
            request.addHeader("charset","utf-8");
            if (headers != null){
                for (Map.Entry<String, String> entry : headers.entrySet()){
                    request.addHeader(entry.getKey(),entry.getValue());
                }
            }

            response = client.execute(request);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine == null){
                LogUtils.e(this,"Statusline = null");
                throw new NimHttpException();
            }
            int statusCode = statusLine.getStatusCode();
            if (statusCode < 200 || statusCode > 299){
                throw new NimHttpException(statusCode);
            }
            return EntityUtils.toString(response.getEntity(),"utf-8");
        }catch (Exception e){
            if (e instanceof NimHttpException){
                throw (NimHttpException)e;
            }
            if (e instanceof UnknownHostException) {
                throw new NimHttpException(408);
            } else if(e instanceof SSLPeerUnverifiedException) {
                throw new NimHttpException(408);
            }
            throw new NimHttpException(e);
        }

    }

    private String post(String url, Map<String, String> headers, List<NameValuePair> jsonBody) {
        HttpResponse response;
        HttpPost request;

        try {
            request = new HttpPost(url);
            //add header
            if (headers != null){
                request.addHeader(ContactHttpDao.HEADER_KEY_APP_KEY,headers.get(ContactHttpDao.HEADER_KEY_APP_KEY));
                request.addHeader(ContactHttpDao.HEADER_USER_Nonce,headers.get(ContactHttpDao.HEADER_USER_Nonce));
                request.addHeader(ContactHttpDao.HEADER_CURTIME,headers.get(ContactHttpDao.HEADER_CURTIME));
                request.addHeader(ContactHttpDao.HEADER_CheckSUM,headers.get(ContactHttpDao.HEADER_CheckSUM));
                request.addHeader(ContactHttpDao.HEADER_CONTENT_TYPE,headers.get(ContactHttpDao.HEADER_CONTENT_TYPE));

            }

            //add body
            HttpEntity entity = null;
            if (jsonBody != null){
                entity = new UrlEncodedFormEntity(jsonBody);
            }if (entity != null){
                request.setEntity(entity);
            }
            //execute
            response = client.execute(request);
            //response
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusLine == null){
                LogUtils.e(this,"StatusLine is null");
                throw new NimHttpException();
            }
            if (statusCode < 200 || statusCode > 299){
                throw new NimHttpException(statusCode);
            }
            return EntityUtils.toString(response.getEntity(),"utf-8");
        }catch (Exception e){
            if (e instanceof NimHttpException){
                throw (NimHttpException)e;
            }
            if (e instanceof UnknownHostException){
                throw new NimHttpException(408);
            }
            throw new NimHttpException(e);
        }
    }
}
