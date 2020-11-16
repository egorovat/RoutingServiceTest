import io.restassured.response.Response;

import static io.restassured.RestAssured.get;


public class RoutingServiceClient {

    private String localhostRoutingService;

    public RoutingServiceClient(String localhostRoutingService) {
        this.localhostRoutingService = localhostRoutingService;
    }

    public String getRoute(String seed){
        Response response = get(localhostRoutingService + seed);
        return response.getHeader("X-Transaction-Id");
    }
}
