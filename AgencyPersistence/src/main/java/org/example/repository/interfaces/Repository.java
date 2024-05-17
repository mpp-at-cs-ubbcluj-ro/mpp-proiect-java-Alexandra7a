package org.example.repository.interfaces;

import org.example.model.Entitate;;

import java.util.Optional;

public interface Repository <ID, E extends Entitate>{
    public Optional<E> findOne(ID id);
    public Iterable<E> findAll();
    public Optional<E> save(E entity);
    public Optional<E> update(ID id,E entity);
    public Optional<E> delete(ID id);
    public int size();
}
