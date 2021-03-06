/*
 * Copyright 2016 QAware GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.qaware.cloud.deployer.kubernetes.resource.pod;

import de.qaware.cloud.deployer.commons.config.resource.ContentType;
import de.qaware.cloud.deployer.commons.config.util.FileUtil;
import de.qaware.cloud.deployer.commons.error.ResourceException;
import de.qaware.cloud.deployer.commons.resource.ClientFactory;
import de.qaware.cloud.deployer.kubernetes.config.resource.KubernetesResourceConfig;
import de.qaware.cloud.deployer.kubernetes.resource.namespace.NamespaceResource;
import de.qaware.cloud.deployer.kubernetes.test.KubernetesClientUtil;
import de.qaware.cloud.deployer.kubernetes.test.KubernetesTestEnvironment;
import de.qaware.cloud.deployer.kubernetes.test.KubernetesTestEnvironmentUtil;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import junit.framework.TestCase;

public class PodResourceIntegrationTest extends TestCase {

    private KubernetesClient kubernetesClient;
    private NamespaceResource namespaceResource;
    private PodResource podResource;

    @Override
    public void setUp() throws Exception {
        // Create test environment
        KubernetesTestEnvironment testEnvironment = KubernetesTestEnvironmentUtil.createTestEnvironment();
        namespaceResource = testEnvironment.getNamespaceResource();
        kubernetesClient = testEnvironment.getKubernetesClient();
        KubernetesTestEnvironmentUtil.createTestNamespace(namespaceResource);

        // Create the PodResource object
        ClientFactory clientFactory = testEnvironment.getClientFactory();
        String podDescription = FileUtil.readFileContent("/de/qaware/cloud/deployer/kubernetes/resource/pod/pod.json");
        KubernetesResourceConfig resourceConfig = new KubernetesResourceConfig("test", ContentType.JSON, podDescription);
        podResource = new PodResource(namespaceResource.getNamespace(), resourceConfig, clientFactory);
    }

    @Override
    public void tearDown() throws Exception {
        namespaceResource.delete();
    }

    public void testExists() throws ResourceException {

        // Check that the pod doesn't exist already
        Pod pod = KubernetesClientUtil.retrievePod(kubernetesClient, podResource);
        assertNull(pod);

        // Test exists method
        assertFalse(podResource.exists());

        // Create pod
        podResource.create();

        // Check that the pod exists
        pod = KubernetesClientUtil.retrievePod(kubernetesClient, podResource);
        assertNotNull(pod);

        // Test exists method
        assertTrue(podResource.exists());
    }

    public void testCreate() throws ResourceException {

        // Check that the pod doesn't exist already
        Pod pod = KubernetesClientUtil.retrievePod(kubernetesClient, podResource);
        assertNull(pod);

        // Create pod
        podResource.create();

        // Check that the pod exists
        pod = KubernetesClientUtil.retrievePod(kubernetesClient, podResource);
        assertNotNull(pod);

        // Compare services
        assertEquals(pod.getMetadata().getName(), podResource.getId());
        assertEquals(pod.getApiVersion(), podResource.getResourceConfig().getResourceVersion());
        assertEquals(pod.getKind(), podResource.getResourceConfig().getResourceType());
    }

    public void testDelete() throws ResourceException {

        // Create pod
        podResource.create();

        // Check that the pod exists
        Pod pod = KubernetesClientUtil.retrievePod(kubernetesClient, podResource);
        assertNotNull(pod);

        // Delete pod
        podResource.delete();

        // Check that pod doesn't exist anymore
        pod = KubernetesClientUtil.retrievePod(kubernetesClient, podResource);
        assertNull(pod);
    }
}
