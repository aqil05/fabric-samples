You can use Fabric samples to get started working with Hyperledger Fabric, explore important Fabric features, and learn how to build applications that can interact with blockchain networks using the Fabric SDKs. To learn more about Hyperledger Fabric, visit the Fabric documentation.

Getting started with the Fabric samples
To use the Fabric samples, you need to download the Fabric Docker images and the Fabric CLI tools. First, make sure that you have installed all of the Fabric prerequisites. You can then follow the instructions to Install the Fabric Samples, Binaries, and Docker Images in the Fabric documentation. In addition to downloading the Fabric images and tool binaries, the Fabric samples will also be cloned to your local machine.

**Test network**
The Fabric test network in the samples repository provides a Docker Compose based test network with two Organization peers and an ordering service node. You can use it on your local machine to run the samples listed below. You can also use it to deploy and test your own Fabric chaincodes and applications. To get started, see the test network tutorial.

The Kubernetes Test Network sample builds upon the Compose network, constructing a Fabric network with peer, orderer, and CA infrastructure nodes running on Kubernetes. In addition to providing a sample Kubernetes guide, the Kube test network can be used as a platform to author and debug cloud ready Fabric Client applications on a development or CI workstation.
**Asset transfer samples and tutorials**
The asset transfer series provides a series of sample smart contracts and applications to demonstrate how to store and transfer assets using Hyperledger Fabric. Each sample and associated tutorial in the series demonstrates a different core capability in Hyperledger Fabric. The Basic sample provides an introduction on how to write smart contracts and how to interact with a Fabric network using the Fabric SDKs. The Ledger queries, Private data, and State-based endorsement samples demonstrate these additional capabilities. Finally, the Secured agreement sample demonstrates how to bring all the capabilities together to securely transfer an asset in a more realistic transfer scenario.
**License**
Hyperledger Project source code files are made available under the Apache License, Version 2.0 (Apache-2.0), located in the LICENSE file. Hyperledger Project documentation files are made available under the Creative Commons Attribution 4.0 International License (CC-BY-4.0), available at http://creativecommons.org/licenses/by/4.0/.
