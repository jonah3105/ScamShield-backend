
package com.scamshield.backend.repository;

import com.scamshield.backend.model.ScamReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ScamReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScamReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ScamReport> rowMapper = (rs, rowNum) -> {
        ScamReport report = new ScamReport();

        report.setId(rs.getInt("id"));
        report.setReporterName(rs.getString("reporter_name"));
        report.setType(rs.getString("type"));
        report.setContent(rs.getString("content"));
        report.setCategory(rs.getString("category"));
        report.setRiskLevel(rs.getString("risk_level"));
        report.setStatus(rs.getString("status"));

        Timestamp timestamp = rs.getTimestamp("created_at");

        if (timestamp != null) {
            report.setCreatedAt(timestamp.toLocalDateTime());
        }

        return report;
    };

    public List<ScamReport> findAll() {
        String sql = "SELECT * FROM scam_reports ORDER BY id DESC";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ScamReport> findById(int id) {
        String sql = "SELECT * FROM scam_reports WHERE id = ?";

        List<ScamReport> reports =
                jdbcTemplate.query(sql, rowMapper, id);

        if (reports.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(reports.get(0));
    }

    public ScamReport save(ScamReport report) {
        String sql = "INSERT INTO scam_reports " +
                "(reporter_name, type, content, category, risk_level, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                report.getReporterName(),
                report.getType(),
                report.getContent(),
                report.getCategory(),
                report.getRiskLevel(),
                report.getStatus() == null ? "PENDING" : report.getStatus()
        );

        return jdbcTemplate.queryForObject(
                "SELECT * FROM scam_reports WHERE id = ?",
                rowMapper,
                jdbcTemplate.queryForObject(
                        "SELECT LAST_INSERT_ID()",
                        Integer.class
                )
        );
    }

    public boolean update(int id, ScamReport report) {
        String sql = "UPDATE scam_reports SET " +
                "reporter_name = ?, type = ?, content = ?, " +
                "category = ?, risk_level = ?, status = ? " +
                "WHERE id = ?";

        int rows = jdbcTemplate.update(
                sql,
                report.getReporterName(),
                report.getType(),
                report.getContent(),
                report.getCategory(),
                report.getRiskLevel(),
                report.getStatus(),
                id
        );

        return rows > 0;
    }
    
public boolean updateStatus(int id, String status) {
    String sql = "UPDATE scam_reports SET status = ? WHERE id = ?";

    int rows = jdbcTemplate.update(sql, status, id);

    return rows > 0;
}


    public boolean delete(int id) {
        String sql = "DELETE FROM scam_reports WHERE id = ?";

        int rows = jdbcTemplate.update(sql, id);

        return rows > 0;
    }
}
