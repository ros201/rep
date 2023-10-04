public class Info {
    private Integer filesCount;
    private Long size;
    private Long allLinesCount;
    private Long notEmptyLinesCount;
    private Long commentsCount;

    public Info(Long size, Long allLinesCount, Long notEmptyLinesCount, Long commentsCount) {
        this.filesCount = 1;
        this.size = size;
        this.allLinesCount = allLinesCount;
        this.notEmptyLinesCount = notEmptyLinesCount;
        this.commentsCount = commentsCount;
    }

    public Integer getFilesCount() {
        return filesCount;
    }

    public Long getSize() {
        return size;
    }

    public Long getAllLinesCount() {
        return allLinesCount;
    }

    public Long getNotEmptyLinesCount() {
        return notEmptyLinesCount;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void addItemInfo(Long size, Long allLinesCount, Long notEmptyLinesCount, Long commentsCount) {
        this.filesCount += 1;
        this.size += size;
        this.allLinesCount += allLinesCount;
        this.notEmptyLinesCount += notEmptyLinesCount;
        this.commentsCount += commentsCount;
    }

    public String toString () {
        return " files count " + this.filesCount +
                ", size " + this.size +
                ", all Lines count " + this.allLinesCount +
                ", not empty lines count " + this.notEmptyLinesCount +
                ", comments count " + this.commentsCount;
    }
}
