package in.nmaloth.zookeepercoordinatortest.service;

import in.nmaloth.rsocketServices.service.DispatcherService;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;

public interface DispatcherServiceTest extends DispatcherService {

    Flux<String> requestForStreamForOutgoingProcessor(RSocketRequester rSocketRequester);
}
