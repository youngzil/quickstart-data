package org.quickstart.ceph;

import org.junit.Test;
import org.twonote.rgwadmin4j.RgwAdmin;
import org.twonote.rgwadmin4j.impl.RgwAdminImpl;

import java.util.List;

public class AdminCephTest {

    @Test
    public void testSimple() {
        String adminAccessKey = "TMIKXGNOL242YD2UY40O";
        String adminSecretKey = "DX6Itv5VjKXGzqXd3Qxvzuz0OMpvxCDjnFZHZXnk";
        String adminEndpoint = "http://cephtest.test.wacai.info";

        RgwAdmin rgwAdmin = new RgwAdminImpl(adminAccessKey, adminSecretKey, adminEndpoint);
        System.out.println(rgwAdmin);

        List<String> buckets = rgwAdmin.listBucket();
        buckets.forEach(System.out::println);

    }
}
