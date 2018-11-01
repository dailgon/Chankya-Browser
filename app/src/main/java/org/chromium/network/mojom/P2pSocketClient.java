
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by:
//     mojo/public/tools/bindings/mojom_bindings_generator.py
// For:
//     services/network/public/mojom/p2p.mojom
//

package org.chromium.network.mojom;

import org.chromium.mojo.bindings.DeserializationException;


public interface P2pSocketClient extends org.chromium.mojo.bindings.Interface {



    public interface Proxy extends P2pSocketClient, org.chromium.mojo.bindings.Interface.Proxy {
    }

    Manager<P2pSocketClient, P2pSocketClient.Proxy> MANAGER = P2pSocketClient_Internal.MANAGER;


    void socketCreated(
org.chromium.net.interfaces.IpEndPoint localAddress, org.chromium.net.interfaces.IpEndPoint remoteAddress);



    void sendComplete(
P2pSendPacketMetrics sendMetrics);



    void incomingTcpConnection(
org.chromium.net.interfaces.IpEndPoint socketAddress, P2pSocket socket, org.chromium.mojo.bindings.InterfaceRequest<P2pSocketClient> client);



    void dataReceived(
org.chromium.net.interfaces.IpEndPoint socketAddress, byte[] data, org.chromium.mojo_base.mojom.TimeTicks timestamp);


}
