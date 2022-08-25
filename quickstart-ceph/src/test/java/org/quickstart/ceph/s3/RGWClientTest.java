/**
 * Created by KyleBai on 2017/3/28.
 */

package org.quickstart.ceph.s3;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.quickstart.ceph.s3.RGWClient;

import java.util.List;

public class RGWClientTest {

    static final String ACCESS_KEY = "access-test";
    static final String SECRET_KEY = "secret-test";
    static final String HOSTNAME = "http://172.16.35.100:8080"; // THIS IS PRIVATE IP!!!
    static final String BUCKET_NAME = "files";
    static RGWClient client;

    public static void main(String [] argv) {
        client = new RGWClient(ACCESS_KEY, SECRET_KEY, HOSTNAME);

        System.out.println("----- List all bucket -----");
        List<Bucket> buckets = client.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.toString());
        }

        System.out.println("\n----- [files] bucket -----");

        ObjectListing files_objects = client.listObjects(client.getBucket(BUCKET_NAME));
        List<S3ObjectSummary> objects = files_objects.getObjectSummaries();
        for (S3ObjectSummary object: objects) {
            System.out.println(object.getKey() + ", " + object.getSize() + " bytes");
        }
    }

}
