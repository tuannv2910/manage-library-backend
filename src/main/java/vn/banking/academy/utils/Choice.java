package vn.banking.academy.utils;

import java.util.ArrayList;


public  class Choice{
    public int index;
    public Message message;
    public Object logprobs;
    public String finish_reason;


    public static class Message{
        public String role;
        public String content;
    }

    public static class Root{
        public String id;
        public String object;
        public int created;
        public String model;
        public ArrayList<Choice> choices;
        public Usage usage;
        public String system_fingerprint;
    }

    public static class Usage{
        public int prompt_tokens;
        public int completion_tokens;
        public int total_tokens;
    }


}

