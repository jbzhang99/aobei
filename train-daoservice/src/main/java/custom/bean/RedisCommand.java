package custom.bean;

import java.util.Map;

public class RedisCommand {

    private Command command;
    private String messageId;
    private Map<String,String> data;


    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public enum Command{
        wx_refund(1);

        private String name;
        private int command;
        private Command(int command){
          this.command = command;
          this.name = name();
        }
        public int getCommand(){
            return this.command;
        }
    }
}
