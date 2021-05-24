package in.nmaloth.zookeepercoordinatortest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
        basePackages = {"in.nmaloth.rsocketServices","in.nmaloth.zookeepercoordinator"},
        basePackageClasses = {ZooKeeperCoordinatorTest.class}
)
public class ZooKeeperCoordinatorTest {

    public static void main(String[] args) {
        SpringApplication.run(ZooKeeperCoordinatorTest.class,args);
    }
}
