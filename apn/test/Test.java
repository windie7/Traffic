

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String s[]={"/root/sms/sendsms", "/dev/ttyACM0","13581586503","asfas阿斯顿飞"};
		//String s[]={"cmd.exe","/c","E:/tem/sms/sendsms", "/dev/ttyACM0","13581586503","asf制定法"};
		try {
			Process p=Runtime.getRuntime().exec(s);
			int ret=p.waitFor();
			System.out.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
