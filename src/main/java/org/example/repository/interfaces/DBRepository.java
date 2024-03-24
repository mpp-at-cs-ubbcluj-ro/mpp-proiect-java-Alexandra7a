package org.example.repository.interfaces;

import org.example.model.Entity;
import org.example.repository.interfaces.Repository;

public interface DBRepository<ID,E extends Entity<ID>> extends Repository<ID,E>
{
}
