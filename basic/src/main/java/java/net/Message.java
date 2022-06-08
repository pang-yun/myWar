package java.net;

/**
 * @author pang-yun
 * Date:  2022-05-16 14:03
 * Description:  消息抽象类
 */

public interface Message {

    /**
     * 解码
     *
     * @param msg
     */
    void decode(byte[] msg);


    /**
     * 编码
     *
     * @return
     */
    byte[] encode();

    /**
     * 消息长度
     * @return
     */
    int length();

    void setLength(int var1);

    int getId();

    void setSequence(short var1);

    short getSequence();

    default int getHostId() {
        return 0;
    }

}
