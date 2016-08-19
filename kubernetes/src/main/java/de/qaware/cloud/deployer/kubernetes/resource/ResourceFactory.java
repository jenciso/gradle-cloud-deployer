package de.qaware.cloud.deployer.kubernetes.resource;

import de.qaware.cloud.deployer.kubernetes.config.auth.AuthConfig;
import de.qaware.cloud.deployer.kubernetes.config.resource.ResourceConfig;
import de.qaware.cloud.deployer.kubernetes.config.namespace.NamespaceConfigFactory;
import de.qaware.cloud.deployer.kubernetes.resource.base.DeletableResource;
import de.qaware.cloud.deployer.kubernetes.resource.base.Resource;
import de.qaware.cloud.deployer.kubernetes.resource.deployment.DeploymentResource;
import de.qaware.cloud.deployer.kubernetes.resource.namespace.NamespaceResource;
import de.qaware.cloud.deployer.kubernetes.resource.pod.PodResource;
import de.qaware.cloud.deployer.kubernetes.resource.service.ServiceResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceFactory {

    private final NamespaceResource namespaceResource;

    public ResourceFactory(String namespace) throws IOException {
        ResourceConfig namespaceResourceConfig = NamespaceConfigFactory.create(namespace);
        namespaceResource = new NamespaceResource(namespaceResourceConfig);
    }

    public DeletableResource getNamespaceResource() {
        return namespaceResource;
    }

    public List<Resource> createResources(List<ResourceConfig> resourceConfigs) throws IOException {
        List<Resource> resources = new ArrayList<>();
        for (ResourceConfig resourceConfig : resourceConfigs) {
            resources.add(createResource(resourceConfig));
        }
        return resources;
    }

    public Resource createResource(ResourceConfig resourceConfig) throws IOException {
        String resourceVersion = resourceConfig.getResourceVersion();
        String resourceType = resourceConfig.getResourceType();
        switch (resourceVersion) {
            case "extensions/v1beta1":
                switch (resourceType) {
                    case "Deployment":
                        return new DeploymentResource(namespaceResource.getNamespace(), resourceConfig);
                    default:
                        throw new IllegalArgumentException("Unknown Kubernetes resource type for api version " + resourceVersion);
                }
            case "v1":
                switch (resourceType) {
                    case "Pod":
                        return new PodResource(namespaceResource.getNamespace(), resourceConfig);
                    case "Service":
                        return new ServiceResource(namespaceResource.getNamespace(), resourceConfig);
                    default:
                        throw new IllegalArgumentException("Unknown Kubernetes resource type for api version " + resourceVersion);
                }
            default:
                throw new IllegalArgumentException("Unknown Kubernetes api version");
        }
    }
}
