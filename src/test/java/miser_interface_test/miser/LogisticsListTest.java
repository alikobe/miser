package miser_interface_test.miser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import util.Const;

/**
 * 查询订单的物流列表 ，支持单个查询和多个查询
 * 
 * @author yongrui
 *
 */
public class LogisticsListTest {
	String methodName = "logisticsList";
	String userId = "3646391903";
	String sellerId = "3646391903";

	String orderId1 = "194158332727000";
	String orderId2 = "194221359897000";

	/**
	 * @throws Exception
	 */
	@Test
	public void testLogisticsListSuccess4OneOrder() throws Exception {
		JSONObject response = request(methodName, userId, sellerId, "", "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		JSONArray dataJsonArray = response.getJSONArray("data");
		JSONObject data = dataJsonArray.getJSONObject(0);
		Assert.assertEquals(data.getLong("orderId"), 194158332727000L);
		JSONArray packagesJsonArray = data.getJSONArray("packages");
		JSONObject traceDetails = packagesJsonArray.getJSONObject(0);
		JSONArray traceDetailsJsonArray = traceDetails.getJSONArray("traceDetails");
		if (null == traceDetailsJsonArray)
			return;
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(0).getString("action"), "GOT");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(0).getString("time"), "1462263280000");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(0).getString("desc"), "收到");

		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(1).getString("action"), "ARRIVAL");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(1).getString("time"), "1462264777000");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(1).getString("desc"), "进站了！！！");

		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(2).getString("action"), "DEPARTURE");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(2).getString("time"), "1462264903000");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(2).getString("desc"), "出站了！");

		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(3).getString("action"), "SENT_SCAN");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(3).getString("time"), "1462264969000");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(3).getString("desc"), "派件扫描！ 电话:13666668888");

		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(4).getString("action"), "SIGNED");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(4).getString("time"), "1462265002000");
		Assert.assertEquals(traceDetailsJsonArray.getJSONObject(4).getString("desc"), "买家已签收！");

	}

	/**
	 * 多个订单参数的验证
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLogisticsListSuccess4TwoOrder() throws Exception {
		JSONObject response = request(methodName, userId, sellerId, "", "two");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		JSONArray dataJsonArray = response.getJSONArray("data");
		JSONObject data1 = dataJsonArray.getJSONObject(0);
		Assert.assertEquals(data1.getLong("orderId"), 194158332727000L);
		JSONArray firstPackagesJsonArray = data1.getJSONArray("packages");
		JSONObject firstTraceDetails = firstPackagesJsonArray.getJSONObject(0);
		JSONArray firstTraceDetailsJsonArray = firstTraceDetails.getJSONArray("traceDetails");
		if (null == firstTraceDetailsJsonArray)
			return;
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(0).getString("action"), "GOT");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(0).getString("time"), "1462263280000");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(0).getString("desc"), "收到");

		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(1).getString("action"), "ARRIVAL");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(1).getString("time"), "1462264777000");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(1).getString("desc"), "进站了！！！");

		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(2).getString("action"), "DEPARTURE");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(2).getString("time"), "1462264903000");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(2).getString("desc"), "出站了！");

		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(3).getString("action"), "SENT_SCAN");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(3).getString("time"), "1462264969000");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(3).getString("desc"), "派件扫描！ 电话:13666668888");

		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(4).getString("action"), "SIGNED");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(4).getString("time"), "1462265002000");
		Assert.assertEquals(firstTraceDetailsJsonArray.getJSONObject(4).getString("desc"), "买家已签收！");

		// 订单二的物流信息
		JSONObject data2 = dataJsonArray.getJSONObject(1);
		Assert.assertEquals(data2.getLong("orderId"), 194221359897000L);
		JSONArray secondPackagesJsonArray = data2.getJSONArray("packages");
		JSONObject secondTraceDetails = secondPackagesJsonArray.getJSONObject(0);
		JSONArray secondTraceDetailsJsonArray = secondTraceDetails.getJSONArray("traceDetails");
		if (null == secondTraceDetailsJsonArray)
			return;
		Assert.assertEquals(secondTraceDetailsJsonArray.getJSONObject(0).getString("action"), "GOT");
		Assert.assertEquals(secondTraceDetailsJsonArray.getJSONObject(0).getString("time"), "1462447565000");
		Assert.assertEquals(secondTraceDetailsJsonArray.getJSONObject(0).getString("desc"), "lanshou!");

		Assert.assertEquals(secondTraceDetailsJsonArray.getJSONObject(1).getString("action"), "SIGNED");
		Assert.assertEquals(secondTraceDetailsJsonArray.getJSONObject(1).getString("time"), "1462447717000");
		Assert.assertEquals(secondTraceDetailsJsonArray.getJSONObject(1).getString("desc"), "签收");

	}

	@Test
	public void testLogisticsList4ArgBlank()
			throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		JSONObject response = request(methodName, "", sellerId, "", "");
		// TODO userId为空都能输出物流报文？
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 100);
		Assert.assertEquals(response.getString("msg"), "arg error");

		response = request(methodName, userId, "", "", "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 100);
		Assert.assertEquals(response.getString("msg"), "arg error");
	}

	@Test
	public void testLogisticsList4ArgError()
			throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		JSONObject response = request(methodName, "123", sellerId, "", "");
		// userId为错误值，没校验
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		if (!response.isNull("data")) {
			Assert.assertEquals(response.getJSONArray("data").length(), 0);
		}

		response = request(methodName, userId, "1234", "", "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		if (!response.isNull("data")) {
			Assert.assertEquals(response.getJSONArray("data").length(), 0);
		}
	}

	/**
	 * 请求封装方法
	 * 
	 * @param method
	 * @param userId
	 * @param orderId
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JSONException
	 */
	private JSONObject request(String method, String userId, String sellerId, String ver, String queryType)
			throws URISyntaxException, IOException, ClientProtocolException, JSONException {
		JSONObject jsonobj = new JSONObject();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String oneArg = sellerId + "|" + orderId1;
		String twoArg = sellerId + "|" + orderId1 + "," + sellerId + "|" + orderId2;
		oneArg = URLEncoder.encode(oneArg, "UTF-8");
		twoArg = URLEncoder.encode(twoArg, "UTF-8");
		String requestUrl = "";
		// 使用queryType来判断是否多个查询
		if (null == queryType || "".equals(queryType)) {
			requestUrl = Const.SERVER_BASE_URL + method + ".json?userId=" + userId + "&soIds=" + oneArg + "&ver=" + ver;
		} else {
			requestUrl = Const.SERVER_BASE_URL + method + ".json?userId=" + userId + "&soIds=" + twoArg + "&ver=" + ver;
		}
		HttpGet request = new HttpGet();
		request.setURI(new URI(requestUrl));
		CloseableHttpResponse response = httpclient.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String resString = EntityUtils.toString(entity);
			jsonobj = new JSONObject(resString);
		}
		return jsonobj;
	}

}
