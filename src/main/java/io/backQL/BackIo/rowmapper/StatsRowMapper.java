package io.backQL.BackIo.rowmapper;

import io.backQL.BackIo.domain.Statistics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsRowMapper implements RowMapper<Statistics> {
    public Statistics mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Statistics.builder()
                .totalCustomer(resultSet.getInt("total_customer"))
                .totalInvoices(resultSet.getInt("total_invoices"))
                .totalBilled(resultSet.getDouble("total_billed"))
                .build();
    }
}
