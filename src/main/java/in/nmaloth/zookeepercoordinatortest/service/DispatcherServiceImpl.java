package in.nmaloth.zookeepercoordinatortest.service;

import in.nmaloth.rsocketServices.config.model.NodeInfo;
import in.nmaloth.rsocketServices.model.proto.ServerRegistrationOuterClass;
import in.nmaloth.rsocketServices.service.MessageServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

@Slf4j
@Service
public class DispatcherServiceImpl implements DispatcherServiceTest {


    private final NodeInfo nodeInfo;

    private final MessageServices messageServices;

    public DispatcherServiceImpl(NodeInfo nodeInfo, MessageServices messageServices) {
        this.nodeInfo = nodeInfo;
        this.messageServices = messageServices;
    }


    @Override
    public Flux requestForStreamForIncoming(String serviceName, String serviceInstance, String route,
                                            RSocketRequester rSocketRequester) {

        ServerRegistrationOuterClass.ServerRegistration registration = ServerRegistrationOuterClass.ServerRegistration.newBuilder()
                .setServiceName(nodeInfo.getAppName())
                .setServiceInstance(serviceInstance)
                .setStatusReady(true)
                .build();

        return messageServices.requestForStreamMessageProcessorIn(rSocketRequester,route,registration);
    }

    @Override
    public Flux<String> requestForStreamForOutgoingProcessor(RSocketRequester rSocketRequester){

        Stream<Integer> stream = Stream.iterate(0,(i)-> i + 1);

        return Flux.fromStream(stream)
                .delayElements(Duration.ofSeconds(1))
                .map(integer -> "This is test " + nodeInfo.getAppName() +  integer);

    }


}
