/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package simpleMessenger;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
//@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2019-08-05")
public class UserDefinition implements org.apache.thrift.TBase<UserDefinition, UserDefinition._Fields>, java.io.Serializable, Cloneable, Comparable<UserDefinition> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("UserDefinition");

  private static final org.apache.thrift.protocol.TField UNIQUE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("uniqueID", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField IP_ADDR_FIELD_DESC = new org.apache.thrift.protocol.TField("ip_addr", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PORT_FIELD_DESC = new org.apache.thrift.protocol.TField("port", org.apache.thrift.protocol.TType.I32, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new UserDefinitionStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new UserDefinitionTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String uniqueID; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String ip_addr; // required
  public int port; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    UNIQUE_ID((short)1, "uniqueID"),
    IP_ADDR((short)2, "ip_addr"),
    PORT((short)3, "port");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // UNIQUE_ID
          return UNIQUE_ID;
        case 2: // IP_ADDR
          return IP_ADDR;
        case 3: // PORT
          return PORT;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __PORT_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.UNIQUE_ID, new org.apache.thrift.meta_data.FieldMetaData("uniqueID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.IP_ADDR, new org.apache.thrift.meta_data.FieldMetaData("ip_addr", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PORT, new org.apache.thrift.meta_data.FieldMetaData("port", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(UserDefinition.class, metaDataMap);
  }

  public UserDefinition() {
  }

  public UserDefinition(
    java.lang.String uniqueID,
    java.lang.String ip_addr,
    int port)
  {
    this();
    this.uniqueID = uniqueID;
    this.ip_addr = ip_addr;
    this.port = port;
    setPortIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public UserDefinition(UserDefinition other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetUniqueID()) {
      this.uniqueID = other.uniqueID;
    }
    if (other.isSetIp_addr()) {
      this.ip_addr = other.ip_addr;
    }
    this.port = other.port;
  }

  public UserDefinition deepCopy() {
    return new UserDefinition(this);
  }

  @Override
  public void clear() {
    this.uniqueID = null;
    this.ip_addr = null;
    setPortIsSet(false);
    this.port = 0;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getUniqueID() {
    return this.uniqueID;
  }

  public UserDefinition setUniqueID(@org.apache.thrift.annotation.Nullable java.lang.String uniqueID) {
    this.uniqueID = uniqueID;
    return this;
  }

  public void unsetUniqueID() {
    this.uniqueID = null;
  }

  /** Returns true if field uniqueID is set (has been assigned a value) and false otherwise */
  public boolean isSetUniqueID() {
    return this.uniqueID != null;
  }

  public void setUniqueIDIsSet(boolean value) {
    if (!value) {
      this.uniqueID = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getIp_addr() {
    return this.ip_addr;
  }

  public UserDefinition setIp_addr(@org.apache.thrift.annotation.Nullable java.lang.String ip_addr) {
    this.ip_addr = ip_addr;
    return this;
  }

  public void unsetIp_addr() {
    this.ip_addr = null;
  }

  /** Returns true if field ip_addr is set (has been assigned a value) and false otherwise */
  public boolean isSetIp_addr() {
    return this.ip_addr != null;
  }

  public void setIp_addrIsSet(boolean value) {
    if (!value) {
      this.ip_addr = null;
    }
  }

  public int getPort() {
    return this.port;
  }

  public UserDefinition setPort(int port) {
    this.port = port;
    setPortIsSet(true);
    return this;
  }

  public void unsetPort() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __PORT_ISSET_ID);
  }

  /** Returns true if field port is set (has been assigned a value) and false otherwise */
  public boolean isSetPort() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __PORT_ISSET_ID);
  }

  public void setPortIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __PORT_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case UNIQUE_ID:
      if (value == null) {
        unsetUniqueID();
      } else {
        setUniqueID((java.lang.String)value);
      }
      break;

    case IP_ADDR:
      if (value == null) {
        unsetIp_addr();
      } else {
        setIp_addr((java.lang.String)value);
      }
      break;

    case PORT:
      if (value == null) {
        unsetPort();
      } else {
        setPort((java.lang.Integer)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case UNIQUE_ID:
      return getUniqueID();

    case IP_ADDR:
      return getIp_addr();

    case PORT:
      return getPort();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case UNIQUE_ID:
      return isSetUniqueID();
    case IP_ADDR:
      return isSetIp_addr();
    case PORT:
      return isSetPort();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof UserDefinition)
      return this.equals((UserDefinition)that);
    return false;
  }

  public boolean equals(UserDefinition that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_uniqueID = true && this.isSetUniqueID();
    boolean that_present_uniqueID = true && that.isSetUniqueID();
    if (this_present_uniqueID || that_present_uniqueID) {
      if (!(this_present_uniqueID && that_present_uniqueID))
        return false;
      if (!this.uniqueID.equals(that.uniqueID))
        return false;
    }

    boolean this_present_ip_addr = true && this.isSetIp_addr();
    boolean that_present_ip_addr = true && that.isSetIp_addr();
    if (this_present_ip_addr || that_present_ip_addr) {
      if (!(this_present_ip_addr && that_present_ip_addr))
        return false;
      if (!this.ip_addr.equals(that.ip_addr))
        return false;
    }

    boolean this_present_port = true;
    boolean that_present_port = true;
    if (this_present_port || that_present_port) {
      if (!(this_present_port && that_present_port))
        return false;
      if (this.port != that.port)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetUniqueID()) ? 131071 : 524287);
    if (isSetUniqueID())
      hashCode = hashCode * 8191 + uniqueID.hashCode();

    hashCode = hashCode * 8191 + ((isSetIp_addr()) ? 131071 : 524287);
    if (isSetIp_addr())
      hashCode = hashCode * 8191 + ip_addr.hashCode();

    hashCode = hashCode * 8191 + port;

    return hashCode;
  }

  @Override
  public int compareTo(UserDefinition other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetUniqueID()).compareTo(other.isSetUniqueID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUniqueID()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.uniqueID, other.uniqueID);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetIp_addr()).compareTo(other.isSetIp_addr());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIp_addr()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ip_addr, other.ip_addr);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetPort()).compareTo(other.isSetPort());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPort()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.port, other.port);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("UserDefinition(");
    boolean first = true;

    sb.append("uniqueID:");
    if (this.uniqueID == null) {
      sb.append("null");
    } else {
      sb.append(this.uniqueID);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("ip_addr:");
    if (this.ip_addr == null) {
      sb.append("null");
    } else {
      sb.append(this.ip_addr);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("port:");
    sb.append(this.port);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class UserDefinitionStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public UserDefinitionStandardScheme getScheme() {
      return new UserDefinitionStandardScheme();
    }
  }

  private static class UserDefinitionStandardScheme extends org.apache.thrift.scheme.StandardScheme<UserDefinition> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, UserDefinition struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // UNIQUE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.uniqueID = iprot.readString();
              struct.setUniqueIDIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // IP_ADDR
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.ip_addr = iprot.readString();
              struct.setIp_addrIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PORT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.port = iprot.readI32();
              struct.setPortIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, UserDefinition struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.uniqueID != null) {
        oprot.writeFieldBegin(UNIQUE_ID_FIELD_DESC);
        oprot.writeString(struct.uniqueID);
        oprot.writeFieldEnd();
      }
      if (struct.ip_addr != null) {
        oprot.writeFieldBegin(IP_ADDR_FIELD_DESC);
        oprot.writeString(struct.ip_addr);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PORT_FIELD_DESC);
      oprot.writeI32(struct.port);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class UserDefinitionTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public UserDefinitionTupleScheme getScheme() {
      return new UserDefinitionTupleScheme();
    }
  }

  private static class UserDefinitionTupleScheme extends org.apache.thrift.scheme.TupleScheme<UserDefinition> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, UserDefinition struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetUniqueID()) {
        optionals.set(0);
      }
      if (struct.isSetIp_addr()) {
        optionals.set(1);
      }
      if (struct.isSetPort()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetUniqueID()) {
        oprot.writeString(struct.uniqueID);
      }
      if (struct.isSetIp_addr()) {
        oprot.writeString(struct.ip_addr);
      }
      if (struct.isSetPort()) {
        oprot.writeI32(struct.port);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, UserDefinition struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.uniqueID = iprot.readString();
        struct.setUniqueIDIsSet(true);
      }
      if (incoming.get(1)) {
        struct.ip_addr = iprot.readString();
        struct.setIp_addrIsSet(true);
      }
      if (incoming.get(2)) {
        struct.port = iprot.readI32();
        struct.setPortIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

