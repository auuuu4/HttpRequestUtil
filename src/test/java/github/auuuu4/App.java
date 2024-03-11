package github.auuuu4;

import github.auuuu4.utils.HttpRequestUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        Map<String,Object> params = new HashMap<>();
        params.put("qq",3414608521L);
        Map<String,Object> headers = new HashMap<>();
        headers.put("domain","qq.com");
        headers.put("ssl",0);

//        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");

        System.out.println(HttpRequestUtil.doGetWithParams("https://api.asilu.com/php/domain.php",headers));
//        CloseableHttpClient client = HttpClients.createDefault();
//        System.out.println(EntityUtils.toString(client.execute(new HttpGet("https://tenapi.cn/v2/getip?ip=47.97.16.195")).getEntity(),"utf-8"));

    }
}
