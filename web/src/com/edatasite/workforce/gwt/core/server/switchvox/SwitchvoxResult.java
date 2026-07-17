package com.edatasite.workforce.gwt.core.server.switchvox;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Aziz
 * Date: 7/1/13
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "result", propOrder = {
        "current_calls", "extensions"
})
public class SwitchvoxResult {
    @XmlElement(name = "current_calls")
    CurrentCalls current_calls;

    @XmlElement(name = "extensions")
    Extensions extensions;

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "current_calls", propOrder = {
            "calls"
    })
    public static class CurrentCalls {
        @XmlAttribute(name = "total_items")
        protected Integer total_items;
        @XmlElement(name = "current_call")
        protected List<CallItem> calls;

        public Integer getTotal_items() {
            return total_items;
        }

        public void setTotal_items(Integer total_items) {
            this.total_items = total_items;
        }

        public List<CallItem> getCalls() {
            return calls != null ? calls:new ArrayList();
        }

        public void setCalls(List<CallItem> calls) {
            this.calls = calls;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType()
        public static class CallItem {
            @XmlAttribute(name = "state")
            protected String state;
            @XmlAttribute(name = "start_time")
            protected String start_time;
            @XmlAttribute(name = "duration")
            protected String duration;
            @XmlAttribute(name = "from_caller_id_number")
            protected String from_caller_id_number;
            @XmlAttribute(name = "to_caller_id_number")
            protected String to_caller_id_number;
            @XmlAttribute(name = "from_caller_id_name")
            protected String from_caller_id_name;
            @XmlAttribute(name = "to_caller_id_name")
            protected String to_caller_id_name;

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getFrom_caller_id_number() {
                return from_caller_id_number;
            }

            public void setFrom_caller_id_number(String from_caller_id_number) {
                this.from_caller_id_number = from_caller_id_number;
            }

            public String getTo_caller_id_number() {
                return to_caller_id_number;
            }

            public void setTo_caller_id_number(String to_caller_id_number) {
                this.to_caller_id_number = to_caller_id_number;
            }

            public String getFrom_caller_id_name() {
                return from_caller_id_name;
            }

            public void setFrom_caller_id_name(String from_caller_id_name) {
                this.from_caller_id_name = from_caller_id_name;
            }

            public String getTo_caller_id_name() {
                return to_caller_id_name;
            }

            public void setTo_caller_id_name(String to_caller_id_name) {
                this.to_caller_id_name = to_caller_id_name;
            }
        }

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "extensions", propOrder = {
            "extension"
    })
    public static class Extensions {
        @XmlElement(name = "extension")
        Extension extension;

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType()
        public static class Extension {
            @XmlAttribute(name = "account_id")
            protected Integer account_id;

            @XmlAttribute(name = "display")
            protected String display;

            public Integer getAccount_id() {
                return account_id;
            }

            public String getDisplay() {
                return display;
            }
        }

        public Extension getExtension() {
            return extension;
        }
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public CurrentCalls getCurrentCalls() {
        return current_calls;
    }

    public void setCurrent_calls(CurrentCalls current_calls) {
        this.current_calls = current_calls;
    }

}
