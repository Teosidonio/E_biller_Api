package mz.co.standardbank.e_biller.biller;

import javax.persistence.Column;

/**
 * @author - C816346 on 2020/09/16
 */
public class BillerDao {
	private String name;
	private String contract;
	private String url;

	public String getName () { return name; }

	public void setName ( String name ) { this.name = name; }

	public String getContract () { return contract; }

	public void setContract ( String contract ) { this.contract = contract; }

	public String getUrl () { return url; }

	public void setUrl ( String url ) { this.url = url; }
}
