package com.statistic.readFile;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PersonUtils {
	public String name;		//����
	public String place;		//�����ص�
	public String team;		//������
	public String module; //�����ģ��
	public String leader; //������
	public boolean onsite; 	//�Ƿ�onsite
	public String start_time; 	//��ʼˢ��ʱ��
	public String end_time;	//����ˢ��ʱ��
	public void  clear() {
		name = null;
		place = null;
		team = null;
		onsite = false;
		start_time = null;
		end_time = null;
	}
	public void InsertSql(XSSFWorkbook workBook) {
		String sql = null;
		DatabaseOperate dbo = DatabaseOperate.getInstance();
		System.out.println(3);
		//dbo.connectDatabase();
		for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
		XSSFSheet xssfSheet = workBook.getSheetAt(numSheet);
		for (int i = 0; i<= xssfSheet.getLastRowNum(); i++) {
			XSSFRow xssfRow = xssfSheet.getRow(i);
			clear();
			for (int j =0; j < xssfRow.getLastCellNum(); j++) {
				String result = FormatValue.getXLSXValue(xssfRow.getCell(j));
				if ("���".equals(result) || "No.".equals(result)) {
					break;
				}
				switch (j) {
					case 1: 
						name = result;
						break;
					case 2: 
						place = result;
						break;
					case 3: 
						team = result;
						break;
					case 4: 
						module = result;
						break;
					case 5: 
						leader = result;
						break;
					case 6: 
						start_time = result;
						break;
					case 7: 
						end_time = result;
						break;
					case 8: 
						if ("��".equals(result)) {
							onsite = false;
						} else {
							onsite = true;
						}
						break;
				}
			}
			if (null == name) {
				continue;
			}
			System.out.println(sql);
			sql = "insert into person (`name`, `place`, `team`, `onsite`, `module`, `leader`, `start_time`, `end_time`)"
					+"values('"+name+"','"+place+"','"+team+"',"+onsite+",'"+module+"','"+leader+"','"+start_time+"','"
					+end_time+"')";
			dbo.insert(sql);
			}
		}
		//dbo.closeDatabase();
	}
}