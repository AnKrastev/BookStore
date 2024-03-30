import java.util.Date;

public class Reduction {

    private Date startDate;
    private Date endDate;
    private int percentReduction;

    public Reduction(Date startDate, Date endDate, int percentReduction) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentReduction = percentReduction;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getPercentReduction() {
        return percentReduction;
    }

    public void setPercentReduction(int percentReduction) {
        this.percentReduction = percentReduction;
    }
}
