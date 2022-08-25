package org.quickstart.ceph.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AmazonS3ClientBuilder {
    public static void main(String[] args) {
        String accessKey = "XXXXX";
        String secretKey = "XXXXX";

        // Our firewall on DEV does some weird stuff so we disable SSL cert check
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        if (SDKGlobalConfiguration.isCertCheckingDisabled()) {
            System.out.println("Cert checking is disabled");
        }

        // S3 Client configuration
        ClientConfiguration config = new ClientConfiguration();
        // Not the standard "AWS3SignerType", maar expliciet signerTypeV2
        config.setSignerOverride("S3SignerType");
        config.setProtocol(Protocol.HTTPS);
        config.setProxyHost("proxy.rubix.nl");

        config.setProxyPort(8080);
        // S3 Credentials
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        // S3 Endpoint
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("objects.dc1.rubix.nl", "");
        AmazonS3 s3 = com.amazonaws.services.s3.AmazonS3ClientBuilder.standard().withClientConfiguration(config)
            .withCredentials(new AWSStaticCredentialsProvider(credentials)).withEndpointConfiguration(endpointConfiguration).build();

        System.out.println("===========================================");
        System.out.println(" Connection to the Rubix S3 ");
        System.out.println("===========================================n");
        try {
            /*
             * List of buckets and objects in our account
             */
            System.out.println("Listing buckets and objects");
            for (Bucket bucket : s3.listBuckets()) {
                System.out.println(
                    " - " + bucket.getName() + " " + "(owner = " + bucket.getOwner() + " " + "(creationDate = " + bucket.getCreationDate());
                ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucket.getName()));
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    System.out.println(" --- " + objectSummary.getKey() + " " + "(size = " + objectSummary.getSize() + ")" + " " + "(eTag = "
                        + objectSummary.getETag() + ")");
                    System.out.println();
                }
            }
        } catch (AmazonServiceException ase) {
            System.out.println(
                "Caught an AmazonServiceException, which means your request made it to S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code: " + ase.getErrorCode());
            System.out.println("Error Type: " + ase.getErrorType());
            System.out.println("Request ID: " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
