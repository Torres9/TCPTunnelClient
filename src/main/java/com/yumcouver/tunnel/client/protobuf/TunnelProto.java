// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/java/tunnel.proto

package com.yumcouver.tunnel.client.protobuf;

public final class TunnelProto {
    private TunnelProto() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
    }

    public interface TunnelCommandOrBuilder extends
            // @@protoc_insertion_point(interface_extends:TunnelCommand)
            com.google.protobuf.MessageOrBuilder {

        /**
         * <code>required .TunnelCommand.Method method = 1;</code>
         */
        boolean hasMethod();

        /**
         * <code>required .TunnelCommand.Method method = 1;</code>
         */
        com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method getMethod();

        /**
         * <code>optional string message = 2;</code>
         */
        boolean hasMessage();

        /**
         * <code>optional string message = 2;</code>
         */
        java.lang.String getMessage();

        /**
         * <code>optional string message = 2;</code>
         */
        com.google.protobuf.ByteString
        getMessageBytes();
    }

    /**
     * Protobuf type {@code TunnelCommand}
     */
    public static final class TunnelCommand extends
            com.google.protobuf.GeneratedMessage implements
            // @@protoc_insertion_point(message_implements:TunnelCommand)
            TunnelCommandOrBuilder {
        // Use TunnelCommand.newBuilder() to construct.
        private TunnelCommand(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private TunnelCommand(boolean noInit) {
            this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
        }

        private static final TunnelCommand defaultInstance;

        public static TunnelCommand getDefaultInstance() {
            return defaultInstance;
        }

        public TunnelCommand getDefaultInstanceForType() {
            return defaultInstance;
        }

        private final com.google.protobuf.UnknownFieldSet unknownFields;

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
            return this.unknownFields;
        }

        private TunnelCommand(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            initFields();
            int mutable_bitField0_ = 0;
            com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                    com.google.protobuf.UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields,
                                    extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 8: {
                            int rawValue = input.readEnum();
                            com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method value = com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method.valueOf(rawValue);
                            if (value == null) {
                                unknownFields.mergeVarintField(1, rawValue);
                            } else {
                                bitField0_ |= 0x00000001;
                                method_ = value;
                            }
                            break;
                        }
                        case 18: {
                            com.google.protobuf.ByteString bs = input.readBytes();
                            bitField0_ |= 0x00000002;
                            message_ = bs;
                            break;
                        }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(
                        e.getMessage()).setUnfinishedMessage(this);
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return com.yumcouver.tunnel.client.protobuf.TunnelProto.internal_static_TunnelCommand_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return com.yumcouver.tunnel.client.protobuf.TunnelProto.internal_static_TunnelCommand_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.class, com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Builder.class);
        }

        public static com.google.protobuf.Parser<TunnelCommand> PARSER =
                new com.google.protobuf.AbstractParser<TunnelCommand>() {
                    public TunnelCommand parsePartialFrom(
                            com.google.protobuf.CodedInputStream input,
                            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                        return new TunnelCommand(input, extensionRegistry);
                    }
                };

        @java.lang.Override
        public com.google.protobuf.Parser<TunnelCommand> getParserForType() {
            return PARSER;
        }

        /**
         * Protobuf enum {@code TunnelCommand.Method}
         */
        public enum Method
                implements com.google.protobuf.ProtocolMessageEnum {
            /**
             * <code>SYN = 0;</code>
             */
            SYN(0, 0),
            /**
             * <code>CONTROLLER_INIT = 1;</code>
             */
            CONTROLLER_INIT(1, 1),
            /**
             * <code>TUNNEL_INIT = 2;</code>
             */
            TUNNEL_INIT(2, 2),
            /**
             * <code>ERROR = 3;</code>
             */
            ERROR(3, 3),;

            /**
             * <code>SYN = 0;</code>
             */
            public static final int SYN_VALUE = 0;
            /**
             * <code>CONTROLLER_INIT = 1;</code>
             */
            public static final int CONTROLLER_INIT_VALUE = 1;
            /**
             * <code>TUNNEL_INIT = 2;</code>
             */
            public static final int TUNNEL_INIT_VALUE = 2;
            /**
             * <code>ERROR = 3;</code>
             */
            public static final int ERROR_VALUE = 3;


            public final int getNumber() {
                return value;
            }

            public static Method valueOf(int value) {
                switch (value) {
                    case 0:
                        return SYN;
                    case 1:
                        return CONTROLLER_INIT;
                    case 2:
                        return TUNNEL_INIT;
                    case 3:
                        return ERROR;
                    default:
                        return null;
                }
            }

            public static com.google.protobuf.Internal.EnumLiteMap<Method>
            internalGetValueMap() {
                return internalValueMap;
            }

            private static com.google.protobuf.Internal.EnumLiteMap<Method>
                    internalValueMap =
                    new com.google.protobuf.Internal.EnumLiteMap<Method>() {
                        public Method findValueByNumber(int number) {
                            return Method.valueOf(number);
                        }
                    };

            public final com.google.protobuf.Descriptors.EnumValueDescriptor
            getValueDescriptor() {
                return getDescriptor().getValues().get(index);
            }

            public final com.google.protobuf.Descriptors.EnumDescriptor
            getDescriptorForType() {
                return getDescriptor();
            }

            public static final com.google.protobuf.Descriptors.EnumDescriptor
            getDescriptor() {
                return com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.getDescriptor().getEnumTypes().get(0);
            }

            private static final Method[] VALUES = values();

            public static Method valueOf(
                    com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new java.lang.IllegalArgumentException(
                            "EnumValueDescriptor is not for this type.");
                }
                return VALUES[desc.getIndex()];
            }

            private final int index;
            private final int value;

            private Method(int index, int value) {
                this.index = index;
                this.value = value;
            }

            // @@protoc_insertion_point(enum_scope:TunnelCommand.Method)
        }

        private int bitField0_;
        public static final int METHOD_FIELD_NUMBER = 1;
        private com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method method_;

        /**
         * <code>required .TunnelCommand.Method method = 1;</code>
         */
        public boolean hasMethod() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        /**
         * <code>required .TunnelCommand.Method method = 1;</code>
         */
        public com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method getMethod() {
            return method_;
        }

        public static final int MESSAGE_FIELD_NUMBER = 2;
        private java.lang.Object message_;

        /**
         * <code>optional string message = 2;</code>
         */
        public boolean hasMessage() {
            return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        /**
         * <code>optional string message = 2;</code>
         */
        public java.lang.String getMessage() {
            java.lang.Object ref = message_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    message_ = s;
                }
                return s;
            }
        }

        /**
         * <code>optional string message = 2;</code>
         */
        public com.google.protobuf.ByteString
        getMessageBytes() {
            java.lang.Object ref = message_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8(
                                (java.lang.String) ref);
                message_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        private void initFields() {
            method_ = com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method.SYN;
            message_ = "";
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized == 1) return true;
            if (isInitialized == 0) return false;

            if (!hasMethod()) {
                memoizedIsInitialized = 0;
                return false;
            }
            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output)
                throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeEnum(1, method_.getNumber());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                output.writeBytes(2, getMessageBytes());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeEnumSize(1, method_.getNumber());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeBytesSize(2, getMessageBytes());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        private static final long serialVersionUID = 0L;

        @java.lang.Override
        protected java.lang.Object writeReplace()
                throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseFrom(
                com.google.protobuf.ByteString data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseFrom(
                com.google.protobuf.ByteString data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseFrom(byte[] data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseFrom(
                byte[] data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseFrom(java.io.InputStream input)
                throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseDelimitedFrom(java.io.InputStream input)
                throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseDelimitedFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseFrom(
                com.google.protobuf.CodedInputStream input)
                throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parseFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
                com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /**
         * Protobuf type {@code TunnelCommand}
         */
        public static final class Builder extends
                com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                // @@protoc_insertion_point(builder_implements:TunnelCommand)
                com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommandOrBuilder {
            public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
                return com.yumcouver.tunnel.client.protobuf.TunnelProto.internal_static_TunnelCommand_descriptor;
            }

            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
                return com.yumcouver.tunnel.client.protobuf.TunnelProto.internal_static_TunnelCommand_fieldAccessorTable
                        .ensureFieldAccessorsInitialized(
                                com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.class, com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Builder.class);
            }

            // Construct using com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(
                    com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                method_ = com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method.SYN;
                bitField0_ = (bitField0_ & ~0x00000001);
                message_ = "";
                bitField0_ = (bitField0_ & ~0x00000002);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
                return com.yumcouver.tunnel.client.protobuf.TunnelProto.internal_static_TunnelCommand_descriptor;
            }

            public com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand getDefaultInstanceForType() {
                return com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.getDefaultInstance();
            }

            public com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand build() {
                com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            public com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand buildPartial() {
                com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand result = new com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.method_ = method_;
                if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
                    to_bitField0_ |= 0x00000002;
                }
                result.message_ = message_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand) {
                    return mergeFrom((com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand other) {
                if (other == com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.getDefaultInstance())
                    return this;
                if (other.hasMethod()) {
                    setMethod(other.getMethod());
                }
                if (other.hasMessage()) {
                    bitField0_ |= 0x00000002;
                    message_ = other.message_;
                    onChanged();
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                if (!hasMethod()) {

                    return false;
                }
                return true;
            }

            public Builder mergeFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                    throws java.io.IOException {
                com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand) e.getUnfinishedMessage();
                    throw e;
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            private int bitField0_;

            private com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method method_ = com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method.SYN;

            /**
             * <code>required .TunnelCommand.Method method = 1;</code>
             */
            public boolean hasMethod() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            /**
             * <code>required .TunnelCommand.Method method = 1;</code>
             */
            public com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method getMethod() {
                return method_;
            }

            /**
             * <code>required .TunnelCommand.Method method = 1;</code>
             */
            public Builder setMethod(com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                method_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required .TunnelCommand.Method method = 1;</code>
             */
            public Builder clearMethod() {
                bitField0_ = (bitField0_ & ~0x00000001);
                method_ = com.yumcouver.tunnel.client.protobuf.TunnelProto.TunnelCommand.Method.SYN;
                onChanged();
                return this;
            }

            private java.lang.Object message_ = "";

            /**
             * <code>optional string message = 2;</code>
             */
            public boolean hasMessage() {
                return ((bitField0_ & 0x00000002) == 0x00000002);
            }

            /**
             * <code>optional string message = 2;</code>
             */
            public java.lang.String getMessage() {
                java.lang.Object ref = message_;
                if (!(ref instanceof java.lang.String)) {
                    com.google.protobuf.ByteString bs =
                            (com.google.protobuf.ByteString) ref;
                    java.lang.String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        message_ = s;
                    }
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>optional string message = 2;</code>
             */
            public com.google.protobuf.ByteString
            getMessageBytes() {
                java.lang.Object ref = message_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b =
                            com.google.protobuf.ByteString.copyFromUtf8(
                                    (java.lang.String) ref);
                    message_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>optional string message = 2;</code>
             */
            public Builder setMessage(
                    java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                message_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>optional string message = 2;</code>
             */
            public Builder clearMessage() {
                bitField0_ = (bitField0_ & ~0x00000002);
                message_ = getDefaultInstance().getMessage();
                onChanged();
                return this;
            }

            /**
             * <code>optional string message = 2;</code>
             */
            public Builder setMessageBytes(
                    com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                message_ = value;
                onChanged();
                return this;
            }

            // @@protoc_insertion_point(builder_scope:TunnelCommand)
        }

        static {
            defaultInstance = new TunnelCommand(true);
            defaultInstance.initFields();
        }

        // @@protoc_insertion_point(class_scope:TunnelCommand)
    }

    private static final com.google.protobuf.Descriptors.Descriptor
            internal_static_TunnelCommand_descriptor;
    private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internal_static_TunnelCommand_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\n\032src/main/java/tunnel.proto\"\213\001\n\rTunnelC" +
                        "ommand\022%\n\006method\030\001 \002(\0162\025.TunnelCommand.M" +
                        "ethod\022\017\n\007message\030\002 \001(\t\"B\n\006Method\022\007\n\003SYN\020" +
                        "\000\022\023\n\017CONTROLLER_INIT\020\001\022\017\n\013TUNNEL_INIT\020\002\022" +
                        "\t\n\005ERROR\020\003B5\n$com.yumcouver.tunnel.clien" +
                        "t.protobufB\013TunnelProtoH\001"
        };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
                new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
                    public com.google.protobuf.ExtensionRegistry assignDescriptors(
                            com.google.protobuf.Descriptors.FileDescriptor root) {
                        descriptor = root;
                        return null;
                    }
                };
        com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                        }, assigner);
        internal_static_TunnelCommand_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_TunnelCommand_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                internal_static_TunnelCommand_descriptor,
                new java.lang.String[]{"Method", "Message",});
    }

    // @@protoc_insertion_point(outer_class_scope)
}
