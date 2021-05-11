package cc.aguesuka.btfind.dao;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.net.SocketAddress;
import java.util.List;

/**
 * @author :aguesuka 2019/9/12 16:31
 */
@Repository
@Slf4j
public class InfoHashDao {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public InfoHashDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void creatTable() {
        try {
            // create table
            jdbcTemplate.execute("create table INFO_HASH\n" +
                    "(\n" +
                    "    PR_ID       integer not null\n" +
                    "        constraint INFO_HASH_pk\n" +
                    "            primary key autoincrement,\n" +
                    "    CREATE_TIME TEXT,\n" +
                    "    HEX_HASH    text,\n" +
                    "    ADDRESS     TEXT\n" +
                    ");");
            logger.info("create table INFO_HASH success");
        } catch (DataAccessException e) {
            // table exsit ignore
            logger.info("table INFO_HASH already exists");
        }
    }

    public void save(String infoHashHex, SocketAddress address) {
        jdbcTemplate.update(
                "insert into INFO_HASH (CREATE_TIME, HEX_HASH,ADDRESS) VALUES (current_timestamp, ?,?)",
                infoHashHex, address.toString());
    }

    public List<String> hexHash() {
        return jdbcTemplate.queryForList("select distinct  HEX_HASH  from INFO_HASH", String.class);
    }
}
