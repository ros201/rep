public class Params {
    private String path;
    private Integer depth;
    private Boolean recursively;
    private String includeFilter;
    private String excludeFilter;
    private String output;
    private Integer thread;
    private String errorOrHelpMessage;

    public Params(String path,
                  Integer depth,
                  Boolean recursively,
                  String includeFilter,
                  String excludeFilter,
                  String output,
                  Integer thread,
                  String errorOrHelpMessage) {
        this.path = path;
        this.depth = depth;
        this.recursively = recursively;
        this.includeFilter = includeFilter;
        this.excludeFilter = excludeFilter;
        this.output = output;
        this.thread = thread;
        this.errorOrHelpMessage = errorOrHelpMessage;
    }

    public Params() {
        this.path = null;
        this.depth = null;
        this.recursively = null;
        this.includeFilter = null;
        this.excludeFilter = null;
        this.output = null;
        this.thread = null;
        this.errorOrHelpMessage = null;
    }

    public String getPath() {
        return path;
    }

    public Integer getDepth() {
        return depth;
    }

    public Boolean getRecursively() {
        return recursively;
    }

    public String getIncludeFilter() {
        return includeFilter;
    }

    public String getExcludeFilter() {
        return excludeFilter;
    }

    public String getOutput() {
        return output;
    }

    public Integer getThread() {
        return thread;
    }

    public String getErrorOrHelpMessage() {
        return errorOrHelpMessage;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setIncludeFilter(String includeFilter) {
        this.includeFilter = includeFilter;
    }

    public void setExcludeFilter(String excludeFilter) {
        this.excludeFilter = excludeFilter;
    }

    public void setRecursively(boolean recursively) {
        this.recursively = recursively;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setThread(Integer thread) {
        this.thread = thread;
    }

    public void setErrorOrHelpMessage(String errorOrHelpMessage) {
        this.errorOrHelpMessage = errorOrHelpMessage;
    }

    public void clearParams() {
        this.path = null;
        this.depth = null;
        this.recursively = null;
        this.includeFilter = null;
        this.excludeFilter = null;
        this.output = null;
        this.thread = null;
        this.errorOrHelpMessage = null;
    }
}
