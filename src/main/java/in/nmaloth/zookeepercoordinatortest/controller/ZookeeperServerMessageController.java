package in.nmaloth.zookeepercoordinatortest.controller;

import in.nmaloth.rsocketServices.config.model.NodeInfo;
import in.nmaloth.rsocketServices.constants.RouteConstantNames;
import in.nmaloth.rsocketServices.controller.ServerMessageController;
import in.nmaloth.rsocketServices.model.proto.ServerRegistrationOuterClass;
import in.nmaloth.rsocketServices.service.DispatcherService;
import in.nmaloth.rsocketServices.service.ServerService;
import in.nmaloth.zookeepercoordinatortest.constants.RouteConstants;
import in.nmaloth.zookeepercoordinatortest.service.DispatcherServiceTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ZookeeperServerMessageController  extends ServerMessageController {

    public static final String ENTERING_CLIENT_REGISTRATION = "#######Entering Client Registration for service ... ######";
    public static final String STREAMING_REQUEST_FOR_SERVICE = "######## Streaming request for {} Service from Instance {}";
    public static final String STREAMING_REQUEST_FOR_PROCESSOR = "streaming request for processor";
    public static final String ENTERED_CONNECT_MAPPING = "entered connect Mapping ";
    public static final String FROM_SERVER = "From Server {}";


    private final DispatcherService dispatcherService;

    public ZookeeperServerMessageController(NodeInfo nodeInfo, ServerService serverService, DispatcherService dispatcherService) {

        super(nodeInfo, serverService);
        this.dispatcherService = dispatcherService;
    }






    @MessageMapping(RouteConstants.DISTRIBUTOR)
    public Flux<String> getTestFLuxDistributor(RSocketRequester rSocketRequester,
                                               Mono<ServerRegistrationOuterClass.ServerRegistration> registrationMono){

        registrationMono
                .doOnNext(registration -> log.info(STREAMING_REQUEST_FOR_SERVICE,registration.getServiceName(),
                        registration.getServiceInstance()))
                .subscribe();

        return( (DispatcherServiceTest)dispatcherService).requestForStreamForOutgoingProcessor(rSocketRequester);


    }

    @MessageMapping(RouteConstants.CONNECTOR)
    public Flux<String> getTestFLux(RSocketRequester rSocketRequester,
                                    Mono<ServerRegistrationOuterClass.ServerRegistration> registrationMono){

        registrationMono
                .doOnNext(registration -> log.info(STREAMING_REQUEST_FOR_SERVICE,registration.getServiceName(),
                        registration.getServiceInstance()))
                .subscribe();

        return ((DispatcherServiceTest)dispatcherService).requestForStreamForOutgoingProcessor(rSocketRequester);


    }



}
