package org.quickstart.ceph.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

public class S3CephTest {

    @Test
    public void testSimple2() {

        String accessKey = "G9VHIWJGK07HB7WULKFC";
        String secretKey = "1mn3mo3EboZZ25kh1iXj1ZjCqk8eBPfCCOT8vPsn";
        String url = "http://api.ceph.rgw.cache.test.wacai.info";

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(credentials);

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(10_000);
        clientConfiguration.setMaxConnections(100);
        clientConfiguration.setMaxErrorRetry(3);
        clientConfiguration.setProtocol(Protocol.HTTP);
        clientConfiguration.setSocketTimeout(60_000);
        clientConfiguration.setUseTcpKeepAlive(true);
        clientConfiguration.setSocketBufferSizeHints(65536, 65536);
        clientConfiguration.withSignerOverride("S3SignerType");

        AmazonS3 conn = new AmazonS3Client(awsStaticCredentialsProvider, clientConfiguration);
        // AmazonS3 conn = new AmazonS3Client(credentials);
        conn.setEndpoint(url);
        conn.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());

        // 新建一个名为 my-new-bucket 的bucket。
        Bucket newBucket = conn.createBucket("my-new-bucket");
        System.out.println(newBucket);

        List<Bucket> buckets = conn.listBuckets();
        for (Bucket bucket : buckets) {
            if (bucket.getName().equals(newBucket.getName())) {
                System.out.println(bucket.getName() + "\t" + StringUtils.fromDate(bucket.getCreationDate()));
            }
        }

        // 列出 BUCKET 的内容
        // 下面的代码会输出 bucket 内的所有对象列表。 这也会打印出每一个对象的名字、文件尺寸和最近修改时间。
        ObjectListing objects = conn.listObjects(newBucket.getName());
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                System.out.println(
                    objectSummary.getKey() + "\t" + objectSummary.getSize() + "\t" + StringUtils.fromDate(objectSummary.getLastModified()));
            }
            objects = conn.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());

        // 新建一个对象
        // 下面的代码会新建一个内容是字符串``”Hello World!”`` 的文件 hello.txt。
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        conn.putObject(newBucket.getName(), "hello.txt", input, new ObjectMetadata());

        conn.getObject(new GetObjectRequest(newBucket.getName(), "hello.txt"), new File("/Users/lengfeng/ceph/hello.txt"));

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(newBucket.getName(), "hello.txt");
        System.out.println(conn.generatePresignedUrl(request));

        // 删除一个对象
        // 下面的代码会删除对象 goodbye.txt
        conn.deleteObject(newBucket.getName(), "hello.txt");

        // 删除 BUCKET
        // Note Bucket必须为空！否则它不会工作!
        conn.deleteBucket(newBucket.getName());

    }

    @Test
    public void testSimple() {

        String accessKey = "G9VHIWJGK07HB7WULKFC";
        String secretKey = "1mn3mo3EboZZ25kh1iXj1ZjCqk8eBPfCCOT8vPsn";
        String url = "http://api.ceph.rgw.cache.test.wacai.info";

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);

        AmazonS3 conn = new AmazonS3Client(credentials, clientConfig);
        // AmazonS3 conn = new AmazonS3Client(credentials);
        conn.setEndpoint(url);

        // 列出用户的所有 BUCKET
        List<Bucket> buckets = conn.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName() + "\t" + StringUtils.fromDate(bucket.getCreationDate()));
        }

        // 新建一个名为 my-new-bucket 的bucket。
        Bucket bucket = conn.createBucket("my-new-bucket");

        // 列出 BUCKET 的内容
        // 下面的代码会输出 bucket 内的所有对象列表。 这也会打印出每一个对象的名字、文件尺寸和最近修改时间。
        ObjectListing objects = conn.listObjects(bucket.getName());
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                System.out.println(
                    objectSummary.getKey() + "\t" + objectSummary.getSize() + "\t" + StringUtils.fromDate(objectSummary.getLastModified()));
            }
            objects = conn.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());

    }

}
