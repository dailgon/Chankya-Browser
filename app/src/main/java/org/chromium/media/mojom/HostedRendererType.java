
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by:
//     mojo/public/tools/bindings/mojom_bindings_generator.py
// For:
//     media/mojo/interfaces/interface_factory.mojom
//

package org.chromium.media.mojom;

import org.chromium.mojo.bindings.DeserializationException;

public final class HostedRendererType {


    public static final int DEFAULT = 0;

    public static final int MEDIA_PLAYER = DEFAULT + 1;

    public static final int FLINGING = MEDIA_PLAYER + 1;


    private static final boolean IS_EXTENSIBLE = false;

    public static boolean isKnownValue(int value) {
        switch (value) {
            case 0:
            case 1:
            case 2:
                return true;
        }
        return false;
    }

    public static void validate(int value) {
        if (IS_EXTENSIBLE || isKnownValue(value))
            return;

        throw new DeserializationException("Invalid enum value.");
    }

    private HostedRendererType() {}

}