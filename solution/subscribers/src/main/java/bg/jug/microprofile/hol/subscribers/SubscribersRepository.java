package bg.jug.microprofile.hol.subscribers;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;


/**
 * Created by Dmitry Alexandrov on 26.02.2018.
 */
@ApplicationScoped
public class SubscribersRepository {

    private Map<String, Subscriber> subscribers = new HashMap<>();

    public List<Subscriber> getSubscribers() {
        return new ArrayList<>(subscribers.values());
    }

    public Optional<Subscriber> findSubscriberByEmail(String email) {
        return Optional.ofNullable(subscribers.get(email));
    }

    @Metered(name = "Subscriber added")
    public void addSubscriber(Subscriber subscriber) {
        subscribers.put(subscriber.getEmail(), subscriber);
        //used for metrics
        subscribersDBCounter.inc();
    }

    @PostConstruct
    public void addTestData() {
        Subscriber frodoBaggins = new Subscriber("Frodo", "Baggins",
                "frodo@example.org", LocalDate.of(2018, 6, 1));
        Subscriber aragornSonOfAragorn = new Subscriber("Aragorn", "son of Aratorn",
                "aragorn@example.org",  LocalDate.of(2019, 3, 15));
        Subscriber legolas = new Subscriber("Legolas", "son of Thranduil",
                "legolas@example.org",LocalDate.of(2018, 12, 1));

        addSubscriber(frodoBaggins);
        addSubscriber(aragornSonOfAragorn);
        addSubscriber(legolas);

        //Metrics
        registry.getGauges();
        System.out.println("Log all gauges:" +registry.getGauges());
        System.out.println("Log all counters:" +registry.getCounters());
    }

    /* Metrics */
    @Inject
    private MetricRegistry registry;

    @Inject
    @Metric
    private Counter subscribersDBCounter;

    @Gauge(name = "Subscribers DB usage", unit = MetricUnits.NONE, absolute = true)
    public int getDBUsage(){
        return subscribers.size();
    }




}
