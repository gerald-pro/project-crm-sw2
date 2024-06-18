package com.jonatan.dev.crm_sales.repositories;

import com.jonatan.dev.crm_sales.domains.Contact;
import com.jonatan.dev.crm_sales.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<User> findAll(Pageable pageable, Optional<String> filter) {
        Query query = new Query().with(pageable);
/*
        name.ifPresent(n -> query.addCriteria(Criteria.where("name").regex(".*" + n + ".*", "i")));
        email.ifPresent(e -> query.addCriteria(Criteria.where("email").regex(".*" + e + ".*", "i")));
        phone.ifPresent(p -> query.addCriteria(Criteria.where("phone").regex(".*" + p + ".*", "i")));
*/
        filter.ifPresent(f -> {
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("name").regex(".*" + f + ".*", "i")
                    //Criteria.where("email").regex(".*" + f + ".*", "i"),
                    //Criteria.where("phone").regex(".*" + f + ".*", "i")
            );
            query.addCriteria(criteria);
        });

        List<User> users = mongoTemplate.find(query, User.class);
        long total = mongoTemplate.count(query.skip(0).limit(0), Contact.class);

        return new PageImpl<>(users, pageable, total);
    }
}
