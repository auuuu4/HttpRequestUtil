package github.auuuu4.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 *
 * @author: m2on
 * @date: 2024/03/10/11:34
 * @description:
 */

public class HttpRequestUtil {
    private static final Logger logger = LogManager.getLogger(HttpRequestUtil.class);
    private static final CloseableHttpClient httpClient = getCloseClient();

    /**
    * @author: m2on
    * @date: 2024/3/11
    * @return : org.apache.http.impl.client.CloseableHttpClient
    * @description: 获取一个 CloseableHttpClient 用于默认连接，设置最大连接数 500，单个路由最大连接 100
    */
    private static CloseableHttpClient getCloseClient(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(500); // 连接最大数
        connectionManager.setDefaultMaxPerRoute(100); // 每个路由最大连接数
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom().build())
                .build();
    }


    /**
    * @author: m2on
    * @date: 2024/3/11
    * @description: 发送 Get 请求，默认接口超时时间为 60s
     * <pre>
     * 默认情况会自动在 headers 中添加以下字段：
     *  "Accept":"application/json"
     *  "Content-Type":"application/json"
     * 当手动设置值后会覆盖
     * </pre>
    * @param url  网址
    * @param headers 请求头，为 null 时表示不设置
    * @param params 请求体，为 null 时表示不设置
    * @return : java.lang.String 响应数据，JSON格式
    */
    public static String doGet(String url, Map<String,Object> headers,Map<String,Object> params){
        HttpGet get = new HttpGet(url);
        addParamsToRequest(get,params);
        addHeadersToRequest(get,headers);
        // 设置超时时间
        RequestConfig config = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
        get.setConfig(config);

        try (CloseableHttpResponse response = httpClient.execute(get)){
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("Get:"+url+"返回 statusCode:"+statusCode);
            return EntityUtils.toString(response.getEntity(),"utf-8");
        }catch (IOException e){
            logger.warn("Get:"+url+"发生错误");
            return null;
        }

    }

    /**
    * @author: m2on
    * @date: 2024/3/11
    * @param url : 网址
    * @param headers : 请求头
    * @return : java.lang.String
    * @description: 发送一个指定请求头的 Get 请求，调用了 {@link #doGet(String, Map, Map)}
    */
    public static String doGetWithHeaders(String url,Map<String,Object> headers){
        return doGet(url,headers,null);
    }

    /**
    * @author: m2on
    * @date: 2024/3/11
    * @param url : 网址
    * @param params : 请求体
    * @return : java.lang.String
    * @description: 发送一个指定请求体的 Get 请求，调用了 {@link #doGet(String, Map, Map)}
    */
    public static String doGetWithParams(String url,Map<String,Object> params){
        return doGet(url,null,params);
    }

    /**
    * @author: m2on
    * @date: 2024/3/11
    * @description: 发送一个 Get 请求，调用了 {@link #doGet(String, Map, Map)}
    * @param url : 网址
    * @return : java.lang.String 响应数据，JSON格式
    */

    public static String doGet(String url){
        return doGet(url,null,null);
    }
    /**
    * @author: m2on
    * @date: 2024/3/11
    * @description: 发送一个 Post ，默认超时时间 60s。
     * <pre>
     * 默认情况会自动在 headers 中添加以下字段：
     *  "Accept":"application/json"
     *  "Content-Type":"application/json"
     * 当手动设置值后会覆盖
     * </pre>
    * @param url : 网址
    * @param params : 请求体
    * @param headers : 请求头
    * @return : java.lang.String 响应数据，JSON格式
    */

    public static String doPost(String url,Map<String,Object> params,Map<String,Object> headers) {
        HttpPost post = new HttpPost(url);
        addHeadersToRequest(post,headers);
        addParamsToRequest(post,params);
        // 设置超时时间
        RequestConfig config = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
        post.setConfig(config);
        try (CloseableHttpResponse response = httpClient.execute(post)){
            logger.info(EntityUtils.toString(post.getEntity(),"utf-8"));

            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("Post:"+url+"返回 statusCode:"+statusCode);
            return EntityUtils.toString(response.getEntity(),"utf-8");
        }catch (IOException e){
            logger.warn("Post:"+url+"发生错误");
            return null;
        }
    }
    /**
    * @author: m2on
    * @date: 2024/3/11
    * @description: 发送一个 Post ，默认超时时间 60s，调用了 {@link #doPost(String, Map, Map)}
    * @param url : 网址
    * @param params : 请求体
    * @return : java.lang.String 响应数据，JSON格式
    */
    public static String doPostWithParams(String url,Map<String,Object> params){
        return doPost(url,params,null);
    }

    /**
    * @author: m2on
    * @date: 2024/3/11
    * @description: 发送一个 Post ，默认超时时间 60s，调用了 {@link #doPost(String, Map, Map)}
     * @param url : 网址
    * @param headers : 请求头
    * @return : java.lang.String 响应数据，JSON格式
    */
    public static String doPostWithHeaders(String url, Map<String,Object> headers){
        return doPost(url,null,headers);
    }

    /**
    * @author: m2on
    * @date: 2024/3/11
    * @param url : 网址
    * @return : java.lang.String
    * @description: 仅仅发送一个 Post 请求
    */
    public static String doPost(String url){
        return doPost(url,null,null);
    }
    /**
    * @author: m2on
    * @date: 2024/3/11
    * @description: 向请求中添加 headers，对 params 中的值调用 toString() 方法
    * @param request : 需要更改的请求
    * @param headers : 请求头，为 null 时不做动作
    * @return : void
    */
    private static void addHeadersToRequest(HttpRequestBase request, Map<String, Object> headers){
        if(headers == null) return ;
        if(!headers.containsKey("Accept"))
            headers.put("Accept", "application/json");
        if(!headers.containsKey("Content-Type"))
            headers.put("Content-Type", "application/json");
        for(String key: headers.keySet()){
            request.addHeader(key, headers.get(key).toString());
        }
    }
    /**
    * @author: m2on
    * @date: 2024/3/11
    * @description: 向 request 中添加 请求体
    * @param request : 需要更改的请求
    * @param params : 参数
    * @return : void
    */
    private static void addParamsToRequest(HttpRequestBase request,Map<String,Object> params){
        try {
            if(request instanceof HttpPost){
                HttpPost post = (HttpPost) request;
                if(params != null)
                    post.setEntity(new StringEntity(JSONObject.toJSONString(params)));
            }else{
                HttpGet get = (HttpGet) request;
                URIBuilder builder = new URIBuilder(get.getURI());
                if(params != null){
                    for(String key:params.keySet()){
                        builder.setParameter(key,params.get(key).toString());
                    }
                }
                request.setURI(builder.build());
            }
        }catch (UnsupportedEncodingException e){
            logger.warn("Post:"+request.getURI()+"设置请求体出错");
        }catch (URISyntaxException e){
            logger.warn("Get:"+request.getURI()+"设置请求体出错");
        }


    }


}
