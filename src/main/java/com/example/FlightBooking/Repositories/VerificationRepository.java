package com.example.FlightBooking.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.io.Console;

@Repository
public class VerificationRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public VerificationRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void deleteByExpireTime() {
        String sql = "DELETE FROM public.veritification WHERE expire_time < :currentTime";

        // Tạo một MapSqlParameterSource để truyền giá trị của currentTime vào câu lệnh SQL
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("currentTime", java.sql.Timestamp.from(java.time.Instant.now()));

        namedParameterJdbcTemplate.update(sql, params);
    }
}
