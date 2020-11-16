import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;


public class KinesisClientConfiguration {

    private static final String LOCAL_KINESIS_ENDPOINT = "http://localhost:4568";

    public static AmazonKinesis getLocalhostClient () {
        System.setProperty("com.amazonaws.sdk.disableCbor", "true");
        AwsClientBuilder.EndpointConfiguration localEndpointConfig  =
                new AwsClientBuilder.EndpointConfiguration(LOCAL_KINESIS_ENDPOINT, Regions.US_EAST_1.getName());
        return AmazonKinesisClientBuilder.standard().withEndpointConfiguration(localEndpointConfig).build();
    }
}
