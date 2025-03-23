package com.sdgp.MediPass.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object){
        Random random = new Random();
        long uniqueId;
        do {
            uniqueId =100000+ random.nextInt(900000);

        }while (!isUnique(session, uniqueId));

        return uniqueId;
    }

    private boolean isUnique(SharedSessionContractImplementor session, long id){
        Long count = (Long) session.createQuery(
                "SELECT COUNT(e) FROM Patient e WHERE e.mediId = :id")
                .setParameter("id",id)
                .uniqueResult();

        return count == 0;

    }
}
