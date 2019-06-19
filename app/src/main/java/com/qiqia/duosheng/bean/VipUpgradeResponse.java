package com.qiqia.duosheng.bean;

import java.io.Serializable;

public class VipUpgradeResponse  implements Serializable {

    /**
     * upgradeCondition : {"UlName":"超级VIP会员","UlCondition1":"12","UlCondition2":"34","UlCondition3":"56"}
     * profitDistributionCondition : {"PdName":"超级VIP会员","PdDivideInto":"51","PdSubordinate":"10","PdEr":"0","PdLevel":"10"}
     * isUpgrade : 0
     * upgradeInfo : {"subCount":"2","subSubCount":"1","total":0}
     * level : 1
     */

    private UpgradeConditionBean upgradeCondition;
    private ProfitDistributionConditionBean profitDistributionCondition;
    private int isUpgrade;
    private UpgradeInfoBean upgradeInfo;
    private int level;
    private String levelName;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public UpgradeConditionBean getUpgradeCondition() {
        return upgradeCondition;
    }

    public void setUpgradeCondition(UpgradeConditionBean upgradeCondition) {
        this.upgradeCondition = upgradeCondition;
    }

    public ProfitDistributionConditionBean getProfitDistributionCondition() {
        return profitDistributionCondition;
    }

    public void setProfitDistributionCondition(ProfitDistributionConditionBean profitDistributionCondition) {
        this.profitDistributionCondition = profitDistributionCondition;
    }

    public int getIsUpgrade() {
        return isUpgrade;
    }

    public void setIsUpgrade(int isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public UpgradeInfoBean getUpgradeInfo() {
        return upgradeInfo;
    }

    public void setUpgradeInfo(UpgradeInfoBean upgradeInfo) {
        this.upgradeInfo = upgradeInfo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static class UpgradeConditionBean  implements Serializable{
        /**
         * UlName : 超级VIP会员
         * UlCondition1 : 12
         * UlCondition2 : 34
         * UlCondition3 : 56
         */

        private String UlName;
        private String UlCondition1;
        private String UlCondition2;
        private String UlCondition3;

        public String getUlName() {
            return UlName;
        }

        public void setUlName(String UlName) {
            this.UlName = UlName;
        }

        public String getUlCondition1() {
            return UlCondition1;
        }

        public void setUlCondition1(String UlCondition1) {
            this.UlCondition1 = UlCondition1;
        }

        public String getUlCondition2() {
            return UlCondition2;
        }

        public void setUlCondition2(String UlCondition2) {
            this.UlCondition2 = UlCondition2;
        }

        public String getUlCondition3() {
            return UlCondition3;
        }

        public void setUlCondition3(String UlCondition3) {
            this.UlCondition3 = UlCondition3;
        }
    }

    public static class ProfitDistributionConditionBean  implements Serializable{
        /**
         * PdName : 超级VIP会员
         * PdDivideInto : 51
         * PdSubordinate : 10
         * PdEr : 0
         * PdLevel : 10
         */

        private String PdName;
        private String PdDivideInto;
        private String PdSubordinate;
        private String PdEr;
        private String PdLevel;

        public String getPdName() {
            return PdName;
        }

        public void setPdName(String PdName) {
            this.PdName = PdName;
        }

        public String getPdDivideInto() {
            return PdDivideInto;
        }

        public void setPdDivideInto(String PdDivideInto) {
            this.PdDivideInto = PdDivideInto;
        }

        public String getPdSubordinate() {
            return PdSubordinate;
        }

        public void setPdSubordinate(String PdSubordinate) {
            this.PdSubordinate = PdSubordinate;
        }

        public String getPdEr() {
            return PdEr;
        }

        public void setPdEr(String PdEr) {
            this.PdEr = PdEr;
        }

        public String getPdLevel() {
            return PdLevel;
        }

        public void setPdLevel(String PdLevel) {
            this.PdLevel = PdLevel;
        }
    }

    public static class UpgradeInfoBean  implements Serializable{
        /**
         * subCount : 2
         * subSubCount : 1
         * total : 0
         */

        private String subCount;
        private String subSubCount;
        private String total;

        public String getSubCount() {
            return subCount;
        }

        public void setSubCount(String subCount) {
            this.subCount = subCount;
        }

        public String getSubSubCount() {
            return subSubCount;
        }

        public void setSubSubCount(String subSubCount) {
            this.subSubCount = subSubCount;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
