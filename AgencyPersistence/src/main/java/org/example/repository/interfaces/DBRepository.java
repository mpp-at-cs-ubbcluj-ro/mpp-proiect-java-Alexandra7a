package org.example.repository.interfaces;

import org.example.model.Entitate;

public interface DBRepository<ID,E extends Entitate> extends Repository<ID,E>
{
}
