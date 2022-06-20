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
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class S3CephTest {

    public static final String accessKey = "XXX";
    public static final String secretKey = "XXX";
    public static final String url = "http://api.ceph.rgw.test.info";

    @Test
    public void testSimple() throws IOException {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(credentials);

       /* final AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION)//
            .withCredentials(awsStaticCredentialsProvider)//
            .withClientConfiguration(getClientConfiguration())//
            .withEndpointConfiguration()//
            .build();*/

        // final AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();

        // 上述方式创建失败

        AmazonS3 amazonS3Client = new AmazonS3Client(awsStaticCredentialsProvider, getClientConfiguration());
        //AmazonS3 conn = new AmazonS3Client(credentials);
        amazonS3Client.setEndpoint(url);
        amazonS3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());

        List<Bucket> buckets = amazonS3Client.listBuckets();
        buckets.forEach(System.out::println);

        String bucketName = "test-lengfeng";

        boolean exists = amazonS3Client.doesBucketExist(bucketName);
        System.out.println("exists=" + exists);

        // 新建一个名为 my-new-bucket 的bucket。
        Bucket newBucket = amazonS3Client.createBucket(bucketName);
        // CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        // amazonS3Client.createBucket(createBucketRequest);
        System.out.println(newBucket);

        exists = amazonS3Client.doesBucketExist(bucketName);
        System.out.println("exists=" + exists);

        buckets = amazonS3Client.listBuckets();
        buckets.forEach(System.out::println);

        // 列出 BUCKET 的内容
        // 下面的代码会输出 bucket 内的所有对象列表。 这也会打印出每一个对象的名字、文件尺寸和最近修改时间。
        ObjectListing objects = amazonS3Client.listObjects(bucketName);
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                System.out.println(
                    objectSummary.getKey() + "\t" + objectSummary.getSize() + "\t" + StringUtils.fromDate(objectSummary.getLastModified()));
            }
            objects = amazonS3Client.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());

        String content = "test context";
        byte[] fileSource = content.getBytes();
        String fileName = "test.txt";
        String contentType = "txt";
        // 根据bucket和fileName来获取bucket的分区名称
        ByteArrayInputStream bis = new ByteArrayInputStream(fileSource);

        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentLength(fileSource.length);
        omd.setHeader("filename", fileName);
        omd.setContentType(contentType);

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, bis, omd));

        S3Object s3Object = amazonS3Client.getObject(bucketName, fileName);
        byte[] getObject = IOUtils.toByteArray(s3Object.getObjectContent());
        System.out.println(new String(getObject));

        objects = amazonS3Client.listObjects(bucketName);
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                System.out.println(
                    objectSummary.getKey() + "\t" + objectSummary.getSize() + "\t" + StringUtils.fromDate(objectSummary.getLastModified()));
            }
            objects = amazonS3Client.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());

        // 上传公共文件
        bis = new ByteArrayInputStream(fileSource);
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, bis, omd).withCannedAcl(CannedAccessControlList.PublicRead));

        String publicUrl = url + "/" + bucketName + "/" + fileName;
        System.out.println(publicUrl);

        // 新建一个对象
        // 下面的代码会新建一个内容是字符串``”Hello World!”`` 的文件 hello.txt。
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        amazonS3Client.putObject(newBucket.getName(), "hello.txt", input, new ObjectMetadata());

        amazonS3Client.getObject(new GetObjectRequest(newBucket.getName(), "hello.txt"), new File("/Users/lengfeng/ceph/hello.txt"));

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(newBucket.getName(), "hello.txt");
        System.out.println(amazonS3Client.generatePresignedUrl(request));

        // 删除一个对象
        // 下面的代码会删除对象 goodbye.txt
        amazonS3Client.deleteObject(newBucket.getName(), "hello.txt");

        amazonS3Client.deleteObject(bucketName, fileName);

        // 删除 BUCKET
        // Note Bucket必须为空！否则它不会工作!
        // 报错 com.amazonaws.services.s3.model.AmazonS3Exception: null (Service: Amazon S3; Status Code: 409; Error Code: BucketNotEmpty; Request ID: tx00000000000000297a57c-0062b012bd-ce176-default; S3 Extended Request ID: ce176-default-default)
        // , S3 Extended Request ID: ce176-default-default
        amazonS3Client.deleteBucket(newBucket.getName());

    }

    @Test
    public void testSimple2() {

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

    private static ClientConfiguration getClientConfiguration() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();

        clientConfiguration.setConnectionTimeout(10_000);
        clientConfiguration.setMaxConnections(100);
        clientConfiguration.setMaxErrorRetry(3);
        clientConfiguration.setProtocol(Protocol.HTTP);
        clientConfiguration.setSocketTimeout(60_000);
        clientConfiguration.setUseTcpKeepAlive(true);
        clientConfiguration.setSocketBufferSizeHints(65536, 65536);
        clientConfiguration.withSignerOverride("S3SignerType");
        return clientConfiguration;
    }

}
