package org.speer.assessment.dtos;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.speer.assessment.entities.Note;
import org.speer.assessment.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class NoteSpecifications {
    public static Specification<Note> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            // generate a query that full text matching or fuzzy match to title or content
            List<Predicate> listOr = new ArrayList<>();
            listOr.add(cb.isTrue(
                    cb.function(
                            "tsvector_match",
                            Boolean.class,
                            root.get("contentSearchVector"),
                            cb.function("plainto_tsquery", String.class, cb.literal(search))
                    )
            ));
            listOr.add(cb.like(root.get("title"), "%" + search + "%"));
            listOr.add(cb.like(root.get("content"), "%" + search + "%"));
            Predicate[] arrayOr = new Predicate[listOr.size()];
            Predicate pre_or = cb.or(listOr.toArray(arrayOr));
            return query.where(pre_or).getRestriction();
        };
    }

    public static Specification<Note> author(User author) {
        return (root, query, cb) -> {
            // is author or is in share list
            List<Predicate> listOr = new ArrayList<>();
            listOr.add(cb.equal(root.get("author"), author));
            listOr.add(cb.equal(root.joinSet("shareList", JoinType.LEFT), author));
            Predicate[] arrayOr = new Predicate[listOr.size()];
            Predicate pre_or = cb.or(listOr.toArray(arrayOr));
            return query.where(pre_or).getRestriction();
        };
    }
}
