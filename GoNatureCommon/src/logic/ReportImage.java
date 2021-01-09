package logic;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;


public class ReportImage implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7779028804346337323L;

	private String reportName;
	private SerializableInputStream reportImage;
	private Date date;
	private String parkName;
	private int reportID;

	
	public ReportImage(String reportName, SerializableInputStream reportImage, Date date,String parkName) {
		super();
		this.reportName = reportName;
		this.reportImage = reportImage;
		this.date = date;
		this.parkName = parkName;
	}
	public ReportImage( int reportID,String parkName, String reportName, Date date, SerializableInputStream reportImage) {
		super();
		this.reportName = reportName;
		this.reportImage = reportImage;
		this.date = date;
		this.parkName = parkName;
		this.reportID = reportID;
	}
	
	public void setReportImage(SerializableInputStream reportImage) {
		this.reportImage = reportImage;
	}
	public void setReportImage(InputStream reportImage) {
		this.reportImage = (SerializableInputStream) reportImage;
	}

	public int getReportID() {
		return reportID;
	}
	public String getParkName() {
		return parkName;
	}

	public String getReportName() {
		return reportName;
	}

	public SerializableInputStream getReportImage() {
		return reportImage;
	}

	public Date getDate() {
		return date;
	}
}
