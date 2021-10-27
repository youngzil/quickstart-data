package org.quickstart.ceph;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.junit.Test;

public class QiniuTest {

    @Test
    public void testSimple() throws QiniuException {
        String accessKey = null;
        String secretKey = null;
        String bucketName = null;

        // 分片上传 v1
        Configuration cfg = new Configuration();
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucketName);
        Response r = uploadManager.put("hello world".getBytes(), "yourkey", token);

    }

    @Test
    public void testSimple2() throws QiniuException {
        String accessKey = null;
        String secretKey = null;
        String bucketName = null;

        // 分片上传 v2
        Configuration cfg = new Configuration();
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucketName);
        Response r = uploadManager.put("hello world".getBytes(), "yourkey", token);

    }

    @Test
    public void testSimple3() {

        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = "Access_Key";
        String SECRET_KEY = "Secret_Key";
        //要上传的空间
        String bucketname = "Bucket_Name";
        //上传到七牛后保存的文件名
        String key = "my-java.png";
        //上传文件的路径
        String FilePath = "/.../...";

        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        ///////////////////////指定上传的Zone的信息//////////////////
        //第一种方式: 指定具体的要上传的zone
        //注：该具体指定的方式和以下自动识别的方式选择其一即可
        //要上传的空间(bucket)的存储区域为华东时
        // Zone z = Zone.zone0();
        //要上传的空间(bucket)的存储区域为华北时
        // Zone z = Zone.zone1();
        //要上传的空间(bucket)的存储区域为华南时
        // Zone z = Zone.zone2();

        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);

        //简单上传，使用默认策略，只需要设置上传的空间名就可以了
        String token = auth.uploadToken(bucketname);

        try {
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key, token);
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }

    }
}
