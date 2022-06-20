package org.quickstart.ceph;

import org.junit.Test;
import org.twonote.rgwadmin4j.RgwAdmin;
import org.twonote.rgwadmin4j.impl.RgwAdminImpl;
import org.twonote.rgwadmin4j.model.BucketInfo;

import java.util.List;

public class AdminCephTest {

    @Test
    public void testSimple() {
        String adminAccessKey = "TMIKXGNOL242YD2UY40O";
        String adminSecretKey = "DX6Itv5VjKXGzqXd3Qxvzuz0OMpvxCDjnFZHZXnk";
        String adminEndpoint = "http://cephtest.test.wacai.info/admin";

        RgwAdmin rgwAdmin = new RgwAdminImpl(adminAccessKey, adminSecretKey, adminEndpoint);
        System.out.println(rgwAdmin);

        String bucketName = "domino-test";
        String userId = "dingqin";

        List<String> buckets = rgwAdmin.listBucket(userId);
        buckets.forEach(System.out::println);

        BucketInfo bucketInfo = rgwAdmin.getBucketInfo(bucketName).get();
        System.out.println(bucketInfo);

    }
}
