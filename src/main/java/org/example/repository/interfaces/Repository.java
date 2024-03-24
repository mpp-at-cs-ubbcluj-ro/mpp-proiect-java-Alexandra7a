package org.example.repository.interfaces;

import org.example.model.Entity;

import java.util.Optional;

public interface Repository <ID, E extends Entity<ID>>{
    public Optional<E> findOne(ID id);
    public Iterable<E> findAll();
    public Optional<E> save(E entity);
    public Optional<E> update(ID id,E entity);
    public Optional<E> delete(ID id);
    public int size();
}
