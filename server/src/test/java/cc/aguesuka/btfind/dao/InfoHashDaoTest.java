package cc.aguesuka.btfind.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author :yangmingyuxing
 * 2019/9/12 16:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InfoHashDaoTest {

    private InfoHashDao infoHashDao;

    @Autowired
    public void setInfoHashDao(InfoHashDao infoHashDao) {
        this.infoHashDao = infoHashDao;
    }


}