// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service.proto

package org.chromium.chrome.browser.autofill_assistant.proto;

/**
 * <pre>
 * Show contextual information.
 * </pre>
 *
 * Protobuf type {@code autofill_assistant.ShowDetailsProto}
 */
public  final class ShowDetailsProto extends
    com.google.protobuf.GeneratedMessageLite<
        ShowDetailsProto, ShowDetailsProto.Builder> implements
    // @@protoc_insertion_point(message_implements:autofill_assistant.ShowDetailsProto)
    ShowDetailsProtoOrBuilder {
  private ShowDetailsProto() {
  }
  private int bitField0_;
  public static final int DETAILS_FIELD_NUMBER = 1;
  private org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto details_;
  /**
   * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
   */
  public boolean hasDetails() {
    return ((bitField0_ & 0x00000001) == 0x00000001);
  }
  /**
   * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
   */
  public org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto getDetails() {
    return details_ == null ? org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto.getDefaultInstance() : details_;
  }
  /**
   * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
   */
  private void setDetails(org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto value) {
    if (value == null) {
      throw new NullPointerException();
    }
    details_ = value;
    bitField0_ |= 0x00000001;
    }
  /**
   * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
   */
  private void setDetails(
      org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto.Builder builderForValue) {
    details_ = builderForValue.build();
    bitField0_ |= 0x00000001;
  }
  /**
   * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
   */
  private void mergeDetails(org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto value) {
    if (details_ != null &&
        details_ != org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto.getDefaultInstance()) {
      details_ =
        org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto.newBuilder(details_).mergeFrom(value).buildPartial();
    } else {
      details_ = value;
    }
    bitField0_ |= 0x00000001;
  }
  /**
   * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
   */
  private void clearDetails() {  details_ = null;
    bitField0_ = (bitField0_ & ~0x00000001);
  }

  public static final int CHANGE_FLAGS_FIELD_NUMBER = 2;
  private org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges changeFlags_;
  /**
   * <pre>
   * Flags indicating which parts of the details (if any) have changed.
   * </pre>
   *
   * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
   */
  public boolean hasChangeFlags() {
    return ((bitField0_ & 0x00000002) == 0x00000002);
  }
  /**
   * <pre>
   * Flags indicating which parts of the details (if any) have changed.
   * </pre>
   *
   * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
   */
  public org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges getChangeFlags() {
    return changeFlags_ == null ? org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges.getDefaultInstance() : changeFlags_;
  }
  /**
   * <pre>
   * Flags indicating which parts of the details (if any) have changed.
   * </pre>
   *
   * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
   */
  private void setChangeFlags(org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges value) {
    if (value == null) {
      throw new NullPointerException();
    }
    changeFlags_ = value;
    bitField0_ |= 0x00000002;
    }
  /**
   * <pre>
   * Flags indicating which parts of the details (if any) have changed.
   * </pre>
   *
   * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
   */
  private void setChangeFlags(
      org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges.Builder builderForValue) {
    changeFlags_ = builderForValue.build();
    bitField0_ |= 0x00000002;
  }
  /**
   * <pre>
   * Flags indicating which parts of the details (if any) have changed.
   * </pre>
   *
   * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
   */
  private void mergeChangeFlags(org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges value) {
    if (changeFlags_ != null &&
        changeFlags_ != org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges.getDefaultInstance()) {
      changeFlags_ =
        org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges.newBuilder(changeFlags_).mergeFrom(value).buildPartial();
    } else {
      changeFlags_ = value;
    }
    bitField0_ |= 0x00000002;
  }
  /**
   * <pre>
   * Flags indicating which parts of the details (if any) have changed.
   * </pre>
   *
   * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
   */
  private void clearChangeFlags() {  changeFlags_ = null;
    bitField0_ = (bitField0_ & ~0x00000002);
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      output.writeMessage(1, getDetails());
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      output.writeMessage(2, getChangeFlags());
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSerializedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getDetails());
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getChangeFlags());
    }
    size += unknownFields.getSerializedSize();
    memoizedSerializedSize = size;
    return size;
  }

  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }

  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }

  /**
   * <pre>
   * Show contextual information.
   * </pre>
   *
   * Protobuf type {@code autofill_assistant.ShowDetailsProto}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageLite.Builder<
        org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto, Builder> implements
      // @@protoc_insertion_point(builder_implements:autofill_assistant.ShowDetailsProto)
      org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProtoOrBuilder {
    // Construct using org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto.newBuilder()
    private Builder() {
      super(DEFAULT_INSTANCE);
    }


    /**
     * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
     */
    public boolean hasDetails() {
      return instance.hasDetails();
    }
    /**
     * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
     */
    public org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto getDetails() {
      return instance.getDetails();
    }
    /**
     * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
     */
    public Builder setDetails(org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto value) {
      copyOnWrite();
      instance.setDetails(value);
      return this;
      }
    /**
     * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
     */
    public Builder setDetails(
        org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto.Builder builderForValue) {
      copyOnWrite();
      instance.setDetails(builderForValue);
      return this;
    }
    /**
     * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
     */
    public Builder mergeDetails(org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto value) {
      copyOnWrite();
      instance.mergeDetails(value);
      return this;
    }
    /**
     * <code>optional .autofill_assistant.DetailsProto details = 1;</code>
     */
    public Builder clearDetails() {  copyOnWrite();
      instance.clearDetails();
      return this;
    }

    /**
     * <pre>
     * Flags indicating which parts of the details (if any) have changed.
     * </pre>
     *
     * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
     */
    public boolean hasChangeFlags() {
      return instance.hasChangeFlags();
    }
    /**
     * <pre>
     * Flags indicating which parts of the details (if any) have changed.
     * </pre>
     *
     * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
     */
    public org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges getChangeFlags() {
      return instance.getChangeFlags();
    }
    /**
     * <pre>
     * Flags indicating which parts of the details (if any) have changed.
     * </pre>
     *
     * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
     */
    public Builder setChangeFlags(org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges value) {
      copyOnWrite();
      instance.setChangeFlags(value);
      return this;
      }
    /**
     * <pre>
     * Flags indicating which parts of the details (if any) have changed.
     * </pre>
     *
     * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
     */
    public Builder setChangeFlags(
        org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges.Builder builderForValue) {
      copyOnWrite();
      instance.setChangeFlags(builderForValue);
      return this;
    }
    /**
     * <pre>
     * Flags indicating which parts of the details (if any) have changed.
     * </pre>
     *
     * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
     */
    public Builder mergeChangeFlags(org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges value) {
      copyOnWrite();
      instance.mergeChangeFlags(value);
      return this;
    }
    /**
     * <pre>
     * Flags indicating which parts of the details (if any) have changed.
     * </pre>
     *
     * <code>optional .autofill_assistant.DetailsChanges change_flags = 2;</code>
     */
    public Builder clearChangeFlags() {  copyOnWrite();
      instance.clearChangeFlags();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:autofill_assistant.ShowDetailsProto)
  }
  protected final Object dynamicMethod(
      com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
      Object arg0, Object arg1) {
    switch (method) {
      case NEW_MUTABLE_INSTANCE: {
        return new org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto();
      }
      case IS_INITIALIZED: {
        return DEFAULT_INSTANCE;
      }
      case MAKE_IMMUTABLE: {
        return null;
      }
      case NEW_BUILDER: {
        return new Builder();
      }
      case VISIT: {
        Visitor visitor = (Visitor) arg0;
        org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto other = (org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto) arg1;
        details_ = visitor.visitMessage(details_, other.details_);
        changeFlags_ = visitor.visitMessage(changeFlags_, other.changeFlags_);
        if (visitor == com.google.protobuf.GeneratedMessageLite.MergeFromVisitor
            .INSTANCE) {
          bitField0_ |= other.bitField0_;
        }
        return this;
      }
      case MERGE_FROM_STREAM: {
        com.google.protobuf.CodedInputStream input =
            (com.google.protobuf.CodedInputStream) arg0;
        com.google.protobuf.ExtensionRegistryLite extensionRegistry =
            (com.google.protobuf.ExtensionRegistryLite) arg1;
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default: {
                if (!parseUnknownField(tag, input)) {
                  done = true;
                }
                break;
              }
              case 10: {
                org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto.Builder subBuilder = null;
                if (((bitField0_ & 0x00000001) == 0x00000001)) {
                  subBuilder = details_.toBuilder();
                }
                details_ = input.readMessage(org.chromium.chrome.browser.autofill_assistant.proto.DetailsProto.parser(), extensionRegistry);
                if (subBuilder != null) {
                  subBuilder.mergeFrom(details_);
                  details_ = subBuilder.buildPartial();
                }
                bitField0_ |= 0x00000001;
                break;
              }
              case 18: {
                org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges.Builder subBuilder = null;
                if (((bitField0_ & 0x00000002) == 0x00000002)) {
                  subBuilder = changeFlags_.toBuilder();
                }
                changeFlags_ = input.readMessage(org.chromium.chrome.browser.autofill_assistant.proto.DetailsChanges.parser(), extensionRegistry);
                if (subBuilder != null) {
                  subBuilder.mergeFrom(changeFlags_);
                  changeFlags_ = subBuilder.buildPartial();
                }
                bitField0_ |= 0x00000002;
                break;
              }
            }
          }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw new RuntimeException(e.setUnfinishedMessage(this));
        } catch (java.io.IOException e) {
          throw new RuntimeException(
              new com.google.protobuf.InvalidProtocolBufferException(
                  e.getMessage()).setUnfinishedMessage(this));
        } finally {
        }
      }
      case GET_DEFAULT_INSTANCE: {
        return DEFAULT_INSTANCE;
      }
      case GET_PARSER: {
        if (PARSER == null) {    synchronized (org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto.class) {
            if (PARSER == null) {
              PARSER = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
            }
          }
        }
        return PARSER;
      }
    }
    throw new UnsupportedOperationException();
  }


  // @@protoc_insertion_point(class_scope:autofill_assistant.ShowDetailsProto)
  private static final org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new ShowDetailsProto();
    DEFAULT_INSTANCE.makeImmutable();
  }

  public static org.chromium.chrome.browser.autofill_assistant.proto.ShowDetailsProto getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static volatile com.google.protobuf.Parser<ShowDetailsProto> PARSER;

  public static com.google.protobuf.Parser<ShowDetailsProto> parser() {
    return DEFAULT_INSTANCE.getParserForType();
  }
}
