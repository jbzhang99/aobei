package com.aobei.trainapi.server.bean;

/**
 * Created by liqizhen on 2018/7/20.
 */
public class StudentImgInfo {
    private String studentName;
    private String jobCertUrl;
    private String cardJustUrl;
    private String cardAgainstUrl;
    private String healthUrl;
    private String innocenceProofUrl;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getJobCertUrl() {
        return jobCertUrl;
    }

    public void setJobCertUrl(String jobCertUrl) {
        this.jobCertUrl = jobCertUrl;
    }

    public String getCardJustUrl() {
        return cardJustUrl;
    }

    public void setCardJustUrl(String cardJustUrl) {
        this.cardJustUrl = cardJustUrl;
    }

    public String getCardAgainstUrl() {
        return cardAgainstUrl;
    }

    public void setCardAgainstUrl(String cardAgainstUrl) {
        this.cardAgainstUrl = cardAgainstUrl;
    }

    public String getHealthUrl() {
        return healthUrl;
    }

    public void setHealthUrl(String healthUrl) {
        this.healthUrl = healthUrl;
    }

    public String getInnocenceProofUrl() {
        return innocenceProofUrl;
    }

    public void setInnocenceProofUrl(String innocenceProofUrl) {
        this.innocenceProofUrl = innocenceProofUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StudentImgInfo{");
        sb.append("studentName='").append(studentName).append('\'');
        sb.append(", jobCertUrl='").append(jobCertUrl).append('\'');
        sb.append(", cardJustUrl='").append(cardJustUrl).append('\'');
        sb.append(", cardAgainstUrl='").append(cardAgainstUrl).append('\'');
        sb.append(", healthUrl='").append(healthUrl).append('\'');
        sb.append(", innocenceProofUrl='").append(innocenceProofUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
