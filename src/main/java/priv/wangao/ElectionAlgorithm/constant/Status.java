package priv.wangao.ElectionAlgorithm.constant;

public class Status {
	public static final String NORMAL="NORMAL";
    public static final String CRASH="CRASH";

    public static final String ELECTING="ELECTING";
    public static final String WAIT_FOR_LEADER="WAIT_FOR_LEADER";     //收到了OK消息
    public static final String BECOMING_LEADER="BECOMING_LEADER";     //成为LEADER，但未发送消息
}
