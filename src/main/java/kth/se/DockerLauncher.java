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

import java.util.Set;

/**
 * Created by shelan on 1/25/16.
 */
public class DockerLauncher {
    // get a context with docker that offers the portable ComputeService api


    public static void main(String[] args) throws RunNodesException {


        String email = "";
        String password = "";

        // get a context with docker that offers the portable ComputeService api
        ComputeServiceContext context = ContextBuilder.newBuilder("docker")
                .credentials(email, password)
                .modules(ImmutableSet.<Module>of(new Log4JLoggingModule(),
                        new SshjSshClientModule()))
                .buildView(ComputeServiceContext.class);
        ComputeService client = context.getComputeService();

        String sshableImageId = "your-sshable-image-id"; // this can be obtained using `docker images --no-trunc` command
        Template template = client.templateBuilder().imageId(sshableImageId).build();

// run a couple nodes accessible via group container
        Set<? extends NodeMetadata> nodes = client.createNodesInGroup("container", 2, template);

// release resources
        context.close();

    }
}
