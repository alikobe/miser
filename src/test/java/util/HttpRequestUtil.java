package util;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpRequestUtil {
	public JSONObject requestGet(String methodName, String sn, String imei, String ver, String clientType)
			throws Exception {
		JSONObject jsonobj = new JSONObject();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String requestUrl = Const.SERVER_BASE_URL + "" + methodName + ".do?ni=" + sn + "&ei=" + imei + "&ver=" + ver
				+ "" + "&clientType=" + clientType;
		HttpGet request = new HttpGet();
//		request.addHeader("origin", "pc.ucweb.com");
//		request.addHeader("Referer", "pc.ucweb.com");
//		request.addHeader("UA", "UCBrowser");
		
		request.setURI(new URI(requestUrl));
		CloseableHttpResponse response = httpclient.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String resString = EntityUtils.toString(entity);
			jsonobj = new JSONObject(resString);
		}

		return jsonobj;
	}
	
	public static void main(String[] args) throws Exception {
		new HttpRequestUtil().requestGet("", "", "", "", "");
	}
}
