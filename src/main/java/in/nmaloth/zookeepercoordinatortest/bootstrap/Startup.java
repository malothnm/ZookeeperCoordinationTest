package in.nmaloth.zookeepercoordinatortest.bootstrap;

import in.nmaloth.rsocketServices.config.model.NodeInfo;
import in.nmaloth.rsocketServices.config.model.ServiceEvent;
import in.nmaloth.rsocketServices.service.*;
import in.nmaloth.zookeepercoordinator.service.CoordinationService;
import in.nmaloth.zookeepercoordinatortest.handler.DistributorClientHandler;
import in.nmaloth.zookeepercoordinatortest.service.DispatcherServiceTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.ConnectableFlux;

@Slf4j
@Component
public class Startup implements CommandLineRunner {


    private final ServerService serverService;
    private final DispatcherServiceTest dispatcherService;
    private final ServiceTracker serviceTracker;
    private final ClientService clientService;
    private final NodeInfo nodeInfo;
    private final ConnectableFlux<ServiceEvent> connectableFlux;
    private final ServiceEventsService serviceEventsService;
    private final CoordinationService coordinationService;


    public Startup(ServerService serverService,
                   DispatcherServiceTest dispatcherService,
                   ServiceTracker serviceTracker,
                   ClientService clientService,
                   NodeInfo nodeInfo,
                   ConnectableFlux<ServiceEvent> connectableFlux,
                   ServiceEventsService serviceEventsService,
                   CoordinationService coordinationService) {

        this.serverService = serverService;
        this.dispatcherService = dispatcherService;
        this.serviceTracker = serviceTracker;
        this.clientService = clientService;
        this.nodeInfo = nodeInfo;
        this.connectableFlux = connectableFlux;
        this.serviceEventsService = serviceEventsService;
        this.coordinationService = coordinationService;
    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {

        log.info(" Updating dispatcher Service");
        serviceTracker.updateDispatcherService(dispatcherService);
        clientService.updateClientHandler(new DistributorClientHandler(nodeInfo,serviceTracker,dispatcherService));
        serverService.createServer();
        serviceEventsService.fluxSubscriptions(connectableFlux);
        coordinationService.houseKeeping();
        coordinationService.setWatchers();


    }
}
