package org.example.repository;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.example.tables.pojos.GameField;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.codegen.maven.example.Tables.GAME_FIELD;

@Repository
@AllArgsConstructor
public class SudokuRepository {

    private final DSLContext dslContext;

    /**
     * Поиск в бд полей с заданной сложностью
     * @param difficulty - сложность
     * @return - список полей
     */
    public List<GameField> fetchFieldsByDifficulty(Integer difficulty){
        return dslContext
                .selectFrom(GAME_FIELD)
                .where(GAME_FIELD.DIFFICULTY.eq(difficulty))
                .fetchInto(GameField.class);
    }
}
