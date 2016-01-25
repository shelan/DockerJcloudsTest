package kth.se;


import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;

import java.util.Properties;
import java.util.Set;

/**
 * Created by shelan on 1/25/16.
 */
public class DockerLauncher {
    // get a context with docker that offers the portable ComputeService api


    public static void main(String[] args) throws RunNodesException {


        String email = "/Users/shelan/.docker/machine/certs/cert.pem";
        String password = "/Users/shelan/.docker/machine/certs/key.pem";

        // get a context with docker that offers the portable ComputeService api
        ContextBuilder contextBuilder = ContextBuilder.newBuilder("docker").endpoint("https://192.168.99.100:2376")
                .credentials(email, password)
                .modules(ImmutableSet.<Module>of(new Log4JLoggingModule(),
                        new SshjSshClientModule()));

        Properties props = new Properties();

        props.setProperty("docker.cacert.path","/Users/shelan/.docker/machine/certs/ca.pem");
        contextBuilder.overrides(props);

        ComputeServiceContext context = contextBuilder.buildView(ComputeServiceContext.class);
        ComputeService client = context.getComputeService();

        String sshableImageId = "dfd70f9dcca4091b8cafb7614af6389bc3d9b47051867b4790c46ea81bd7493f"; // this can be obtained using `docker images --no-trunc` command
        Template template = client.templateBuilder().imageId(sshableImageId).build();

// run a couple nodes accessible via group container
        Set<? extends NodeMetadata> nodes = client.createNodesInGroup("container", 2, template);

// release resources
        context.close();

    }
}
