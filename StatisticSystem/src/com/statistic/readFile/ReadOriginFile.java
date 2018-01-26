package com.statistic.readFile;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.statistic.time.TimeUtil;

import bean.DateBean;

public class ReadOriginFile {
	private final String PERSONFILE = "��������";
	private final String LEAVES = "��ټ�¼";
	private final String DATEMODIFY = "�����޸�";
	
	
	/**
	 * ���ֲ�ͬ���͵��ļ�����ȡ
	 * @param filePath Ҫ��ȡ���ļ�·��
	 */
	public void readFile(String filePath) {
		File file = new File (filePath);
		String fileName = file.getName();
		String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
		System.out.println(fileName);
		System.out.println(suffix);
		if ("xlsx".equals(suffix)) {
			readXLSXExcel(file);
		} else if ("csv".equals(suffix)) {
			readCVSFile(file);
		} else if ("txt".equals(suffix)){
			readTXT(file);
		} else {
			readCVSFile(file);
		}
		System.out.println("read complete" +file.getName());
	}
	
	/**
	 * �õ�ָ��Ŀ¼�µ������ļ�·��
	 * @param path   ָ�����ļ���Ŀ¼·��
	 * @param fileList ����·����ArrayList
	 * @return
	 */
	public List<String> getFilePath(String path, ArrayList<String> fileList) {
		File file = new File(path);
		File[] tempList = file.listFiles();
		for (File tempFile : tempList) {
			if (tempFile.isFile()) {
				fileList.add(tempFile.getAbsolutePath());
			} else if (tempFile.isDirectory()) {
				getFilePath(tempFile.getAbsolutePath(), fileList);
			}
		}
		return fileList;
	}
	
	/**
	 * ��ȡ�ļ���ʽΪtxt�ĵ�
	 * @param file Ҫ��ȡ���ļ�
	 */
	public void readTXT(File file) {
		try {
			String line;
			String fileName = file.getName();
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			DatabaseOperate dbo = DatabaseOperate.getInstance();
			if (fileName.contains(DATEMODIFY)) {
				while (null != (line = br.readLine())) {
					String[] result = line.split("\\s+");
					String date;
					int category;
					date = result[0];
					if ("����:".equals(date) || "".equals(date)) {
						continue;
					} else {
						category = Integer.parseInt(result[1]);
					}
					String sql = "insert into date_category (date, category) values ('"+date+"',"+category+")";
					dbo.insert(sql);
				}
			} 
			fr.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�ļ���ʽΪxlsx���ļ�
	 * @param file Ҫ��ȡ���ļ�
	 */
	public void readXLSXExcel(File file) {
		try {
			String fileName = file.getName();
			InputStream inputStrem =  new FileInputStream(file);
			XSSFWorkbook workBook  = new XSSFWorkbook(inputStrem);
			PersonUtils personUtils = new PersonUtils();
			LeavesUtils leaveUtils = new LeavesUtils();
			
			if (fileName.contains(PERSONFILE)) {
			    personUtils.InsertSql(workBook);
			} else if (fileName.contains(LEAVES)) {
				leaveUtils.InsertSql(workBook, fileName);
			} 
			inputStrem.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
		
	/**
	 * ��ȡ����Ϊcvs���ļ�
	 * @param file Ҫ��ȡ���ļ�
	 */
	public void readCVSFile (File file) {
		try {
			BufferedReader bufferReader = new BufferedReader(new FileReader(file));
			String line;
			final String SERIAL = "���";
			final String IDCARD = "����";
			boolean isInsert = false;
			String fileName = file.getName();
			RecordsUtil recordsUtils  = new RecordsUtil();
			String[] result;
			DatabaseOperate dbo = DatabaseOperate.getInstance();
			ArrayList <DateBean>modifyDateList = new ArrayList<>();
			String dateSql = "select * from date_category";
			modifyDateList = dbo.queryModityDateCategory(dateSql);
			while ((line = bufferReader.readLine()) != null) {
				result = line.split(",");
				if (SERIAL.equals(result[0]) || IDCARD.equals(result[0])) {
					continue;
				}
				if (result.length == 8) {
					recordsUtils.serial = result[0];
					recordsUtils.id_card = result[1];
					recordsUtils.name = result[2];
					recordsUtils.team_name = result[3];
					recordsUtils.datetime = result[4];
					recordsUtils.site = result[5];
					recordsUtils.pass = result[6];
					recordsUtils.description = result[7];
					recordsUtils.file_name = fileName;
				} else if (result.length == 2) {
					recordsUtils.name = result[0];
					recordsUtils.datetime =  result[1];
					recordsUtils.file_name = fileName;
				} else if (result.length == 3){
					recordsUtils.id_card = result[0];
					recordsUtils.name = result[1];
					recordsUtils.datetime =  result[2];
					recordsUtils.file_name = fileName;
				} else {
					System.out.println("Original file isn't a standard file");
					System.out.println("file="+file.getName());
					continue;
				}
				if (TimeUtil.grepDateTime(recordsUtils.datetime) == 1) {
					recordsUtils.datetime = TimeUtil.formatStandardTime(recordsUtils.datetime);
				}
				recordsUtils.getDataAndTime();
				for(DateBean dateBean:modifyDateList) {
					if (TimeUtil.isSameDay(TimeUtil.StringToDateWithoutTime(recordsUtils.dateDay), TimeUtil.StringToDateWithoutTime(dateBean.date))) {
						recordsUtils.category = dateBean.category;
						break;
					}
				}
				String sql = "insert into records (`serial`, `id_card`, `name`, `team_name`, `date`, `dateDay`, `site`, `pass`, `description`, `file_name`, `insert`, `category`)"
						+"values('"+recordsUtils.serial+"','"+recordsUtils.id_card+"','"+recordsUtils.name+"','"+recordsUtils.team_name+"','"+recordsUtils.datetime+"','"
						+recordsUtils.dateDay+"','"+recordsUtils.site+"','"+recordsUtils.pass+"','"+recordsUtils.description+"','"+recordsUtils.file_name+"',"
						+isInsert+","+recordsUtils.category+")";
				dbo.insert(sql);
			}
			bufferReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
	
	
