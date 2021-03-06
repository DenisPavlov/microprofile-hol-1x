package bg.jug.microprofile.hol.content;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

@ApplicationScoped
public class AuthorClient {

    @Inject
    @ConfigProperty(name = "authorsServiceUrl", defaultValue = "http://localhost:9110/authors")
    private String authorsUrl;

    @Inject
    @RestClient
    private AuthorsRestClient authorsRestClient;

    @Retry
    @Fallback(fallbackMethod = "defaultAuthor")
    @Timeout(800)
//    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.6, delay = 2000L, successThreshold = 2)
    public JsonObject findAuthorByEmail(String email) {
        JsonObject authorByEmail = authorsRestClient.findAuthorByEmail(email);
        return authorByEmail;
    }

    public JsonObject defaultAuthor(String email) {
        return Json.createObjectBuilder()
                .add("firstName", "")
                .add("lastName", "Unkown")
                .add("bio", "Try again later")
                .add("email", email)
                .build();
    }
}
