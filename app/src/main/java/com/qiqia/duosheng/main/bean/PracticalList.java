package com.qiqia.duosheng.main.bean;

public class PracticalList {
    public enum Type{
        今日上新(1,"今日上新");
        private int key;
        private String value;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        Type(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
