
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by:
//     mojo/public/tools/bindings/mojom_bindings_generator.py
// For:
//     third_party/blink/public/mojom/portal/portal.mojom
//

package org.chromium.blink.mojom;

import org.chromium.mojo.bindings.DeserializationException;


public interface Portal extends org.chromium.mojo.bindings.Interface {



    public interface Proxy extends Portal, org.chromium.mojo.bindings.Interface.Proxy {
    }

    Manager<Portal, Portal.Proxy> MANAGER = Portal_Internal.MANAGER;


    void init(

InitResponse callback);

    interface InitResponse extends org.chromium.mojo.bindings.Callbacks.Callback1<org.chromium.mojo_base.mojom.UnguessableToken> { }



    void navigate(
org.chromium.url.mojom.Url url);


}
