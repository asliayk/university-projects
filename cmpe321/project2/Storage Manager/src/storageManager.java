import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;





public class storageManager {
	public static int pageSize = 1536;
	public static int fileSize = (int) Math.pow(2, 20);

    public static void main(String[] args) throws IOException {
    	String fileName = "SysCatData0.dat";
    	RandomAccessFile sc = new RandomAccessFile(fileName, "rw");
    	String x = args[0];  //input file name
    	String y = args[1];  //output file name
    	//String x = "input2.txt";
    	//String y = "OutputFile";
    	
    	runSystem(fileName, x, y);
        sc.close();
    }
 
    public static void initFileHeader(String fName, int fileID, String typeName, String[] fieldNames, int numOfFields) throws IOException {
    	RandomAccessFile sc = new RandomAccessFile(fName, "rw");
    	String s = intToString(fileID, 3, false);
    	sc.writeBytes(s);  
    	if(!fName.startsWith("SysCat")) {
		for(int j=0; j<10-typeName.length();j++) {
			sc.writeBytes("_");
		} // 10 
		sc.writeBytes(typeName);
    	}
    	if(fName.startsWith("SysCat")) {
    	sc.writeBytes("000");  // num of types
    	}
		s = intToString(numOfFields, 3, false);
		sc.writeBytes(s); // num of fields
		for(int i=0; i<fieldNames.length; i++) {
			for(int j=0; j<10-fieldNames[i].length();j++) {
				sc.writeBytes("_");
			}
			sc.writeBytes(fieldNames[i]);

		} 
		sc.writeBytes("001"); // (page counter)
		int fhSize = 0;
		if(fName.startsWith("SysCat")) {
		fhSize = 32;
		} else {
		fhSize = fieldNames.length*10+19;	
		}
		sc.close();
		initPageHeader(fName, fhSize, 0);  // initializing first page
    }
    
 
    public static void initPageHeader(String fName, int fhSize, int pageID) throws IOException {
    	RandomAccessFile sc = new RandomAccessFile(fName, "rw");
    	sc.seek(fhSize + pageID*pageSize);
    	sc.writeBytes(intToString(pageID, 3, false)); // page id 
    	sc.writeBytes("000"); // rec counter
    	sc.close();
    }
    

    public static String intToString(int y, int length, boolean fdata) {
    	String s = String.valueOf(y);
        int slength = s.length();
        	int zero = length-slength;
        	String x = "";
        	for(int i=0; i<zero; i++) {
        		if(!fdata)
        		x+="0";
        		else
        		x+="_";	
        	}
        	s = x + s;
        return s;
    }
    
   
    public static String getFieldDatafromString(String s) {
    	String data = "";
    	for(int j=0; j<s.length(); j++) {
			if(s.charAt(j)!='_') {
				data +=s.charAt(j);
			}
		}
    	return data;
    }
    
    public static void deleteFiles(String typeName) throws IOException {
    	ArrayList<File> files = getTypeFiles(typeName);
    	for(int i=0; i<files.size(); i++) {
    		files.get(i).delete();
    	}
    }

    
    public static void runSystem(String scfileName, String inputN, String outputN) throws IOException {
    	//File file = new File(System.getProperty("user.dir")+"/2016400222/src/" + inputN);
		File file = new File(inputN); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String input; 
		while ((input = br.readLine()) != null)  {
		String[] words = input.trim().split("\\s+");
		
		if(words[0].equalsIgnoreCase("create") && words[1].equalsIgnoreCase("type")) {
			String[] fieldNames = Arrays.copyOfRange(words, 4, words.length);
			createType(scfileName, words[2], Integer.parseInt(words[3]), fieldNames, outputN);   
			
			
		} else if(words[0].equalsIgnoreCase("delete") && words[1].equalsIgnoreCase("type")) {
			String typeName = words[2];
			delRecord("SysCat", typeName, false, outputN);
			deleteFiles(typeName);
			
		} else if(words[0].equalsIgnoreCase("list") && words[1].equalsIgnoreCase("type")) {
			listRecord("", outputN);
			
		} else if(words[0].equalsIgnoreCase("create") && words[1].equalsIgnoreCase("record")) {
			String typeName = words[2];
			String[] data = Arrays.copyOfRange(words, 3, words.length);
			 for(int i=0; i<data.length; i++) {
				 int l = data[i].length();
				 for(int j=0; j<10-l; j++) {
				     data[i] = '_' + data[i];
				 }
			} 
			addRecord(typeName, data, true);
			
		} else if(words[0].equalsIgnoreCase("delete") && words[1].equalsIgnoreCase("record")) {
			String typeName = words[2];
			int primKey = Integer.parseInt(words[3]);
			String x = Integer.toString(primKey);
			String data = "";
			for(int i=0; i<10-x.length();i++) {
				data+='_';
			}
			data +=x;
			delRecord(typeName, data, true, outputN);
			
		} else if(words[0].equalsIgnoreCase("update") && words[1].equalsIgnoreCase("record")) {
			String typeName = words[2];
			String[] data = Arrays.copyOfRange(words, 3, words.length);
			int[] idata = new int[words.length-3];
			 for(int i=0; i<data.length; i++) {
				idata[i] = Integer.parseInt(data[i]);
			} 
			updateRecord(typeName, idata, outputN);
			
		} else if(words[0].equalsIgnoreCase("search") && words[1].equalsIgnoreCase("record")) {
			String typeName = words[2];
			int primKey = Integer.parseInt(words[3]);
			boolean x = searchRecord(typeName, primKey, "", outputN);
		} else if(words[0].equalsIgnoreCase("list") && words[1].equalsIgnoreCase("record")) {
			String typeName = words[2];
			listRecord(typeName, outputN);
			
		} 
	
		}
	}
    

    public static boolean searchRecord(String typeName, int primKey, String type, String outputN) throws IOException {
    	int[] address = new int[3];
    	if(getTypeFiles(typeName).size()==0) {
    		return false;
    	}
    	
    	if(!typeName.startsWith("SysCat")) {
    	address = BinarySearch(typeName, primKey, "", true);
    	} else {
    	RandomAccessFile scfile = new RandomAccessFile("SysCatData0.dat", "rw");
    	if(scfile.length()==0) {
         scfile.close();
         return false;
    	}
    	scfile.close();
    	address = BinarySearch(typeName, 0, type, true); 	
    	}
    	int fileID = address[0];
    	int pageID = address[1];
    	int recID = address[2];
    	if(fileID==-1) {
    	    return false;
    	}
    	String fName = typeName + "Data" + fileID+".dat";
    	byte[] pagebytes = readPage(fName, pageID);
    	int[] tInfo = getTypeInfo(typeName);
    	int numOfFields = tInfo[0];
    	int recSize = 0;
    	if(typeName.startsWith("SysCat")) {
    		recSize = 16;
    	} else {
    		recSize = 3+numOfFields*10;
    	}
    	if(typeName.equals("SysCat")) {
    		byte[] typeN = {pagebytes[3+6+recSize*recID], pagebytes[3+6+recSize*recID+1],pagebytes[3+6+recSize*recID+2],pagebytes[3+6+recSize*recID+3],
    				pagebytes[3+6+recSize*recID+4],pagebytes[3+6+recSize*recID+5],pagebytes[3+6+recSize*recID+6],pagebytes[3+6+recSize*recID+7],
    				pagebytes[3+6+recSize*recID+8],pagebytes[3+6+recSize*recID+9]};
    	String tn = new String(typeN);
    	tn = getFieldDatafromString(tn);
    	if(tn.contentEquals(type)) {
    		return true;
    	}
    	return false;
    	}

    	
    	byte[] recbytes = new byte[10];
    	int index = 0;
    	int findex = 0;
    	String[] allDatas = new String[numOfFields];
    	for(int i=3+6+recSize*recID; i<6+recSize*recID+recSize; i++) {
    		recbytes[index] = pagebytes[i];
    		index++;
    		if(index==10) {
    		String s = new String(recbytes);
    		recbytes = new byte[10];
    		String data = "";
    		for(int j=0; j<s.length(); j++) {
    			if(s.charAt(j)!='_') {
    				data +=s.charAt(j);
    			}
    		}
			allDatas[findex] = data;
			if(findex==0) {
				String x = Integer.toString(primKey);
				if(!x.equals(data)) {
					return false;
				}
			}
			findex++;
    		index=0;
    		}
    		
    	}
    	String filen = outputN;
    	File f = new File(outputN);
    	//int size = (int) f.length();
    	FileWriter fw = new FileWriter(filen,true);
    	BufferedWriter writer = new BufferedWriter(fw);
    	BufferedReader br = new BufferedReader(new FileReader(f));     
    	if (br.readLine() != null && allDatas.length!=0) {
    		writer.write("\r\n");
    	}
    	for(int i=0; i<allDatas.length; i++) {
    		if((i+1)!=allDatas.length)
    		writer.write(allDatas[i]+ " ");
    		else
    		writer.write(allDatas[i]);	
    	}
    	//writer.write("\r\n");
    	writer.close();
    	
    	return true;
    }
    

    public static void updateRecord(String typeName, int[] idata, String outputN) throws IOException {
    	if(!searchRecord("SysCat", 0, typeName, outputN))
    		return;
    	
    	int[] address = BinarySearch(typeName, idata[0], "", true);
    	int fileID = address[0];
    	int pageID = address[1];
    	int recID = address[2];
    	if(fileID==-1) {
    	    return;
    	}
    	String fName = typeName + "Data" + fileID+".dat";
    	int[] tInfo = getTypeInfo(typeName);
    	int numOfFields = tInfo[0];
    	int recSize = 3+numOfFields*10;
    	RandomAccessFile x = new RandomAccessFile(typeName + "Data" + address[0] +".dat", "rw");
    	x.seek(tInfo[2]+3+6+recSize*recID+pageSize*pageID);
    	
    	for(int i=0; i<idata.length; i++) {
    		String q=intToString(idata[i], 10, true);
    		x.writeBytes(q);
    	}
    	x.close();

    }
    

    public static void listRecord(String typeName, String outputN) throws IOException {
    	ArrayList<File> tfiles = new ArrayList<File>();
    	int[] typeInfo = new int[3];
    	int recSize = 0;
    	/*RandomAccessFile scfile = new RandomAccessFile("SysCatData0.dat", "rw");
    	if(scfile.length()==0) {
    		scfile.close();
    		return;
    	}
    	scfile.close(); */
    	if(typeName!="" && !searchRecord("SysCat", 0, typeName, outputN))
    		return;
    	if(typeName!="") {
    	tfiles = getTypeFiles(typeName);
    	typeInfo = getTypeInfo(typeName);
    	recSize = 10*typeInfo[0]+3;
    	} else {
    	tfiles = getTypeFiles("SysCat");
    	typeInfo = getTypeInfo("SysCat");
    	recSize = 16;
    	}
    	
    	if(tfiles.size()==0) {
    		return;
    	}
    	
    	

    	String filen = outputN;
    	ArrayList<String> types = new ArrayList<String>();
    	ArrayList<String> rdatas = new ArrayList<String>();
    	//FileWriter fw = new FileWriter(filen,true);
	    //BufferedWriter writer = new BufferedWriter(fw);
    	for(int i=0; i<tfiles.size(); i++) {    
    		RandomAccessFile tfile = new RandomAccessFile(tfiles.get(i).getName(), "rw");
    		tfile.seek(typeInfo[2]-3);
    		byte[] empid = new byte[3];
    		tfile.read(empid);
    		String xx = new String(empid);
    		int x = Integer.parseInt(xx);   // page counter
     		for(int j=0; j<x; j++) {    
    			byte[] bytes = readPage(tfiles.get(i).getName(), j);
    			byte[] recbytes = new byte[recSize-3];
    			int counter = 0;
    			for(int index=9; index<pageSize; index+=recSize) {    
    			   recbytes = Arrays.copyOfRange(bytes, index, index+recSize-3);
    			   byte[] field = new byte[10];
                   if(typeName!="") { 
                   String data = "";	   
    			   for(int z=0; z<recSize-3; z+=10) {           
    				   field = Arrays.copyOfRange(recbytes, z, z+10);
    				   String s = new String(field);
    				   String ss = getFieldDatafromString(s);
    				   if(ss.matches("[a-zA-Z0-9]+") && (z+10)!=recSize-3) {
    				       data+=ss + " ";
                	   } else if(ss.matches("[a-zA-Z0-9]+") && z+10==recSize-3) {
    				       data+=ss;
                	   }
    			    }
    			   if(!data.equals(""))
    			   rdatas.add(data);
                   }
                   else {
                	   
                	   for(int z=0; z<recSize; z+=16) { 
                	   field = Arrays.copyOfRange(recbytes, z, z+10); 
                	   String s = new String(field);
                	   String ss = getFieldDatafromString(s);
                	   if(ss.matches("[a-zA-Z0-9]+")) {
    				       types.add(ss);
                	   }
                	   }
                	   
                   }
    			}
    		}
     		
    		tfile.close();
    	}
    	FileWriter fw = new FileWriter(filen,true);
    	File f = new File(outputN);
	    BufferedWriter writer = new BufferedWriter(fw);
	    BufferedReader br = new BufferedReader(new FileReader(f));     
    	if (br.readLine() != null && (types.size()!=0 || rdatas.size()!=0)) {
    		System.out.println("satýr baþý yaptý");
    		writer.write("\r\n");
    	}
    	if(types.size()!=0) {
    		for(int i=0; i<types.size(); i++) {
    			//writer.write("\r\n");
    			writer.write(types.get(i));
    			if(i!=types.size()-1)
    			writer.write("\r\n");
    		}
    		writer.close();
    	} else {
    		for(int i=0; i<rdatas.size(); i++) {
    			//writer.write("\r\n");
    			writer.write(rdatas.get(i));
    			if(i!=rdatas.size()-1)
    			writer.write("\r\n");
    			
    		}
    		writer.close();
    	}
    	
    	
    	
    	
    	
    }
    
    
    

    public static void updateNumOfTypes(String fileName, boolean add) throws IOException {
    	RandomAccessFile sc = new RandomAccessFile(fileName, "rw");
    	sc.seek(3);
    	byte[] bytes = new byte[3];
    	sc.read(bytes);
    	String z = new String(bytes);
        int x = Integer.parseInt(z);
    	if(add) {
    	x++;
    	String s = intToString(x,3, false);
    	sc.seek(3);
    	sc.writeBytes(s);
    	} else {
    	x--;
    	String s = intToString(x,3, false);
    	sc.seek(3);
    	sc.writeBytes(s);
    	}
    	sc.close();
    }
 
    
    
    public static void createType(String fName, String typeName, int FNum, String[] fieldNames, String outputN) throws IOException {
    	File f = new File(fName);
    	RandomAccessFile sys = new RandomAccessFile(fName, "rw");
    	
		if(sys.length()!=0 && searchRecord("SysCat", 0, typeName, outputN)) {
			sys.close();
			return;
		}
		
		if(sys.length()==0)  {
			String[] shfieldNames = {"typeName", "NoOfFields"};	
			initFileHeader(fName, 0, typeName, shfieldNames, 2);   // init type file header
		}
		initFileHeader(typeName+ "Data" + "0.dat", 0, typeName, fieldNames, fieldNames.length);		
		String fl = intToString(fieldNames.length, 3, false);
		String ftypename = typeName;
		for(int i=0; i<10-typeName.length(); i++) {
			ftypename = '_' + ftypename;
		}
		String[] screcord = {ftypename, fl};
		addRecord(typeName, screcord, false);           // adding type record to syscat.dat
		sys.close();
	
    }
    

    public static void updateFileHeader(String fName, int FHeadSize, boolean add) throws IOException {
    	RandomAccessFile sc = new RandomAccessFile(fName, "rw");
    	sc.seek(FHeadSize-3);
    	byte[] bytes = new byte[3];
    	sc.read(bytes);
    	sc.seek(FHeadSize-3);
    	String pid = new String(bytes);
    	int id = Integer.parseInt(pid);
    	if(add)
    	id++;
    	else
    	id--;	
    	String s = intToString(id, 3, false);
    	sc.writeBytes(s);
    	sc.close();
    } 

    public static void delRecord(String typeName, String data, boolean isType, String outputN) throws IOException {
    	String datax = getFieldDatafromString(data);
    	if(isType && !searchRecord("SysCat", 0, typeName, outputN)) {
    		return;
    	}

    	String fileName = "";
    	String file = "";
    	int recSize = 0;
    	int[] nearAddress = new int[3];
    	ArrayList<File> tfiles = new ArrayList<File>();
    	int[] typeInfo = new int[3];
    	if(isType) {
    	int x = Integer.parseInt(datax);
    	nearAddress = BinarySearch(typeName, x, "", true);
    	tfiles = getTypeFiles(typeName);
    	typeInfo = getTypeInfo(typeName);
    	fileName = typeName + "Data" + nearAddress[0] + ".dat";
    	recSize = 3+typeInfo[0]*10;
    	file = typeName;
    	} else {
    	if(!searchRecord("SysCat", 0, datax, outputN)) {
    		return;
    	}
    	nearAddress = BinarySearch("SysCat", 0, datax, true);
    	tfiles = getTypeFiles("SysCat");
    	typeInfo = getTypeInfo("SysCat");
    	fileName = "SysCatData" + nearAddress[0] + ".dat";
    	recSize = 16;
    	file = "SysCat";
    	}

    	String[] delData = shiftDataforDel(fileName, nearAddress[0], typeName, nearAddress[1], nearAddress[2]);
    	byte[] bytes = readFileHeader(file, nearAddress[0]);
    	byte[] pageCount = {bytes[typeInfo[2]-3], bytes[typeInfo[2]-2], bytes[typeInfo[2]-1]};
    	String pc = new String(pageCount);
    	if(!pc.matches("[a-zA-Z0-9]+"))
    		return;
    	int pagc = Integer.parseInt(pc);
    	int index = nearAddress[1];
    	int lastRecID = (pageSize-6)/recSize-1;
    	while(index+1!=pagc) {
    		delData = shiftDataforDel(fileName, nearAddress[0], typeName, index+1, 0);
    		
    		shiftPageRecordsforAdd(fileName, typeName, index, lastRecID, delData);
    		index++;
    		nearAddress[2]=0;
    	}
    	int fNum = tfiles.size();
    	int findex = nearAddress[0];
    	int lastPageID = (fileSize-typeInfo[2])/pageSize-1;
    	String newFileName = "";
    	int fileIndex = nearAddress[0];
    	if(findex+1==fNum && fileName.startsWith("SysCat")) {
    		updateNumOfTypes(fileName, false);
    		return;
    	}
    	while(findex+1!=fNum) {
    		if(isType) {
    		newFileName = typeName + "Data" + (findex+1) + ".dat";
    		} else {
    		newFileName = "SysCatData" + (findex+1) + ".dat";	
    		}
    		delData = shiftDataforDel(newFileName, (findex+1), typeName, 0, 0);
    		shiftPageRecordsforAdd(fileName, typeName, lastPageID, lastRecID, delData);
    		byte[] fbytes = readFileHeader(file, findex+1);
    		byte[] den = {fbytes[0], fbytes[1]};
    		String x = new String(den);
    		if(!x.matches("[a-zA-Z0-9]+")) {   // file is deleted
    			break;
    		}
    		byte[] fpageCount = {fbytes[typeInfo[2]-3], fbytes[typeInfo[2]-2], fbytes[typeInfo[2]-1]};
        	String fpc = new String(fpageCount);
        	int fpagc = Integer.parseInt(fpc);
        	int pindex=1;
    		while(pindex!=fpagc) {
        		delData = shiftDataforDel(newFileName, (findex+1), typeName, pindex, 0);
        		shiftPageRecordsforAdd(newFileName, typeName, pindex-1, lastRecID, delData);
        		pindex++;
        	} 
    		findex++;
    		fileIndex = findex;
    		fileName = newFileName;
    	}
    	    if(fileName.startsWith("SysCat"))
    		updateNumOfTypes(fileName, false);
    	}
    	
    

    public static String[] shiftDataforDel(String fileName, int fileID, String typeName, int pageID, int recID) throws IOException {
    	RandomAccessFile tff = new RandomAccessFile(fileName, "rw");
    	int[] typeInfo = new int[3];
    	String file = "";
    	int recSize = 0;
    	int dataind = 0;
    	if(fileName.startsWith("SysCat")) {
    		typeInfo = getTypeInfo("SysCat");
    		file = "SysCat";
    		recSize = 16;
    		dataind = 2;
    	} else {
    		typeInfo = getTypeInfo(typeName);
    		file = typeName;
    		recSize = typeInfo[0]*10+3;
    		dataind = typeInfo[0];
    	}
    	byte[] bytes = readPage(fileName, pageID);
    	byte[] fbytes = readFileHeader(file, fileID);
    	byte[] pageCount = {fbytes[typeInfo[2]-3], fbytes[typeInfo[2]-2], fbytes[typeInfo[2]-1]};
    	String pc = new String(pageCount);
    	int pageC = Integer.parseInt(pc);
    	byte[] reccby = {bytes[3], bytes[4], bytes[5]};
      	String s = new String(reccby);
    	int recCount = Integer.parseInt(s); 

    	int index = 0;
    	
    	String[] newdata = new String[dataind];
    	for(int i = recID*recSize+6; i <= (recCount-1) * recSize+6; i = i + recSize) {
    		
			   byte[] b = new byte[recSize-3];
			   byte[] idd = new byte[3];
			   for(int j = 0; j < recSize; j++) {					   
				  if(j==0 || j==1 || j==2) {
				    idd[j] = bytes[i+j];
				  } else {
					  b[j-3] = bytes[i+j];
					  	  
				  }
			   }
			   String sid = new String(idd);
		       int id = Integer.parseInt(sid);
		       id--;
		       if(index==0) {
		    	   String datax = new String(b);
		    	   for(int j = 0; j < datax.length(); j+=10) {
		    		     if(j+10>datax.length())
		    		    	 break;
			    	 	 String fd = datax.substring(j, j+10);
			    	     newdata[j/10] = fd;
			    	     if(fileName.startsWith("SysCat")) {
			    	    	 newdata[1] = datax.substring(j+10, j+13);
			    	    	 break;
			    	     }
			       }
		    	   
		      } else {
		    	   tff.seek(i-recSize+typeInfo[2]+pageID*pageSize);
				   String myx = intToString(id,3, false);
				   String xxx = new String(b);
				   String ns = myx+xxx; 
				   tff.writeBytes(ns);
		    	  
		      }
		       index++;

    	}
    	tff.seek(typeInfo[2]+(recCount-1)*recSize+6+pageID*pageSize);
    	for(int k=0; k<recSize; k++) {
    		tff.writeBytes(" ");
    	}
    	updatePageHeader(file, fileName, pageID, false);
    
    	
    	if(recCount==1 && pageC!=1) {
    		tff.seek(typeInfo[2] + pageID*pageSize);
    		for(int i=0; i<6; i++) {
    			tff.writeBytes(" ");
    		}
    		updateFileHeader(fileName, typeInfo[2], false);
   
    	}
    	
    
    	tff.close();
    	return newdata;
  
    	
    }
    
    
    
    
    public static void addRecord(String typeName, String[] data, boolean isType) throws IOException {
    	RandomAccessFile tff = new RandomAccessFile(typeName+"Data0.dat", "rw");
    	int[] typeInfo = new int[3];
    	int recSize = 0;
    	if(!isType) {
    		typeInfo = getTypeInfo("SysCat");
    		recSize = 16;
    		tff.close();
    		tff = new RandomAccessFile("SysCatData0.dat", "rw");
    	} else {
    	   	typeInfo = getTypeInfo(typeName);
    	   	recSize = 10*data.length+3;
    	}   	
    	   	
    	tff.seek(typeInfo[2]+3);
    	byte[] bytess = new byte[3];
    	tff.read(bytess);
    	String z = new String(bytess);
        int totalRec = Integer.parseInt(z);        // show total record num
        if(totalRec==0) {
        	tff.seek(typeInfo[2]+6);
        	String id = "000";   // id of first rec
        	tff.writeBytes(id);
        	String sdata = "";
        	
        	    for(int j = 0; j < data.length; j++) {
        			tff.writeBytes(data[j]);
        		}
    		
    		
            if(isType) {
            updatePageHeader(typeName, typeName+"Data0.dat", 0, true);
            } else {
            updatePageHeader("SysCat", "SysCatData0.dat", 0, true);	
            updateNumOfTypes("SysCatData0.dat", true);
            }
            tff.close();
        	return;
        }
        
        int[] nearAddress = new int[3];
        if(isType) {
        	String datax = getFieldDatafromString(data[0]);
        	int x = Integer.parseInt(datax);
        	nearAddress = BinarySearch(typeName, x, "", false); 
        } else {	
        	String ftypeName = getFieldDatafromString(typeName);
        	nearAddress = BinarySearch("SysCat", 0, ftypeName, false);	
        }

        String fileName = "";
        if(isType) {
        	fileName = typeName + "Data" + nearAddress[0] + ".dat";
        } else {
        	fileName = "SysCatData" + nearAddress[0] + ".dat";	
        }
        
        int maxPageNum = (fileSize-typeInfo[2])/pageSize;
        if((fileSize-typeInfo[2])/pageSize==nearAddress[1]+1 && (pageSize-6)/recSize==nearAddress[2]) {
        	nearAddress[0]++;
        	nearAddress[1]=0;
        	nearAddress[2]=0;
        	String newFileName = "";
        	if(isType) {
        	newFileName = typeName + "Data" + nearAddress[0] + ".dat";
        	File f = new File(newFileName);
        	if(!f.exists()) {
        		initFileHeader(newFileName, nearAddress[0], typeName, getTypeFieldNames(typeName), typeInfo[0]);
        	}
        	}
        	else {
        		newFileName = "SysCatData" + nearAddress[0] + ".dat";
            	File f = new File(newFileName);
            	if(!f.exists()) {
            		initFileHeader(newFileName, nearAddress[0], typeName, getTypeFieldNames("SysCat"), typeInfo[0]);
            	}
        	}
        	fileName = newFileName;
        }
        else if((pageSize-6)/recSize==nearAddress[2]) {    // page in son recordunu gösteriyorsa diðer page e eklenmeli
    		nearAddress[1]++;
    		nearAddress[2]=0;
    	} 
        
        String[] shiftPData = new String[data.length];
        String[] syshiftPData = new String[data.length];
        
        if(isType)
        shiftPData = shiftPageRecordsforAdd(fileName, typeName, nearAddress[1], nearAddress[2], data);
        else
        syshiftPData = shiftPageRecordsforAdd(fileName, typeName, nearAddress[1], nearAddress[2], data);	
        
        int pindex = nearAddress[1];
        int findex = nearAddress[0];
        
        while((isType && shiftPData!=null) | (!isType && syshiftPData!=null)) {
        	pindex++;
        	if(pindex==maxPageNum) {
        	pindex = 0;	 
        	findex++;
        	String newFileName = "";
        	if(isType)
        	newFileName = typeName + "Data" + findex + ".dat";
        	else
        	newFileName = "SysCatData" + findex + ".dat";	
        	
        	File f = new File(newFileName);
        	if(!f.exists()) {
                if(isType)
        		initFileHeader(newFileName, findex, typeName, getTypeFieldNames(typeName), typeInfo[0]);
                else
                initFileHeader(newFileName, findex, typeName, getTypeFieldNames("SysCat"), typeInfo[0]);
        	}
        	
        	fileName = newFileName;
        	if(isType)
        	shiftPData = shiftPageRecordsforAdd(fileName, typeName, pindex, 0, shiftPData);
        	else
        	syshiftPData = shiftPageRecordsforAdd(fileName, typeName, pindex, 0, syshiftPData);
        	} else {
        		if(isType)
        			shiftPData = shiftPageRecordsforAdd(fileName, typeName, pindex, 0, shiftPData);
        		else
        			syshiftPData = shiftPageRecordsforAdd(fileName, typeName, pindex, 0, syshiftPData);
        	}
        }
 
        tff.close();    
    	
    }
    
    public static String[] shiftPageRecordsforAdd(String fileName, String typeName, int pageID, int recID, String[] data) throws IOException {
    	int typeInfo[] = new int[3];
    	int recSize = 0;
    	
    	if(fileName.startsWith("SysCat")) {
    		typeInfo = getTypeInfo("SysCat");
    		recSize = 16;
    	} else {
    		typeInfo = getTypeInfo(typeName);
    		recSize = typeInfo[0]*10+3;
    	}
    	
    	byte[] bytes = readPage(fileName, pageID);
    	
    	if(bytes==null) {     	
      		writeFirstRectoPage(typeName, fileName, pageID, data); 
      		updateFileHeader(fileName, typeInfo[2], true);
      		if(fileName.startsWith("SysCat")) {
      			updateNumOfTypes(fileName, true);
      		}
    		return null;
    	}
    	
        byte[] reccby = {bytes[3], bytes[4], bytes[5]};
      	String s = new String(reccby);
    	int recCount = Integer.parseInt(s);   
    	int index = 0;
    	boolean othpage = false;
    	if((recCount+1)*recSize+6>pageSize) {   // page doluysa, son recordu kaydetmek gerekiyor
    		othpage = true;
    	}
    	
    	String[] newdata = new String[typeInfo[0]];
    	RandomAccessFile tff = new RandomAccessFile(fileName, "rw");
    	for(int i = (recCount-1) * recSize+6; i >= recID*recSize+6; i = i - recSize) {
    		   int size = 0;
			   byte[] b = new byte[recSize-3];
			   byte[] idd = new byte[3];
			   for(int j = 0; j < recSize; j++) {					   
				  if(j==0 || j==1 || j==2) {
				    idd[j] = bytes[i+j];
				  } else {
					  b[j-3] = bytes[i+j];
					  	  
				  }
			   }
			   String sid = new String(idd);
		       int id = Integer.parseInt(sid);
		       id++; 
		       if(!(othpage && index==0)) {
				   tff.seek(i+recSize+typeInfo[2]+pageID*pageSize);
				   String myx = intToString(id,3, false);
				   String xxx = new String(b);
				   String ns = myx+xxx; 
				   tff.writeBytes(ns);
		       } else {   
		            String datax = new String(b);
		            if(!fileName.startsWith("SysCat")) {
		    	   for(int j = 0; j < datax.length(); j+=10) {
		    	 	 String fd = datax.substring(j, j+10);
		    	     newdata[j/10] = fd;
		    	   }
		            } else {
		            	String fd = datax.substring(0, 10);
		            	newdata[0] = fd;
		            	String fdd = datax.substring(10, 13);
		            	newdata[1] = fdd;
		            	
		            }
		    	   
		       }
			 index++;  
		   }
    	tff.seek(typeInfo[2]+ recSize*recID+pageID*pageSize+6);
     	String sx = intToString(recID,3, false);
     	tff.writeBytes(sx);
     	for(int j = 0; j < data.length; j++) {
    			   tff.writeBytes(data[j]);
    		} 
     
     	int maxRecNum = (pageSize-6)/recSize;
     	
     	tff.close();
     	
     	if(othpage) {
     		return newdata;
     	} else {
     		if(fileName.startsWith("SysCat")) {
     			updatePageHeader("SysCat", fileName, pageID, true);
     			updateNumOfTypes(fileName, true);
     		} else {
     		updatePageHeader(typeName, fileName, pageID, true); 
     		}
     		return null;
     	}
    	
    	
    	
    }
    	
    
    
    
    
    public static void writeFirstRectoPage(String tname, String fName, int pageID, String[] data) throws IOException {
    	RandomAccessFile tff = new RandomAccessFile(fName, "rw");
    	if(fName.startsWith("SysCat")) {
    		tname = "SysCat";
    	}
    	tff.seek(getTypeInfo(tname)[2] + pageID*pageSize);
    	String id = intToString(pageID,3,false);
    	tff.writeBytes(id);
    	tff.writeBytes("001000");   
    	for(int j = 0; j < data.length; j++) {
    			   tff.writeBytes(data[j]);
    		} 
    	
    	tff.close();
    	
    }
    

    
    public static void updatePageHeader(String typeName, String fileName, int pageID, boolean add) throws IOException {
    	RandomAccessFile tff = new RandomAccessFile(fileName, "rw");
    	int[] typeInfo = getTypeInfo(typeName);
    	int recSize = 0;
    	int fhSize = typeInfo[2];
    	if(typeName.equals("SysCat")) {
    		recSize = 16;
    	} else {
    		recSize = typeInfo[0]*10+3;
    	}
    	tff.seek(fhSize+pageID*pageSize+3);
    	byte[] bytes = new byte[3];
		tff.read(bytes);
		String s = new String(bytes);
		int ss = Integer.parseInt(s);
    	if(add) {
    		ss++;
    		
    	} else {
    		ss--;
    	}
    	tff.seek(fhSize+pageID*pageSize+3);
    	s = intToString(ss, 3, false);
    	tff.writeBytes(s);    	
    	tff.close();
    	
    }
    
    
    public static int[] BinarySearch(String typeName, int primKey, String primS, boolean find) throws IOException {
    	ArrayList<File> compl = getTypeFiles(typeName);
     	int[] typeInfo = getTypeInfo(typeName);

    	if(primS.contentEquals("")) {     // type record search
    	ArrayList<Integer> primsofFiles = new ArrayList<Integer>();
    	for(int i=0; i<compl.size(); i++) {  // ilk sayfalardaki ilk rec key
    			byte[] bytes = readPage(compl.get(i).getName(), 0);
    			byte[] emrd = {bytes[3], bytes[4], bytes[5]};
    			String xy = new String(emrd);
    			int xx = Integer.parseInt(xy);
    			if(xx!=0) {
    			byte[] intBytes = {bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]
    					, bytes[16], bytes[17], bytes[18]};
    			String x = new String(intBytes);
    			String nx = getFieldDatafromString(x);
    			int primK = Integer.parseInt(nx);
    		    primsofFiles.add(primK);
    			}
    	}

    			
    		int fileID = nearAddress(primsofFiles, null, primKey, "");
    		if(fileID==-1 && !find) // if it is inserting
    			fileID++;
    		if(fileID==-1 && find) {
    			int[] notfound = {-1,0,0};
    		    return notfound;
    		}
    		String fName = typeName + "Data" + (fileID+".dat");
    		int index = 0;
    		byte[] bytes1 = readPage(fName, index);
    		ArrayList<Integer> primsofPages = new ArrayList<Integer>();
			byte[] emrdd = {bytes1[3], bytes1[4], bytes1[5]};
			String xyy = new String(emrdd);
			int xxd = Integer.parseInt(xyy);
			while(bytes1!=null )  {
    			byte[] intBytes = {bytes1[9], bytes1[10], bytes1[11], bytes1[12], bytes1[13], bytes1[14], bytes1[15]
    					, bytes1[16], bytes1[17], bytes1[18]};
    			String x = new String(intBytes);
    			String nx = getFieldDatafromString(x);
    			if(!nx.matches("[a-zA-Z0-9]+")) {
    				break;
    			}
    			int primK = Integer.parseInt(nx);
    			index++;
    			bytes1 = readPage(fName, index);
    			primsofPages.add(primK);
    			
    			
    		}
    		int pageID = nearAddress(primsofPages, null, primKey, "");
    		if(pageID==-1 && !find)  // if it is inserting
    			pageID++;
    		if(pageID==-1 && find) {
    			int[] notfound = {-1,0,0};
    		    return notfound;
    		}
    		
    		byte[] bytesp = readPage(fName, pageID);
    		int numOfFields = typeInfo[0];
    		int recSize = 3+numOfFields*10;
    		ArrayList<Integer> primsofRecords = new ArrayList<Integer>();
    		for(int j=9; j<bytesp.length; j+=recSize) {
    			if(j+9>=pageSize)
    				break;
    			byte[] intBytes = {bytesp[j], bytesp[j+1], bytesp[j+2], bytesp[j+3], bytesp[j+4], bytesp[j+5], bytesp[j+6]
    					, bytesp[j+7], bytesp[j+8], bytesp[j+9]};
    			String x = new String(intBytes);
    			String nx = getFieldDatafromString(x);
    			if(!nx.matches("[a-zA-Z0-9]+")) {
    				break;
    			}
    			int primK = Integer.parseInt(nx);
    			primsofRecords.add(primK);
    		}
    		int recID = nearAddress(primsofRecords, null, primKey, "");
    		if(!find) // if it is inserting
    		recID++;
    		if(find && recID == -1) {
        		int[] notfound = {-1,0,0};
        		return notfound;
        		
    		}
    		
    		int[] address = {fileID, pageID, recID};
    		return address;
    		
    	} else {
    		int numOfFields = typeInfo[0];
    		ArrayList<String> primsofFiles = new ArrayList<String>();
    		for(int i=0; i<compl.size(); i++) { 
    			byte[] bytes = readPage(compl.get(i).getName(), 0);
    			int index = 9;
    			byte[] tBytes = {bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]
    					, bytes[16], bytes[17], bytes[18]};
    			String x = new String(tBytes);
    			String nx = getFieldDatafromString(x);
    		    primsofFiles.add(nx);
    		}
    		int fileID = nearAddress(null, primsofFiles, 0, primS);
    		if(fileID==-1 && !find) // if it is inserting
    			fileID++;
    		if(fileID==-1 && find) {
    			int[] notfound = {-1,0,0};
    		    return notfound;
    		}
    		String fName = typeName + "Data" + (fileID+".dat");
    		int index = 0;
    		byte[] bytes = readPage(fName, index);
    		ArrayList<String> primsofPages = new ArrayList<String>();
    		String xx = "";
    		while(bytes!=null) {
    			byte[] tBytes = {bytes[9], bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]
    					, bytes[16], bytes[17], bytes[18]};
    			String x = new String(tBytes);
    			String nx = getFieldDatafromString(x);
    		    primsofPages.add(nx);
    		    index++;
    		    bytes = readPage(fName, index);
    		}
    		int pageID = nearAddress(null, primsofPages, 0, primS);
    		if(pageID==-1 && !find)  // if it is inserting
    			pageID++;
    		if(pageID==-1 && find) {
    			int[] notfound = {-1,0,0};
    		    return notfound;
    		}
    		byte[] bytesp = readPage(fName, pageID);
    		int recSize = 16;                
    		ArrayList<String> primsofRecords = new ArrayList<String>();
    		for(int j=9; j<bytesp.length; j+=recSize) {
    			if(j+9>=pageSize)
    				break;
    			byte[] intBytes = {bytesp[j], bytesp[j+1], bytesp[j+2], bytesp[j+3], bytesp[j+4], bytesp[j+5], bytesp[j+6]
    					, bytesp[j+7], bytesp[j+8], bytesp[j+9]};
    			String x = new String(intBytes);
    			String nx = getFieldDatafromString(x);
    			if(!nx.matches("[a-zA-Z0-9]+")) {
    				break;
    			}
    			primsofRecords.add(nx);
    		}
    		int recID = nearAddress(null, primsofRecords, 0, primS);
    		if(!find)
    		recID++;
    		if(find && recID == -1) {
        		int[] notfound = {-1,0,0};
        		return notfound;
        		
    		}
    		int[] address = {fileID, pageID, recID};
    		return address;
    		
    	}
    	
    	
    
    	
    }
    
    public static int nearAddress(ArrayList<Integer> values, ArrayList<String> svalues, int primKey, String sprimKey)  
    {  
        int begin = 0;
        int last = 0;
        boolean isInt = false;
        if(values==null) {		
        last = svalues.size()-1;
        } else {
        last = values.size()-1;	
        isInt = true;
        }
 
    
        int val = -1;  
        while (begin <= last) {  
            int mid = (begin + last) / 2;  
    
               if ((isInt && values.get(mid) > primKey) || (!isInt && svalues.get(mid).compareTo(sprimKey)>0)) {  
                last = mid - 1;  
            }  else if((isInt && values.get(mid)==primKey) || (!isInt && svalues.get(mid).compareTo(sprimKey)==0)){
            	return mid;
            }
    
               else {  
                val = mid;  
                begin = mid + 1;  
            }  
        }  
        return val;  
    } 

    public static byte[] readPage(String fileName, int pageID) throws IOException {
    	byte[] byteList = new byte[pageSize];
    	RandomAccessFile f = new RandomAccessFile(fileName, "rw");
    	int epi = 0;
    	int eri = 0;
    	int recSize = 0;
    	int FHeadSize = 0;
    	String typeName;
    	if(!fileName.startsWith("SysCat")) {    
    		f.seek(1);
    		byte[] typen = new byte[10];
    		f.read(typen);
    		typeName = new String(typen);
    		f.seek(13);
        	byte[] bytes = new byte[3];
        	f.read(bytes);
        	String z = new String(bytes);
            int numOfFields = Integer.parseInt(z);
    		FHeadSize = numOfFields*10 + 19;
    		f.seek(numOfFields*10+16);
    		byte[] bytes1 = new byte[3];
        	f.read(bytes1);
    		String empPID = new String(bytes1);     
    		epi = Integer.parseInt(empPID);    
    		recSize = numOfFields*10+3;
    	} else {  // "SysCat.dat"
    		f.seek(6);
    		byte[] bytes = new byte[3];
        	f.read(bytes);
        	String z = new String(bytes);
        	int numOfFields = Integer.parseInt(z);
    	    FHeadSize = numOfFields*10 + 12;
    	}
     
    	if((int) f.length() <= FHeadSize + pageID*pageSize) {   // böyle bir page yok
    		f.close();
    		return null;
    	} 

    	f.seek(FHeadSize+pageSize*pageID);
    	f.read(byteList);
    	f.close();
  
    	return byteList;
    	}
    	
    
    
    public static void updateTypeHeader(String fileName, int FileHeadSize, int x) throws IOException {
    	RandomAccessFile f = new RandomAccessFile(fileName, "rw");
    	f.seek(FileHeadSize-3);
    	String epi = intToString(x+1,3, false);
    	f.writeBytes(epi);
    	f.close();
		
	}

	public static byte[] readFileHeader(String typeName, int index) throws IOException {
    	byte[] bytelist = new byte[100];
    	
    	String fName = typeName + "Data" + index + ".dat";
        RandomAccessFile f = new RandomAccessFile(fName, "rw");
        f.read(bytelist);
        f.close();
        return bytelist;
    	
    }
    

    public static int[] getTypeInfo(String typeName) throws IOException {
    	int[] info = new int[3];
    	byte[] bytes = readFileHeader(typeName, 0);
    	if(!typeName.startsWith("SysCat")) {   
    		byte[] noF = {bytes[13], bytes[14], bytes[15]};
    		String s = new String(noF);
    		int numOfField = Integer.parseInt(s);
    		info[0] = numOfField;         
    		byte emPId[] = {bytes[16+numOfField*10], bytes[16+numOfField*10+1], bytes[16+numOfField*10+2]};
    		s = new String(emPId);
    		int empPId = Integer.parseInt(s);
    		info[1] = empPId;             
    		info[2] = 19 + numOfField*10; 
    	} else {
    		byte[] noF = {bytes[6], bytes[7], bytes[8]};
    		String s = new String(noF);
    		int numOfField = Integer.parseInt(s);    		
    		info[0] = numOfField;           
    		byte emPId[] = {bytes[9+numOfField*10], bytes[9+numOfField*10+1], bytes[9+numOfField*10+2]};
    		s = new String(emPId);
    		int empPId = Integer.parseInt(s);
    		info[1] = empPId;             
    		info[2] = 12 + numOfField*10; 
    	}
    	return info;
    }
    

    public static String[] getTypeFieldNames(String typeName) throws IOException {
    	String fileName = typeName + "Data0.dat";
    	byte[] bytes = readFileHeader(typeName, 0);
    	int fieldNum = getTypeInfo(typeName)[0];
    	String[] fieldNames = new String[fieldNum];
    	int index = 0;
    	if(typeName.startsWith("SysCat")) {
    		String[] fNames = {"typeName", "NoOfFields"};
    		return fNames;
    	}
    	for(int i=16; i<10*fieldNum+16; i+=10) {
    		byte[] field = new byte[10];
    		for(int j=0; j<10; j++) {
    		 field[j] = bytes[i+j];
    		}
    		String s = new String(field);
    		fieldNames[index] = s;
    		index++;
    		
    	}
    	return fieldNames;
    }
    
    public static ArrayList<File> getTypeFiles(String typeName) throws IOException {
    	ArrayList<File> flist = new ArrayList<File>(); 
    	File x = new File ("SysCatData0.dat").getCanonicalFile();
    	String fpath = x.getParent();
    	File folder = new File(fpath);
    	File[] listOfFiles = folder.listFiles();
    	for(int i=0; i<listOfFiles.length; i++) {
    		for(int j=0; j<listOfFiles.length; j++) {
    		if(listOfFiles[j].getName().equals(typeName + "Data" + i + ".dat")) {
    			flist.add(listOfFiles[j]);
    		} 
    		}
    	}
    	
    	return flist;
    }
}
    
    
    
    

