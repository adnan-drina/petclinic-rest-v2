package com.demo.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * Local stand-in for the removed Spring Data JDBC Extensions
 * {@code OneToManyResultSetExtractor} (migration-general helper).
 */
public abstract class OneToManyResultSetExtractor<R, C, K> implements ResultSetExtractor<List<R>> {

    private final RowMapper<R> rootMapper;
    private final RowMapper<C> childMapper;

    protected OneToManyResultSetExtractor(RowMapper<R> rootMapper, RowMapper<C> childMapper) {
        this.rootMapper = rootMapper;
        this.childMapper = childMapper;
    }

    @Override
    public List<R> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<R> results = new ArrayList<>();
        R current = null;
        K currentKey = null;
        int row = 0;
        while (rs.next()) {
            K primary = mapPrimaryKey(rs);
            if (primary != null && !primary.equals(currentKey)) {
                current = rootMapper.mapRow(rs, row);
                currentKey = primary;
                results.add(current);
            }
            K foreign = mapForeignKey(rs);
            if (current != null && foreign != null && foreign.equals(currentKey)) {
                addChild(current, childMapper.mapRow(rs, row));
            }
            row++;
        }
        return results;
    }

    protected abstract K mapPrimaryKey(ResultSet rs) throws SQLException;

    protected abstract K mapForeignKey(ResultSet rs) throws SQLException;

    protected abstract void addChild(R root, C child);
}
