package concurrent;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author pang-yun
 * Date:  2022-05-17 17:40
 * Description:
 */

@Slf4j
public class QueueMonitor {


    static boolean open;
    private static Map<String, QueueCount> commandCountMap;
    private static Map<String, QueueCount> command50CountMap;
    private static Map<String, QueueCount> command100CountMap;
    private static Map<String, QueueCount> command200CountMap;
    private static Map<String, QueueCount> command500CountMap;
    private static Map<String, QueueCount> command1000CountMap;
    private static Map<String, QueueCount> command2000CountMap;
    private static Map<String, QueueCount> command10000CountMap;

    public QueueMonitor() {
    }

    public static void reset() {
        commandCountMap.clear();
        command50CountMap.clear();
        command100CountMap.clear();
        command200CountMap.clear();
        command500CountMap.clear();
        command1000CountMap.clear();
        command2000CountMap.clear();
        command10000CountMap.clear();
    }

    public static void setOpen(boolean open) {
        QueueMonitor.open = open;
    }

    public static void monitor(IQueueDriverCommand command, long startTime, int totalTime) {
        String key = command.getClass().getSimpleName();
        if (totalTime > 10000) {
            addCount(command10000CountMap, key, totalTime, startTime);
        } else if (totalTime > 2000) {
            addCount(command2000CountMap, key, totalTime, startTime);
        } else if (totalTime > 1000) {
            addCount(command1000CountMap, key, totalTime, startTime);
        } else if (totalTime > 500) {
            addCount(command500CountMap, key, totalTime, startTime);
        } else if (totalTime > 200) {
            addCount(command200CountMap, key, totalTime, startTime);
        } else if (totalTime > 100) {
            addCount(command100CountMap, key, totalTime, startTime);
        } else if (totalTime > 50) {
            addCount(command50CountMap, key, totalTime, startTime);
        }

        addCount(commandCountMap, key, totalTime, startTime);
    }

    private static void addCount(Map<String, QueueCount> map, String key, int totalLTime, long maxOccurTime) {
        QueueCount curCount = (QueueCount)map.get(key);
        if (curCount == null) {
            curCount = new QueueCount();
            QueueCount old = (QueueCount)map.putIfAbsent(key, curCount);
            if (old != null) {
                curCount = old;
            }
        }

        if ((long)totalLTime > curCount.maxExecTime.get()) {
            log.info("发现command最大执行时间, command:{}->{}", key, totalLTime);
            curCount.maxExecTime.set((long)totalLTime);
            curCount.maxOccurTime.set(maxOccurTime);
        }

        curCount.count.incrementAndGet();
        curCount.totalExecTime.addAndGet((long)totalLTime);
    }

    public static String dump() {
        if (!open) {
            return "未开启监视器";
        } else {
            long time = System.currentTimeMillis();
            String fileName = "queue." + time;
            String ret = detailStr();
            PrintWriter pw = null;

            try {
                File file = new File(fileName);
                pw = new PrintWriter(file);
                pw.println(ret);
            } catch (Exception var9) {
            } finally {
                if (pw != null) {
                    pw.flush();
                    pw.close();
                }

            }

            return fileName;
        }
    }

    static String detailStr() {
        StringBuilder sb = new StringBuilder();
        sb.append("command,count,maxExecTime,maxOccurTime,totalExecTime,avg\n");
        map2String(sb, commandCountMap, "commandCountMap");
        map2String(sb, command50CountMap, "command50CountMap");
        map2String(sb, command100CountMap, "command100CountMap");
        map2String(sb, command200CountMap, "command200CountMap");
        map2String(sb, command500CountMap, "command500CountMap");
        map2String(sb, command1000CountMap, "command1000CountMap");
        map2String(sb, command2000CountMap, "command2000CountMap");
        map2String(sb, command10000CountMap, "command10000CountMap");
        return sb.toString();
    }

    static void map2String(StringBuilder sb, Map<String, QueueCount> map, String name) {
        sb.append("[" + name + "],,,,,\n");
        Iterator var3 = map.keySet().iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            sb.append(key).append(",").append(((QueueCount)map.get(key)).toString()).append("\n");
        }

        sb.append(",,,,,\n");
    }

    public static boolean isOpen() {
        return open;
    }

    static {
        String commandCount = System.getProperty("game.log.CommandCount", "false");
        open = Boolean.parseBoolean(commandCount);
        commandCountMap = new ConcurrentHashMap();
        command50CountMap = new ConcurrentHashMap();
        command100CountMap = new ConcurrentHashMap();
        command200CountMap = new ConcurrentHashMap();
        command500CountMap = new ConcurrentHashMap();
        command1000CountMap = new ConcurrentHashMap();
        command2000CountMap = new ConcurrentHashMap();
        command10000CountMap = new ConcurrentHashMap();
    }

    static class QueueCount {
        AtomicLong count = new AtomicLong(0L);
        AtomicLong maxExecTime = new AtomicLong(0L);
        AtomicLong maxOccurTime = new AtomicLong(0L);
        AtomicLong totalExecTime = new AtomicLong(0L);

        QueueCount() {
        }

        @Override
        public String toString() {
            double avg = 0.0D;
            if (this.count.get() > 0L) {
                avg = (double)(this.totalExecTime.get() / this.count.get());
            }

            return this.count.get() + "," + this.maxExecTime.get() + "," + this.maxOccurTime.get() + "," + this.totalExecTime.get() + "," + avg;
        }
    }
}