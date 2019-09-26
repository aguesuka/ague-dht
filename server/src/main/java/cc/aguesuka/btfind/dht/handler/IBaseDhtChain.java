package cc.aguesuka.btfind.dht.handler;

/**
 * @author :yangmingyuxing
 * 2019/9/25 12:36
 */
public interface IBaseDhtChain {
    /**
     * 是否启用
     *
     * @return 是否启用
     */
    default boolean enable(){
        return true;
    }
}
