namespace java simpleMessenger

struct Message {
    1: i64 timestamp,
    2: string msgString,
    3: string toID,
    4: string fromID,
    5: bool toGroup,
}

struct UserDefinition {
    1: string uniqueID,
    2: string ip_addr,
    3: i32 port,
}

struct GroupDefinition {
    1: string groupID,
    2: list<string> members,
}

service ClientService {
    void receive(1:Message msg),
}

service ServerService {
    bool join(1:UserDefinition cl),

    void closeConnection(1:UserDefinition cl),

    bool connectTo(1:string recipient),

    void sendMessage(1:Message msg),

    list<string> enquireGroups(1:string clientID),

    bool joinGroup(1:string groupID, 2:string clientID),

    bool leaveGroup(1:string groupID, 2:string clientID),
}