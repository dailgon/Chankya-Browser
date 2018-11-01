
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by:
//     mojo/public/tools/bindings/mojom_bindings_generator.py
// For:
//     media/mojo/interfaces/video_decoder.mojom
//

package org.chromium.media.mojom;

import org.chromium.mojo.bindings.DeserializationException;


class VideoDecoderClient_Internal {

    public static final org.chromium.mojo.bindings.Interface.Manager<VideoDecoderClient, VideoDecoderClient.Proxy> MANAGER =
            new org.chromium.mojo.bindings.Interface.Manager<VideoDecoderClient, VideoDecoderClient.Proxy>() {

        @Override
        public String getName() {
            return "media.mojom.VideoDecoderClient";
        }

        @Override
        public int getVersion() {
          return 0;
        }

        @Override
        public Proxy buildProxy(org.chromium.mojo.system.Core core,
                                org.chromium.mojo.bindings.MessageReceiverWithResponder messageReceiver) {
            return new Proxy(core, messageReceiver);
        }

        @Override
        public Stub buildStub(org.chromium.mojo.system.Core core, VideoDecoderClient impl) {
            return new Stub(core, impl);
        }

        @Override
        public VideoDecoderClient[] buildArray(int size) {
          return new VideoDecoderClient[size];
        }
    };


    private static final int ON_VIDEO_FRAME_DECODED_ORDINAL = 0;

    private static final int REQUEST_OVERLAY_INFO_ORDINAL = 1;


    static final class Proxy extends org.chromium.mojo.bindings.Interface.AbstractProxy implements VideoDecoderClient.Proxy {

        Proxy(org.chromium.mojo.system.Core core,
              org.chromium.mojo.bindings.MessageReceiverWithResponder messageReceiver) {
            super(core, messageReceiver);
        }


        @Override
        public void onVideoFrameDecoded(
VideoFrame frame, boolean canReadWithoutStalling, org.chromium.mojo_base.mojom.UnguessableToken releaseToken) {

            VideoDecoderClientOnVideoFrameDecodedParams _message = new VideoDecoderClientOnVideoFrameDecodedParams();

            _message.frame = frame;

            _message.canReadWithoutStalling = canReadWithoutStalling;

            _message.releaseToken = releaseToken;


            getProxyHandler().getMessageReceiver().accept(
                    _message.serializeWithHeader(
                            getProxyHandler().getCore(),
                            new org.chromium.mojo.bindings.MessageHeader(ON_VIDEO_FRAME_DECODED_ORDINAL)));

        }


        @Override
        public void requestOverlayInfo(
boolean restartForTransitions) {

            VideoDecoderClientRequestOverlayInfoParams _message = new VideoDecoderClientRequestOverlayInfoParams();

            _message.restartForTransitions = restartForTransitions;


            getProxyHandler().getMessageReceiver().accept(
                    _message.serializeWithHeader(
                            getProxyHandler().getCore(),
                            new org.chromium.mojo.bindings.MessageHeader(REQUEST_OVERLAY_INFO_ORDINAL)));

        }


    }

    static final class Stub extends org.chromium.mojo.bindings.Interface.Stub<VideoDecoderClient> {

        Stub(org.chromium.mojo.system.Core core, VideoDecoderClient impl) {
            super(core, impl);
        }

        @Override
        public boolean accept(org.chromium.mojo.bindings.Message message) {
            try {
                org.chromium.mojo.bindings.ServiceMessage messageWithHeader =
                        message.asServiceMessage();
                org.chromium.mojo.bindings.MessageHeader header = messageWithHeader.getHeader();
                if (!header.validateHeader(org.chromium.mojo.bindings.MessageHeader.NO_FLAG)) {
                    return false;
                }
                switch(header.getType()) {

                    case org.chromium.mojo.bindings.interfacecontrol.InterfaceControlMessagesConstants.RUN_OR_CLOSE_PIPE_MESSAGE_ID:
                        return org.chromium.mojo.bindings.InterfaceControlMessagesHelper.handleRunOrClosePipe(
                                VideoDecoderClient_Internal.MANAGER, messageWithHeader);





                    case ON_VIDEO_FRAME_DECODED_ORDINAL: {

                        VideoDecoderClientOnVideoFrameDecodedParams data =
                                VideoDecoderClientOnVideoFrameDecodedParams.deserialize(messageWithHeader.getPayload());

                        getImpl().onVideoFrameDecoded(data.frame, data.canReadWithoutStalling, data.releaseToken);
                        return true;
                    }





                    case REQUEST_OVERLAY_INFO_ORDINAL: {

                        VideoDecoderClientRequestOverlayInfoParams data =
                                VideoDecoderClientRequestOverlayInfoParams.deserialize(messageWithHeader.getPayload());

                        getImpl().requestOverlayInfo(data.restartForTransitions);
                        return true;
                    }


                    default:
                        return false;
                }
            } catch (org.chromium.mojo.bindings.DeserializationException e) {
                System.err.println(e.toString());
                return false;
            }
        }

        @Override
        public boolean acceptWithResponder(org.chromium.mojo.bindings.Message message, org.chromium.mojo.bindings.MessageReceiver receiver) {
            try {
                org.chromium.mojo.bindings.ServiceMessage messageWithHeader =
                        message.asServiceMessage();
                org.chromium.mojo.bindings.MessageHeader header = messageWithHeader.getHeader();
                if (!header.validateHeader(org.chromium.mojo.bindings.MessageHeader.MESSAGE_EXPECTS_RESPONSE_FLAG)) {
                    return false;
                }
                switch(header.getType()) {

                    case org.chromium.mojo.bindings.interfacecontrol.InterfaceControlMessagesConstants.RUN_MESSAGE_ID:
                        return org.chromium.mojo.bindings.InterfaceControlMessagesHelper.handleRun(
                                getCore(), VideoDecoderClient_Internal.MANAGER, messageWithHeader, receiver);






                    default:
                        return false;
                }
            } catch (org.chromium.mojo.bindings.DeserializationException e) {
                System.err.println(e.toString());
                return false;
            }
        }
    }


    
    static final class VideoDecoderClientOnVideoFrameDecodedParams extends org.chromium.mojo.bindings.Struct {

        private static final int STRUCT_SIZE = 32;
        private static final org.chromium.mojo.bindings.DataHeader[] VERSION_ARRAY = new org.chromium.mojo.bindings.DataHeader[] {new org.chromium.mojo.bindings.DataHeader(32, 0)};
        private static final org.chromium.mojo.bindings.DataHeader DEFAULT_STRUCT_INFO = VERSION_ARRAY[0];
        public VideoFrame frame;
        public boolean canReadWithoutStalling;
        public org.chromium.mojo_base.mojom.UnguessableToken releaseToken;

        private VideoDecoderClientOnVideoFrameDecodedParams(int version) {
            super(STRUCT_SIZE, version);
        }

        public VideoDecoderClientOnVideoFrameDecodedParams() {
            this(0);
        }

        public static VideoDecoderClientOnVideoFrameDecodedParams deserialize(org.chromium.mojo.bindings.Message message) {
            return decode(new org.chromium.mojo.bindings.Decoder(message));
        }

        /**
         * Similar to the method above, but deserializes from a |ByteBuffer| instance.
         *
         * @throws org.chromium.mojo.bindings.DeserializationException on deserialization failure.
         */
        public static VideoDecoderClientOnVideoFrameDecodedParams deserialize(java.nio.ByteBuffer data) {
            return deserialize(new org.chromium.mojo.bindings.Message(
                    data, new java.util.ArrayList<org.chromium.mojo.system.Handle>()));
        }

        @SuppressWarnings("unchecked")
        public static VideoDecoderClientOnVideoFrameDecodedParams decode(org.chromium.mojo.bindings.Decoder decoder0) {
            if (decoder0 == null) {
                return null;
            }
            decoder0.increaseStackDepth();
            VideoDecoderClientOnVideoFrameDecodedParams result;
            try {
                org.chromium.mojo.bindings.DataHeader mainDataHeader = decoder0.readAndValidateDataHeader(VERSION_ARRAY);
                final int elementsOrVersion = mainDataHeader.elementsOrVersion;
                result = new VideoDecoderClientOnVideoFrameDecodedParams(elementsOrVersion);
                    {
                        
                    org.chromium.mojo.bindings.Decoder decoder1 = decoder0.readPointer(8, false);
                    result.frame = VideoFrame.decode(decoder1);
                    }
                    {
                        
                    result.canReadWithoutStalling = decoder0.readBoolean(16, 0);
                    }
                    {
                        
                    org.chromium.mojo.bindings.Decoder decoder1 = decoder0.readPointer(24, true);
                    result.releaseToken = org.chromium.mojo_base.mojom.UnguessableToken.decode(decoder1);
                    }

            } finally {
                decoder0.decreaseStackDepth();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected final void encode(org.chromium.mojo.bindings.Encoder encoder) {
            org.chromium.mojo.bindings.Encoder encoder0 = encoder.getEncoderAtDataOffset(DEFAULT_STRUCT_INFO);
            
            encoder0.encode(this.frame, 8, false);
            
            encoder0.encode(this.canReadWithoutStalling, 16, 0);
            
            encoder0.encode(this.releaseToken, 24, true);
        }
    }



    
    static final class VideoDecoderClientRequestOverlayInfoParams extends org.chromium.mojo.bindings.Struct {

        private static final int STRUCT_SIZE = 16;
        private static final org.chromium.mojo.bindings.DataHeader[] VERSION_ARRAY = new org.chromium.mojo.bindings.DataHeader[] {new org.chromium.mojo.bindings.DataHeader(16, 0)};
        private static final org.chromium.mojo.bindings.DataHeader DEFAULT_STRUCT_INFO = VERSION_ARRAY[0];
        public boolean restartForTransitions;

        private VideoDecoderClientRequestOverlayInfoParams(int version) {
            super(STRUCT_SIZE, version);
        }

        public VideoDecoderClientRequestOverlayInfoParams() {
            this(0);
        }

        public static VideoDecoderClientRequestOverlayInfoParams deserialize(org.chromium.mojo.bindings.Message message) {
            return decode(new org.chromium.mojo.bindings.Decoder(message));
        }

        /**
         * Similar to the method above, but deserializes from a |ByteBuffer| instance.
         *
         * @throws org.chromium.mojo.bindings.DeserializationException on deserialization failure.
         */
        public static VideoDecoderClientRequestOverlayInfoParams deserialize(java.nio.ByteBuffer data) {
            return deserialize(new org.chromium.mojo.bindings.Message(
                    data, new java.util.ArrayList<org.chromium.mojo.system.Handle>()));
        }

        @SuppressWarnings("unchecked")
        public static VideoDecoderClientRequestOverlayInfoParams decode(org.chromium.mojo.bindings.Decoder decoder0) {
            if (decoder0 == null) {
                return null;
            }
            decoder0.increaseStackDepth();
            VideoDecoderClientRequestOverlayInfoParams result;
            try {
                org.chromium.mojo.bindings.DataHeader mainDataHeader = decoder0.readAndValidateDataHeader(VERSION_ARRAY);
                final int elementsOrVersion = mainDataHeader.elementsOrVersion;
                result = new VideoDecoderClientRequestOverlayInfoParams(elementsOrVersion);
                    {
                        
                    result.restartForTransitions = decoder0.readBoolean(8, 0);
                    }

            } finally {
                decoder0.decreaseStackDepth();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected final void encode(org.chromium.mojo.bindings.Encoder encoder) {
            org.chromium.mojo.bindings.Encoder encoder0 = encoder.getEncoderAtDataOffset(DEFAULT_STRUCT_INFO);
            
            encoder0.encode(this.restartForTransitions, 8, 0);
        }
    }



}
