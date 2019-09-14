package cc.aguesuka.btfind.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.net.SocketAddress;

/**
 * @author :yangmingyuxing
 * 2019/9/12 16:31
 */
@Repository
@Slf4j
public class InfoHashDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InfoHashDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void save(String infoHashHex, SocketAddress address){
        jdbcTemplate.update(
                "insert into INFO_HASH (CREATE_TIME, HEX_HASH,ADDRESS) VALUES (current_timestamp, ?,?)",
                infoHashHex,address.toString());
    }
}
