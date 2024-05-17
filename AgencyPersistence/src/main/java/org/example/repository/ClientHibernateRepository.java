package org.example.repository;

import org.example.HibernateUtils;
import org.example.model.Client;
import org.example.repository.interfaces.ClientRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class ClientHibernateRepository implements ClientRepository {
    @Override
    public Optional<Client> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        System.out.println("IN CLIENTI HIBERNATE ---------");

        try( Session session=HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Client ", Client.class).getResultList();
        }
    }

    @Override
    public Optional<Client> save(Client entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return Optional.of(entity);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return Optional.empty();
        }
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