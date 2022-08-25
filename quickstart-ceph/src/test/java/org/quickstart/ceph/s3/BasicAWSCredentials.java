package org.quickstart.ceph.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;

public class BasicAWSCredentials {
    public static void main(String[] args) {
        String accessKey = "XXXXXXX";
        String secretKey = "XXXXXXX";
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");

        if (SDKGlobalConfiguration.isCertCheckingDisabled()) {
            System.out.println("Cert checking is disabled");
        }
        AWSCredentials credentials = new com.amazonaws.auth.BasicAWSCredentials(accessKey, secretKey);

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setSignerOverride("S3SignerType");
        clientConfig.setProxyHost("proxy.rubix.nl");
        clientConfig.setProxyPort(8080);

        AmazonS3 conn = new AmazonS3Client(credentials, clientConfig);
        conn.setEndpoint("objects.gn3.rubix.nl");

        for (Bucket bucket : conn.listBuckets()) {
            System.out.println(
                " - " + bucket.getName() + " " + "(owner = " + bucket.getOwner() + " " + "(creationDate = " + bucket.getCreationDate());
        }
    }
}
