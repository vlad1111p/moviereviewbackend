package demo.demo.amazonAws.config.form;


public class SimpleJsonFile1 {
    private String content;
    private String content1;
    private String content2;


    public SimpleJsonFile1(String content, String content1, String content2) {
        this();
        this.content = content;
        this.content1 = content1;
        this.content2 = content2;
    }

    public SimpleJsonFile1() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }
}
