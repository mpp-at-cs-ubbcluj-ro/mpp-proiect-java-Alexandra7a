package org.example.utils;

import org.example.ServiceInterface;
import org.example.protobuffprotocol.AgencyClientProtoWorker;
import org.example.rpcprotocol.AgencyClientRpcWorker;

import java.net.Socket;

public class AgencyProtoConcurrentServer extends AbsConcurrentServer {
    private ServiceInterface server;
    public AgencyProtoConcurrentServer(int port, ServiceInterface server) {
        super(port);
        this.server = server;
        System.out.println("---AgencyRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AgencyClientProtoWorker worker=new AgencyClientProtoWorker(server,client);
        Thread tw=new Thread(worker);
        return tw;
    }
    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }


}
