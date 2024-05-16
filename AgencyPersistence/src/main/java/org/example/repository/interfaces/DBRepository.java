package org.example.repository.interfaces;

import org.example.model.Entityy;

public interface DBRepository<ID,E extends Entityy<ID>> extends Repository<ID,E>
{
}
