package miser_interface_test.miser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
 * @author yongrui
 *
 */
public class OrderDetailTest {
	String methodName = "orderDetail";
	String userId = "3698730070";
	Long orderId = 194158332727000L;

	@Test
	public void testOrderDetailSuccess() throws Exception {
		JSONObject response = request(methodName, userId, orderId, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		JSONObject data = response.getJSONObject("data");
		Assert.assertEquals(data.getString("orderDetailPageUrl"),
				"http://trade.daily.taobao.net/trade/detail/trade_item_detail.htm?bizOrderId=194158332727000");
		Assert.assertEquals(data.getLong("sellerId"), 3646391903L);
		Assert.assertEquals(data.getInt("shipping"), 2);
		Assert.assertEquals(data.getString("logisticsDetailPageUrl"),
				"http://detail.i56.daily.taobao.net/trace/trace_detail.htm?tId=194158332727000&userId=3646391903");
		Assert.assertEquals(data.getLong("orderId"), 194158332727000L);
		Assert.assertEquals(data.getLong("totalFee"), 24600L);
		Assert.assertEquals(data.getLong("postFee"), 1200L);
		Assert.assertEquals(data.getLong("buyerId"), 3698730070L);

		JSONArray detailOrderListJsonArray = data.getJSONArray("detailOrderList");
		if (null == detailOrderListJsonArray)
			return;
		JSONObject detailOrderList = (JSONObject) detailOrderListJsonArray.get(0);
		Assert.assertEquals(detailOrderList.getString("pictUrl"),
				"http://img.daily.taobaocdn.net/bao/uploaded/i3/3646391903/TB2vhVaXXXXXXXlXFXXXXXXXXXX_!!3646391903.gif_120x120.jpg");
		Assert.assertEquals(detailOrderList.getString("title"), "食品管控");

		JSONObject logistics = data.getJSONObject("logistics");
		Assert.assertEquals(logistics.getLong("orderId"), 194158332727000L);
		JSONArray packagesJsonArray = logistics.getJSONArray("packages");
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

	@Test
	public void testOrderDetail4ArgBlank()
			throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		JSONObject response = request(methodName, "", orderId, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 100);
		Assert.assertEquals(response.getString("msg"), "arg error");

		response = request(methodName, userId, null, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 100);
		Assert.assertEquals(response.getString("msg"), "arg error");
	}

	@Test
	public void testOrderDetail4ArgError()
			throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		JSONObject response = request(methodName, "123", orderId, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		Assert.assertTrue(response.isNull("data"));

		response = request(methodName, userId, 1234L, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		Assert.assertTrue(response.isNull("data"));
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
	private JSONObject request(String method, String userId, Long orderId, String ver)
			throws URISyntaxException, IOException, ClientProtocolException, JSONException {
		JSONObject jsonobj = new JSONObject();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String requestUrl = Const.SERVER_BASE_URL + method + ".json?userID=" + userId + "&orderID=" + orderId + "&ver=" + ver;
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
