package org.example.repository;

import org.example.DBConnection;
import org.example.repository.interfaces.ClientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Client;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

public class ClientDBRepository implements ClientRepository {
    private Logger logger= LogManager.getLogger();
    private DBConnection dbConnection;
    private Connection connection;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public ClientDBRepository(Properties properties) {
        logger.info("Initializing ClientRepo with properties: {} ",properties);
        this.dbConnection=new DBConnection(properties);
        this.connection= dbConnection.getConnection();

    }
    @Override
    public Optional<Client> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() {
        logger.traceEntry("getting all clients entry");
        ArrayList<Client> clients=new ArrayList<>();
        try(PreparedStatement statement=connection.prepareStatement("SELECT * FROM clients")){

            ResultSet resultSet= statement.executeQuery();
            logger.info("suntem dupa resultset");
            while (resultSet.next())
            {
                long id= resultSet.getLong("id_client");
                String username=resultSet.getString("username");
                LocalDate birthDay=LocalDate.parse(resultSet.getString("birthDay"));
               Client client=new Client(username,birthDay);
               client.setId(id);
                clients.add(client);
            }
            logger.traceExit("Got all trips successfully");
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Db failed in findAll trips" + e);
        }
        return clients;
    }

    @Override
    public Optional<Client> save(Client entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Long aLong, Client entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public int size() {
        return 0;
    }
}
