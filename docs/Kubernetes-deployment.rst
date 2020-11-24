**********************************
Deploying bookkeeper To Kubernetes
**********************************

The bookkeeper facility can run as a Kubernetes (k8s) service. These instructions
describe how to build the required Docker image and deploy bookkeeper on a Kubernetes (k8s)
cluster.

Build The bookkeeper Docker Image
#################################

In order to run bookkeeper on a k8s cluster, the Docker image 'bookkeeper' must be built
from a bookkeeper development environment created from this repository.

Before building the image, the Docker tag should be updated. This is set in *pom.xml*,
for example:
::

     <docker.tag>0.3.0</docker.tag>


Next, the file *Docker* should be checked to determine if any changes are needed for the build,
for example, to update the version number of the bookkeeper jar file to use.

To build the Docker image use the command:
..

     mvn docker:build

When the image has been built successfully, it can be uploaded to Docker Hub.

A Docker hub username must be setup, then authentication must be configured in the
bookkeeper development environment so that the image can be uploaded.

There are several ways to configure Docker hub authentication for the bookkeeper development environment
so that the Docker image can be uploaded. One simple method is to put the authentication information
('username', 'password') into the Maven settings file. Here
is an example `~/.m2/settings.xml` file, but with the password value not filled in:
::

     <settings xmlns="http://maven.apache.org/SETTINGS/1.1.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd">
          <localRepository/>
          <interactiveMode/>
          <offline/>
          <pluginGroups/>
          <servers>
               <server>
                    <id>docker.io</id>
                    <username>your Docker Hub username</username>
                    <password>your Docker hub password</password>
               </server>
          </servers>
          <mirrors/>
          <proxies/>
          <profiles/>
          <activeProfiles/>
     </settings>

The image is pushed to Docker Hub with the command:
::

     mvn docker:push

Deploying bookkeeper to Kubernetes
##################################

The k8s deployment for bookkeeper uses several k8s *manifest* files that are used to
config k8s resources, start podds and services.

k8s Deployment Configuration
============================

The following configuration can be performed on the k8s primary node.

Setup the bookkeeper Linux user
===============================

A separate Linux username *bookkeeper* is used for all k8s configuration and
maintenance that will configure and start any needed Linux and k8s resources.
These

First setup ‘bookkeeper’ username:
::

     sudo useradd -m -d /home/bookkeeper -s /bin/bash bookkeeper


Setup k8s Authentication
========================

k8s authentication is described in more detail at https://kubernetes.io/docs/reference/access-authn-authz/authentication/.

k8s authentication must be setup so that bookkeeper can execute k8s commands to configure, start and stop
k8s resources.

These are the commands that were used to setup k8s authentication for the *bookkeeper* user. Note that
currently bookkeeper is setup as either a k8s admin user or as a more restricted k8s *role*. These roles can
be switched by using the *kubectl config use-context* command shown below.
::

    # bookkeeper resources are in this namespace
    kubectl create namespace bookkeeper

    # setup k8s authentication for restricted *bookkeeper* user
    kubectl create -f bookkeeper-role.yaml
    kubectl create -f bookkeeper-role-binding.yaml:

    # setup k8s authentication for bookkeeper as admin user
    openssl genrsa -out bookkeeper.key 2048
    openssl req -new -key bookkeeper.key -out bookkeeper.csr -subj '/CN=bookkeeper/O=nceas'
    sudo openssl x509 -req -in bookkeeper.csr -CA /etc/kubernetes/pki/ca.crt -CAkey /etc/kubernetes/pki/ca.key -CAcreateserial -out bookkeeper.crt -days 1000
    mkdir -p $HOME/.kube
    sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
    sudo chown $(id -u):$(id -g) $HOME/.kube/config
    kubectl config set-credentials bookkeeper --client-certificate=~/.kube/bookkeeper.crt  --client-key=~/.kube/bookkeeper.key
    kubectl config use-context bookkeeper
    kubectl config use-context kubernetes-admin@kubernetes

Copy Deployment Files
=====================

Copy the bookkeeper git repository directory './deployments' to the bookkeeper user home directory on
the k8s primary node (aka the 'master' node as described in the k8s documentation).

Setup A Persistent Volume
=========================

The bookkeeper service uses a k8s persistent volume to access data while it is running. The bookkeeper
configuration file *bookkeeper.yml* is stored on this persistent disk and read when the bookkeeper
pod starts.

Also, the Postgresql database used by bookkeeper is persisted to this volume.

These are the Linux commands that were used to setup the NFS fileshare that is used by the
persistent volume. This fileshare can only be used by the bookkeeper service.
::

    # add entry for new node in /etc/exports on docker4
    exportfs -a

    # add entry, on new node, in /etc/fstab
    # for example. on the primary k8s node, add the line
    docker-ucsb-4.dataone.org:/data /data   nfs     rsize=8192,wsize=8192,timeo=14,intr

    # Ensure NFS is installed
    sudo apt install nfs-common

    # mount the volume
    sudo mount -a

To start the k8s persistent volume, enter these commands:
::

     kubectl apply -f pv.yml
     kubectl apply -f pvc.yml

Create The Bookkeeper k8s configuration
=======================================

The bookkeeper k8s configuration creates a k8s configmap that contains the Postgresql authentication
information. This command only needs to be entered when bookkeeper k8s is initially configured:
::

     kubectl apply -f bookkeeper-config.yml

Start The bookkeeper Kubernetes Service
=======================================

The bookkeeper k8s service is dependant on a separate Postgresql k8s service.
This Postgresql service can be launche from the './deployments' directory on a k8s node with the command
::

     kubectl create -f ./postgresql-deployment.yml

Next, the bookkeeper service can be launched with the command
::

     kubectl create -f ./bookkeeper-deployment.yml

Inspect The bookkeeper service
==============================

To verify that the bookkeeper pod and service have been started, enter the following command:
::

     kubectl get pods,services --namespace=bookkeeper -o wide

The pod status 'Running' confirms that the bookkeeper and PostgreSQL pods have been started.
::

     NAME                              READY   STATUS    RESTARTS   AGE   IP               NODE                NOMINATED NODE   READINESS GATES
     pod/bookkeeper-76d8976859-hkws2   1/1     Running   0          31d   192.168.50.184   docker-dev-ucsb-2   <none>           <none>
     pod/postgres-6dfd57f7f4-8xr2l     1/1     Running   0          31d   192.168.50.180   docker-dev-ucsb-2   <none>           <none>

     NAME                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)             AGE   SELECTOR
     service/bookkeeper   ClusterIP   10.97.186.202   <none>        8080/TCP,8081/TCP   31d   app=bookkeeper
     service/postgres     ClusterIP   10.96.158.33    <none>        5432/TCP            31d   app=postgres

