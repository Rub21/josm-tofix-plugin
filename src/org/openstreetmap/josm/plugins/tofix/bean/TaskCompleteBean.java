package org.openstreetmap.josm.plugins.tofix.bean;

/**
 *
 * @author ruben
 */
public class TaskCompleteBean {

    int statusCode;
    String error;
    Message message;
    
    public class Message {

        String key;
        Value value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

    }

    public class Value {

        int skip;
        int noterror;
        int fix;
        int total;

        public int getSkip() {
            return skip;
        }

        public void setSkip(int skip) {
            this.skip = skip;
        }

        public int getNoterror() {
            return noterror;
        }

        public void setNoterror(int noterror) {
            this.noterror = noterror;
        }

        public int getFix() {
            return fix;
        }

        public void setFix(int fix) {
            this.fix = fix;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
   
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
    
}
