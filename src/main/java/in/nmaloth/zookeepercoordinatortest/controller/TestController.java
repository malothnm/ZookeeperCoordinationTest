package in.nmaloth.zookeepercoordinatortest.controller;

import in.nmaloth.rsocketServices.config.model.*;
import in.nmaloth.rsocketServices.processor.ServiceEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;


@RestController
@Slf4j
public class TestController {

    private final ServiceEventProcessor<ServiceEvent> serviceEventProcessor;
    private final NodeInfo nodeInfo;


    public TestController(ServiceEventProcessor<ServiceEvent> serviceEventProcessor, NodeInfo nodeInfo) {
        this.serviceEventProcessor = serviceEventProcessor;
        this.nodeInfo = nodeInfo;
    }


    @GetMapping("/connect/{ip}/{port}/{serviceName}/{instanceName}")
    public Mono<ResponseEntity> createServerEvent(@PathVariable String ip, @PathVariable Integer port, @PathVariable String serviceName,
                                                  @PathVariable String instanceName ){


        Optional<ServiceInfo> serviceInfoOptional = nodeInfo.getDependentService()
                .stream()
                .filter(serviceInfo -> serviceInfo.getServiceName().equalsIgnoreCase(serviceName))
                .findFirst();

        if(serviceInfoOptional.isEmpty()){
            log.info(serviceName + "Invalid service Name");
            return Mono.just(new ResponseEntity(HttpStatus.NOT_FOUND));

        }

        ServiceInfo serviceInfo = serviceInfoOptional.get();

        ServerInfo serverInfo = ServerInfo.builder()
                .serverName(ip)
                .serverPort(port)
                .connected(false)
                .build();

        ServiceEvent serviceEvent = ServiceEvent.builder()
                .serviceAction(ServiceAction.CONNECT)
                .serviceName(serviceName)
                .serviceInstance(instanceName)
                .serverInfo(serverInfo)
                .isIn(serviceInfo.getIsIn())
                .isOut(serviceInfo.getIsOut())
                .build();

        serviceEventProcessor.processMessage(serviceEvent);

        return Mono.just(new ResponseEntity(HttpStatus.OK));
    }
}
