package io.backQL.BackIo.rowmapper;

import io.backQL.BackIo.domain.Userr;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<Userr> {

    @Override
    public Userr mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Userr.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .address(rs.getString("address"))
                .phone(rs.getString("phone"))
                .title(rs.getString("title"))
                .bio(rs.getString("bio"))
                .imageUrl(rs.getString("image_url"))
                .enabled(rs.getBoolean("enabled"))
                .isUsingMfa(rs.getBoolean("using_mfa"))
                .isNotLocked(rs.getBoolean("non_Locked"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();

    }
}
