import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.Record;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static io.restassured.RestAssured.get;


public class RoutingServiceTest {

    private static final String LOCALHOST_ROUTING_SERVICE = "http://localhost:9000/route/";

    private AmazonKinesis kinesisClient = KinesisClientConfiguration.getLocalhostClient();
    private KinesisStreamComsumer kinesisStreamComsumer = new KinesisStreamComsumer(kinesisClient);
    private RoutingServiceClient routingService = new RoutingServiceClient(LOCALHOST_ROUTING_SERVICE);


    private static final String EVEN_STREAM = "li-stream-even";
    private static final String ODD_STREAM = "li-stream-odd";
    private Date timestamp;

    @Before
    public void BeforeTest() {
        timestamp = new Date();
    }

    @Test
    public void given_Send_Odd_Number_Seed_Then_Record_Added_To_OddStream() {

        String oddSeedValue = "1";

        String transactionId = routingService.getRoute(oddSeedValue);
        Record recordOdd = kinesisStreamComsumer.getRecord(ODD_STREAM, timestamp);

        Assertions.assertThat(recordOdd.getPartitionKey()).isEqualTo(transactionId);

    }

    @Test
    public void given_Send_Odd_Number_Seed_Then_Record_Not_Added_To_EvenStream() {

        String oddSeedValue = "131";

        String transactionId = routingService.getRoute(oddSeedValue);
        Record recordEven = kinesisStreamComsumer.getRecord(EVEN_STREAM, timestamp);

        Assertions.assertThat(recordEven.getPartitionKey()).isNotEqualTo(transactionId);

    }

    @Test
    public void given_Send_Even_Number_Seed_Then_Record_Added_To_EvenStream() {

        String evenSeedValue = "2";

        String transactionId = routingService.getRoute(evenSeedValue);

        Record recordEven = kinesisStreamComsumer.getRecord(EVEN_STREAM, timestamp);
        Assertions.assertThat(recordEven.getPartitionKey()).isEqualTo(transactionId);

    }

    @Test
    public void given_Send_Even_Number_Seed_Then_Record_Not_Added_To_OddStream() {

        String evenSeedValue = "54";

        String transactionId = routingService.getRoute(evenSeedValue);
        Record recordOdd = kinesisStreamComsumer.getRecord(ODD_STREAM, timestamp);

        Assertions.assertThat(recordOdd.getPartitionKey()).isNotEqualTo(transactionId);

    }

    @Test
    public void  given_Send_Negative_Odd_Number_Seed_Then_Record_Added_To_OddStream(){

        String negativeOddSeedValue = "-1";

        String transactionId = routingService.getRoute(negativeOddSeedValue);
        Record recordOdd = kinesisStreamComsumer.getRecord(ODD_STREAM, timestamp);

        Assertions.assertThat(recordOdd.getPartitionKey()).isEqualTo(transactionId);
    }

    @Test
    public void  given_Send_Zero_Seed_Then_Record_Added_To_EvenStream(){

        String zeroSeedValue = "0";
        String transactionId = routingService.getRoute(zeroSeedValue);

        Record recordEven = kinesisStreamComsumer.getRecord(EVEN_STREAM, timestamp);
        Assertions.assertThat(recordEven.getPartitionKey()).isEqualTo(transactionId);
    }

    @Test
    public void  given_Send_BigInt_Seed_Then_Record_Added_To_EvenStream(){

        String bigIntValue = "9007199254740992";
        String transactionId = routingService.getRoute(bigIntValue);

        Record recordEven = kinesisStreamComsumer.getRecord(EVEN_STREAM, timestamp);
        Assertions.assertThat(recordEven.getPartitionKey()).isEqualTo(transactionId);
    }

   //The following test cases can written in one parameterized test. Left as is for the sake of readability
    @Test
    public void given_Send_Odd_Number_Seed_Then_Service_Returns_Status_200() {

        String validSeedValue = "1";
        Assertions.assertThat(get(LOCALHOST_ROUTING_SERVICE + validSeedValue).statusCode()).isEqualTo(200);

    }

    @Test
    public void  given_Send_Floating_Point_Number_Seed_Then_Record_Added_To_EvenStream(){

        String floatingPointNumber = "2.8";
        Assertions.assertThat(get(LOCALHOST_ROUTING_SERVICE + floatingPointNumber).statusCode()).isEqualTo(400);

    }

    @Test
    public void  given_Send_Not_A_Number_Seed_Then_Service_Returns_BadRequest(){

        String invalidNotANumberSeed = "abc";
        Assertions.assertThat(get(LOCALHOST_ROUTING_SERVICE + invalidNotANumberSeed).statusCode()).isEqualTo(400);
    }

}