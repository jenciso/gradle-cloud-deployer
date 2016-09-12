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
package de.qaware.cloud.deployer.plugin.task;

import de.qaware.cloud.deployer.commons.error.ResourceConfigException;
import de.qaware.cloud.deployer.commons.error.ResourceException;
import de.qaware.cloud.deployer.kubernetes.KubernetesDeployer;
import de.qaware.cloud.deployer.kubernetes.config.cloud.KubernetesCloudConfig;
import de.qaware.cloud.deployer.plugin.DeployerExtension;
import de.qaware.cloud.deployer.plugin.KubernetesCloudConfigFactory;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Represents a task which deletes the specified configuration from the cloud.
 */
public class DeleteTask extends DefaultTask {

    /**
     * Delete the specified configuration.
     *
     * @throws ResourceException       If a error during resource interaction with the backend occurs.
     * @throws ResourceConfigException If a error during config creation/parsing occurs.
     */
    @TaskAction
    public void delete() throws ResourceException, ResourceConfigException {
        DeployerExtension extension = getProject().getExtensions().findByType(DeployerExtension.class);
        KubernetesCloudConfig cloudConfig = KubernetesCloudConfigFactory.create(extension);
        KubernetesDeployer deployer = new KubernetesDeployer();
        deployer.delete(cloudConfig);
    }
}
