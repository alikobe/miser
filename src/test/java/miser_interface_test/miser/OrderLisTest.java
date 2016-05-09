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
 * 未完成订单列表
 * 
 * @author yongrui
 *
 */
public class OrderLisTest {
	String methodName = "orderList";
	String userId = "3698730070";
	int pageSize = 1;
	int pageNum = 1;

	@Test
	public void testOrderLisSuccess() throws Exception {
		JSONObject response = request(methodName, userId, pageSize, pageNum, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		JSONObject joData = response.getJSONObject("data");
		Assert.assertEquals(joData.getInt("totalNumber"), 1);
		Assert.assertEquals(joData.getInt("pageSize"), 1);
		Assert.assertEquals(joData.getInt("pageNum"), 1);

		JSONArray dataJsonArray = joData.getJSONArray("data");
		JSONObject data = dataJsonArray.getJSONObject(0);

		Assert.assertEquals(data.getString("orderDetailPageUrl"),
				"http://trade.daily.taobao.net/trade/detail/trade_item_detail.htm?bizOrderId=194199244907000");
		Assert.assertEquals(data.getLong("sellerId"), 3646391903L);
		Assert.assertEquals(data.getInt("shipping"), 2);
		Assert.assertEquals(data.getString("logisticsDetailPageUrl"),
				"http://detail.i56.daily.taobao.net/trace/trace_detail.htm?tId=194199244907000&userId=3646391903");
		Assert.assertEquals(data.getLong("orderId"), 194199244907000L);
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

	}

	@Test
	public void testOrderLis4ArgBlank()
			throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		JSONObject response = request(methodName, "", pageSize, pageNum, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 100);
		Assert.assertEquals(response.getString("msg"), "arg error");

		response = request(methodName, userId, 0, pageNum, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
	}

	@Test
	public void testOrderLis4ArgError()
			throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		JSONObject response = request(methodName, "123", pageSize, pageNum, "");
		JSONObject JOData = null;
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		JOData = response.getJSONObject("data");
		
		if (!JOData.isNull("data")) {
			Assert.assertEquals(JOData.getJSONArray("data").length(), 0);
		}

		response = request(methodName, userId, 1234342345, pageNum, "");
		if (null == response)
			return;
		Assert.assertEquals(response.getInt("code"), 1);
		Assert.assertEquals(response.getString("msg"), "success");
		JOData = response.getJSONObject("data");
		
		if (!JOData.isNull("data")) {
			Assert.assertEquals(JOData.getJSONArray("data").length(), 1);
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
	private JSONObject request(String method, String userId, int pageSize, int pageNum, String ver)
			throws URISyntaxException, IOException, ClientProtocolException, JSONException {
		JSONObject jsonobj = new JSONObject();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String requestUrl = Const.SERVER_BASE_URL + method + ".json?userID=" + userId + "&pageSize=" + pageSize
				+ "&pageNum=" + pageNum + "&ver=" + ver;
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
