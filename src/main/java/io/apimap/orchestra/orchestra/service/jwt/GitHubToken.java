/*
Copyright 2021-2023 TELENOR NORGE AS

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package io.apimap.orchestra.orchestra.service.jwt;

public class GitHubToken extends Token {
    protected final String actor;
    protected final String actorId;
    protected final String baseRef;
    protected final String eventName;
    protected final String headRef;
    protected final String jobWorkflowRef;
    protected final String jobWorkflowSha;
    protected final String ref;
    protected final String refType;
    protected final String repositoryVisibility;
    protected final String repository;
    protected final String repositoryId;
    protected final String repositoryOwner;
    protected final String repositoryOwnerId;
    protected final String runId;
    protected final String runNumber;
    protected final String runAttempt;
    protected final String workflow;
    protected final String workflowRef;
    protected final String workflowSha;

    public GitHubToken(String actor, String actorId, String baseRef, String eventName, String headRef, String jobWorkflowRef, String jobWorkflowSha, String ref, String refType, String repositoryVisibility, String repository, String repositoryId, String repositoryOwner, String repositoryOwnerId, String runId, String runNumber, String runAttempt, String workflow, String workflowRef, String workflowSha) {
        this.actor = actor;
        this.actorId = actorId;
        this.baseRef = baseRef;
        this.eventName = eventName;
        this.headRef = headRef;
        this.jobWorkflowRef = jobWorkflowRef;
        this.jobWorkflowSha = jobWorkflowSha;
        this.ref = ref;
        this.refType = refType;
        this.repositoryVisibility = repositoryVisibility;
        this.repository = repository;
        this.repositoryId = repositoryId;
        this.repositoryOwner = repositoryOwner;
        this.repositoryOwnerId = repositoryOwnerId;
        this.runId = runId;
        this.runNumber = runNumber;
        this.runAttempt = runAttempt;
        this.workflow = workflow;
        this.workflowRef = workflowRef;
        this.workflowSha = workflowSha;
    }

    public String getActor() {
        return actor;
    }

    public String getActorId() {
        return actorId;
    }

    public String getBaseRef() {
        return baseRef;
    }

    public String getEventName() {
        return eventName;
    }

    public String getHeadRef() {
        return headRef;
    }

    public String getJobWorkflowRef() {
        return jobWorkflowRef;
    }

    public String getJobWorkflowSha() {
        return jobWorkflowSha;
    }

    public String getRef() {
        return ref;
    }

    public String getRefType() {
        return refType;
    }

    public String getRepositoryVisibility() {
        return repositoryVisibility;
    }

    public String getRepository() {
        return repository;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public String getRepositoryOwner() {
        return repositoryOwner;
    }

    public String getRepositoryOwnerId() {
        return repositoryOwnerId;
    }

    public String getRunId() {
        return runId;
    }

    public String getRunNumber() {
        return runNumber;
    }

    public String getRunAttempt() {
        return runAttempt;
    }

    public String getWorkflow() {
        return workflow;
    }

    public String getWorkflowRef() {
        return workflowRef;
    }

    public String getWorkflowSha() {
        return workflowSha;
    }

    @Override
    public String toString() {
        return "GitHubToken{" +
            "actor='" + actor + '\'' +
            ", actorId='" + actorId + '\'' +
            ", baseRef='" + baseRef + '\'' +
            ", eventName='" + eventName + '\'' +
            ", headRef='" + headRef + '\'' +
            ", jobWorkflowRef='" + jobWorkflowRef + '\'' +
            ", jobWorkflowSha='" + jobWorkflowSha + '\'' +
            ", ref='" + ref + '\'' +
            ", refType='" + refType + '\'' +
            ", repositoryVisibility='" + repositoryVisibility + '\'' +
            ", repository='" + repository + '\'' +
            ", repositoryId='" + repositoryId + '\'' +
            ", repositoryOwner='" + repositoryOwner + '\'' +
            ", repositoryOwnerId='" + repositoryOwnerId + '\'' +
            ", runId='" + runId + '\'' +
            ", runNumber='" + runNumber + '\'' +
            ", runAttempt='" + runAttempt + '\'' +
            ", workflow='" + workflow + '\'' +
            ", workflowRef='" + workflowRef + '\'' +
            ", workflowSha='" + workflowSha + '\'' +
            '}';
    }
}
