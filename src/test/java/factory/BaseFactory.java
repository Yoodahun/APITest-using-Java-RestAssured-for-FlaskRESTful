package factory;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class BaseFactory {

    private static String URI = "https://practice-flask-restful-dh.herokuapp.com";


    private static Response response = null;
    private static PrintStream log = null;
    private static RequestSpecification req = null;

    public RequestSpecification getRequestSpec()  {

        if(req == null) {
            try {
                log = new PrintStream(new FileOutputStream("logging.txt"));
                req = new RequestSpecBuilder().setBaseUri(URI)
                        .addFilter(RequestLoggingFilter.logRequestTo(log))
                        .addFilter(ResponseLoggingFilter.logResponseTo(log))
                        .setContentType(ContentType.JSON)
                        .build();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return req;
    }


    public Response getResponse() {
        return response;
    }
    public JsonPath getJsonPathInResponse() {
        return getResponse().body().jsonPath();
    }

    public void setResponse(Response response) {
        BaseFactory.response = response;
    }




}
