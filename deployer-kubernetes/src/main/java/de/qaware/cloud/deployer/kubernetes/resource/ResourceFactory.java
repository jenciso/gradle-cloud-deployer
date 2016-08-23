package de.qaware.cloud.deployer.kubernetes.resource;

import de.qaware.cloud.deployer.kubernetes.config.cloud.CloudConfig;
import de.qaware.cloud.deployer.kubernetes.config.namespace.NamespaceConfigFactory;
import de.qaware.cloud.deployer.kubernetes.config.resource.ResourceConfig;
import de.qaware.cloud.deployer.kubernetes.error.ResourceConfigException;
import de.qaware.cloud.deployer.kubernetes.error.ResourceException;
import de.qaware.cloud.deployer.kubernetes.resource.base.ClientFactory;
import de.qaware.cloud.deployer.kubernetes.resource.base.Resource;
import de.qaware.cloud.deployer.kubernetes.resource.deployment.DeploymentResource;
import de.qaware.cloud.deployer.kubernetes.resource.namespace.NamespaceResource;
import de.qaware.cloud.deployer.kubernetes.resource.pod.PodResource;
import de.qaware.cloud.deployer.kubernetes.resource.service.ServiceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ResourceFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceFactory.class);
    private final NamespaceResource namespaceResource;
    private final ClientFactory clientFactory;

    public ResourceFactory(String namespace, CloudConfig cloudConfig) throws ResourceConfigException {
        this.clientFactory = new ClientFactory(cloudConfig);
        ResourceConfig namespaceResourceConfig = NamespaceConfigFactory.create(namespace);
        this.namespaceResource = new NamespaceResource(namespaceResourceConfig, this.clientFactory);
    }

    public Resource getNamespaceResource() {
        return namespaceResource;
    }

    public List<Resource> createResources(List<ResourceConfig> resourceConfigs) throws ResourceException {

        LOGGER.info("Creating resources...");

        List<Resource> resources = new ArrayList<>();
        for (ResourceConfig resourceConfig : resourceConfigs) {
            resources.add(createResource(resourceConfig));
        }

        LOGGER.info("Finished creating resources...");

        return resources;
    }

    public Resource createResource(ResourceConfig resourceConfig) throws ResourceException {
        String resourceVersion = resourceConfig.getResourceVersion();
        String resourceType = resourceConfig.getResourceType();
        Resource resource;
        switch (resourceVersion) {
            case "extensions/v1beta1":
                switch (resourceType) {
                    case "Deployment":
                        resource = new DeploymentResource(namespaceResource.getNamespace(), resourceConfig, clientFactory);
                        break;
                    default:
                        throw new ResourceException("Unknown Kubernetes resource type for api version " + resourceVersion + "(ResourceConfig: " + resourceConfig + ")");
                }
                break;
            case "v1":
                switch (resourceType) {
                    case "Pod":
                        resource = new PodResource(namespaceResource.getNamespace(), resourceConfig, clientFactory);
                        break;
                    case "Service":
                        resource = new ServiceResource(namespaceResource.getNamespace(), resourceConfig, clientFactory);
                        break;
                    default:
                        throw new ResourceException("Unknown Kubernetes resource type for api version " + resourceVersion + "(ResourceConfig: " + resourceConfig + ")");
                }
                break;
            default:
                throw new ResourceException("Unknown Kubernetes api version (ResourceConfig: " + resourceConfig + ")");
        }

        LOGGER.info("- " + resource);

        return resource;
    }
}
