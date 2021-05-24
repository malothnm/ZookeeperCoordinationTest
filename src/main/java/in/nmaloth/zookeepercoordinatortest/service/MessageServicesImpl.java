package in.nmaloth.zookeepercoordinatortest.service;


import in.nmaloth.rsocketServices.model.proto.ServerRegistrationOuterClass;
import in.nmaloth.rsocketServices.service.MessageServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class MessageServicesImpl implements MessageServices {


    public static final String CANCELLING_FLUX_FOR_SERVICE = "Cancelling flux for service {}";
    public static final String TERMINATING_FLUX_FOR_SERVICE = "Terminating  flux for service {}";




    @Override
    public Flux requestForStreamMessageProcessorIn(RSocketRequester rSocketRequester, String route,
                                                                                    ServerRegistrationOuterClass.ServerRegistration registration) {

        return rSocketRequester.route(route)
                .data(registration)
                .retrieveFlux(String.class)
                .doOnNext(s -> log.info(s))
                ;
    }




    private void logErrors(Throwable throwable, Object o) {

        throwable.printStackTrace();
        log.error(o.toString());
    }
}
