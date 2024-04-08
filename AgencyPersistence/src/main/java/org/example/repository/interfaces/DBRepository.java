package org.example.repository.interfaces;

import org.example.model.Entity;

public interface DBRepository<ID,E extends Entity<ID>> extends Repository<ID,E>
{
}
