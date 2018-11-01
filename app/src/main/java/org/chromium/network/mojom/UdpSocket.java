
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by:
//     mojo/public/tools/bindings/mojom_bindings_generator.py
// For:
//     services/network/public/mojom/udp_socket.mojom
//

package org.chromium.network.mojom;

import org.chromium.mojo.bindings.DeserializationException;


public interface UdpSocket extends org.chromium.mojo.bindings.Interface {



    public interface Proxy extends UdpSocket, org.chromium.mojo.bindings.Interface.Proxy {
    }

    Manager<UdpSocket, UdpSocket.Proxy> MANAGER = UdpSocket_Internal.MANAGER;


    void bind(
org.chromium.net.interfaces.IpEndPoint localAddr, UdpSocketOptions socketOptions, 
BindResponse callback);

    interface BindResponse extends org.chromium.mojo.bindings.Callbacks.Callback2<Integer, org.chromium.net.interfaces.IpEndPoint> { }



    void connect(
org.chromium.net.interfaces.IpEndPoint remoteAddr, UdpSocketOptions socketOptions, 
ConnectResponse callback);

    interface ConnectResponse extends org.chromium.mojo.bindings.Callbacks.Callback2<Integer, org.chromium.net.interfaces.IpEndPoint> { }



    void setBroadcast(
boolean broadcast, 
SetBroadcastResponse callback);

    interface SetBroadcastResponse extends org.chromium.mojo.bindings.Callbacks.Callback1<Integer> { }



    void setSendBufferSize(
int sendBufferSize, 
SetSendBufferSizeResponse callback);

    interface SetSendBufferSizeResponse extends org.chromium.mojo.bindings.Callbacks.Callback1<Integer> { }



    void setReceiveBufferSize(
int receiveBufferSize, 
SetReceiveBufferSizeResponse callback);

    interface SetReceiveBufferSizeResponse extends org.chromium.mojo.bindings.Callbacks.Callback1<Integer> { }



    void joinGroup(
org.chromium.net.interfaces.IpAddress groupAddress, 
JoinGroupResponse callback);

    interface JoinGroupResponse extends org.chromium.mojo.bindings.Callbacks.Callback1<Integer> { }



    void leaveGroup(
org.chromium.net.interfaces.IpAddress groupAddress, 
LeaveGroupResponse callback);

    interface LeaveGroupResponse extends org.chromium.mojo.bindings.Callbacks.Callback1<Integer> { }



    void receiveMore(
int numAdditionalDatagrams);



    void receiveMoreWithBufferSize(
int numAdditionalDatagrams, int bufferSize);



    void sendTo(
org.chromium.net.interfaces.IpEndPoint destAddr, org.chromium.mojo_base.mojom.ReadOnlyBuffer data, MutableNetworkTrafficAnnotationTag trafficAnnotation, 
SendToResponse callback);

    interface SendToResponse extends org.chromium.mojo.bindings.Callbacks.Callback1<Integer> { }



    void send(
org.chromium.mojo_base.mojom.ReadOnlyBuffer data, MutableNetworkTrafficAnnotationTag trafficAnnotation, 
SendResponse callback);

    interface SendResponse extends org.chromium.mojo.bindings.Callbacks.Callback1<Integer> { }



    void close(
);


}
