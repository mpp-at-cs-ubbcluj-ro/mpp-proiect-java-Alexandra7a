package org.example.utils;
import org.example.ServiceInterface;
import org.example.rpcprotocol.AgencyClientRpcWorker;

import java.net.Socket;


public class AgencyRpcConcurrentServer extends AbsConcurrentServer {
    private ServiceInterface server;
    public AgencyRpcConcurrentServer(int port, ServiceInterface server) {
        super(port);
        this.server = server;
        System.out.println("---AgencyRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        AgencyClientRpcWorker worker=new AgencyClientRpcWorker(server, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
