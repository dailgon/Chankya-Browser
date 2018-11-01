
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by:
//     mojo/public/tools/bindings/mojom_bindings_generator.py
// For:
//     third_party/blink/public/mojom/indexeddb/indexeddb.mojom
//

package org.chromium.blink.mojom;

import org.chromium.mojo.bindings.DeserializationException;


public final class IdbKeyData extends org.chromium.mojo.bindings.Union {

    public static final class Tag {
        public static final int KeyArray = 0;
        public static final int Binary = 1;
        public static final int String = 2;
        public static final int Date = 3;
        public static final int Number = 4;
        public static final int Other = 5;
    };
    private IdbKey[] mKeyArray;
    private byte[] mBinary;
    private org.chromium.mojo_base.mojom.String16 mString;
    private double mDate;
    private double mNumber;
    private int mOther;

    public void setKeyArray(IdbKey[] keyArray) {
        this.mTag = Tag.KeyArray;
        this.mKeyArray = keyArray;
    }

    public IdbKey[] getKeyArray() {
        assert this.mTag == Tag.KeyArray;
        return this.mKeyArray;
    }

    public void setBinary(byte[] binary) {
        this.mTag = Tag.Binary;
        this.mBinary = binary;
    }

    public byte[] getBinary() {
        assert this.mTag == Tag.Binary;
        return this.mBinary;
    }

    public void setString(org.chromium.mojo_base.mojom.String16 string) {
        this.mTag = Tag.String;
        this.mString = string;
    }

    public org.chromium.mojo_base.mojom.String16 getString() {
        assert this.mTag == Tag.String;
        return this.mString;
    }

    public void setDate(double date) {
        this.mTag = Tag.Date;
        this.mDate = date;
    }

    public double getDate() {
        assert this.mTag == Tag.Date;
        return this.mDate;
    }

    public void setNumber(double number) {
        this.mTag = Tag.Number;
        this.mNumber = number;
    }

    public double getNumber() {
        assert this.mTag == Tag.Number;
        return this.mNumber;
    }

    public void setOther(int other) {
        this.mTag = Tag.Other;
        this.mOther = other;
    }

    public int getOther() {
        assert this.mTag == Tag.Other;
        return this.mOther;
    }


    @Override
    protected final void encode(org.chromium.mojo.bindings.Encoder encoder0, int offset) {
        encoder0.encode(org.chromium.mojo.bindings.BindingsHelper.UNION_SIZE, offset);
        encoder0.encode(this.mTag, offset + 4);
        switch (mTag) {
            case Tag.KeyArray: {
                
                if (this.mKeyArray == null) {
                    encoder0.encodeNullPointer(offset + 8, false);
                } else {
                    org.chromium.mojo.bindings.Encoder encoder1 = encoder0.encodePointerArray(this.mKeyArray.length, offset + 8, org.chromium.mojo.bindings.BindingsHelper.UNSPECIFIED_ARRAY_LENGTH);
                    for (int i0 = 0; i0 < this.mKeyArray.length; ++i0) {
                        
                        encoder1.encode(this.mKeyArray[i0], org.chromium.mojo.bindings.DataHeader.HEADER_SIZE + org.chromium.mojo.bindings.BindingsHelper.POINTER_SIZE * i0, false);
                    }
                }
                break;
            }
            case Tag.Binary: {
                
                encoder0.encode(this.mBinary, offset + 8, org.chromium.mojo.bindings.BindingsHelper.NOTHING_NULLABLE, org.chromium.mojo.bindings.BindingsHelper.UNSPECIFIED_ARRAY_LENGTH);
                break;
            }
            case Tag.String: {
                
                encoder0.encode(this.mString, offset + 8, false);
                break;
            }
            case Tag.Date: {
                
                encoder0.encode(this.mDate, offset + 8);
                break;
            }
            case Tag.Number: {
                
                encoder0.encode(this.mNumber, offset + 8);
                break;
            }
            case Tag.Other: {
                
                encoder0.encode(this.mOther, offset + 8);
                break;
            }
            default: {
                break;
            }
        }
    }

    public static IdbKeyData deserialize(org.chromium.mojo.bindings.Message message) {
        return decode(new org.chromium.mojo.bindings.Decoder(message).decoderForSerializedUnion(), 0);
    }

    public static final IdbKeyData decode(org.chromium.mojo.bindings.Decoder decoder0, int offset) {
        org.chromium.mojo.bindings.DataHeader dataHeader = decoder0.readDataHeaderForUnion(offset);
        if (dataHeader.size == 0) {
            return null;
        }
        IdbKeyData result = new IdbKeyData();
        switch (dataHeader.elementsOrVersion) {
            case Tag.KeyArray: {
                
                org.chromium.mojo.bindings.Decoder decoder1 = decoder0.readPointer(offset + org.chromium.mojo.bindings.DataHeader.HEADER_SIZE, false);
                {
                    org.chromium.mojo.bindings.DataHeader si1 = decoder1.readDataHeaderForPointerArray(org.chromium.mojo.bindings.BindingsHelper.UNSPECIFIED_ARRAY_LENGTH);
                    result.mKeyArray = new IdbKey[si1.elementsOrVersion];
                    for (int i1 = 0; i1 < si1.elementsOrVersion; ++i1) {
                        
                        org.chromium.mojo.bindings.Decoder decoder2 = decoder1.readPointer(org.chromium.mojo.bindings.DataHeader.HEADER_SIZE + org.chromium.mojo.bindings.BindingsHelper.POINTER_SIZE * i1, false);
                        result.mKeyArray[i1] = IdbKey.decode(decoder2);
                    }
                }
                result.mTag = Tag.KeyArray;
                break;
            }
            case Tag.Binary: {
                
                result.mBinary = decoder0.readBytes(offset + org.chromium.mojo.bindings.DataHeader.HEADER_SIZE, org.chromium.mojo.bindings.BindingsHelper.NOTHING_NULLABLE, org.chromium.mojo.bindings.BindingsHelper.UNSPECIFIED_ARRAY_LENGTH);
                result.mTag = Tag.Binary;
                break;
            }
            case Tag.String: {
                
                org.chromium.mojo.bindings.Decoder decoder1 = decoder0.readPointer(offset + org.chromium.mojo.bindings.DataHeader.HEADER_SIZE, false);
                result.mString = org.chromium.mojo_base.mojom.String16.decode(decoder1);
                result.mTag = Tag.String;
                break;
            }
            case Tag.Date: {
                
                result.mDate = decoder0.readDouble(offset + org.chromium.mojo.bindings.DataHeader.HEADER_SIZE);
                result.mTag = Tag.Date;
                break;
            }
            case Tag.Number: {
                
                result.mNumber = decoder0.readDouble(offset + org.chromium.mojo.bindings.DataHeader.HEADER_SIZE);
                result.mTag = Tag.Number;
                break;
            }
            case Tag.Other: {
                
                result.mOther = decoder0.readInt(offset + org.chromium.mojo.bindings.DataHeader.HEADER_SIZE);
                    IdbDatalessKeyType.validate(result.mOther);
                result.mTag = Tag.Other;
                break;
            }
            default: {
                break;
            }
        }
        return result;
    }
}