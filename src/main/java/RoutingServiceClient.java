import io.restassured.RestAssured;
import io.restassured.response.Response;



public class RoutingServiceClient {

    private String localhostRoutingService;

    public RoutingServiceClient(String localhostRoutingService) {
        this.localhostRoutingService = localhostRoutingService;
    }

    public String getRoute(String seed){
        Response response = RestAssured.get(localhostRoutingService + seed);
        return response.getHeader("X-Transaction-Id");
    }
}
